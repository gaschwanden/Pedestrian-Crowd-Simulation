package Geometry;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;
import UrbanModel.UrbanMap;

public class softObstacle {

	private int myRef;
	float size;
	PApplet p; 
	myBox boundingBox;
	Vector <vertex>vertexes; 
	UrbanMap UrbanMap;

	public softObstacle(provisoryPolygone provisoryPolygone, PApplet _pApp, UrbanMap _urbanMap){

		p = _pApp;
		UrbanMap = _urbanMap;
		vertexes = new Vector<vertex>();
		for (int i = 0; i < provisoryPolygone.vertexes.size(); i++) {
			vertex v = provisoryPolygone.vertexes.elementAt(i);
			vertexes.add(new vertex(v.x,v.y));
		}
		size = (float) provisoryPolygone.area();
		boundingBox = provisoryPolygone.getBoundingBox(this);
		incrementSoftobstruction(provisoryPolygone);
	}
	
	public softObstacle(provisoryPolygone provisoryPolygone, PApplet _pApp, UrbanMap _urbanMap, int Streetsize){

		p = _pApp;
		UrbanMap = _urbanMap;
		vertexes = new Vector<vertex>();
		for (int i = 0; i < provisoryPolygone.vertexes.size(); i++) {
			vertex v = provisoryPolygone.vertexes.elementAt(i);
			vertexes.add(new vertex(v.x,v.y));
		}
		size = (float) provisoryPolygone.area();
		boundingBox = provisoryPolygone.getBoundingBox(this);
		incrementSoftobstruction(provisoryPolygone);
	}

	private void incrementSoftobstruction(provisoryPolygone b) {
		
	}

	public void addSoftObstacle(softObstacle o, UrbanModel.UrbanMap urbanMap, float gridSize) {
		float X_X = 0;
		float Y_Y = 0;
		softObstacle b = o;
		vertex v;
		vertex w;
		float VectorY;
		float VectorX;
		
		for (int i = (int)boundingBox.xMin; i<(int)boundingBox.xMax; i++) {
			for (int y = (int)boundingBox.yMin; y<(int)boundingBox.yMax;y++ ) {
				if (pointInside(i,y, b) == true) {
					UrbanMap.incrementStreetWalkobstruction(PApplet.round(i/gridSize), PApplet.round(y/gridSize), 4);
					UrbanMap.incrementObjectId(myRef,(int)(i/gridSize), (int)(y/gridSize));
				}
			}
		}	
	}
	
	public void addSoftObstacle(softObstacle o, UrbanModel.UrbanMap urbanMap, int gridSize, int Streetsize) {
		float X_X = 0;
		float Y_Y = 0;
		softObstacle b = o;
		vertex v;
		vertex w;
		float VectorY;
		float VectorX;
		
		for (int i = (int)boundingBox.xMin; i<(int)boundingBox.xMax; i++) {
			for (int y = (int)boundingBox.yMin; y<(int)boundingBox.yMax;y++ ) {
				if (pointInside(i,y, b) == true) {
					UrbanMap.incrementStreetWalkobstruction(PApplet.round(i/gridSize), PApplet.round(y/gridSize), Streetsize);
					UrbanMap.incrementObjectId(myRef,(int)(i/gridSize), (int)(y/gridSize));
				}
			}
		}	
	}
	
	public boolean pointInside(float  x, float y, softObstacle b){
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

	public void drawMe() {
		
//		p.fill(67,0,113,60);
//		p.stroke(122,49,6);
		p.fill(47,38,23,6);
		
		//draw polyline
//		if (vertexes.size()>1) { 
//			p.noStroke();
//			p.beginShape();
//			for (int i = 0; i < vertexes.size(); i++) {
//				vertex v = vertexes.elementAt(i);
//				p.vertex(v.x,v.y);
//			}
//			p.endShape(PConstants.CLOSE);
//		}else {
////			p.noStroke();
//			p.stroke(0);
//			p.ellipse(vertexes.elementAt(0).x,vertexes.elementAt(0).y, 5, 5);
//		}
	}

}
