package Geometry;

import UrbanModel.*;
import Geometry.*;
import Importer.pair;
import MovingAgents.*;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;

public class building {

	public int myRef;
	public float size;
	PApplet p; 
	public boolean isGeometry;

	public vertex centroid;

	public Vector <vertex>vertexes;
	Vector<building> buildings;
	public Vector <vertex>midpoints;

	public myBox boundingBox;
	public buildingInformation buildingInfo;

	public UrbanModel urbanModel;
	public UrbanMap UrbanMap;
	public Vector<String> functions;
	Vector<solidObstacle> obstacles;
	private int buidlingReview = 3000 ; //every 3000 the building could change
	public Vector<pedestrians> pedestrians;
	public float totaltimeTraveled = 0;
	public float totalDistanceTraveled = 0;
	public int totalPedestriansArrived = 0;
	public float totalEuclidianDistanceofArrivals = 0;
	public float totalMetricDistanceofArrivals =0;
	public String text = "-";


	///////////////////////////////////////////////////////////////////
	//			Constructors
	///////////////////////////////////////////////////////////////////


	public building(float _x, float _y, int _size, PApplet _pApp, Vector<building> _b, Vector<solidObstacle> _obstacles, Vector<MovingAgents.pedestrians> pedestrians2, UrbanModel _urbanModel) {
		isGeometry =false;
		p = _pApp;
		buildings = _b;
		obstacles = _obstacles;
		urbanModel = _urbanModel;
		vertexes = new Vector<vertex>();
		vertexes.add(new vertex(_x,_y));

		size = _size;
		pedestrians = pedestrians2;

		midpoints = new Vector<vertex>();
		midpoints.add(new vertex(_x,_y));
		centroid = new vertex(_x,_y);
		myRef = buildings.size();
		buildingInfo = new buildingInformation();
		initializeBuildingInfo();


	}

	public building(float _x, float _y, int _size, PApplet _pApp, Vector<building> _b, 
			Vector<solidObstacle> _obstacles, Vector<MovingAgents.pedestrians> pedestrians2, Vector<String> functionsList, UrbanModel _urbanModel) {
		isGeometry =false;
		p = _pApp;
		buildings = _b;
		obstacles = _obstacles;
		urbanModel = _urbanModel;
		vertexes = new Vector<vertex>();
		vertexes.add(new vertex(_x-2,_y-2));
		vertexes.add(new vertex(_x+2,_y-2));
		vertexes.add(new vertex(_x+2,_y+2));
		vertexes.add(new vertex(_x-2,_y+2));
		provisoryPolygone provisoryPolygone = new provisoryPolygone(_pApp); 
		provisoryPolygone.vertexes.add(new vertex(_x-2,_y-2));
		provisoryPolygone.vertexes.add(new vertex(_x+2,_y-2));
		provisoryPolygone.vertexes.add(new vertex(_x+2,_y+2));
		provisoryPolygone.vertexes.add(new vertex(_x-2,_y+2));
		size = _size;
		//		functions = _functions;

		pedestrians = pedestrians2;
		midpoints = new Vector<vertex>();
		for (int i = 0; i <provisoryPolygone.vertexes.size();i++) {
			int y = 0;
			if (i == provisoryPolygone.vertexes.size()-1) {
				y = -1;
			}else {
				y = i;
			}
			vertex v= (vertex)provisoryPolygone.vertexes.elementAt(i);
			vertex w= (vertex)provisoryPolygone.vertexes.elementAt(y+1);
			int mPointx = p.abs(p.round(v.x-((v.x-w.x)/2)));
			int mPointy = p.abs(p.round(v.y-((v.y-w.y)/2)));
			midpoints.add(new vertex(mPointx,mPointy));
		}
		centroid = provisoryPolygone.centroid();
		System.out.println(centroid.x+"/"+centroid.y);
		System.out.println("This is building nr"+buildings.size());
		myRef = buildings.size();
		boundingBox = provisoryPolygone.getBoundingBox(this);
		buildingInfo = new buildingInformation();		

	}

