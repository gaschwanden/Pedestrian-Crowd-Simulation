package Geometry;

import UrbanModel.*;
import Geometry.*;
import MovingAgents.*;

import java.util.Vector;

import processing.core.PApplet;
import processing.core.PConstants;

public class publicTransportStop {
	
	PApplet p;
	
	//individual infromation
	private String name;

	private float xCoordinate;
	private float yCoordinate;
	int [] timeBins;
	
	public publicTransportStop(PApplet _p, String _Name, float _x, float _y, int numberOfBins){
		p = _p;
		name = _Name;
		xCoordinate = _x;
		yCoordinate = _y;
		timeBins = new int [numberOfBins];
		
	}
	
	public void drawMe()
	{
		p.fill(155,180,255,50);
		p.rect(xCoordinate, yCoordinate, 4, 4);
		/*
		 * if time bin of this stop is != 0 emit people accordingly
		 */
	}
	
}
