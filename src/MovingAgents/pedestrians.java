package MovingAgents;

import java.util.Vector;


import processing.core.*;
import Geometry.*;
import UrbanModel.*;

public class pedestrians {

	///////////////////////////////////////////////////////////////////
	//			Variables
	///////////////////////////////////////////////////////////////////

	// External Lib
	PApplet p;

	// Externally Referenced information
	public UrbanMap UrbanMap;
	public Vector<String> functions;
	Vector<solidObstacle> obstacles;
	Vector<building> buildings;
	public UrbanModel urbanModel;

	// Internally Referenced information


	// Pedestrian specific information
	int origineID = 9999999;
	int destinationID = 9999999;
	public float x = 0;
	public float y = 0;
	float dirx = 0;
	float diry = 0;
	float speed = 3;
	private float pedestrianSize = (float) 2;
	public int numberOfVisionRays = 20;
	public float visionField = PConstants.PI;
	private float radiusDevider = visionField/numberOfVisionRays;
	public vertex dirVertex = new vertex(1,1);
	public int goalID;
	public Boolean pedestrianArrived = false;
	public socialInformation personalInfo;
	public float distance = 0;
	public float timeTraveled = 0;
	public vertex origine= new vertex(0,0);

	public int pixx;
	public int pixy;

	public int xGridPedestrian;
	public int yGridPedestrian;

	private float walkingSpeed;
	private float minSpeed = 1.0f;
	public int visionrayLength = 100;
	public Trace myTrace;
	public Trace myPollution;
	private int increment = 5;
	private int visionDepth = 0;
	public float myTotalPollution;
	private boolean foundExit = false;
	private int aliveSince = 0;

	///////////////////////////////////////////////////////////////////
	//			Constructors
	///////////////////////////////////////////////////////////////////

	public pedestrians(float _x, float _y,UrbanMap _UrbanMap, PApplet _pApp, Vector<building> _b, Vector<solidObstacle> _obstacles, Vector<String> _functions, UrbanModel _urbanModel, int myRef)
	{

		// Recieve  information
		urbanModel = _urbanModel;
		UrbanMap = _UrbanMap;
		p = _pApp;
		functions = _functions;
		buildings = _b;
		obstacles = _obstacles;
		x = _x;
		y =_y;
		origine.x = _x;
		origine.y = _y;

		origineID = myRef;


		//initialize repository
		personalInfo = new socialInformation();


		//use initial methods
		findDestination();
		initializePersonalInfo();

		myTrace = new Trace(origineID, goalID);
		myPollution = new Trace(origineID, goalID);
		myTrace.myPolygone.addVertex(_x, _y);
		myTotalPollution = 0;
		//		pedestrianSize = Math.round(urbanModel.width/600);

	}

	///////////////////////////////////////////////////////////////////
	//			Methods
	///////////////////////////////////////////////////////////////////


	private void initializePersonalInfo() {

		personalInfo.age = (int)p.random(15, 99);
		//		preference pref = new preference("office","Chinese","Christian");
		//		personalInfo.preferences.add(pref);
	}

	public void drawMe() {
		p.fill(210,210,255,100);
		//		p.stroke(117,117,134);
		//		p.fill(walkingSpeed*20, walkingSpeed*80, 255-walkingSpeed*20);
		//		p.noStroke();
//		p.noSmooth();
		p.noStroke();
		p.ellipse(x, y, 3, 3);
	}

	public void findDestination() {
		if(buildings.size()==1)pedestrianArrived = true;
		goalID = PApplet.round(p.random(buildings.size()-1));
		if (goalID <0)goalID =0;
	}