	//1
	public building(provisoryPolygone provisoryPolygone, PApplet _pApp, Vector<building> _b, Vector<solidObstacle> _obstacles, Vector<pedestrians> _pedestrians, UrbanModel _urbanModel){
		isGeometry = true;
		p = _pApp;
		buildings = _b;
		obstacles = _obstacles;
		urbanModel = _urbanModel;

		vertexes = new Vector<vertex>();
		for (int i = 0; i < provisoryPolygone.vertexes.size(); i++) {
			vertex v = provisoryPolygone.vertexes.elementAt(i);
			vertexes.add(new vertex(v.x,v.y));
		}
		size = (float) provisoryPolygone.area();
		pedestrians = _pedestrians;
		centroid = provisoryPolygone.centroid(); 

		midpoints = new Vector<vertex>();
		for (int i = 0; i <provisoryPolygone.vertexes.size();i++) {
			int y = 0;
			if (i == provisoryPolygone.vertexes.size()-1) {
				y = -1;
			}else {
				y = i;
			}
			vertex v= (vertex)provisoryPolygone.vertexes.elementAt(i);
			vertex w= (vertex)provisoryPolygone.vertexes.elementAt(y+1);
			int mPointx = p.abs(p.round(v.x-((v.x-w.x)/2)));
			int mPointy = p.abs(p.round(v.y-((v.y-w.y)/2)));
			midpoints.add(new vertex(mPointx,mPointy));
		}
		boundingBox = provisoryPolygone.getBoundingBox(this);

		myRef = buildings.size()+1;//+1
		buildingInfo = new buildingInformation();
		initializeBuildingInfo();

	}

	public building(provisoryPolygone provisoryPolygone, PApplet _pApp, Vector<building> _b, Vector<solidObstacle> _obstacles, UrbanModel _urbanModel){
		isGeometry = true;
		p = _pApp;
		buildings = _b;
		obstacles = _obstacles;
		urbanModel = _urbanModel;

		vertexes = new Vector<vertex>();
		for (int i = 0; i < provisoryPolygone.vertexes.size(); i++) {
			vertex v = provisoryPolygone.vertexes.elementAt(i);
			vertexes.add(new vertex(v.x,v.y));
		}
		size = (float) provisoryPolygone.area();
		centroid = provisoryPolygone.centroid(); 

		midpoints = new Vector<vertex>();
		for (int i = 0; i <provisoryPolygone.vertexes.size();i++) {
			int y = 0;
			if (i == provisoryPolygone.vertexes.size()-1) {
				y = -1;
			}else {
				y = i;
			}
			vertex v= (vertex)provisoryPolygone.vertexes.elementAt(i);
			vertex w= (vertex)provisoryPolygone.vertexes.elementAt(y+1);
			int mPointx = p.abs(p.round(v.x-((v.x-w.x)/2)));
			int mPointy = p.abs(p.round(v.y-((v.y-w.y)/2)));
			midpoints.add(new vertex(mPointx,mPointy));
		}
		boundingBox = provisoryPolygone.getBoundingBox(this);
		myRef = buildings.size();

		p.println("Construcor of Buildings");
		buildingInfo = new buildingInformation();
		initializeBuildingInfo();

	}

	public building(Geometry.provisoryPolygone provisoryPolygone, PApplet pApp,
			Vector<Geometry.building> _b,
			Vector<solidObstacle> _obstacles,
			Vector<MovingAgents.pedestrians> _pedestrians,
			Vector<String> functions2,
			UrbanModel _urbanModel
			) {
		isGeometry = true;
		p = pApp;
		buildings = _b;
		obstacles = _obstacles;
		urbanModel = _urbanModel;

		vertexes = new Vector<vertex>();
		for (int i = 0; i < provisoryPolygone.vertexes.size(); i++) {
			vertex v = provisoryPolygone.vertexes.elementAt(i);
			vertexes.add(new vertex(v.x,v.y));
		}
		size = (float) provisoryPolygone.area();
		pedestrians = _pedestrians;
		centroid = provisoryPolygone.centroid(); 

		midpoints = new Vector<vertex>();
		for (int i = 0; i <provisoryPolygone.vertexes.size();i++) {
			int y = 0;
			if (i == provisoryPolygone.vertexes.size()-1) {
				y = -1;
			}else {
				y = i;
			}
			vertex v= (vertex)provisoryPolygone.vertexes.elementAt(i);
			vertex w= (vertex)provisoryPolygone.vertexes.elementAt(y+1);
			int mPointx = p.abs(p.round(v.x-((v.x-w.x)/2)));
			int mPointy = p.abs(p.round(v.y-((v.y-w.y)/2)));
			midpoints.add(new vertex(mPointx,mPointy));
		}
		boundingBox = provisoryPolygone.getBoundingBox(this);
		myRef = buildings.size();
		buildingInfo = new buildingInformation();
		initializeBuildingInfo();


	}

