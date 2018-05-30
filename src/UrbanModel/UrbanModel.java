package UrbanModel;

import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import Geometry.*;
import Importer.*;
import MovingAgents.*;
import processing.core.*;
import Graphics.*;
import NetVisual.*;

//import javax.media.opengl.*; 


public class UrbanModel extends PApplet{

	/**
	 * This Program has been produced by
	 * Gideon Daniel Philipp Alfred Aschwanden
	 * in the Future Cities Laboratory
	 * part of the ETH Zurich
	 */

	private static final long serialVersionUID = 6431620651950366422L;

	// Global Variables
//	h = devices[0].getDisplayMode().getHeight();
//	GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
//	GraphicsDevice devices[] = environment.getScreenDevices();
	public int height = 2560;/*860	/*1080*4*/; //big display 1440/2560 ; HD 1080/1920; VGA 768/1024
	public int width = 1440;/*1440 /*1920*4*/;
	float gridSize = 2;
	public float proj_min_y;
	public float proj_max_y;
	public float proj_min_x;
	public float proj_max_x;

	PApplet p = this;
	UrbanMap UrbanMap;
	sliderPedestrian pedestrianSlider;
	public buildingInformation maximumBuildingInformation;
	public PImage imageObject;
	public PImage pollutionMap;

	Vector<pedestrians> pedestrians = new Vector<pedestrians>();
	public Vector<solidObstacle> obstacles = new Vector<solidObstacle>();
	public Vector<softObstacle> streets = new Vector<softObstacle>();
	public Vector<softObstacle> obstaclesS = new Vector<softObstacle>();
	public Vector<building> buildings = new Vector<building>();
	Vector<sliders> sliders = new Vector<sliders>();
	Vector<Button> buttons = new Vector<Button>();
	Vector<publicTransportStop> publicTransportStops = new Vector<publicTransportStop>();
	Vector<Trace> pollutions_current = new Vector<Trace>();
	Vector<Trace> pollutions_noPref = new Vector<Trace>();
	Vector<Geometry.vertex> pollutions_points_noPref = new Vector<Geometry.vertex>();
	Vector<Trace> pollutions_Backalley = new Vector<Trace>();
	Vector<Geometry.vertex> pollutions_points_Backalley = new Vector<Geometry.vertex>();
	Vector<Trace> pollutions_Streets = new Vector<Trace>();
	Vector<Geometry.vertex> pollutions_points_Streets = new Vector<Geometry.vertex>();
	PVector averageLine = new PVector();


	provisoryPolygone provisoryPolygone = null;

	Thread L;
	public int backgroundDraw = 3; //Setting for background drawing
	public boolean setBounds = true;
	public Vector<String> functions;
	public int  vari = 3;

	public int buildingDrawingCondition = 4;
	private int maximumPeopleSeen = 0;

	public int firstNumberOfObstacles = 1000;
	private boolean emmittingPedestrians = true;
	public int maximumNrPedestrians = 200;
	private float totalBuildingSize;
	private float averageBuildingSize;
	private boolean drawRace = false;

	private String textTitel = "this is the beginning";
	private String textBuildings = "";

	private boolean drawPed = true;

	private boolean polygoneSelected;
	private int polygoneSelectedID;
	private boolean movingPolygone;
	private int origineX;
	private int origineY;

	private softObstacle s;
	private int deltaX = 0;
	private int deltaY = 0;

	private EZLink_loader EZ;
	public boolean followAgents = false;
	private int maxAge;
	private float maxDistance = 1500;
	private float maxPollution = 6500;
	private int myTotalPollution;
	private float allMAXTotalPollution = 2500;
	private float xNull = height-100;
	private int yNull = 10;
	private float myDistance;

	private int frameSize=20;
	private float maxInstantPollution = 6500;
	private float minDistance = 999999;

	private float deltaDistance = 1;

	private float allMINTotalPollution = 999999;
	private float deltaTotalPollution = 100;
	private float plotDiameter;
	private float minTotalDistance = 999999;
	private int plotDiameterW;
	private int loadcycle = 0;
	public float orig_min_y;
	public float orig_min_x;
	public float orig_max_x;
	public float orig_max_y;

	private boolean cmd = false;
	private boolean alt = false;
	private boolean ctrl = false;
	private boolean shift = false;
	private boolean drawsidebar = true;
	public float scale = 1;
	public String backgroundText = "START";
	private boolean printImage = false;

	List<statisticPoints> statisticPointsContainer = new Vector<statisticPoints>();
	private int statisticBinSize = 300;
	private statisticBins[] statistic = new statisticBins[statisticBinSize];

	private int xMax = 2;
	private int yMax = 10000;



	private String[][] differentPollutantscontent;

	private int numerater = 1;




	///////////////////////////////////////////////////////////////////
	//			Setup
	///////////////////////////////////////////////////////////////////

	public void setup() 
	{
		//		size(width,height, OPENGL);

		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice devices[] = environment.getScreenDevices();
		height = devices[0].getDisplayMode().getHeight();
		width = devices[0].getDisplayMode().getWidth();
		
		size(width,height,P2D);
		p.rectMode(CENTER);
		UrbanMap = new UrbanMap(gridSize, PApplet.round(width/gridSize+1), PApplet.round(height/gridSize+1),this, buildings, pedestrians, obstacles, obstaclesS);

		maximumBuildingInformation = new buildingInformation();
		initializeMaxBuildingInformation();

		sliders.addElement(new sliders(width-300, (50+40*sliders.size()), 200,20, 10,1, 40, "Pedestrian Visionlength", this));
		sliders.addElement(new sliders(width-300, (50+40*sliders.size()), 200,20, 8,1, 50, "Pedestrian VisionRays", this));
		sliders.addElement(new sliders(width-300, (50+40*sliders.size()), 200,20, maximumNrPedestrians,0, 5000, "Number of People", this));
		buttons.add(new Button("load Obstacles",this, width-300, (70+80*sliders.size())+40*buttons.size() ));
		buttons.add(new Button("load Buildings",this, width-300, (70+80*sliders.size())+40*buttons.size() ));
		buttons.add(new Button("load Streets",this, width-300, (70+80*sliders.size())+40*buttons.size() ));
		buttons.add(new Button("load SoftObstacles",this, width-300, (70+80*sliders.size())+40*buttons.size() ));
		pedestrianSlider = new sliderPedestrian(width-200, height*3/4, 200,200,20,this,10,PI);
		imageObject = p.loadImage("/Users/gaschwanden/Documents/workspace/UrbanModel_Playground/data/background_II.jpg");
		pollutionMap = p.loadImage("/Users/gaschwanden/Documents/workspace/UrbanModel_Playground/data/background_SUN.jpg");


		for (int i = 0; i<statisticBinSize;i++) {
			statistic[i]= new statisticBins();
		}

		if(height<width) {
			plotDiameter = (height*1.0f-2*frameSize);
			plotDiameterW = (width-2*frameSize);
		}else {
			plotDiameter = (width*1.0f-2*frameSize);
			plotDiameterW = (width-2*frameSize);
		}


	}

