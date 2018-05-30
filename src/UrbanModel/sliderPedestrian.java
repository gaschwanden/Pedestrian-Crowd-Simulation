package UrbanModel;

import processing.core.PApplet;
import processing.core.PConstants;

public class sliderPedestrian {

	int maxVision = 40;
	int w;
	int h;
	int originw;
	int originh;
	float value;
	int minValue;
	int maxValue;
	String name;
	UrbanModel p;
	int numberOfRays;
	float fieldOfView;
	int visionLength;
	private float radiusDevider;
	float g; 
	private float x1;
	private float y1;
	private float pixx;
	private float pixy;


	sliderPedestrian(int _originw,	int _originh,int _w,	int _h,	int _visionLength, UrbanModel _p, int _numberOfRays, float _fieldOfView){
		p = _p;
		w =_w;
		h = _h;
		originw=_originw;
		originh=_originh;

		 g = (w*1.0f)/maxVision;
		numberOfRays = _numberOfRays;
		fieldOfView = _fieldOfView;
		visionLength = _visionLength;
		
	}

	public void drawMe() {
		//centre
		radiusDevider = fieldOfView/numberOfRays;
		
		p.noStroke();
		p.fill(255,20,20,80);
		p.arc(originw, originh, ((visionLength*2.0f*w)/maxVision)+g, ((visionLength*2.0f*w)/maxVision)+g, -fieldOfView/2, fieldOfView/2);
		for(float j = -fieldOfView/2 ;j < fieldOfView/2 +radiusDevider/2;j+=radiusDevider) {
			for(int i = 1 ; i<= visionLength ; i += 1) {
				x1 = g*i * PApplet.cos(j);
				y1 = g*i * PApplet.sin(j);

				pixx = originw + x1;
				pixy = originh + y1;
				
				p.pushMatrix();
				p.translate(pixx, pixy);
				p.rotate(j);
				p.noFill();
				p.stroke(255,255,255);
				p.rectMode(p.CENTER);
				p.rect(0, 0, g, g);
				p.rectMode(p.CORNER);
				p.popMatrix();

			}
		}
		p.fill(255,20,80);
		p.ellipse(originw, originh, w/20, h/20);
	}

}