	///////////////////////////////////////////////////////////////////
	//			Methods
	///////////////////////////////////////////////////////////////////

	private void initializeBuildingInfo() {
		buildingInfo.customersRace.add(new pair("chinese",0));
		buildingInfo.customersRace.add(new pair("indian",0));
		buildingInfo.customersRace.add(new pair("malaysian",0));
		buildingInfo.customersRace.add(new pair("european",0));
		buildingInfo.customersRace.add(new pair("others",0));

		//		buildingInfo.demands.

		buildingInfo.functions.add(new function("office", 99,1));
	}

	public void removeObstacleMap(building b, UrbanMap _UrbanMap,
			float gridSize) {
		float X_X = 0;
		float Y_Y = 0;
		UrbanMap = _UrbanMap;
		vertex v;
		vertex w;
		float VectorY;
		float VectorX;
		float nVectorX = 0;
		float nVectorY = 0;
		int q = 0;


		for (int i = 0; i < b.vertexes.size(); i++) {
			w = b.vertexes.elementAt(i);
			if (i == b.vertexes.size()-1) {
				v = b.vertexes.elementAt(0);
			}else {
				v = b.vertexes.elementAt(i+1);
			}

			for (int y= 1; y<1000; y++) {
				if (PApplet.abs(X_X - p.round(v.x)) < 4 && PApplet.abs(Y_Y - p.round(v.y)) < 4) {

				}else {
					float dist = PApplet.abs(PApplet.dist(v.x, v.y, w.x, w.y));
					VectorX = w.x + y*((v.x-w.x)/dist);
					VectorY = w.y + y*((v.y-w.y)/dist);
					X_X = VectorX;
					Y_Y = VectorY;
					UrbanMap.decrementObstructionMap(p.round(VectorX/gridSize), p.round(VectorY/gridSize));
					UrbanMap.decrementObjectId(b.myRef,PApplet.round(VectorX/gridSize), PApplet.round(VectorY/gridSize));
				}
			}
		}
		for (int i = p.round(boundingBox.xMin); i<p.round(boundingBox.xMax); i++) {
			for (int y = p.round(boundingBox.yMin); y<p.round(boundingBox.yMax);y++ ) {
				if (pointInside(i,y, b) == true) {
					//					p.println("addObstacleMap "+b.myRef);

					UrbanMap.decrementObstructionMap(p.round(i/gridSize), p.round(y/gridSize));
					UrbanMap.decrementObjectId(b.myRef,p.round(i/gridSize), p.round(y/gridSize));
				}
			}
		}
	}

	public void addObstacleMap(building b, UrbanMap _UrbanMap, float gridSize) {

		float X_X = 0;
		float Y_Y = 0;
		UrbanMap = _UrbanMap;
		vertex v;
		vertex w;
		float VectorY;
		float VectorX;
		float nVectorX = 0;
		float nVectorY = 0;
		int q = 0;


		for (int i = 0; i < b.vertexes.size(); i++) {
			w = b.vertexes.elementAt(i);
			if (i == b.vertexes.size()-1) {
				v = b.vertexes.elementAt(0);
			}else {
				v = b.vertexes.elementAt(i+1);
			}

			for (int y= 1; y<1000; y++) {
				if (PApplet.abs(X_X - p.round(v.x)) < 4 && PApplet.abs(Y_Y - p.round(v.y)) < 4) {

				}else {
					float dist = PApplet.abs(PApplet.dist(v.x, v.y, w.x, w.y));
					VectorX = w.x + y*((v.x-w.x)/dist);
					VectorY = w.y + y*((v.y-w.y)/dist);
					X_X = VectorX;
					Y_Y = VectorY;
					UrbanMap.incrementObstructionMap(p.round(VectorX/gridSize), p.round(VectorY/gridSize));
					UrbanMap.incrementObjectId(b.myRef,PApplet.round(VectorX/gridSize), PApplet.round(VectorY/gridSize));
				}
			}
		}
		for (int i = p.round(boundingBox.xMin); i<p.round(boundingBox.xMax); i++) {
			for (int y = p.round(boundingBox.yMin); y<p.round(boundingBox.yMax);y++ ) {
				if (pointInside(i,y, b) == true) {
					//					p.println("addObstacleMap "+b.myRef);

					UrbanMap.incrementObstructionMap(p.round(i/gridSize), p.round(y/gridSize));
					UrbanMap.incrementObjectId(b.myRef,p.round(i/gridSize), p.round(y/gridSize));
				}
			}
		}	
	}