	public void move(boolean followAgents) {

		int gridX = PApplet.round(x/UrbanMap.gridSize);
		int gridY = PApplet.round(y/UrbanMap.gridSize);

		// get on map
		if (gridX<=0) {
			gridX = 1;
			System.out.println("truely arrived 4");

			pedestrianArrived = true;
		}
		if (gridX>UrbanMap.gridCountX-1) {
			gridX = UrbanMap.gridCountX-1;

			pedestrianArrived = true;
		}
		if (gridY<=0) {
			gridY = 1;
			pedestrianArrived = true;
		}
		if (gridY>UrbanMap.gridCountY-1) {
			gridY = UrbanMap.gridCountY-1;
			pedestrianArrived = true;
		}

		UrbanMap.incrementFootFall(gridX,gridY);
		//		aliveSince++;
		//		if (aliveSince>=5000)pedestrianArrived=true;
		myTrace.age++;
		if(p.frameCount%increment ==0) {
//			System.out.println("x/y/z = "+x+"/"+y+"/"+visionDepth);
//			System.out.println("trace ="+myTrace);
//			System.out.println("polygone ="+myTrace.myPolygone);
			if(myTrace.myPolygone != null)	{myTrace.myPolygone.addVertex(x, y,visionDepth);
			
			}else {
				
			}
			if(urbanModel.pollutionMap!= null ) {
				myPollution.myPolygone.addVertex(x, y,p.red(urbanModel.pollutionMap.get((int)x,(int)y)));
				myTotalPollution = myTotalPollution + p.red(urbanModel.pollutionMap.get((int)x,(int)y));
				//				System.out.println(p.red(urbanModel.pollutionMap.get((int)x,(int)y)));
			}else{
				myPollution.myPolygone.addVertex(x, y,0);
				//				System.out.println("couldn't read pollutionmap");
			}



		}
		//		int gridX = PApplet.round(x/UrbanMap.gridSize);
		//		if (gridX<0)gridX=1;
		//		if (gridX>UrbanMap.gridCountX)gridX=UrbanMap.gridCountX;
		//		int gridY = PApplet.round(y/UrbanMap.gridSize);
		if (gridY<0)gridY=1;
		if (gridY>UrbanMap.gridCountY)gridY=UrbanMap.gridCountY;
		//		p.println(gridX+"/"+gridY);



		//		if (UrbanMap.distance[goalID][gridX][gridY] == 0) findDestination();
		//		if (UrbanMap.distance[goalID][gridX][gridY]< 5 && UrbanMap.distance[goalID][gridX][gridY]> 0) pedestrianArrived = true;
		//		stayOnMap();
		if(UrbanMap.ObjectID[gridX][gridY] > 1000 ) {
			//deleting pedestrians in the building
			myTrace.myPolygone = null;
//			pedestrianArrived=true; //caught in a obstacle
			//			findExit();
		}else {
			if(UrbanMap.ObjectID[gridX][gridY] == origineID+1 || UrbanMap.ObjectID[gridX][gridY] == destinationID){
				findExit();
			}else{
				findDirection(followAgents);
			}
		}
		x += dirx;
		y += diry;
		distance += Math.sqrt(dirx*dirx+diry*diry);
		timeTraveled += 1;
	}



	private void bounceFromBorder() {
		if (x>=p.width-1) {
			x = p.width;
			dirx *= -1;
		}
		if (x<=pedestrianSize) {
			x = 1;
			dirx *= -1;
		}
		if (y>=p.height-1) {
			y = p.height;
			diry *= -1;
		}
		if (y<=pedestrianSize) {
			y = 1;
			diry *= -1;
		}
	}

	public void findExit() {
		/*
		 * go in spiral around you to find a pixel without walkobstruction
		 * if found 4 pick shortest and teleport there
		 */
		Vector<VisionPixels> fieldOfView = new Vector<VisionPixels>();
		int pixx; int pixy;
		float angle = 			p.random(2*PConstants.PI);  //the initial direction we are looking into
		float x1 = 				0; 
		float y1 = 				0;
		if(diry!=0) angle = -PApplet.atan(dirx/diry);
		if(diry<0)  angle = PConstants.PI+PApplet.atan(dirx/-diry);
		foundExit = false;
		for(int i = 1 ; i<= 500 ; i += 1) {
			if(foundExit == true)break;
			for(float j = angle ;j <2*PConstants.PI+angle ;j+=PConstants.PI/12) {

				if(foundExit == true)break;
				x1 = i * PApplet.cos(j);
				y1 = i * PApplet.sin(j);
				pixx = PApplet.round((x/UrbanMap.gridSize) + x1);
				pixy = PApplet.round((y/UrbanMap.gridSize) + y1);
				if (pixx >= UrbanMap.gridCountX || pixx <= 0) break; 
				if (pixy >= UrbanMap.gridCountY || pixy <= 0) break;
				if (UrbanMap.targetHitTest(pixx, pixy)==-2) { 

					float g = UrbanMap.gridSize;
					//					p.fill(1,255,50);
					//					p.stroke(200);
					//					p.rect(pixx*g, pixy*g, g, g);
					//					VisionPixels vp = new VisionPixels(pixx, pixy, i); // 3rd Dimension is weight
					//					fieldOfView.add(vp);
					dirVertex.x = pixx*UrbanMap.gridSize - x;
					dirVertex.y = pixy*UrbanMap.gridSize - y;
					foundExit = true;

					break;
				}else {
					continue;
				}
			}

		}
		probabilisticDraw(fieldOfView);
		dirx = dirVertex.x;
		diry = dirVertex.y;
		//		PApplet.println("dir = " + dirx +"/"+ diry);

		float l = PApplet.sqrt(dirx*dirx+diry*diry);
		dirx /= l;
		diry /= l;

		dirx*=speed;
		diry*=speed;
	}