	public void draw() 
	{
		
		
		//		if(maximumNrPedestrians<10000) {
		//			maximumNrPedestrians += 5;
		//		}
//		if(frameCount == 2000) {
//			backgroundDraw=3;
//			printImage=true;
//		}
//		if(frameCount == 2600) {
//			printImage=false;
//		}
		translate(width/2, height/2);
		p.scale(scale);
		translate(-width/2, -height/2);
		if(backgroundDraw==2) {
			background(10,15,20);
		}else {
			background(0);
		}

		//////////////////////////////////
		// UrbanMap is drawn
		//////////////////////////////////
		if(backgroundDraw==6) {
			if(imageObject != null)	image(imageObject,0,0,width,height);
		}else if(backgroundDraw==7){
			drawStatistics();
		}else if(backgroundDraw==8){

			if(pollutionMap != null) {
				image(pollutionMap,0,0,width,height);
			}
		}else {
			UrbanMap.drawMe(backgroundDraw,vari);
		}


//		p.smooth();

		strokeWeight(1);
		if (L == null || !L.isAlive()) {
			if(backgroundDraw==7 || backgroundDraw ==8 || backgroundDraw==0) {

			}else {


				//////////////////////////////////
				//		Drawing softObstacles	//
				//////////////////////////////////

				for (int i = 0; i < obstaclesS.size(); i++) {
					s = obstaclesS.elementAt(i);
					s.drawMe();
				}


				//////////////////////////////////
				// Drawing Highlight
				//////////////////////////////////
				if(polygoneSelected) {
					if(polygoneSelectedID>buildings.size()) {
						solidObstacle o = obstacles.elementAt(polygoneSelectedID-firstNumberOfObstacles);
						o.drawHighlight();

					}else {
						building b = (building)buildings.elementAt(polygoneSelectedID);
						b.drawHighlight();
					}
				}

				//////////////////////////////////
				//		 Pedestrians			//
				//////////////////////////////////
				if(backgroundDraw==7 /*|| backgroundDraw ==8*/){
				}else {
					for (int i = 0; i < pedestrians.size(); i++) {

						MovingAgents.pedestrians p = pedestrians.elementAt(i);
						if(p.pedestrianArrived) {
							//				PApplet.println("removing Pedestrian "+ i);
							if(p.timeTraveled>200000) {
								if(UrbanMap.targetHitTest(p.pixx, p.pixy) != -2) {
									//																	p.putPersonalFinalDestination(UrbanMap.targetHitTest(p.pixx, p.pixy), p); // Neglect pedestrians who don't find an exit 
									if(p.myTotalPollution>0) {
										p.myPollution.totalValue = p.myTotalPollution;
										pollutions_current.add(p.myPollution);
										if(pollutions_current.size()%100==0)System.out.println("number of traces = "+pollutions_current.size());
										if(pollutions_current.size()==5000) {
											addTracesToPoints(pollutions_current);
											reset();
										}
									}
								}
							}

							//							System.out.println("distance ="+p.distance+"; Goal id = "+p.goalID+"; arrived? "+p.pedestrianArrived);
							pedestrians.remove(p);

						}else {
							if(drawPed)p.drawMe();

							p.move(followAgents);

							if (backgroundDraw==5) {
								strokeWeight(1);
								polygone trace = p.myPollution.myPolygone;
								Vector<vertex> vertexes = trace.vertexes;
								float paint = (1.0f*p.myTrace.origineID)/(1.0f*buildings.size());
								if(p.myTrace.age>maxAge)maxAge=p.myTrace.age;
								//						float alfa = 70-((p.myTrace.age*60.0f)/(1.0f*maxAge));
								stroke(255*paint,255-255*paint,255*paint/2+120,80);
								//						if(p.goalID%2==1) {
								//							stroke(80,80,255,90);
								//						}else {
								//							stroke(253,50,50,90);
								//						}
								//						stroke(253,225,98,90);
								noFill();
								beginShape();
								for (int jj = 0; jj < vertexes.size(); jj++) {
									vertex v = vertexes.elementAt(jj);
									curveVertex(v.x,v.y);
								}
								vertex(p.x, p.y);
								endShape(PConstants.OPEN);

								if(p.timeTraveled>10000) {
									System.out.println("2000 times walked");
									pedestrians.remove(p);
								}
							}
						}
					}
				}

				//////////////////////////////////
				//		Drawing Obstacles		//
				//////////////////////////////////

				for (int i = 0; i < obstacles.size(); i++) {
					solidObstacle o = (solidObstacle)obstacles.elementAt(i);
					o.drawMe(backgroundDraw);
				}

				//////////////////////////////////
				// Drawing buildings
				//////////////////////////////////

				for (int i = 0; i < buildings.size(); i++) {
					building b = (building)buildings.elementAt(i);
					b.drawMe(buildingDrawingCondition);
					b.drawBuildingConditions(buildingDrawingCondition);

					if(drawRace)b.drawRaces();
				}

				if(emmittingPedestrians) {
					float emitNRpedestrians = (maximumNrPedestrians-pedestrians.size())/(1+buildings.size());
					int starrter = (int)random(buildings.size());
					for (int i = 0; i < buildings.size(); i++) {
						int k = (starrter+i)%buildings.size();
						building b = (building)buildings.elementAt(k);
						if(pedestrians.size()<maximumNrPedestrians) {

							for(int ii = 0; ii < emitNRpedestrians ; ii++) {
								b.emitPedestrians();
							}
						}
					}
				}

				//////////////////////////////////
				// Drawing provisoryPolygone only draws the outlines
				//////////////////////////////////
				if (provisoryPolygone != null){
					if (polygoneSelected) {
						deltaX =  mouseX-origineX;
						deltaY =  mouseY-origineY;
					}
					provisoryPolygone.drawMe(polygoneSelected, deltaX, deltaY);
				}
			}
		}

		//////////////////////////////////
		// Drawing Mouse Information
		//////////////////////////////////
		//				drawMouseInformation();

		//////////////////////////////////
		// Drawing Screen Information
		//////////////////////////////////

		p.scale(1/scale);
		if(drawsidebar)drawScreanInformation();
		if(printImage) {
			int frameNR = frameCount;
			save("/Users/gaschwanden/Desktop/Projects/2015_MelbUni_Pedestrians/PIC/"+frameNR+"_MELBOURNE_exisiting.JPG");
						printImage = false;
		}else {

		}
	}


	private void reset() {
//		boolean pedestriansEmpty = false;
		setBounds = true;


		L = null;

		//				for (int i =0; i< buildings.size(); i++) {
		//					building b = buildings.elementAt(i);
		//					b.totalDistanceTraveled = 0;
		//					b.totaltimeTraveled = 0;
		//					b.totalPedestriansArrived = 0;
		//					b.totalEuclidianDistanceofArrivals = 0;
		//					b.totalMetricDistanceofArrivals =0;
		//				}

		for (int i = 0; i<width/gridSize;i++) {
			for(int j = 0; j<height/gridSize; j++) {
				UrbanMap.footFall[i][j] = 0;
				UrbanMap.ObstructionMap[i][j]=0;
				UrbanMap.density[i][j]=0;
				UrbanMap.footFall[i][j]=0;
				UrbanMap.Softobstruction[i][j]=0;
				UrbanMap.ObjectID[i][j]=0;
				for(int ii = 0; ii<buildings.size();ii++) {
					UrbanMap.distance[ii][i][j]=0;
				}
			}
		}
		buildings.removeAllElements();
		pedestrians.removeAllElements();
		if(pollutions_noPref.size()==0) {
			pollutions_noPref.addAll(pollutions_current);

		}else {
			if(pollutions_Streets.size()==0) {
				pollutions_Streets.addAll(pollutions_current);
			} else {
				pollutions_Backalley.addAll(pollutions_current);
				addTracesToPoints(pollutions_current);
			}
		}
		pollutions_current.removeAllElements();
		obstacles.removeAllElements();
		streets.removeAllElements();
		backgroundDraw = 6;

		//			minTotalDistance = 0;
		//			allMAXTotalPollution = 7000;
		//			allMINTotalPollution = 0;	
	}

