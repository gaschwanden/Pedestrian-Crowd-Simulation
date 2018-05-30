package NetVisual;
import java.util.Vector;

import Geometry.*;
import Geometry.vertex;
import peasy.*;
import processing.core.*; 
//import processing.opengl.*;
import peasy.*;

public class NetVisual_main extends PApplet{

	private net net;
	private PeasyCam cam;
	private int time;
	Vector<lines> allLines = new Vector<lines>();
	
	

	public void setup() {
		size(1200,1200, P3D);
		cam = new PeasyCam(this, width / 2, height / 2, 20, 900);
		net = new net(width,height, this);
		addRandomLines();
		
	}
	
	public void draw() {
		time = frameCount%2000;
		if (time == -99) {
			addRandomLines();
		}
		background(0,0, 0);
		net.drawme();
//		randomisePoint();
		for (int i = 0;i < allLines.size();i++) {
			lines lines = allLines.elementAt(i);
			lines.drawme(time);
			for (int j = 0;j < lines.stones.size();j++) {
				waves waves = lines.stones.elementAt(j);
				waves.drawme(time);
				if (waves.remove) {
					net.updatePoint((int)(waves.pos.x),(int)(waves.pos.y), 5);
					lines.stones.remove(waves);
				}
			}
			
		}
		
		
		
	}
	
//	private void randomisePoint() {
//		float deltaX = random(-100,100);
//		if(x+delatX > width || )
//		 x = (int) (xCo + random(-100,100));
//		
//		net.updatePoint((int)(random(0,width)), (int)(random(0, width)), (int)(random(-100,100)));
//
//		
//	}

	public void mousePressed(){
		net.updatePoint(mouseX,mouseY,50);
	}
	
	public void addRandomLines() {
		for (int i= 0; i <200; i++) {
			allLines.add(new lines(new vertex(random(0,width),random(0,width)),new vertex(random(1,width),random(1,width)),(int)(random(0,200)), (int)(random(400,2000)), this));
		}
	}
	
}
