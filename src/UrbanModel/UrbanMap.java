package UrbanModel;

import Geometry.*;
import Importer.*;
import MovingAgents.*;
//import NetVisual.*;


import java.util.*;
//import java.lang.Thread;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class UrbanMap extends UrbanModel{
	public PApplet p;

	public float gridSize;
	public int gridCountX;
	public int gridCountY;

	private vertex v;

	public int ObjectID[][];
	int Softobstruction[][];
	public int ObstructionMap[][];
	public int distance[][][];

	Vector<building> buildings;
	Vector<pedestrians> pedestrians;
	Vector<solidObstacle> obstacles;
	Vector<softObstacle> obstaclesS;

//	private int x;

	public int maximumNRbuildings = 150; //2000 with gridsize 3 is max

	public float[][] footFall;
	public float[][] density;

	float decay = (float) 0.999;
	public int buildingVariable;

	private int incrementalStep = 10;

//	private int s = 100;

	private int obstructionValue = 0;

	public int breakOffDistance = 500000;

	public UrbanMap(float _gridSize, int _gridCountX, int _gridCountY, PApplet _p, 
			Vector<building> _buildings, Vector<pedestrians> _pedestrians, 
			Vector<solidObstacle> _obstacles, Vector<softObstacle> obstaclesS2) {

		gridSize = _gridSize;
		gridCountX = _gridCountX;
		gridCountY = _gridCountY;
		p = _p;
		buildings = _buildings;
		pedestrians = _pedestrians;
		obstacles = _obstacles;
		obstaclesS = obstaclesS2;	

		ObjectID = new int [gridCountX][gridCountY];
		for (int i = 0; i <gridCountX; i++) {
			for (int j = 0; j <gridCountY; j++) {
				ObjectID[i][j]=-1;
			}
		}
		Softobstruction = new int[gridCountX][gridCountY];
		ObstructionMap = new int[gridCountX][gridCountY];
		distance = new int  [maximumNRbuildings][gridCountX][gridCountY];//this number defines how many buildings we can have
		for (int z = 0; z < maximumNRbuildings; z++) {
			for (int i = 0; i < gridCountX; i++) {
				for (int j = 0; j < gridCountY; j++) {
					distance[z][i][j]=-2;
				}
			}
		}
		footFall = new float[gridCountX][gridCountY];
		density = new float[gridCountX][gridCountY];


	}

	public void incrementObstructionMap(int x, int y) {
		if(ObstructionMap==null) System.out.println("ObstructionMap not initialized");
		if (x>=gridCountX) return;
		if (x< 0) return;
		if (y>=gridCountY) return;
		if (y< 0) return;
		ObstructionMap[x][y]=1;

	}

	public void decrementObstructionMap(int x, int y) {
		if(ObstructionMap==null) System.out.println("ObstructionMap not initialized");
		if (x>=gridCountX) return;
		if (x< 0) return;
		if (y>=gridCountY) return;
		if (y< 0) return;
		ObstructionMap[x][y]=0;
	}

	public void incrementObjectId(int myref, int i, int j) {
		if(i>= 0 && j >= 0 && i < gridCountX && j < gridCountY) {
			ObjectID[i][j] = myref+1;
		}
	}

	public void decrementObjectId(int myRef, int i, int j) {
		if(i>= 0 && j >= 0 && i < gridCountX && j < gridCountY) {
			ObjectID[i][j] = -1;
		}
	}
	public void decrementObjectId(int i, int j) {
		if(i>= 0 && j >= 0 && i < gridCountX && j < gridCountY) {
			ObjectID[i][j] = -1;
		}
	}

	public int targetHitTest(int mx,int my)
	{
		if(mx>(gridCountX-1) || mx<0 || my>(gridCountY-1) || my<0) {
			return -1;
		}else {
			return ObjectID[mx][my]-1;
		}
	}


	public void incrementFootFall(int gridX, int gridY) {
		footFall[gridX][gridY]+=1;
		density[gridX][gridY]+=1;
	}

	public void metricDistanceMap(Vector<building> _buildings, int num) //num is the building number from which the distance is been taken
	{
		//		p.println("DistanceMap "+num);
		buildings = _buildings;
		if(num<0)return;
		System.out.println("Metric Distance Map of "+num+"/"+buildings.size());
		if(buildings == null)p.println("there is no building vector");
		if(num >= buildings.size())return; //check if there is a building with this number
		distance[num] = new int[gridCountX][gridCountY]; // initializing distance array
		Vector<PVector> queue = new Vector<PVector>(); // points which havn't been checked
		PVector a = new PVector();



		building t = (building)buildings.elementAt(num);

		//		boolean obstructed = true;

		v = t.centroid;
		int XX = PApplet.round(v.x/gridSize) ;
		if (XX>gridCountX) XX=gridCountX-1;
		if (XX<1) XX=1;
		int YY = PApplet.round(v.y/gridSize);
		if (YY>gridCountY) YY=gridCountY-1;
		if (YY<1) YY=1;

		distance[num][XX][YY] = 1;
		PVector position = new PVector(XX,YY,1); // distance is in the 3rd dimension

		queue.addElement(position);
		while (queue.size()!=0 )
		{
			a = (PVector)queue.elementAt(0);
			int d = (int)a.z;
			for (int i=-1; i<2; i++) {
				for (int j=-1; j<2; j++) {
					if (i==0 && j==0) continue;  
					//this makes it swap between rectangular and diagonal 
					if (p.random(100) >= 30) { 
						if (i==-1 && j==-1) continue; 
						if (i==-1 && j==1) continue;
						if (i==1 && j==-1) continue; 
						if (i==1 && j==1) continue; 
					}
					int x = PApplet.round(a.x+i);
					int y = PApplet.round(a.y+j);


					if (x<0 || x >= gridCountX || y<0 || y >= gridCountY) continue;  				//not on the ground plane
					if(d+incrementalStep+Softobstruction[x][y]>breakOffDistance)break;
					if (ObstructionMap[x][y] == 1) {
						//						p.println("target hit test "+num+" should be "+targetHitTest(x,y));
						if(targetHitTest(x, y)-1 == num) {
						}else {
							break;}
					}

					if (distance[num][x][y] != 0) {								
						if (distance[num][x][y] > distance[num][Math.round(a.x)][Math.round(a.y)]+incrementalStep+Softobstruction[x][y] ) //check if the point reached from a.y/a.x is getting a smaller distance then it has
						{
							distance[num][x][y] = d+incrementalStep+Softobstruction[x][y];								// putting better distance into the point
							PVector b = new PVector(x,y,d+incrementalStep+Softobstruction[x][y]);  						// generating new Vector
							for (int k = 0; k < queue.size() ;k++ ) {										// checking if the new Vector Coordinate in the queue list to propagate the metricDistanceMap 
								PVector c = (PVector)queue.elementAt(k);
								if (c.x == b.x && c.y == b.y) {
									queue.remove(k);														// if so remove the existing one
									break;
								}
							}
							for (int ii = 0; ii < queue.size(); ii++) {
								PVector tempP = queue.elementAt(ii);
								if (tempP.z>=b.z) {
									queue.add(ii, b);														// and add the vector to the list according to its size
									break;
								}
								if(ii+1==queue.size()) {
									queue.addElement(b);															// and add the vector to the list
								}

							}

						}else {
							continue;}
					}else { //a point we havn't seen
						//this is 1 + Softobstruction
						if(Softobstruction[x][y]!=0) {
							distance[num][x][y] = d +incrementalStep+ Softobstruction[x][y];
							PVector b = new PVector(x,y,d+incrementalStep+Softobstruction[x][y]);  //note we are putting the distance into the z component
							queue.addElement(b);
						}else {
							//this is 1 
							distance[num][x][y] = d+incrementalStep;
							PVector b = new PVector(x,y,d+incrementalStep);  //note we are putting the distance into the z component
							queue.addElement(b);
						}
					}
				}
			}
			if (queue.elementAt(0) !=null)	{
				queue.remove(0);
			}
		}

	}

	public void drawMe(int k, int x) {
		/*
		0 = "drawing metric distance map of "
		1 = "drawing targethittest "
		2 = "drawing blank "
		3 = "drawing footFall "
		4 = "drawing density");
		5 = "drawing traces");
		6 = "drawing loaded Image";
		7 = "drawing statistics";
		8 = "drawing Emission Map";
		 */

		buildingVariable = x;
		for (int i = 0; i < gridCountX; i++) {		
			for (int j = 0; j < gridCountY; j++) {
				switch(k) {//here are different cases stored what to display
				case 0:
					//			for (int i = 0; i < gridCountX; i++) {		
					//				for (int j = 0; j < gridCountY; j++) {
					int y = (int) (distance[x][i][j]/20);
					//					if(y > 1)p.println(i+"/"+j+" = "+y);
					int t;
					if(y%50==0) {
						t=250;
//						s=50;
					}else {
						t=(int)(y/5);
//						s=100;
					}
					p.fill(135-t,135-t,215-t);
					p.noStroke();
					if(t!=200)p.rect(i*gridSize, j*gridSize, gridSize, gridSize);
					//				}
					//			}
					break;
				case 1:
					//			for (int i = 0; i < gridCountX; i++) {		
					//				for (int j = 0; j < gridCountY; j++) {
					int Ty = targetHitTest(i, j);
					if(Ty<=0) {
					}else {
						int Tt = Ty*30;
						p.noStroke();
						int xCoord = (int) (i*gridSize) ;
						int yCoord = (int) (j*gridSize) ;
						p.fill(255-Tt,255-Tt,245-Tt);
						p.rect(xCoord, yCoord, gridSize, gridSize);
					}
					//				}
					//			}
					break;

				case 2:
					int yy = Softobstruction[i][j];
					p.fill(135-yy*3,135+yy*1,135-yy*50);
					int xCoord = (int) (i*gridSize) ;
					int yCoord = (int) (j*gridSize) ;
					p.noStroke();
					p.rect(xCoord, yCoord, gridSize, gridSize);
					break;

				case 3:
					int footy = (int) footFall[i][j];
					//					int t=(int)(y/4);
					//					p.fill(footy*9,footy*7,footy*1);
					//					if (y>0) {
					p.fill(185,194,211,footy*8);
					//					}else {
					//					p.fill(t*15,235-t*25,185-t*10);
					//					}
					p.noStroke();
					if(footy>0)p.rect(i*gridSize, j*gridSize, gridSize, gridSize);
					break;
				case 4:
					int dens = (int) density[i][j];
					if(dens>5){
						if(dens>25){
							p.fill(255, 255, 255);
						}else {
							p.fill(dens*25, 100, 100);
						}

					}else {
						p.fill(10,10,10);
					}

					p.noStroke();
					if(dens>0)p.rect(i*gridSize, j*gridSize, gridSize, gridSize);
					//				}
					//			}
					break;
				case 5:

					break;
				case 6:
				}
				if(density[i][j]>0) {
					density[i][j]*= 0.98;
				}
				if(footFall[i][j]>0) {
					footFall[i][j] *= decay;
				}
			}
		}
	}


	public void incrementSoftWalkobstruction(int i, int j) {
		if(i<0 || j<0 || i>=gridCountX ||j>=gridCountY) {

		}else {Softobstruction[i][j] =  1;}
	}
	public void incrementSoftWalkobstruction(int i, int j, int z) {
		Softobstruction[i][j] =  z;
	}

	public void incrementStreetWalkobstruction(int i, int j, int z) {
		if(i<0 || j<0 || i>=gridCountX ||j>=gridCountY) {

		}else {
			Softobstruction[i][j] =  z;
		}
	}

	public void resetValues() {
		for (int i = 1; i< gridCountX;i++) {
			for( int j = 1; j < gridCountY; j++) {
				int t = distance[1][i][j];
				p.fill(t*5, t*5, t*5);
				p.noStroke();
				p.rect(i*gridSize, j*gridSize, gridSize, gridSize);
			}
		}
	}

	public void checkObjectIdArray() {
		for (int i = 1; i< gridCountX;i++) {
			for( int j = 1; j < gridCountY; j++) {
				int x = ObjectID[i][j];
				//				if(x>0)	System.out.println("at "+i+"/"+j+" = "+x);

			}
		}
	}

	public void addStreet(Geometry.provisoryPolygone provisoryPolygone) {
		streets.add(new softObstacle(provisoryPolygone,p, UrbanMap, 150));
		System.out.println("Street added nr "+ streets.size());
		vertex v = (vertex)provisoryPolygone.vertexes.elementAt(0);
		vertex w = (vertex)provisoryPolygone.vertexes.elementAt(1);
		//		System.out.println("x/y = "+v.x+"/"+v.y+" X/Y ="+w.x+"/"+w.y);
		float dist = PApplet.dist(v.x, v.y, w.x, w.y);
		int extend = (int) (5/gridSize);

		if(provisoryPolygone.color==-50) {
			obstructionValue = 5; 
		} else {
			obstructionValue = provisoryPolygone.color; 
		}

		for(int y = 0;y<999999; y++) {


			float VectorX = w.x + y*((v.x-w.x)/dist);
			float VectorY = w.y + y*((v.y-w.y)/dist);
			if(PApplet.dist( w.x, w.y,VectorX,VectorY)>dist)break;

			int CellX = PApplet.round(VectorX/gridSize);
			int CellY = PApplet.round(VectorY/gridSize);



			for(int ii=-extend; ii<extend+1;ii++) {
				for(int jj=-extend; jj<extend+1;jj++) {

					incrementStreetWalkobstruction(CellX+ii, CellY+jj, obstructionValue);
					decrementObstructionMap(CellX+ii, CellY+jj);
					decrementObjectId(CellX+ii, CellY+jj);
				}
			}
		}
	}





}