	private void addTracesToPoints(Vector<Trace> pollutions_current) {
		differentPollutantscontent = new String[pollutions_current.size()+1][yMax];
		for (int i = 0; i<pollutions_current.size(); i++) {
			Trace t = pollutions_current.elementAt(i);

			for (int j = 0; j<t.myPolygone.vertexes.size(); j++) {
				if (j==0) {
					myDistance = 0;
					myTotalPollution = 0;
				}else {
					float xDist = t.myPolygone.vertexes.elementAt(j-1).x-t.myPolygone.vertexes.elementAt(j).x;
					float yDist = t.myPolygone.vertexes.elementAt(j-1).y-t.myPolygone.vertexes.elementAt(j).y;
					myDistance = myDistance+(float) Math.sqrt((xDist*xDist)+(yDist*yDist));
					myTotalPollution +=  t.myPolygone.vertexes.elementAt(j).z;				
				}
				// add information from trace to the statistic bin after the last vertex
				if(j==t.myPolygone.vertexes.size()-1) {
					statisticPoints sPoint = new statisticPoints(myDistance, myTotalPollution);
					numerater ++;
					//			String myDistString="\""+myDistance+"\"";
					//					System.out.println(numerater+"/0 = "+nf(myDistance,10,0)+" = "+str(myDistance));
					//					System.out.println(numerater+"/1 = "+nf(myTotalPollution,10,0));
					differentPollutantscontent[numerater][0]=str(myDistance);
					differentPollutantscontent[numerater][1]=str(myTotalPollution);
					int bin = (int)((myDistance/maxDistance)*statisticBinSize);
					if(bin<statisticBinSize)statistic[bin].addElement(sPoint);
				}
			}
			//remove the trace 
			pollutions_current.remove(t);
		}

		generateCsvFile();
		backgroundDraw=7;
		printImage =true;
	}

	private void drawStatistics() {

		stroke(255,255,150,90);

		String StisticVectorName ="";



		noFill();
		if (pollutions_current != null && vari%4 == 0) {
			/*
			 * to draw with a vector<traces> "pollutions_current"
			 * drawPollutants(pollutions_current,1);
			 */

			drawPollutants();
			StisticVectorName = pollutions_current.size()+" pollutions_current  ";
		}
		if (pollutions_noPref != null && vari%4 == 1) {
			drawPollutants();
			StisticVectorName = pollutions_noPref.size()+" pollutions_noPref  ";
		}
		if (pollutions_Backalley != null && vari%4 == 2) {
			drawPollutants();
			StisticVectorName = pollutions_Backalley.size()+" pollutions_Backalley  ";
		}
		if (vari%4 == 3) {
			drawPollutants();
			StisticVectorName = pollutions_Backalley.size()+"/"+pollutions_noPref.size()+"/"+pollutions_current.size()+" = All  "+pollutions_Backalley.size()+pollutions_noPref.size()+pollutions_current.size()+"    ";
		}




		String  StatisticFrom = 0+" / "+ 0;
		String  StatisticTo =  StisticVectorName+(int) (maxDistance)+" / "+ (int) (allMAXTotalPollution);
		p.fill(255,255,255);
		text(StatisticFrom, frameSize+50,plotDiameter);
		text(StatisticTo, frameSize+plotDiameterW-100,frameSize);
	}

	private void drawPollutants() {

		fill(255,255,0,100);
		for (int i= 0; i<statisticBinSize;i++) {
			statistic[i].calculateStatistics();
			float uhu = i*plotDiameterW/statisticBinSize;
			strokeWeight(1);
			stroke(40);
			line(frameSize+uhu,frameSize, frameSize+uhu,height);
			if(i%5==0)text((int)(i*maxDistance/statisticBinSize), frameSize+uhu, 50);
			for(int j = 0; j<statistic[i].pollutions.size();j++) {
				statisticPoints statP = statistic[i].pollutions.get(j);
				//				System.out.println("statP"+statP.x+"/"+statP.y);
				float x = frameSize+(statP.distance*(plotDiameterW/maxDistance));
				float y = (height-frameSize)-(statP.pollution*(plotDiameter/allMAXTotalPollution));
				//				System.out.println("point = "+x+"/"+y);
				//				stroke(Colors.getRedWhiteBlue(statisticBinSize, i)[0],Colors.getRedWhiteBlue(statisticBinSize, i)[1],Colors.getRedWhiteBlue(statisticBinSize, i)[2],50);
				noStroke();
				fill(Colors.getRedWhiteBlue(statisticBinSize, i)[0],Colors.getRedWhiteBlue(statisticBinSize, i)[1],Colors.getRedWhiteBlue(statisticBinSize, i)[2],90);
				ellipse(x, y, 4, 4);
			}
		}

		//draw average Line
		fill(255,50,50);
		noStroke();
		beginShape();
		for (int i= 0; i<statisticBinSize;i++) {
			float x = frameSize+(plotDiameterW/statisticBinSize)*i;
			float average = statistic[i].average;
			//			System.out.println(i +" average = "+statistic[i].average+" +/-"+statistic[i].standartDeviation);
			float y = (height-frameSize) - ((average*plotDiameter)/allMAXTotalPollution);
			//			System.out.println(i+" average ="+average);
			//			System.out.println("statistic[i].pollution.size() = "+statistic[i].pollutions.size());
			if(average>1)ellipse(x, y, 8, 8);
			if(i>=1) {
				stroke(255);
				line(frameSize+(plotDiameterW/statisticBinSize)*(i-1),height-frameSize-statistic[i-1].pollutions.size(),frameSize+(plotDiameterW/statisticBinSize)*(i),height-frameSize-statistic[i].pollutions.size());


			}
		}
		endShape();

		//draw deviations
		noFill();
		stroke(255,30,30,100);

		//		beginShape();
		strokeWeight(4);
		for (int i= 0; i<statisticBinSize;i++) {
			float average = statistic[i].average;
			float deviation = statistic[i].standartDeviation;
			float x =frameSize+ (plotDiameterW/statisticBinSize)*i;
			float upperBoundary = (height-frameSize) - ((average+deviation)*(plotDiameter/allMAXTotalPollution));
			float lowerBoundary = (height-frameSize) - (((average-deviation)*plotDiameter)/allMAXTotalPollution);
			if(average>1)line(x, upperBoundary, x, lowerBoundary);

		}
		strokeWeight(1);
		//		endShape();
		//		noFill();
		//		beginShape();
		//		stroke(10,10,255,100);
		//		for (int i= 0; i<statisticBinSize;i++) {
		//			float average = statistic[i].average;
		//			float deviation = statistic[i].standartDeviation;
		//			float x = (plotDiameterW/statisticBinSize)*i;
		//			if(average>1)curveVertex(x, lowerBoundary);
		//		}
		//		endShape();
	}

