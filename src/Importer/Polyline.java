package Importer;

import java.util.Vector;
import processing.core.PVector;

public class Polyline {
	public Vector vertices;
	public Vector Polyline;
	public int color;
	
	public Polyline() {
		vertices = new Vector();
	}
	
	Polyline addPolyline() {
		Polyline p = new Polyline();
		Polyline.add(p);
		return (Polyline)Polyline.elementAt(Polyline.size()-1);
	}
	
	PVector addVertex() {
		PVector v = new PVector();
		vertices.add(v);
		return (PVector)vertices.elementAt(vertices.size()-1);
	}

}
