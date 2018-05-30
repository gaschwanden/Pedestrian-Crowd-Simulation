package NetVisual;
import processing.core.*; 

public class net {

	PApplet p;
	int dist = 10;
	private float[][] points;
	private int w;
	private int h;
	public int xLines;
	public int yLines;

	net(int width, int height, PApplet _p){
		p = _p;
		w = width;
		h = height;
		xLines = w/dist;
		yLines = h/dist;
		points = new float[xLines][yLines];
		for(int i = 0; i<xLines;i++) {
			for(int j = 0; j<yLines;j++) {
				points[i][j] = -20;
			}
		}
	}

	public void drawme() {
		p.noFill();
		p.stroke(50, 200, 100);
		for(int i = 1; i<xLines;i++) {
			p.beginShape();
			for(int j = 1; j<h/dist;j++) {
				p.curveVertex(i*dist, j*dist, points[i][j]);
				int ending = j+1;
				if(ending == xLines) {
					p.endShape();
				}
			}

		}
		for(int i = 1; i<yLines;i++) {
			p.beginShape();
			for(int j = 1; j<xLines;j++) {
				p.curveVertex(j*dist, i*dist, points[j][i]);
				int ending = j+1;
				if(ending == yLines) {
					p.endShape();
				}
			}

		}



	}

	public void updatePoint(int x, int y, int z) {
		int xCoordinate = x/dist;
		int yCoordinate = y/dist;
		float delta = (float)(dist/4);


		for(int i = 1; i<xLines;i++) {
			for(int j = 1; j<h/dist;j++) {
				if (xCoordinate == i && yCoordinate == j) {
					points[i][j] += z;
				}else {
					int deltaX = Math.abs(xCoordinate-i);
					int deltaY = Math.abs(yCoordinate-j);

					float k = (float) (z/(Math.sqrt(deltaX*deltaX+deltaY*deltaY)*delta));
					if (Math.round(k)>0) {
						points[i][j] += Math.round(k);
//						System.out.println(k+"/"+Math.round(k));
					}
				}
			}
		}

	}


}






