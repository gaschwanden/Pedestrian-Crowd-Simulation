package Geometry;

import java.util.Vector;

import MovingAgents.VisionPixels;

public class Trace {

	public int origineID;
	public int destinationID;
	public polygone myPolygone;
	public int age;
	public Vector<VisionPixels> visionDepth;
	public float totalValue;

	public Trace(int _origineID, int _goalID) {
		origineID = _origineID;
		destinationID = _goalID;
		myPolygone = new polygone();
		age = 0;
		visionDepth = new Vector<VisionPixels>();
		totalValue = 0;
	}

}