	private void drawPollutants(Vector<Trace> pollutantsDrawing, int number) {
		float xPlotPollutionPedestrian = 0;
		float yPlotPollutionPedestrian = 0;

		averageLine.x = 0;
		averageLine.y = 0;
		averageLine.z = 0;

		if(pollutantsDrawing!=null && pollutantsDrawing.size()>2) {

			for (int i = 0; i<pollutantsDrawing.size(); i++) {
				Trace t = pollutantsDrawing.elementAt(i);
				for (int j = 0; j<t.myPolygone.vertexes.size(); j++) {
					if (t.myPolygone.vertexes.elementAt(j).z>maxPollution)maxPollution=t.myPolygone.vertexes.elementAt(j).z;
					if (t.myPolygone.vertexes.elementAt(j).z<maxPollution);
					if (t.myPolygone.vertexes.elementAt(j).z>maxInstantPollution)maxInstantPollution = t.myPolygone.vertexes.elementAt(j).z;
				}
			}

			for (int i = 0; i<pollutantsDrawing.size(); i++) {
				Trace t = pollutantsDrawing.elementAt(i);
				if (t.totalValue==0)pollutantsDrawing.remove(t);
				noFill();
				//				beginShape();
				for (int j = 0; j<t.myPolygone.vertexes.size(); j++) {
					stroke(255,255,150,30);
					if (j==0) {
						myDistance = 0;
						myTotalPollution = 0;
					}else {
						float xDist = t.myPolygone.vertexes.elementAt(j-1).x-t.myPolygone.vertexes.elementAt(j).x;
						float yDist = t.myPolygone.vertexes.elementAt(j-1).y-t.myPolygone.vertexes.elementAt(j).y;
						myDistance = myDistance+(float) Math.sqrt((xDist*xDist)+(yDist*yDist));
						myTotalPollution +=  t.myPolygone.vertexes.elementAt(j).z;
						if( j == t.myPolygone.vertexes.size()-1) {
							if(myDistance>maxDistance)maxDistance = myDistance;
							if(myDistance<minDistance && minDistance > 10 )minDistance = myDistance;
							if(myDistance<minTotalDistance && myDistance > 10)minTotalDistance = myDistance;
						}
						if(maxDistance<=0)maxDistance=1;
						deltaDistance = maxDistance-minDistance;
						if (deltaDistance <=0) deltaDistance = 100;
						float xStat = height-frameSize-(t.myPolygone.vertexes.elementAt(j).z*plotDiameter/maxInstantPollution); 
						float yStat = frameSize+plotDiameterW  + myDistance*plotDiameterW/(maxDistance);

						//						vertex(yStat,xStat);
					}
				}
				//				endShape();


				if(t.totalValue>allMAXTotalPollution) {
					allMAXTotalPollution = t.totalValue;
					deltaTotalPollution = allMAXTotalPollution-allMINTotalPollution;
				}
				if(t.totalValue<allMINTotalPollution) {
					allMINTotalPollution = t.totalValue;
					deltaTotalPollution = allMAXTotalPollution-allMINTotalPollution;
				}
				if (deltaTotalPollution<=0)deltaTotalPollution=100;

				xPlotPollutionPedestrian  = frameSize+(myDistance/maxDistance)*(plotDiameterW+frameSize);
				yPlotPollutionPedestrian = height-frameSize - (t.totalValue/allMAXTotalPollution)*plotDiameter;
				noStroke();
				// do colors here
				int y = buildings.size();
				int x = t.destinationID;
				//				p.fill(255, 255, 255, 100);
				//				p.println("pedestrian nr"+i+" walked "+xPlotPollutionPedestrian+"/"+yPlotPollutionPedestrian);
				//				p.println("distance = "+myDistance+"/"+maxDistance+" pollutants "+ t.totalValue+"/"+allMAXTotalPollution+")*"+plotDiameter);

				fill(Colors.getRedWhiteBlue(3, number)[0],Colors.getRedWhiteBlue(3, number)[1],Colors.getRedWhiteBlue(3, number)[2],50);
				p.ellipse(xPlotPollutionPedestrian, yPlotPollutionPedestrian, 4, 4);	
				averageLine.x = averageLine.x +myDistance;
				averageLine.z = averageLine.z + 1;
				averageLine.y = averageLine.y + t.totalValue;
			}
			float ratio = 0;
			p.stroke(250);

			if(averageLine.y != 0) {
				ratio = averageLine.x/averageLine.y;
			}
			//			p.line(frameSize, height-frameSize, frameSize + plotDiameterW, height/2-frameSize + (height/2-frameSize)*ratio);
			p.fill(255,255,255,80);
			//				System.out.println("x= "+averageLine.x+" x="+averageLine.y+" z="+averageLine.z);
			float x = frameSize+((averageLine.x/averageLine.z)/maxDistance)*(plotDiameterW+frameSize);
			float y = height-frameSize - ((averageLine.y/averageLine.z)/allMAXTotalPollution)*plotDiameter;
			p.ellipse(x, y, 10, 10);
		}
	}

	private void drawMouseInformation() {
		String text2 = "nothing";
		int ttt = 0;

		ttt = UrbanMap.targetHitTest(PApplet.round(mouseX/gridSize), PApplet.round(mouseY/gridSize));

		text(ttt, mouseX, mouseY);
	}

	private void drawScreanInformation() {

		// Backdrop
		fill(100,100,100,90);
		noStroke();
		p.rect(width-400, 0, 500, height, 7);
		//		p.rect(width-400, 0, 500, height, 7, 7);

		//Text
		fill(255,255,255);
		textAlign(LEFT);
		text(textTitel, width-380, 25);

		//		// Building nr etc
		//		for (int i = 0; i < buildings.size(); i++) {
		//			building b = (building)buildings.elementAt(i);
		//			textBuildings = b.text;
		//			text( textBuildings,width - 350, 50 + i*15);
		//		}

		//Buttons
		for (int i=0; i<buttons.size();i++) {
			Button s = buttons.elementAt(i);
			s.drawme();
		}


		//Sliders
		for (int i=0; i<sliders.size();i++) {
			sliders s = sliders.elementAt(i);
			s.drawMe();
		}
		pedestrianSlider.drawMe();
		float jj = frameRate;
		text(jj, 50, height-50);
		text(pedestrians.size(), 50, height-30);
		text(backgroundText, 100, height-75);
	} 

	public static void main(String args[])  {
		//////////////////////////////////
		// To make it a running jar 	//
		//////////////////////////////////
		PApplet.main(new String[] { "--present", "UrbanModel.UrbanModel" });
		//		SwingUtilities.invokeLater(new Runnable() {
		//			public void run() {
		//				new JFrame("frame1").setVisible(true);
		//				new JFrame("frame2").setVisible(true);
		//			}
		//		});
		new UrbanModel();
	}

	private void initializeMaxBuildingInformation() {
		maximumBuildingInformation.averageAge = 0;
		maximumBuildingInformation.numberOfPeopleSeen = 0;
		maximumBuildingInformation.numberOfFunctions = 0;
		maximumBuildingInformation.numberOfFloors = 0;		
	}

	public void updateBuildingInfo() {
		totalBuildingSize = 0;
		for (int i = 0; i < buildings.size(); i++){
			totalBuildingSize=+ buildings.elementAt(i).size;
		}

		averageBuildingSize = totalBuildingSize/buildings.size();		
	}

	public void keyReleased(KeyEvent e) {
		if (157 == e.getKeyCode()) {
			cmd = false;
		}
		if (18 == e.getKeyCode()) {
			alt = false;
		}
		if (17 == e.getKeyCode()) {
			ctrl = false;
		}
		if (16 == e.getKeyCode()) {
			shift = false;
		}
	}

