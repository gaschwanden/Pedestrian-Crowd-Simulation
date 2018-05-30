package UrbanModel;

import processing.core.*;

public class Button {

	PApplet p;
	String name;
	public int w = 200;
	public int h = 30;
	public int x;
	public int y;
	PFont mono;
	
	
	
	public Button(String _name, PApplet _p, int _x, int _y) {
		x = _x;
		y = _y;
		name = _name;
		p= _p;
		mono = p.loadFont("/Users/gaschwanden/Documents/workspace/CH.ETH.FCL.UrbanModel_Playground/data/HelveticaNeue-25.vlw");
	}
	
	public void drawme() {
		p.fill(255, 0, 0);
		p.rect(x, y, w, h);
		p.fill(255);
		p.textFont(mono,12);
		p.text(name,x+2, y+(h/2)+4);
	}

}