	public boolean pointInside(float  x, float y, building b)
	{
		boolean  oddNodes=false;
		vertex a,c;

		int j = b.vertexes.size()-1;

		for (int i = 0; i < b.vertexes.size(); i++) {
			c = b.vertexes.elementAt(j);
			a = b.vertexes.elementAt(i);
			if ((a.y < y && c.y >= y) || (c.y < y && a.y >= y)) 
			{
				if (a.x+ (y-a.y) / (c.y -a.y) * (c.x-a.x) < x) 
				{
					oddNodes=!oddNodes; 
				}
			}
			j=i; 
		}
		return oddNodes;
	}

	public void drawBoundingBox(int i) {
		p.noFill();
		p.stroke(155,155,255);
		p.rect(boundingBox.xMin, boundingBox.yMin, boundingBox.xMax-boundingBox.xMin, boundingBox.yMax-boundingBox.yMin);
	}

	public void drawMidpoints(int i) {
		building b = (building) buildings.elementAt(i);
		for (int y = 0; y < b.vertexes.size();y++) {
			vertex v = b.midpoints.elementAt(y);
			p.fill(255);
			p.rect(v.x,v.y,10,10);
		}
	}	

	public void drawBuildingConditions(int k) {

		//perfomr methods every loop
		if (p.frameCount%size == 0) {
			emitPedestrians();
		}
		if (p.frameCount%buidlingReview  == 0) {
			buildingReview();
		}

		text = "-";
		//draw different colors
		switch(k) {//here are different cases stored what to display


		case 0:
			if (functions != null) {
				int x = functions.size();
				p.fill(250,255,255);
				//			System.out.println("I'm a building with multiple functions");

			}else {
				p.fill(155,180,255);
			}
			break;

		case 1:
			int x = p.round(255*((buildingInfo.numberOfPeopleSeen/size)/(1+urbanModel.maximumBuildingInformation.numberOfPeopleSeen)));
			p.fill(105-x, 105-x,135-x);
			break;


		case 2:
			x = p.round(255*buildingInfo.averageAge/(1+urbanModel.maximumBuildingInformation.averageAge));
			p.fill(255-x, 255-x, x);
			break;

		case 3:
			x = p.round(255*buildingInfo.numberOfFloors/(1+urbanModel.maximumBuildingInformation.numberOfFunctions));
			p.fill(255-x, 255-x, x);
			break;

		case 4 :
			text = "totalPeople arrived = " + totalPedestriansArrived;
			break;
		case 5:
			text = "av.distance = " + totalDistanceTraveled/totalPedestriansArrived ;
			break;
		case 6:
			text = "av.speed = " + totalDistanceTraveled/(totalPedestriansArrived*totaltimeTraveled) ;
			break;
		case 7:
			text = "add distance euclid in % = "+ p.round(((totalDistanceTraveled/totalEuclidianDistanceofArrivals)-1)*100);
			break;
		case 8:
			//			System.out.println(" total metric distance "+ totalMetricDistanceofArrivals*UrbanMap.gridSize);
			//			System.out.println(" total euclid distance "+ totalEuclidianDistanceofArrivals);
			if(totalEuclidianDistanceofArrivals!=0)	text = " metricDistance / euclidian in % = "+ ((totalMetricDistanceofArrivals*UrbanMap.gridSize*100)/totalEuclidianDistanceofArrivals);
			break;
		case 9:
			//			System.out.println(" total metric distance "+ totalMetricDistanceofArrivals*UrbanMap.gridSize);
			//			if(myRef!=UrbanMap.buildingVariable)
			if(myRef-1 <=0) {
				text = "-";
			}else {
				text =" metricDistance to "+myRef+" to is "+UrbanMap.gridSize*UrbanMap.distance[myRef-1][p.round(p.mouseX/UrbanMap.gridSize)][p.round(p.mouseY/UrbanMap.gridSize)];
			}
			break;
		case 10:
			text =" euclidian Distance is " + Math.sqrt((centroid.x-p.mouseX)*(centroid.x-p.mouseX)+(centroid.y-p.mouseY)*(centroid.y-p.mouseY));

		}
	}