	public void findDirection(boolean followAgents) {

		/*
		 * add pixel in the directions want to go
		 * stop if you see a building
		 * add information of yourself to the building  
		 * 
		 */

		Vector<VisionPixels> fieldOfView = new Vector<VisionPixels>();

		float angle = 			0;  //the initial direction we are looking into
		float x1 = 				0; 
		float y1 = 				0;

		if(diry!= 0) angle = -PApplet.atan(dirx/diry);
		if(diry<0)  angle = PConstants.PI+PApplet.atan(dirx/-diry);

		xGridPedestrian = PApplet.round(x/UrbanMap.gridSize);
		if(xGridPedestrian<1)xGridPedestrian = 1;
		if(xGridPedestrian>=UrbanMap.gridCountX)xGridPedestrian = UrbanMap.gridCountX-1;

		yGridPedestrian = PApplet.round(y/UrbanMap.gridSize);
		if(yGridPedestrian<1)yGridPedestrian = 1;
		if(yGridPedestrian>=UrbanMap.gridCountY)yGridPedestrian = UrbanMap.gridCountY-1;

		if(goalID>buildings.size()||goalID<0) {
//			System.out.println("this is the end");
		}


		visionDepth = 0;
		if(p.random(5)>0) {
			for(float j = 2*PConstants.PI+angle ;j > angle  ;j-=radiusDevider) {
				for(int i = 1 ; i < visionrayLength  ; i += 1) {


					x1 = i * PApplet.cos(j);
					y1 = i * PApplet.sin(j);
					pixx = PApplet.round(xGridPedestrian + x1);
					pixy = PApplet.round(yGridPedestrian + y1);
					if (pixx >= UrbanMap.gridCountX || pixx <= 1) {
						break; 
					}
					if (pixy >= UrbanMap.gridCountY || pixy <= 1) {
						break;
					}

					if (UrbanMap.targetHitTest(pixx, pixy) > 0) {

						//						putPersonalInformation(UrbanMap.targetHitTest(pixx, pixy)-1);
						//						getBuildingInformation(UrbanMap.targetHitTest(pixx, pixy)-1);
						if (UrbanMap.targetHitTest(pixx, pixy)-1==goalID && i < speed+1)	{
//							System.out.println("truely arrived 1");
							pedestrianArrived=true; //Truly arrived
							//							p.fill(255,255,0);
							//							p.rect(pixx*UrbanMap.gridSize, pixy*UrbanMap.gridSize, UrbanMap.gridSize, UrbanMap.gridSize);//draw visionpixel
						}else {
							//													p.fill(255,255,0);
							//													p.rect(pixx*UrbanMap.gridSize, pixy*UrbanMap.gridSize, UrbanMap.gridSize, UrbanMap.gridSize);//draw visionpixel
							break;
						}
					}

					if(goalID == 541) {
						System.out.println("this is goal if");

					}
					//					if(UrbanMap.distance[goalID][pixx][pixy]==0) {
					//						pedestrianArrived=true;
					//					}

					//					if(yGridPedestrian == 541) {
					//						yGridPedestrian = 540;
					//						
					//					}
					if (UrbanMap.distance[goalID][xGridPedestrian][yGridPedestrian]<UrbanMap.distance[goalID][pixx][pixy]) {
						break; // stop if this is not a better point
					}else{
						//					VisionPixels vp = new VisionPixels(pixx, pixy, 1); // 3rd Dimension is weight
						float density = UrbanMap.footFall[pixx][pixy]; //UrbanMap.density[pixx][pixy];
						float weight = density;
						if(weight>0.9)weight=0.9f;
						VisionPixels vp;
						visionDepth++;
						if(followAgents) {


							vp = new VisionPixels(pixx, pixy, 1+density); // 3rd Dimension is weight

						}else {
							vp = new VisionPixels(pixx, pixy, 1); // 3rd Dimension is weight
						}
						//						p.noFill();
						//						p.stroke(185,210,231);
						//						p.rect(pixx*UrbanMap.gridSize, pixy*UrbanMap.gridSize, UrbanMap.gridSize, UrbanMap.gridSize);//draw visionpixel
						fieldOfView.add(vp);
					}
				}
			}
		}else {


			for(float j = angle ;j < 2*PConstants.PI+angle  ;j+=radiusDevider) {
				for(int i = 1 ; i < visionrayLength  ; i += 1) {

					x1 = i * PApplet.cos(j);
					y1 = i * PApplet.sin(j);
					pixx = PApplet.round(xGridPedestrian + x1);
					pixy = PApplet.round(yGridPedestrian + y1);
					if (pixx >= UrbanMap.gridCountX || pixx <= 0) {
						break; 
					}
					if (pixy >= UrbanMap.gridCountY || pixy <= 0) {
						break;
					}

					if (UrbanMap.targetHitTest(pixx, pixy) > 0) {

						putPersonalInformation(UrbanMap.targetHitTest(pixx, pixy)-1);
						getBuildingInformation(UrbanMap.targetHitTest(pixx, pixy)-1);
						if (UrbanMap.targetHitTest(pixx, pixy)-1==goalID && i < speed+1)	{
							System.out.println("truely arrived 2");

							pedestrianArrived=true; //truely arrived

						}else {
//							p.fill(255,255,0);
//							p.rect(pixx*UrbanMap.gridSize, pixy*UrbanMap.gridSize, UrbanMap.gridSize, UrbanMap.gridSize);//draw visionpixel
							break;
						}
					}

					if (UrbanMap.distance[goalID][xGridPedestrian][yGridPedestrian]<UrbanMap.distance[goalID][pixx][pixy]) {
						break; // stop if this is not a better point
					}else{
						//					VisionPixels vp = new VisionPixels(pixx, pixy, 1); // 3rd Dimension is weight
						float density = UrbanMap.density[pixx][pixy]/2;
						float weight = density/10;
						if(weight>0.9)weight=0.9f;
						VisionPixels vp = new VisionPixels(pixx, pixy, 1-weight); // 3rd Dimension is weight
						//						p.fill(200,200,255);
						//						p.rect(pixx*UrbanMap.gridSize, pixy*UrbanMap.gridSize, UrbanMap.gridSize, UrbanMap.gridSize);//draw visionpixel
						fieldOfView.add(vp);
					}
				}
			}
		}
		//		PApplet.println("fieldOfView.Size = " + fieldOfView.size());
		probabilisticDraw(fieldOfView);
		dirx = dirVertex.x;
		diry = dirVertex.y;
		//		PApplet.println("dir = " + dirx +"/"+ diry);

		float l = PApplet.sqrt(dirx*dirx+diry*diry);
		dirx /= l;
		diry /= l;
		if(UrbanMap.density[xGridPedestrian][yGridPedestrian]>5) {
			if(UrbanMap.density[xGridPedestrian][yGridPedestrian]>25) {
				walkingSpeed = minSpeed;
			}else {
				float densVari = (UrbanMap.density[xGridPedestrian][yGridPedestrian]-5)/(speed-minSpeed);
				walkingSpeed = speed-densVari; //(UrbanMap.density[xGridPedestrian][yGridPedestrian]-4);
				if(walkingSpeed <=1)walkingSpeed =1f;
				//				System.out.println("densVari - "+densVari+" / grid vari - "+ UrbanMap.density[xGridPedestrian][yGridPedestrian]+ " speedvar " + walkingSpeed);
			}
		}
		dirx*=speed;
		diry*=speed;
		//		PApplet.println(dirx +"/"+diry);
	}

