package NetVisual;
import java.util.Vector;

import processing.core.PApplet;


public class lines {

	public Geometry.vertex startP;
	public Geometry.vertex endP;
	public int startT;
	public int endT;
	public Vector<waves> stones = new Vector<waves>();
	PApplet p;
	
	
	lines(Geometry.vertex _startP,Geometry.vertex _endP,int _startT, int _endT, PApplet _p){
		startP = _startP;
		endP = _endP;
		startT = _startT;
		endT = _endT;
		p = _p;
		
	}
	
	void drawme(int time){
		if (time > startT && time < endT) {
			float scale = (float)(time*1.0-startT)/(endT-startT);
			Geometry.vertex delta = new Geometry.vertex((endP.x-startP.x)*scale,(endP.y-startP.y)*scale);
			Geometry.vertex pos = new Geometry.vertex(startP.x+delta.x, startP.y+delta.y);
//			System.out.println("position = "+pos.x+"/"+pos.y+" scale = "+scale);
			stones.add(new waves(pos, delta, time, p));
			
		}
	}
}
