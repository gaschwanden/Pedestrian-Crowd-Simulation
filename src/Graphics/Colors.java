package Graphics;

import processing.core.PApplet;

public class Colors {
	public static int[] getRedYellowGreen(float maxval, float val) {
		int[] c = new int[3];
		float splitter = maxval / 2;
		float splitter2 = maxval / 4 * 3;
		if (val <= splitter) {
			float g = PApplet.map(val, 0, splitter, 30, 100);
			// c= color(0,g,0);
			c[0] = 0;
			c[1] = (int) g;
			c[2] = 0;
		} else if (val > splitter && val < splitter2) {
			float r = PApplet.map(val, splitter, splitter2, 0, 255);
			// c=color(r,100+(r/4),0);
			c[0] = (int) r;
			c[1] = (int) (100.0f + (r / 4.0f));
			c[2] = 0;
		} else {
			float r = PApplet.map(val, splitter2, maxval, 0, 200);
			// c=color(255,200-(r/4),0);
			c[0] = 255;
			c[1] = (int) (100.0f - (r / 4.0f));
			c[2] = 0;
			//p.strokeWeight(3);
			// p.println(c);
		}
		return c;
	}

	public static int[] getRedWhiteBlue(float maxval, float val) {
		int[] c = new int[3];
		float splitter = maxval / 4;
		float splitter2 = maxval / 2;
		if (val < splitter) {
			float g = PApplet.map(val, 0, splitter, 0, 105);
			// c= color(0,80,150+g);
			c[0] = 0;
			c[1] = 80;
			c[2] = (int) (150 + g);
		} else if (val > splitter && val < splitter2) {
			float r = PApplet.map(val, splitter, splitter2, 80, 255);
			float r1 = PApplet.map(val, splitter, splitter2, -80, 255);
			// c=color(r1,r,255);
			c[0] = (int) r1;
			c[1] = (int) r;
			c[2] = 255;
		} else {
			float r = PApplet.map(val, splitter2, maxval, 0, 255);
			// c=color(255,255,255-r);
			c[0] = 255;
			c[1] = (int) (255 - r);
			c[2] = (int) (255 - r);
			//p.strokeWeight(3);
			//if (r > 100)
			//	p.strokeWeight(4);
		}
		return c;
	}
}
