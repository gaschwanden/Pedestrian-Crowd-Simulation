package Geometry;



import java.util.Vector;
import Geometry.*;
import UrbanModel.*;

import processing.core.*;

public class solidObstacle {

	///////////////////////////////////////////////////////////////////
	//			Variables
	///////////////////////////////////////////////////////////////////


	private int myRef;
	float size;
	PApplet p; 
	public myBox boundingBox;
	public Vector <vertex>vertexes; 
	UrbanMap UrbanMap;
	public buildingInformation buildingInfo;
	public vertex centroid;

	///////////////////////////////////////////////////////////////////
	//			Constructors
	///////////////////////////////////////////////////////////////////
	
	public solidObstacle(provisoryPolygone provisoryPolygone, PApplet _pApp){

		p = _pApp;
		vertexes = new Vector<vertex>();
		for (int i = 0; i < provisoryPolygone.vertexes.size(); i++) {
			vertex v = provisoryPolygone.vertexes.elementAt(i);
			vertexes.add(new vertex(v.x,v.y));
		}
		size = (float) provisoryPolygone.area();
		boundingBox = provisoryPolygone.getBoundingBox(this);
		buildingInfo = new buildingInformation();
		centroid = provisoryPolygone.centroid(); 
		
	}
	

	///////////////////////////////////////////////////////////////////
	//			Methods
	///////////////////////////////////////////////////////////////////

	public void addObstacleMap(solidObstacle b, UrbanMap _UrbanMap, float gridSize, int j) {

		float X_X = 0;
		float Y_Y = 0;
		UrbanMap = _UrbanMap;
		myRef = j;
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
			for (int y= 1; y<10000; y++) {
				if (PApplet.abs(X_X - p.round(v.x)) < 4 && PApplet.abs(Y_Y - p.round(v.y)) < 4) {

				}else {
					float dist = PApplet.abs(PApplet.dist(v.x, v.y, w.x, w.y));
					VectorX = w.x + y*((v.x-w.x)/dist);
					VectorY = w.y + y*((v.y-w.y)/dist);
					if(p.round(VectorX/gridSize)<0 || p.round(VectorY/gridSize) < 0)break; // exception that point is out of scope
					X_X = VectorX;
					Y_Y = VectorY;
					UrbanMap.incrementObstructionMap(p.round(VectorX/gridSize), p.round(VectorY/gridSize));
					UrbanMap.incrementObjectId(myRef,p.round(VectorX/gridSize), p.round(VectorY/gridSize));					
				}
			}
		}
		
		for (int i = p.round(boundingBox.xMin); i<p.round(boundingBox.xMax); i++) {
			for (int y = p.round(boundingBox.yMin); y<p.round(boundingBox.yMax);y++ ) {
				if (pointInside(i,y, b) == true) {
					UrbanMap.incrementObstructionMap(p.round(i/gridSize), p.round(y/gridSize));
					UrbanMap.incrementObjectId(myRef,p.round(i/gridSize), p.round(y/gridSize));
					UrbanMap.incrementSoftWalkobstruction(p.round(i/gridSize), p.round(y/gridSize),50);
//					System.out.println("Coordinates: "+i+"/"+y);
//					System.out.println(" = "+p.round(i/gridSize)+"/"+ p.round(y/gridSize));
				}
			}
		}	
	}

	public boolean pointInside(float  x, float y, solidObstacle b){
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

	public void drawMe(int drawKondition){
//		p.stroke(100,100,100);
//		p.stroke(120,120,150);
//		p.noStroke();
		p.smooth();
//		p.fill(0);
		p.stroke(105,105,136,90);
		p.fill(25,27,39);
//		p.stroke(228,224,239);
//		p.fill(67,0,113);
//		if(drawKondition == 6) {
//			p.fill(67,0,113,60);
////			p.noStroke();
//		}
		
//		p.fill(10,10,30);
		//		p.fill(5*red,10*red,10*red+40);
		p.beginShape();
		for (int i = 0; i < vertexes.size(); i++) {
			vertex v = vertexes.elementAt(i);
			p.vertex(v.x,v.y);
		}
		p.endShape(PConstants.CLOSE);

	}

	public void drawHighlight() {
		p.stroke(255,255,255,100);
		p.noFill();
		//		p.fill(5*red,10*red,10*red+40);
		p.beginShape();
		for (int i = 0; i < vertexes.size(); i++) {
			vertex v = vertexes.elementAt(i);
			p.vertex(v.x,v.y);
		}
		p.endShape(PConstants.CLOSE);
	}



	public void removeObstacleMap(building b, UrbanMap _UrbanMap,
			int gridSize) {
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


	
	public void removeObstacleMap(solidObstacle b, UrbanMap _UrbanMap,
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
	
	



}

