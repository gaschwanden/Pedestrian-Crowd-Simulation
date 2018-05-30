package NetVisual;
import processing.core.*; 


public class waves {

	PApplet p;
	public Geometry.vertex pos;
	private int time;
	public boolean remove = false;
	private Geometry.vertex delta;
	private int endAlfa = 50;
	
	public waves(Geometry.vertex _pos,Geometry.vertex _delta, int _time, PApplet _p) {
		pos = _pos;
		time = _time;
		p = _p;
		delta = _delta;
	}
	public void drawme(int t){
//		System.out.println(t-time);
		int alfa = t-time;
		p.fill(10, 250, 100, 100-alfa*(100/endAlfa));
		p.noStroke();
//		p.stroke(10, 250, 250, 100-alfa*(100/endAlfa));
		p.ellipse(pos.x, pos.y, (t-time)/2, (t-time)/2);
		if (alfa>endAlfa) {
			remove=true;
			
		}
		
	}

}