	public void keyPressed(KeyEvent e) {

		//		System.out.println(e.getKeyCode());

		key = e.getKeyChar();
		if (157 == e.getKeyCode()) {
			cmd = true;
		}
		if (18 == e.getKeyCode()) {
			alt = true;
		}
		if (17 == e.getKeyCode()) {
			ctrl = true;
		}
		if (16 == e.getKeyCode()) {
			shift = true;
		}

		if(cmd) {
			if(key == 'l'){
				System.out.println("lets go big");
				Evaluation E = new Evaluation(buildings, obstacles, obstaclesS, streets, this,this);
				E.total();

			}
		}
		else {
			if(key == 'c'){
				EZ = new EZLink_loader(this);
				EZ.loadTrips();
			}
			if(key == 'd') {
				if(drawsidebar) {
					drawsidebar=false;
				}else {
					drawsidebar=true;
				}
			}

			if(key == 'e'){
				if(emmittingPedestrians){
					emmittingPedestrians = false;
				}else{
					emmittingPedestrians = true;
				}
			}

			if(key == 'g'){
				buildingDrawingCondition--;
				if (buildingDrawingCondition<0)buildingDrawingCondition=10;
				if(buildingDrawingCondition == 0) {
					println("buildings are colored as multi functional or not");
					textTitel="Buildings are colored as multi functional or not";
				}
				if(buildingDrawingCondition == 1) {
					println("buildings are colored showing how many people have seen it");
					textTitel="buildings are colored showing how many people have seen it";
				}
				if(buildingDrawingCondition == 2) {
					println("buildings are colored showing the average age of people seeing it");
					textTitel="buildings are colored showing the average age of people seeing it";
				}
				if(buildingDrawingCondition == 3) {
					println("buildings are colored showing the number of floors");
					textTitel="buildings are colored showing the number of floors";
				}
				if(buildingDrawingCondition == 4) {
					println("buildings showing how many people arrived in NR");
					textTitel="buildings showing how many people arrived in NR";
				}
				if(buildingDrawingCondition == 5) {
					println("buildings showing how far they walked in grids");
					textTitel="buildings showing how far they walked in grids";
				}
				if(buildingDrawingCondition == 6) {
					println("buildings showing how fast they walked in grids/timestep");
					textTitel="buildings showing how fast they walked in grids/timestep";
				}
				if(buildingDrawingCondition == 7) {
					println("buildings showing the additional distance traveled from euclidian");
					textTitel="buildings showing the additional distance traveled from euclidian";
				}
				if(buildingDrawingCondition == 8) {
					println("buildings showing the additional distance metric/euclid");
					textTitel="buildings showing the additional distance metric/euclid";
				}
				if(buildingDrawingCondition == 9) {
					println("buildings showing distance to other buildings");
					textTitel="buildings showing distance to other buildings";
				}

			}
			if(key == 'h'){
				buildingDrawingCondition++;
				if (buildingDrawingCondition>10)buildingDrawingCondition=0;
				if(buildingDrawingCondition == 0) {
					println("buildings are colored as multi functional or not");
					textTitel="Buildings are colored as multi functional or not";
				}
				if(buildingDrawingCondition == 1) {
					println("buildings are colored showing how many people have seen it");
					textTitel="buildings are colored showing how many people have seen it";
				}
				if(buildingDrawingCondition == 2) {
					println("buildings are colored showing the average age of people seeing it");
					textTitel="buildings are colored showing the average age of people seeing it";
				}
				if(buildingDrawingCondition == 3) {
					println("buildings are colored showing the number of floors");
					textTitel="buildings are colored showing the number of floors";
				}
				if(buildingDrawingCondition == 4) {
					println("buildings showing how many people arrived in NR");
					textTitel="buildings showing how many people arrived in NR";
				}
				if(buildingDrawingCondition == 5) {
					println("buildings showing how far they walked in grids");
					textTitel="buildings showing how far they walked in grids";
				}
				if(buildingDrawingCondition == 6) {
					println("buildings showing how fast they walked in grids/timestep");
					textTitel="buildings showing how fast they walked in grids/timestep";
				}
				if(buildingDrawingCondition == 7) {
					println("buildings showing the additional distance traveled from euclidian");
					textTitel="buildings showing the additional distance traveled from euclidian";
				}
				if(buildingDrawingCondition == 8) {
					println("buildings showing the additional distance metric/euclid");
					textTitel="buildings showing the additional distance metric/euclid";
				}
				if(buildingDrawingCondition == 9) {
					println("buildings showing distance to other buildings");
					textTitel="buildings showing distance to other buildings";
				}
			}

			if(key == 'l'){
				loadcycle ++;
				if (L == null) {
					L = new Thread(new load(width, height, provisoryPolygone, this, obstacles, buildings, obstaclesS,streets, UrbanMap, gridSize, this, pedestrians, loadcycle));
					System.out.println("lets load");
					L.start();
				}else {
					setBounds = false;
					System.out.println("Thread L is getting null");
					System.out.println("Incredible Complex Calculations getting calculated");
					L=null;
					L = new Thread(new load(width, height, provisoryPolygone, this, obstacles, buildings, obstaclesS,streets, UrbanMap, gridSize, this, pedestrians, loadcycle));
					L.start();
				}
			}
			if(key == 's'){
				println("saving pollutants");
				println("");
				println("size of Pollutions = "+pollutions_current.size());
				println("size of Pollutions_old = "+pollutions_noPref.size());
				if(pollutions_current.size() > 0) {
					pollutions_noPref.addAll(pollutions_current);
					pollutions_current.removeAllElements();
				}
				println("size of Pollutions = "+pollutions_current.size());
				println("size of Pollutions_old = "+pollutions_noPref.size());
			}
			if(key == 't') {
				if(pollutions_current.size() > 0) {
					pollutions_Backalley.addAll(pollutions_current);
					pollutions_current.removeAllElements();
				}
			}

			//			if(key == 'tt') {
			//				
			//			}
			if (key == 'm') {
				backgroundDraw++;
				if(backgroundDraw>9)backgroundDraw=0;
				if(backgroundDraw==0)backgroundText = "drawing metric distance map of "+vari;
				if(backgroundDraw==1)backgroundText = "drawing targethittest ";
				if(backgroundDraw==2)backgroundText = "drawing Soft Walking Obstruction Map ";
				if(backgroundDraw==3)backgroundText = "drawing footFall ";
				if(backgroundDraw==4)backgroundText = "drawing density";
				if(backgroundDraw==5)backgroundText = "drawing traces";
				if(backgroundDraw==6)backgroundText = "drawing loaded Image";
				if(backgroundDraw==7) {
					//					for(int i = 0; i<statisticBinSize;i++) {
					//						statistic[i].calculateStatistics();
					//					}
					backgroundText = "drawing statistics";
				}
				if(backgroundDraw==8)backgroundText = "drawing Emission Map";
				System.out.println(backgroundText);
			}
			if (key == 'n') {
				backgroundDraw--;
				if(backgroundDraw<0)backgroundDraw=8;

				if(backgroundDraw==0)backgroundText = "drawing metric distance map of "+vari;
				if(backgroundDraw==1)backgroundText = "drawing targethittest ";
				if(backgroundDraw==2)backgroundText = "drawing Soft Walking Obstruction Map ";
				if(backgroundDraw==3)backgroundText = "drawing footFall ";
				if(backgroundDraw==4)backgroundText = "drawing density";
				if(backgroundDraw==5)backgroundText = "drawing traces";
				if(backgroundDraw==6)backgroundText = "drawing loaded Image";
				if(backgroundDraw==7) {
					for(int i = 0; i<statisticBinSize ;i++) {
						statistic[i].calculateStatistics();
					}
					backgroundText = "drawing statistics";
				}
				if(backgroundDraw==8)backgroundText = "drawing Emission Map";
				System.out.println(backgroundText);

			}
//			if (key == 'i') {
//				scale /= 1.01;
//			}
//
//			if (key == 'u') {
//				scale *= 1.01;
//			}

			if (key == 'j') {
				vari--;
				if (vari<0)vari = buildings.size()-1;
				PApplet.println("vari -- "+vari+1);
			}
			if(key == 'q'){
				provisoryPolygone = null;
			}

			if(key == 'p') {
				if(printImage) {
					printImage = false;

				}else {
					printImage =true;
				}
				//printScrean

				//			if(showPedestrians) {
				//				showPedestrians = false;
				//			}else {
				//				showPedestrians = true;
				//			}
				if(drawPed) {
										drawPed=false;
				}else {
					drawPed=true;
				}
			}

			if (key == 'k') {
				vari++;
				if (vari >= buildings.size())vari =1;
				PApplet.println("vari ++ "+vari+1);
			}


			if(key == 'r'){
				reset();

				//				statisticPoints sPoint = new statisticPoints(xStat, yStat);
				//				statisticPointsContainer.add(sPoint);	
				int bin = (int)((1000*statisticBinSize)/maxDistance);
				statistic[bin].addElement(new statisticPoints(1000,500));
				bin = (int)((700*statisticBinSize)/maxDistance);
				statistic[bin].addElement(new statisticPoints(700,700));
				bin = (int)((350*statisticBinSize)/maxDistance);
				statistic[bin].addElement(new statisticPoints(350,700));

			}
			if (key == 'v') {
				if(followAgents) {
					followAgents = false;
				}else {
					followAgents = true;
				}
				//			p.println("xMin = "+);
			}


			if (key == 'y') {
				int scale = Math.round(72/gridSize);
				int orX = width/2-2*scale;
				int orY = height/2;
				orX = orX-6*scale;
				orY = orY+2*scale;
				for (int z = 0; z<10; z++) {
					provisoryPolygone = new provisoryPolygone(this);
					if (z < 6) {

						if (z==3) {
							orX = width/2-8*scale;
							orY = height/2-5*scale;
						}

						provisoryPolygone.addVertex(orX,orY);
						provisoryPolygone.addVertex(orX,orY+3*scale);
						provisoryPolygone.addVertex(orX+scale,orY+3*scale);
						provisoryPolygone.addVertex(orX+scale,orY);
						obstacles.addElement(new solidObstacle(provisoryPolygone, this));
						System.out.println("finished unsurmountable Obstacles at "+ obstacles.size());
						solidObstacle o = (solidObstacle)obstacles.elementAt(obstacles.size()-1);
						o.addObstacleMap(o, UrbanMap, gridSize, obstacles.size()+firstNumberOfObstacles);
						provisoryPolygone = null;
						orX = orX+6*scale;

					}else {
						if (z==7) {
							orX = width/2-12*scale;
							orY = height/2+4*scale;
							provisoryPolygone.addVertex(orX,orY);
							for (int i = 0; i<3;i++) {
								provisoryPolygone.addVertex(orX+i*6*scale+3*scale,orY);
								provisoryPolygone.addVertex(orX+i*6*scale+3*scale,orY+2*scale);
								provisoryPolygone.addVertex(orX+i*6*scale+6*scale,orY+2*scale);
								provisoryPolygone.addVertex(orX+i*6*scale+6*scale,orY);
							}
							provisoryPolygone.addVertex(orX+21*scale,orY);
							provisoryPolygone.addVertex(orX+21*scale,orY+6*scale);
							provisoryPolygone.addVertex(orX,orY+6*scale);
							obstacles.addElement(new solidObstacle(provisoryPolygone, this));
							System.out.println("finished unsurmountable Obstacles at "+ obstacles.size());
							solidObstacle o = (solidObstacle)obstacles.elementAt(obstacles.size()-1);
							o.addObstacleMap(o, UrbanMap, gridSize, obstacles.size()+firstNumberOfObstacles);
							provisoryPolygone = null;
						}
						if (z==8) {
							orX = width/2-12*scale;
							orY = height/2-3*scale;
							provisoryPolygone.addVertex(orX,orY);
							for (int i = 0; i<3;i++) {
								provisoryPolygone.addVertex(orX+i*6*scale+3*scale,orY);
								provisoryPolygone.addVertex(orX+i*6*scale+3*scale,orY+2*scale);
								provisoryPolygone.addVertex(orX+i*6*scale+6*scale,orY+2*scale);
								provisoryPolygone.addVertex(orX+i*6*scale+6*scale,orY);
							}
							provisoryPolygone.addVertex(orX+21*scale,orY);
							provisoryPolygone.addVertex(orX+21*scale,orY+6*scale);
							for (int i = 0; i<3;i++) {
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-3*scale,orY+6*scale);
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-3*scale,orY+6*scale-2*scale);
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-6*scale,orY+6*scale-2*scale);
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-6*scale,orY+6*scale);
							}
							provisoryPolygone.addVertex(orX+3*scale,orY+6*scale);
							provisoryPolygone.addVertex(orX,orY+6*scale);
							obstacles.addElement(new solidObstacle(provisoryPolygone, this));
							System.out.println("finished unsurmountable Obstacles at "+ obstacles.size());
							solidObstacle o = (solidObstacle)obstacles.elementAt(obstacles.size()-1);
							o.addObstacleMap(o, UrbanMap, gridSize, obstacles.size()+firstNumberOfObstacles);
							provisoryPolygone = null;
						}
						if (z==9) {
							orX = width/2-12*scale;
							orY = height/2-10*scale;
							provisoryPolygone.addVertex(orX,orY);
							provisoryPolygone.addVertex(orX+21*scale,orY);
							provisoryPolygone.addVertex(orX+21*scale,orY+6*scale);
							for (int i = 0; i<3;i++) {
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-3*scale,orY+6*scale);
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-3*scale,orY+6*scale-2*scale);
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-6*scale,orY+6*scale-2*scale);
								provisoryPolygone.addVertex(orX+21*scale-i*6*scale-6*scale,orY+6*scale);
							}
							provisoryPolygone.addVertex(orX+3*scale,orY+6*scale);
							provisoryPolygone.addVertex(orX,orY+6*scale);
							obstacles.addElement(new solidObstacle(provisoryPolygone, this));
							System.out.println("finished unsurmountable Obstacles at "+ obstacles.size());
							solidObstacle o = (solidObstacle)obstacles.elementAt(obstacles.size()-1);
							o.addObstacleMap(o, UrbanMap, gridSize, obstacles.size()+firstNumberOfObstacles);
							provisoryPolygone = null;
						}
					}

				}
				provisoryPolygone = new provisoryPolygone(this);
				provisoryPolygone.addVertex(width/2-15*scale-2,height/2-2);
				provisoryPolygone.addVertex(width/2-15*scale-2,height/2+3);
				provisoryPolygone.addVertex(width/2-15*scale+2,height/2+3);
				provisoryPolygone.addVertex(width/2-15*scale+2,height/2-2);
				buildings.addElement(new building(provisoryPolygone,this, buildings, obstacles, pedestrians, this));
				building b = (building)buildings.elementAt(buildings.size()-1);
				b.addObstacleMap(b, UrbanMap, gridSize);

				provisoryPolygone = null;
				provisoryPolygone = new provisoryPolygone(this);
				provisoryPolygone.addVertex(width/2+11*scale-2,height/2-3);
				provisoryPolygone.addVertex(width/2+11*scale-2,height/2+1);
				provisoryPolygone.addVertex(width/2+11*scale+2,height/2+1);
				provisoryPolygone.addVertex(width/2+11*scale+2,height/2-3);
				buildings.addElement(new building(provisoryPolygone,this, buildings, obstacles, pedestrians, this));
				building o = (building)buildings.elementAt(buildings.size()-1);
				o.addObstacleMap(o, UrbanMap, gridSize);
				updateBuildingInfo();
				//			provisoryPolygone = new provisoryPolygone(this);
				updateMetricDistanceMap();
				
			}

			if (key == 's') {
			}

			if(key == 't') {
				//						dataForCSV = metricDistanceTest();
				//										writeToCSV.writeToExcell(dataForCSV);
			}
		}

	}

	private void updateMetricDistanceMap() {
		for(int i = 0; i<buildings.size();i++) {
//			backgroundDraw =3;
//			vari = i;
			UrbanMap.metricDistanceMap(buildings, i);
		}
		
	}

	private void generateCsvFile()
	{
		try
		{
			Date date = new Date();
			System.out.println("generating CSV");

			@SuppressWarnings("deprecation")
			int month = date.getMonth();
			@SuppressWarnings("deprecation")
			int day = date.getDate();
			//			@SuppressWarnings("deprecation")
			//			int year = date.getYear();
			//			String sFileName = "C:/Users/m9/Desktop/NEAdata.2014."+month+"."+day+".csv";
			String sFileName = "/Users/GA/Desktop/Pedestrian_Statistics_2014."+month+"."+day+".csv";

			FileWriter writer = new FileWriter(sFileName);
			for (int i= 0; i<statisticBinSize;i++) {
				statistic[i].calculateStatistics();
			}

			for(int y =0; y< numerater;y++) {
				writer.append('\n');
				writer.append("Nr,average,variance,standartDeviation,max,min");
				writer.append('\n');
				for(int x=0; x<statisticBinSize;x++) {
					writer.append(str(x));
					writer.append(',');
					if(statistic[x] != null) {
						float max = -1;
						float min= 99999;
						writer.append(str(statistic[x].average));
						writer.append(',');
						writer.append(str(statistic[x].variance));
						writer.append(',');
						writer.append(str(statistic[x].standartDeviation));
						writer.append(',');
						for (int i = 0; i < statistic[x].pollutions.size()	; i++) {

							if(i==0) {
								max = -1;
								min = 99999;
							}
							statisticPoints p = statistic[x].pollutions.get(i);
							if(p.pollution>max)max=p.pollution;
							if(p.pollution< min)min=p.pollution;
							if(i == statistic[x].pollutions.size()-1) {
								writer.append(str(min));
								writer.append(',');
								writer.append(str(max));
								writer.append(',');
							}
						}
					}
					writer.append('\n');
				}
			}
			writer.append('\n');

			writer.flush();
			writer.close();
			//			getDate = false;
			System.out.println("CSV is ready");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} 
	}

	public void mouseDragged() {

		if(mouseX>(pedestrianSlider.originw-pedestrianSlider.w/2) && mouseX<(pedestrianSlider.originw+pedestrianSlider.w/2)  
				&& mouseY<(pedestrianSlider.originh+pedestrianSlider.w/2) && mouseY>(pedestrianSlider.originh-pedestrianSlider.h/2)) {
			provisoryPolygone = null;
			int x1 = mouseX-pedestrianSlider.originw;
			int y1 = mouseY-pedestrianSlider.originh;
			float dist = (float) Math.sqrt(x1*x1+y1*y1);
			int newVisionlength = PApplet.round(PApplet.map(dist, 0, pedestrianSlider.w, 0, pedestrianSlider.w/pedestrianSlider.g));
			pedestrianSlider.visionLength = newVisionlength;
			if(x1!=0) {	
				double v =  Math.toRadians(y1/x1);
				double w = Math.atan2(y1, x1);
				pedestrianSlider.fieldOfView = Math.abs((float)(2*w));
			}
		}

		for(int i=0; i<sliders.size();i++) {

			sliders s = sliders.elementAt(i);
			if(mouseX>s.originw && mouseX<s.originw+s.w && mouseY>s.originh && mouseY<s.originh+s.h) {
				provisoryPolygone = null;
				if(s.name == "Pedestrian Visionlength" ) {
					s.adjustSlider(mouseX);
					pedestrianSlider.visionLength = PApplet.round(s.value);
					Enumeration<pedestrians> pp = pedestrians.elements();
					while(pp.hasMoreElements())
						pp.nextElement().visionrayLength=PApplet.round(s.value);
				}
				if(s.name == "Pedestrian VisionRays" ) {
					s.adjustSlider(mouseX);
					pedestrianSlider.numberOfRays = PApplet.round(s.value);
					Enumeration<pedestrians> pp = pedestrians.elements();
					while(pp.hasMoreElements())
						pp.nextElement().numberOfVisionRays = PApplet.round(s.value);
				} 
				if(s.name == "Number of People" ) {

					s.adjustSlider(mouseX);
					maximumNrPedestrians = PApplet.round(s.value);
				}
			}
		}
	}

	public void mouseReleased() {

		movingPolygone = false;
	}

	public void mouseDoubleClicked() {
		System.out.println("hello double click");
	}

	public void mousePressed(){
		for(int i=0; i<buttons.size();i++) {
			Button b = buttons.elementAt(i);
			if (mouseX> b.x && mouseX< (b.x+b.w) && mouseY> b.y && mouseY< (b.y+b.h)) {
				System.out.println("found a button " + b.name);
				if (b.name =="load Obstacles") loadcycle=1;
				if (b.name =="load Buildings") loadcycle=3;
				if (b.name =="load Streets") loadcycle=4;
				if (b.name =="load SoftObstacles") loadcycle=2;


				L = new Thread(new load(width, height, provisoryPolygone, this, obstacles, buildings, obstaclesS,streets, UrbanMap, gridSize, this, pedestrians, loadcycle));
				L.start();
			}

		}
		//		keyCode = e.getKeyCode();
		//		if (key == CODED) {

		// Generating Buildings who emit pedestrian-agents
		if(key == 'p') {
			pedestrians.add(new pedestrians(mouseX, mouseY, UrbanMap, p, buildings, obstacles, functions, this, 99));
		}
		if (alt ) {
			if(polygoneSelected!=true && UrbanMap.targetHitTest(PApplet.round(mouseX/gridSize), PApplet.round(mouseY/gridSize))>0) {
				polygoneSelected =true;
				polygoneSelectedID = UrbanMap.targetHitTest(PApplet.round(mouseX/gridSize), PApplet.round(mouseY/gridSize))-1;
				if (polygoneSelectedID>buildings.size()) {
					solidObstacle o = obstacles.elementAt(polygoneSelectedID-firstNumberOfObstacles);
					provisoryPolygone = new provisoryPolygone(this);
					for (int i =0; i < o.vertexes.size(); i++) {
						vertex v = o.vertexes.elementAt(i);
						provisoryPolygone.addVertex(v.x, v.y);
					}
					origineX = mouseX;
					origineY = mouseY;


				}else {
					building b = buildings.elementAt(polygoneSelectedID);
					provisoryPolygone = new provisoryPolygone(this);
					for (int i =0; i < b.vertexes.size(); i++) {
						vertex v = b.vertexes.elementAt(i);
						provisoryPolygone.addVertex(v.x, v.y);
					}
					origineX = mouseX;
					origineY = mouseY;
				}
			}
			//				if (polygoneSelected && UrbanMap.targetHitTest(p.round(mouseX/gridSize), p.round(mouseY/gridSize))<=0) {
			//					polygoneSelected = false;
			//				}
			if (polygoneSelected !=true) {
				if (provisoryPolygone == null){
					System.out.println("drawing Building");
					provisoryPolygone = new provisoryPolygone(this);
					provisoryPolygone.addVertex(mouseX,mouseY);
				}else{
					vertex v = (vertex)provisoryPolygone.vertexes.elementAt(0);
					if (abs(v.x-mouseX)<10 && abs(v.y-mouseY)<10 && provisoryPolygone.vertexes.size() > 2) {
						buildings.addElement(new building(provisoryPolygone, this, buildings, obstacles, pedestrians, this));
						updateBuildingInfo();
						PApplet.println("adding Building nr"+buildings.size());
						building b = (building)buildings.elementAt(buildings.size()-1);
						//						p.println("Building b");
						b.addObstacleMap(b, UrbanMap, gridSize);
						//						p.println("has Obstacle map");
						updateMetricDistanceMap();

						// adding metricDistanceMap in different threat
						////						for(int i = 0; i<buildings.size();i++) {
						//							if (F == null || !F.isAlive()) {
						//								for(int i = 0; i<buildings.size();i++) {
						//								F = new Thread(new metricDistanceMaper(buildings, UrbanMap, i));
						//								
						//								F.start();
						//							}
						//						}
						//						if (F == null) {
						//							F = new Thread(new metricDistanceMaper(buildings, UrbanMap, i));
						//							for(int i = 0; i<buildings.size();i++) {
						//								}
						//							}
						//						else {
						//							for(int i = 0; i<buildings.size();i++) {
						//							F = new Thread(new metricDistanceMaper(buildings, UrbanMap, i));
						//							F.start();
						//							}
						//						}

						//						System.out.println("House #"+buildings.size()+" Constructed as "+ b.function);
						provisoryPolygone = null;
					}
					else 
					{
						provisoryPolygone.addVertex(mouseX,mouseY);
					}
				}
			}else {
				if(UrbanMap.targetHitTest(PApplet.round(mouseX/gridSize), PApplet.round(mouseY/gridSize))<=0) {
					/*
					 * we finalize the  moving the polygone
					 */
					deltaX =  mouseX-origineX;
					deltaY =  mouseY-origineY;
					if (polygoneSelectedID>buildings.size()) {

						solidObstacle b = obstacles.elementAt(polygoneSelectedID-firstNumberOfObstacles);
						b.removeObstacleMap(b, UrbanMap, gridSize);

						for (int i = 0; i < b.vertexes.size(); i++) {
							vertex v = b.vertexes.elementAt(i);
							v.x = v.x + deltaX;
							v.y = v.y + deltaY;
						}

						b.boundingBox = provisoryPolygone.getBoundingBox(b);

						deltaX = 0;
						deltaY = 0;

						polygoneSelected = false;
						provisoryPolygone =  null;
						b.addObstacleMap(b, UrbanMap, gridSize, obstacles.size()+firstNumberOfObstacles);
						updateMetricDistanceMap();

					}else {

						building b = buildings.elementAt(polygoneSelectedID);
						b.removeObstacleMap(b, UrbanMap, gridSize);

						for (int i = 0; i < b.vertexes.size(); i++) {
							vertex v = b.vertexes.elementAt(i);
							v.x = v.x + deltaX;
							v.y = v.y + deltaY;
						}

						b.centroid.x = b.centroid.x+deltaX;
						b.centroid.y = b.centroid.y+deltaY;

						for( int i = 0; i <b.midpoints.size(); i++) {
							vertex v = b.midpoints.elementAt(i);
							v.x = v.x + deltaX;
							v.y = v.y + deltaY;
						}

						b.boundingBox = provisoryPolygone.getBoundingBox(b);

						deltaX = 0;
						deltaY = 0;

						polygoneSelected = false;
						provisoryPolygone =  null;
						b.addObstacleMap(b, UrbanMap, gridSize);
						updateMetricDistanceMap();
					}
				}
			}
		}else {

		}

		// Generating Buildings without pedestrians
		if (shift) {
			if (provisoryPolygone == null){
				System.out.println("drawing Obstacles");
				provisoryPolygone = new provisoryPolygone(this);
				provisoryPolygone.addVertex(mouseX,mouseY);
			}else{
				vertex v = (vertex)provisoryPolygone.vertexes.elementAt(0);
				if (abs(v.x-mouseX) < 5 && abs(v.y-mouseY) <5 && provisoryPolygone.vertexes.size() > 2) {
					obstacles.addElement(new solidObstacle(provisoryPolygone, this));
					System.out.println("finished unsurmountable Obstacles at "+ obstacles.size());
					solidObstacle o = (solidObstacle)obstacles.elementAt(obstacles.size()-1);
					o.addObstacleMap(o, UrbanMap, gridSize, obstacles.size()+firstNumberOfObstacles);
					updateMetricDistanceMap();
					//						for(int i = 0; i<buildings.size();i++) {
					//							if (F == null || !F.isAlive()) {
					//								F = new Thread(new metricDistanceMaper(buildings, UrbanMap, i));
					//								F.start();
					//							}
					//						}
					provisoryPolygone = null;
				}
				else {
					provisoryPolygone.addVertex(mouseX,mouseY);
				}
			}
		}else {

		}




		// Generating Obstacle which is changing the iteration in the metricDistanceMap
		if (ctrl) {
			if (provisoryPolygone == null){
				System.out.println("drawing soft Obstacles");
				provisoryPolygone = new provisoryPolygone(this);
				provisoryPolygone.addVertex(mouseX,mouseY);
			}else{
				vertex v = (vertex)provisoryPolygone.vertexes.elementAt(0);
				if (abs(v.x-mouseX) < 10 && abs(v.y-mouseY) <10) {
					if(provisoryPolygone.vertexes.size()==2) {
						UrbanMap.addStreet(provisoryPolygone);

					}else {
						obstaclesS.addElement(new softObstacle(provisoryPolygone,this, UrbanMap));
						System.out.println("should finish Obstacles at "+ obstacles.size());

						softObstacle o = (softObstacle)obstaclesS.elementAt(obstaclesS.size()-1);
						o.addSoftObstacle(o, UrbanMap, gridSize);
					}
					updateMetricDistanceMap();
					//					for(int i = 0; i<buildings.size();i++) {
					//						if (F == null || !F.isAlive()) {
					//							F = new Thread(new metricDistanceMaper(buildings, UrbanMap, i));
					//							F.start();
					//						}
					//					}
					provisoryPolygone = null;
				}
				else {
					provisoryPolygone.addVertex(mouseX,mouseY);
				}
			}
		}

		// Generating a pedestrian-agent going to building #1
		//		if (key == 'p'){
		//			int myRef = 99;
		//			int id = pedestrians.size();
		//			//						String preference = buildings.elementAt(buildingFunctionDrawn-1).function;
		//			MovingAgents.pedestrians a = new MovingAgents.pedestrians(mouseX, mouseY, UrbanMap, this, buildings, obstacles, functions, this, 0);
		//			pedestrians.add(a);
		//			System.out.println("New Pedestrian myRef = "+myRef);
		//			//			System.out.println("preference = "+preference);
		//		}
		if (key == 'b'){

			buildings.addElement(new building(mouseX,mouseY,(int)random(100), this, buildings, obstacles, pedestrians, this));
			updateBuildingInfo();
			building b = (building)buildings.elementAt(buildings.size()-1);
			b.addObstacleMap(b, UrbanMap, gridSize);
			updateMetricDistanceMap();
		}
	}


}