	public void drawMe(int k) {


		// 4 is start

//		p.stroke(200);
//		p.fill(200);
		p.noFill();
		p.noStroke();
		String t = p.str(myRef);
//		p.text(t,centroid.x, centroid.y);

		
		p.noStroke();
		switch(k) {
		case 0:
			if (functions != null) {
				int x = functions.size();
				p.fill(250,255,255);
				//			System.out.println("I'm a building with multiple functions");

			}else {
				p.fill(155,180,255);
			}
			break;

		case 1:
			int x = p.round(255*((buildingInfo.numberOfPeopleSeen/size)/(1+urbanModel.maximumBuildingInformation.numberOfPeopleSeen)));
			p.fill(105-x, 105-x,135-x);
			break;


		case 2:
			x = p.round(255*buildingInfo.averageAge/(1+urbanModel.maximumBuildingInformation.averageAge));
			p.fill(255-x, 255-x, x);
			break;

		case 3:
			x = p.round(255*buildingInfo.numberOfFloors/(1+urbanModel.maximumBuildingInformation.numberOfFunctions));
			p.fill(255-x, 255-x, x);
			break;

		case 4 :

			text = "totalPeople arrived = " + totalPedestriansArrived;
//			p.noStroke();
//						p.stroke(5);
			p.stroke(105,105,136,90);
			p.fill(25,27,39);

			break;
		case 5:
			text = "av.distance = " + totalDistanceTraveled/totalPedestriansArrived ;

			p.noFill();
			break;
		case 6:
			text = "av.speed = " + totalDistanceTraveled/(totalPedestriansArrived*totaltimeTraveled) ;
			p.noFill();
			break;
		case 7:
			text = "add distance euclid in % = "+ p.round(((totalDistanceTraveled/totalEuclidianDistanceofArrivals)-1)*100);
			p.noFill();
			break;
		case 8:
			if(totalEuclidianDistanceofArrivals!=0) {
				text = " metricDistance / euclidian in % = "+ ((totalMetricDistanceofArrivals*UrbanMap.gridSize*100)/totalEuclidianDistanceofArrivals);
			}
			p.fill(10,10,30);
			//			p.noFill();
			break;
		case 9:
			p.noFill();
			break;
		case 10:
			p.noFill();
		}


		//don't show OD
		p.fill(10,10,30,10);
		p.noStroke();
		
		//draw polyline
		if (vertexes.size()>1) { 		
			p.beginShape();
			for (int i = 0; i < vertexes.size(); i++) {
				vertex v = vertexes.elementAt(i);
				p.vertex(v.x,v.y);
			}
			p.endShape(PConstants.CLOSE);
		}else {
			p.noStroke();
			p.ellipse(centroid.x, centroid.y, 5, 5);
		}
		//		p.fill(255);

		//		buildingInfo.numberOfPeopleSeen = p.round (0.9*buildingInfo.numberOfPeopleSeen); XXX This is the problem

	}

	private void buildingReview() {
		// TODO Auto-generated method stub
		/*
		 * check what is wanted
		 * check in surrounding buildings for competition
		 * check for costs of chang
		 * 
		 * adapt or not
		 * 
		 */
	}

	public void emitPedestrians() {
		float x = centroid.x;
		float y = centroid.y;
		MovingAgents.pedestrians a = new MovingAgents.pedestrians(x, y, UrbanMap, p, buildings, obstacles, functions, urbanModel, myRef);
		//		here is a bug
		if(pedestrians.size()<urbanModel.maximumNrPedestrians) {
			pedestrians.add(a);
		}
	}

	public void drawHighlight() {
		p.fill(255,200,200,80);
		p.stroke(255,200,200);
		p.beginShape();
		for (int i = 0; i < vertexes.size(); i++) {
			vertex v = vertexes.elementAt(i);
			p.vertex(v.x,v.y);
		}
		p.endShape(PConstants.CLOSE);
	}

	public void drawRaces() {
		int totalNR = 1;
		float totalArchLength = 0;
		for (int i = 0; i< buildingInfo.customersRace.size(); i++){
			totalNR += buildingInfo.customersRace.elementAt(i).integer; 
		}


		for (int i = 0; i < buildingInfo.customersRace.size(); i++){
			float archLength =0;
			archLength = ((float)(buildingInfo.customersRace.elementAt(i).integer)/(float)(totalNR))*2*p.PI;
			p.fill(i*50,255-50*i,125);
			p.arc(centroid.x, centroid.y, PApplet.sqrt(totalNR)/10 +5 , PApplet.sqrt(totalNR)/10+5, totalArchLength,totalArchLength+archLength );
			totalArchLength += archLength;
			buildingInfo.customersRace.elementAt(i).integer *= 0.999;
		}
		int totalPeople = p.round(totalNR/100)*100;
		p.fill(200);
		p.text(totalPeople,centroid.x, centroid.y, 100);
	}

}
