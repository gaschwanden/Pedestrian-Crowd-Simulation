package Geometry;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.Vector;

public class provisoryPolygone {
	PApplet p;
	public Vector<vertex> vertexes;
	public int color = -50;


	public provisoryPolygone(PApplet _p) {
		vertexes = new Vector<vertex>();
		p=_p;

	}

	public provisoryPolygone(PApplet _p, int _color) {
		vertexes = new Vector<vertex>();
		p=_p;
		color = _color;

	}

	public void addVertex(float _x, float _y){
		vertex v = new vertex(_x,_y);
		vertexes.addElement(v);
	}
	public void addVertex(vertex v){
		vertexes.addElement(v);
	}

	public vertex getCentroid() {
		float x = 0;
		float y = 0;
		for (int i = 0;i < vertexes.size(); i++) {
			vertex v = (vertex)vertexes.elementAt(i);
			x =+ v.x;
			y =+ v.y;
		}
		float cPointx = x/vertexes.size();
		float cPointy = y/vertexes.size();
		return new vertex(cPointx,cPointy);
	}

	public double area() {
		return Math.abs(signedArea());
	}

	public double signedArea(){
		double sum = 0.0;
		for (int i = 0; i < vertexes.size()-1; i++) {
			vertex v = (vertex)vertexes.elementAt(i);
			vertex w = (vertex)vertexes.elementAt(i+1);
			sum = sum + (v.y + w.y) * (v.x - w.x);
		}
		return 0.5 * sum;
	}

	public void drawMe(boolean polygoneSelected, int deltaX, int deltaY){

		int k = 8*p.abs((p.frameCount%60)-30);
		p.stroke(k+10);
		if(polygoneSelected) {
			p.fill(155, k+10);	
		}else {
			p.noFill();
		}
		p.beginShape();
		for (int i = 0; i < vertexes.size(); i++) {
			vertex v = (vertex)vertexes.elementAt(i);
			p.vertex(v.x+deltaX,v.y+deltaY);
		}
		if(polygoneSelected) {
			p.endShape(PConstants.CLOSE);
		}else {
			p.endShape();
		}

	}

	public vertex midpoint() {

		vertex v= (vertex)vertexes.elementAt(0);
		vertex w= (vertex)vertexes.elementAt(1);
		int mPointx = (int)(v.x-w.x);
		int mPointy = (int)(v.y-w.y);



		return new vertex(mPointx, mPointy);

	}

	public vertex centroid(){
		float cx = 0;
		float cy = 0;

		for (int i = 0; i < vertexes.size(); i++) {
			vertex v = (vertex)vertexes.elementAt(i);
			cx = cx + v.x;
			cy = cy + v.y;
		}

		cx /= vertexes.size();
		cy /= vertexes.size();


		return new vertex(cx, cy);
	}

	public vertex getRectangularVector(int i) {
		int y;
		if (i > vertexes.size()) {
			y = 0;
		}else {
			y = i;
		}
		vertex v= (vertex)vertexes.elementAt(y);
		vertex w= (vertex)vertexes.elementAt(i);
		int mVectorx = (int) (v.y-w.y);
		int mVectory = (int) (-v.x+v.x);

		return new vertex(mVectorx, mVectory);
	}

	public myBox getBoundingBox(softObstacle softObstacle) {
		float xMin=9999999;
		float yMin=999999;
		float xMax=0;
		float yMax=0;
		for(int y = 0; y< softObstacle.vertexes.size(); y++) {
			vertex v = softObstacle.vertexes.elementAt(y); 
			if (v.x>xMax) xMax = v.x;
			if (v.y>yMax) yMax = v.y;
			if (v.x<xMin) xMin = v.x;
			if (v.y<yMin) yMin = v.y;
		}
		return new myBox(xMin, yMin, xMax, yMax);


	}

	public myBox getBoundingBox(building b) {
		float xMin=9999999;
		float yMin=999999;
		float xMax=0;
		float yMax=0;
		for(int y = 0; y< b.vertexes.size(); y++) {
			vertex v = b.vertexes.elementAt(y); 
			if (v.x>xMax) xMax = v.x;
			if (v.y>yMax) yMax = v.y;
			if (v.x<xMin) xMin = v.x;
			if (v.y<yMin) yMin = v.y;
		}
		return new myBox(xMin, yMin, xMax, yMax);
	}

	public myBox getBoundingBox(solidObstacle b) {
		float xMin=9999999;
		float yMin=999999;
		float xMax=0;
		float yMax=0;
		for(int y = 0; y< b.vertexes.size(); y++) {
			vertex v = b.vertexes.elementAt(y); 
			if (v.x>xMax) xMax = v.x;
			if (v.y>yMax) yMax = v.y;
			if (v.x<xMin) xMin = v.x;
			if (v.y<yMin) yMin = v.y;
		}
		return new myBox(xMin, yMin, xMax, yMax);
	}
}