	public void putPersonalFinalDestination(int buildingID, pedestrians pedestrian) {
		//		System.out.println("pedestrian at "+ pedestrian.pixx+"/"+pedestrian.pixy);
		//		System.out.println("buildingID = "+buildingID +" building.size = "+buildings.size());
		//		System.out.println("goalID = "+goalID);
		//		System.out.println("distance = "+distance);
		//		System.out.println("timeTraveled = "+timeTraveled);
		//		System.out.println("euclid dist = "+Math.sqrt((origine.x-x)*(origine.x-x)+(origine.y-y)*(origine.y-y)));
		//		
		building b = buildings.elementAt(pedestrian.goalID);
		b.totalDistanceTraveled += distance;
		b.totaltimeTraveled += timeTraveled;
		b.totalPedestriansArrived += 1 ;
		b.totalEuclidianDistanceofArrivals += Math.sqrt((origine.x-x)*(origine.x-x)+(origine.y-y)*(origine.y-y));
		b.totalMetricDistanceofArrivals += speed+1+UrbanMap.distance[origineID][Math.round(x/UrbanMap.gridSize)][Math.round(y/UrbanMap.gridSize)];
	}

	private void getBuildingInformation(int buildingID) {
		//		p.println("building ID = "+ buildingID);
		if(buildingID<urbanModel.firstNumberOfObstacles){
			//			PApplet.println(buildings.size()+" buildings.size = buildingID "+buildingID);
			building b = buildings.elementAt(buildingID);
			//			PApplet.println(b.size);
			//			p.println(personalInfo.race);
			//			b.buildingInfo.incrementDemand(personalInfo);
		}else{
			solidObstacle o = obstacles.elementAt(buildingID-urbanModel.firstNumberOfObstacles);
			o.buildingInfo.incrementDemand(personalInfo);
		}

		// TODO Auto-generated method stub
		/* create Vector<buidlingInfo>
		 * create buildingInfo in buildings
		 * 
		 * f():
		 * target hit test
		 * load building info
		 * 
		 */
	}

