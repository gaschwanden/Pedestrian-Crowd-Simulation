package UrbanModel;
import javax.swing.plaf.basic.BasicScrollPaneUI.VSBChangeListener;

import processing.core.*;


public class sliders {
	int w;
	int h;
	int originw;
	int originh;
	float value;
	int minValue;
	int maxValue;
	String name;
	UrbanModel p;
	
	sliders(int _originw,	int _originh,int _w,	int _h,	float _value,	int _minValue,	int _maxValue,String _name, UrbanModel _p){
		p = _p;
		 w =_w;
		 h = _h;
		 originw=_originw;
		 originh=_originh;
		 value=_value;
		 minValue=_minValue;
		 maxValue=_maxValue;
		 name=_name;
	}
	
	public void drawMe() {
		p.fill(255,255,255,80);
		p.rect(originw,originh,w,h);
		int endOfSlider = originw+w;
		float screenValue = p.map(value, minValue, maxValue, 0, w);
		
		p.fill(255,50,50,100);
		p.rect(originw, originh, screenValue, h);
		p.textAlign(p.CENTER);
		p.text(p.nfc(value,1), originw+w/2, originh-3);
		p.fill(255,255,255);
		p.text(name, originw+w/2, originh+15);
//		p.pushMatrix();
//		p.translate(originw, originh-10);
//		p.text(p.nfc(minValue));
//		p.translate(0, w);
//		p.text(p.nfc(maxValue));
//		p.popMatrix();
	}

	public void adjustSlider(int mouseX) {
		value = p.map(mouseX, originw, originw+w, minValue, maxValue);

		
	}

}
