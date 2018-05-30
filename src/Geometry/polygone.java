package Geometry;
import java.util.Vector;
import processing.core.*;

import Geometry.*;

public class polygone {
	PApplet p;
	public Vector<vertex> vertexes;

	polygone() {
		vertexes = new Vector<vertex>();
	}

	public void addVertex(float _x, float _y){
		vertex v = new vertex(_x,_y);
		vertexes.addElement(v);
	}

	public void addVertex(float _x, float _y, float _z){
		vertex v = new vertex(_x,_y,_z);
		vertexes.addElement(v);
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

	public void drawMe(){

		p.stroke(100);
		p.noFill();
		p.beginShape();
		for (int i = 0; i < vertexes.size(); i++) {
			vertex v = (vertex)vertexes.elementAt(i);
			p.vertex(v.x,v.y);
		}
		p.endShape();
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

	public myBox getBoundingBox(polygone b) {
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