	private void putPersonalInformation(int buildingID) {

		// TODO Auto-generated method stub
		/* 
		 * create personalInfo
		 * create Vector<personalInfo>
		 * 
		 * target hit test
		 * load building
		 * transmit personal info
		 * 
		 */

		if(buildingID<999){
			//			PApplet.println(buildings.size()+" buildings.size = buildingID "+buildingID);
			building b = buildings.elementAt(buildingID);
			b.buildingInfo.incrementAge(personalInfo.age);
			b.buildingInfo.incrementDemand(personalInfo);

		}else{
			//			PApplet.println(obstacles.size()+" obstacles.size = buildingID "+buildingID);
			solidObstacle o = obstacles.elementAt(buildingID-1000);
			o.buildingInfo.incrementDemand(personalInfo);
		}

	}

	private void stayOnMap() {
		// Stay on the map
		if (x>=p.width-pedestrianSize) {
			x = p.width;
			dirx *= -1;
		}
		if (x<=pedestrianSize) {
			x = 0;
			dirx *= -1;
		}
		if (y>=p.height-pedestrianSize) {
			y = p.height;
			diry *= -1;
		}
		if (y<=pedestrianSize) {
			y = 0;
			diry *= -1;
		}
	}

	public void probabilisticDraw(Vector<VisionPixels> fieldOfView) {

		float probablisticSum = 0;
		float probability;
		for (int i = 0; i < fieldOfView.size(); i++) {
			VisionPixels element = fieldOfView.elementAt(i);
			probability = element.w;
			probablisticSum += probability;
		}

		float hit = 0;
		float r = p.random(probablisticSum);
		for (int i = 0; i < fieldOfView.size(); i++) {
			hit++;
			VisionPixels element = fieldOfView.elementAt(i);
			if(hit>r) {
				probability = element.w;
				dirVertex.x = element.x*UrbanMap.gridSize - x;
				dirVertex.y = element.y*UrbanMap.gridSize - y;
				//								PApplet.println(dirVertex.x+"/"+dirVertex.y);
				break;
			}
		}
	}
}
