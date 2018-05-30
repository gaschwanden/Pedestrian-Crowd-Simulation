package Importer;

import java.util.Vector;

import UrbanModel.*;

import Geometry.*;
import Importer.*;
import MovingAgents.pedestrians;
import processing.core.*;


public class load implements Runnable
{
	Thread load;
	int width;
	int height;
	float gridSize;
	Vector<solidObstacle> tobedeleted;
	int inumerater =1;

	PApplet pApp;

	provisoryPolygone provisoryPolygone;
	Vector<Geometry.solidObstacle> obstacles;
	Vector<Geometry.softObstacle> obstacelsS;
	Vector<Geometry.softObstacle> streets;
	Vector<Geometry.building> buildings;

	Geometry.building building;
	UrbanMap UrbanMap;
	private int k;
	private boolean setBounds;
	private float projection_min_y;
	private float projection_max_y;
	private float projection_min_x;
	private float projection_max_x;
	UrbanModel urbanModel;
	private Vector<pedestrians> pedestrians;
	private building bToUpdate;
	Vector<String> functions;
	private PVector firstVertex;
	private int loadcycle = 0;
	private float original_min_y;
	private float original_max_y;
	private float original_min_x;
	private float original_max_x;
	private int temploadcycle = 0;


	////////////////////////////////////////////////////////////
	////////////
	////////////			Constructor
	////////////
	////////////////////////////////////////////////////////////


	public load (int _width, int _height, provisoryPolygone _pP, PApplet _p, Vector<solidObstacle> _obstacles,
			Vector<building>  _buildings, Vector<softObstacle> _obstacelsS, Vector<Geometry.softObstacle> _streets,UrbanMap _urbanMap, float _gridSize, UrbanModel _urbanModel, Vector<pedestrians> _pedestrians, int _loadcycle) {
		width = _width;
		height = _height;
		gridSize = _gridSize;
		provisoryPolygone = _pP;
		pApp = _p;
		obstacles = _obstacles;
		obstacelsS = _obstacelsS;
		buildings = _buildings;
		UrbanMap = _urbanMap;
		urbanModel = _urbanModel;
		setBounds = urbanModel.setBounds;
		pedestrians =_pedestrians;
		firstVertex = new PVector(50, 50);
		loadcycle = _loadcycle;
		streets = _streets;


	}

	public void run() {
		importer a = new importer(UrbanMap, setBounds, urbanModel, pApp);
		
		tobedeleted = new Vector<solidObstacle>();

		k = 0;

		// Loading Buildings from points aka CSV
		if (a.buildings == null) {
			if(a.fileextension == "jpg") {
				loadingImage(a);
			}else {
				System.out.println("no Building Pointsloaded");
			}
		}else {
			//			defineprojection(a, setBounds);

			if(a.fileextension == "csv") {
				if(setBounds) {
					a.dxf_miny = 0;
					a.dxf_maxy = 840;
					a.dxf_minx = (float) 408.46948;
					a.dxf_maxx = (float) 1151.5305;
				}
				addBuildingFromCSVPoints(a);

			}

			if(a.fileextension == "dxf")addBuildingFromDXFPoints(a);
		}

		// Loading Buildings from polygons aka DXF
		if (a.polyline == null) {
			System.out.println("no Polylines loaded");
		}else {
			if(setBounds) {
				defineprojection(a, setBounds);
			}else {
				System.out.println("after define projection");
				//				System.out.println("urbanModel.proj_min_y = "+urbanModel.proj_min_y);
				//				System.out.println("urbanModel.proj_max_y = "+urbanModel.proj_max_y);
				//				System.out.println("urbanModel.proj_min_x = "+urbanModel.proj_min_x);
				//				System.out.println("urbanModel.proj_max_x = "+urbanModel.proj_max_x);
				projection_min_y = urbanModel.proj_min_y;
				projection_max_y = urbanModel.proj_max_y;
				projection_min_x = urbanModel.proj_min_x;
				projection_max_x = urbanModel.proj_max_x;
				//				System.out.println("define projection");
				System.out.println("urbanModel.proj_min_y = "+urbanModel.proj_min_y);
				System.out.println("urbanModel.proj_max_y = "+urbanModel.proj_max_y);
				System.out.println("urbanModel.proj_min_x = "+urbanModel.proj_min_x);
				System.out.println("urbanModel.proj_max_x = "+urbanModel.proj_max_x);
			}
			addBuildingFromDXFPolylines(a, loadcycle);
		}
		for (int i = 0; i < buildings.size(); i++) {
			if(UrbanMap.maximumNRbuildings<=i)break;
			urbanModel.vari = i;
//			urbanModel.backgroundDraw = 0;
			UrbanMap.metricDistanceMap(buildings, i);
		}
		System.out.println("Loading is finished");
		urbanModel.setBounds = false;
	}


	////////////////////////////////////////////////////////////
	////////////
	////////////			Methods
	////////////
	////////////////////////////////////////////////////////////

	private void loadingImage(importer a) {
		if(a.image != null ) {
			urbanModel.imageObject = a.image;
		}
	}

	private void defineprojection(importer a, boolean setBounds) {
		System.out.println("define projection");
		System.out.println("MINX = "+a.dxf_minx);
		System.out.println("MAXX = "+a.dxf_maxx);
		System.out.println("MINY = "+a.dxf_miny);
		System.out.println("MAXY = "+a.dxf_maxy);

		float screen_ratio = (width*1.0f/height);
		System.out.println(width+"/"+height);
		float import_ratio = Math.abs((a.dxf_maxx-a.dxf_minx)/(a.dxf_maxy-a.dxf_miny));
		float imageHeight = a.dxf_maxy-a.dxf_miny;
		float imageWidth = a.dxf_maxx-a.dxf_minx;
		System.out.println(screen_ratio+" = screen_ratio / import_ratio ="+import_ratio);
		if(setBounds) {
			if(screen_ratio >= import_ratio) {

				projection_min_y = 0;
				projection_max_y = height;
				projection_min_x = Math.abs((width*1.0f - (height*imageWidth/imageHeight))/2);
				//						(width-(height/import_ratio))/2;
				projection_max_x= (projection_min_x+(height*imageWidth/imageHeight));
			}else {
				projection_min_x = 0;
				projection_max_x = width;
				projection_min_y = Math.abs((height*1.0f - (width*imageHeight/imageWidth))/2);
				//						(height-(width*import_ratio))/2;
				projection_max_y = (projection_min_y+(width*imageHeight/imageWidth));
			}
			System.out.println("projection_min_x = "+projection_min_x);
			System.out.println("projection_max_x = "+projection_max_x);
			System.out.println("projection_min_y = "+height+"-("+width+"/"+import_ratio+"))/2 = "+projection_min_y);
			System.out.println("projection_max_y = "+projection_max_y);
		}
		urbanModel.proj_min_y = projection_min_y;
		urbanModel.proj_max_y = projection_max_y;
		urbanModel.proj_min_x = projection_min_x;
		urbanModel.proj_max_x = projection_max_x;


		original_max_y = a.dxf_maxy;
		original_min_y = a.dxf_miny;
		original_max_x = a.dxf_maxx;
		original_min_x = a.dxf_minx;

		urbanModel.orig_min_y = original_min_y;
		urbanModel.orig_max_y = original_max_y;
		urbanModel.orig_min_x = original_min_x;
		urbanModel.orig_max_x = original_max_x;



		urbanModel.setBounds = false;

	}

	private void addBuildingFromDXFPolylines(importer a, int loadcycle) {

		if(loadcycle>=1) {
			original_min_y = urbanModel.orig_min_y;
			original_max_y = urbanModel.orig_max_y;
			original_min_x = urbanModel.orig_min_x;
			original_max_x = urbanModel.orig_max_x;
		}

		System.out.println(a.polyline.size()+" nr of polygones");

		for (int i = 0; i < a.polyline.size(); i++) {
			Polyline pp = a.polyline.elementAt(i);

			if(pp.vertices.size()!=0) 
			{

				for(int j = 0; j < pp.vertices.size(); j++) {
					PVector p = (PVector) pp.vertices.elementAt(j);
					float x = PApplet.map(p.x ,original_min_x, original_max_x, projection_min_x, projection_max_x);
					float y = PApplet.map(p.y ,original_min_y, original_max_y, projection_min_y, projection_max_y);

					if (provisoryPolygone == null) {
						provisoryPolygone = new provisoryPolygone(pApp);

						if(p.x!=0 && p.y!=0) {
							provisoryPolygone.addVertex(x,y);
							firstVertex.x = x;
							firstVertex.y = y;
						}
						//						System.out.println(pp.vertices.size()+" / "+j+" first vertices "+x+"/"+y);
					}else {
						if (j == pp.vertices.size()-1) {
							/*last point*/
							if(p.x!=0 && p.y!=0)provisoryPolygone.addVertex(x, y);
							PVector p0 = (PVector) pp.vertices.elementAt(0);
							if(p0.x == 0) {
								//								System.out.println("p0 is 0");
								p0 = (PVector) pp.vertices.elementAt(1);
								if(p0.x == 0) {
									p0 = (PVector) pp.vertices.elementAt(2);
								}
							}

							float x0 = PApplet.map(p0.x ,original_min_x, original_max_x, projection_min_x, projection_max_x);
							float y0 = PApplet.map(p0.y ,original_min_y, original_max_y, projection_min_y, projection_max_y);
							float xi = PApplet.map(0 ,original_min_x, original_max_x, projection_min_x, projection_max_x);
							float yi = PApplet.map(0 ,original_min_y, original_max_y, projection_min_y, projection_max_y);
							provisoryPolygone.addVertex(x0,y0);
							vertex unwantedPoint= new vertex(xi,yi);
							provisoryPolygone.vertexes.remove(unwantedPoint);
							provisoryPolygone.vertexes.remove(unwantedPoint);
							//							System.out.println(pp.vertices.size()+" / "+provisoryPolygone.vertexes.size());
							if (UrbanMap.targetHitTest(PApplet.round(provisoryPolygone.centroid().x/gridSize),PApplet.round(provisoryPolygone.centroid().y/gridSize))<=1) {
								//								float r = pApp.random(0, 100);
								temploadcycle = loadcycle;
								if(provisoryPolygone.vertexes.size()==3) {
									loadcycle = 4;
								}
								if( loadcycle == 1 ) {
									//	Obstacles						
									//									System.out.println("loadcycle 1 "+ provisoryPolygone.vertexes.size());
									if (urbanModel.buildings.size()< 15 && pApp.random(100) > 101 ) {// change 101 to number smaler that 100 for random production of buildings
										buildings.addElement(new Geometry.building(provisoryPolygone, urbanModel, buildings, obstacles, pedestrians,urbanModel));
									}else {
										obstacles.addElement(new Geometry.solidObstacle(provisoryPolygone, pApp));
										solidObstacle o = (solidObstacle)obstacles.elementAt(obstacles.size()-1);
										int t = obstacles.size() + urbanModel.firstNumberOfObstacles;
										o.addObstacleMap(o, UrbanMap, gridSize, t);
									}
									


								}
								else if(loadcycle == 2)
								{
									//	Soft Obstacles
									//	System.out.println("loadcycle 2");

									obstacelsS.addElement(new Geometry.softObstacle(provisoryPolygone,pApp, UrbanMap));
									softObstacle s = (softObstacle)obstacelsS.elementAt(obstacelsS.size()-1);
									s.addSoftObstacle(s, UrbanMap, gridSize);
								}else if(loadcycle == 4){
									// Streets
									//									System.out.println("loadcycle 4");
									UrbanMap.addStreet(provisoryPolygone);
								}
								else {
									// Buildings
									//									System.out.println("loadcycle 3");
									buildings.addElement(new Geometry.building(provisoryPolygone, urbanModel, buildings, obstacles, pedestrians,urbanModel));
//									System.out.println("loadcycle 3");
//									System.out.println("building added nr "+ buildings.size());
									
									building b = (building)buildings.elementAt(buildings.size()-1);
//									System.out.println("number of vertexes = "+provisoryPolygone.vertexes.size() + "; centroid = "+b.centroid.x+"/"+b.centroid.y+ "; points = "+provisoryPolygone.vertexes.elementAt(1).x+"/"+provisoryPolygone.vertexes.elementAt(1).y);							
//									b.addObstacleMap(b, UrbanMap, gridSize);						
								}
								loadcycle = temploadcycle;
							}
							provisoryPolygone = null;
						}else {
							if(p.x!=0 && p.y!=0) {
								provisoryPolygone.addVertex(x, y);
							}
							//							System.out.println(pp.vertices.size()+" / "+j+" Vertices "+x+"/"+y);
						}
					}
				}
			}
		}
		for (int k = 0; k<buildings.size(); k++) {
			building bb = buildings.elementAt(k);
			if(bb.isGeometry)bb.addObstacleMap(bb, UrbanMap, gridSize);
		}
	}

	private void addBuildingFromCSVPoints(importer a) {
		for (int i = 0; i< a.buildings.size(); i++) {
			building b = a.buildings.elementAt(i);


			int size1 = (int) b.size;
			if (buildings.size()<=UrbanMap.maximumNRbuildings) {
				int t = UrbanMap.targetHitTest(pApp.round(b.centroid.x/gridSize), pApp.round(b.centroid.y/gridSize));
				if( t > UrbanMap.firstNumberOfObstacles) {//this is in an obstacle
					solidObstacle o = obstacles.elementAt(t-UrbanMap.maximumNRbuildings);
					provisoryPolygone p = new provisoryPolygone(pApp);
					for(int j=0; j<o.vertexes.size();j++) {
						p.addVertex(o.vertexes.elementAt(j).x,o.vertexes.elementAt(j).y);
					}
					//					tobedeleted.add(o);
					inumerater++;		
					buildings.addElement(new Geometry.building(p, pApp, buildings, obstacles, pedestrians, urbanModel));
					System.out.println("CSV building");
					System.out.println("building added nr "+ buildings.size());
					obstacles.remove(o);
				}else {


					if(t <= -1){//this point is in the middle of no building
						buildings.addElement(new Geometry.building(b.centroid.x, b.centroid.y, size1, pApp, buildings, obstacles, pedestrians, urbanModel));
					}else {//this is a building
						building bb = buildings.elementAt(t);
						provisoryPolygone p = new provisoryPolygone(pApp);
						for (int j = 0; j<bb.vertexes.size(); j++) {
							vertex v = bb.vertexes.elementAt(j);
							p.addVertex(v.x, v.y);
						}

						bToUpdate = new building(p, pApp, buildings, obstacles, pedestrians, urbanModel);
						if (bb.functions != null ) {
							for (int j= 0; j<b.functions.size();j++) {
								String l = b.functions.elementAt(j);
								bb.functions.add(l);
							}

						}else { // doesn't have functions
							//							provisoryPolygone p = new provisoryPolygone(pApp);
							//							for(int j=0; j<bToUpdate.vertexes.size();j++) {
							//								p.addVertex(bToUpdate.vertexes.elementAt(j).x,bToUpdate.vertexes.elementAt(j).y);
							//							}
							functions = b.functions;
							//							buildings.elementAt(t) = new Geometry.building(p,pApp,buildings, obstacles, pedestrians, functions, urbanModel);
							buildings.remove(bb);

							buildings.addElement( new Geometry.building(p,pApp,buildings, obstacles, pedestrians, functions, urbanModel));
						}

					}
				}
			}else {
				k++;
				System.out.println(k+"extrabuildings");
			}
		}
		obstacles.remove(tobedeleted.iterator());

	}


	private void addBuildingFromDXFPoints(importer a) {
		if(setBounds) {
			defineprojection(a, setBounds);
		}else {
			projection_min_y = urbanModel.proj_min_y;
			projection_max_y = urbanModel.proj_max_y;
			projection_min_x = urbanModel.proj_min_x;
			projection_max_x = urbanModel.proj_max_x;
		}
		for (int i = 0; i< a.buildings.size(); i++) {
			building b = a.buildings.elementAt(i);
			if(b.centroid.x>a.dxf_minx && b.centroid.y>a.dxf_miny && b.centroid.x<a.dxf_maxx && b.centroid.y < a.dxf_maxy) {
				float x = PApplet.map(b.centroid.x ,a.dxf_minx, a.dxf_maxx, projection_min_x, projection_max_x);
				float y = PApplet.map(b.centroid.y ,a.dxf_miny, a.dxf_maxy, projection_min_y, projection_max_y);

				int size1 = (int) b.size;
				if (buildings.size()<=UrbanMap.maximumNRbuildings) {
					int t = UrbanMap.targetHitTest(pApp.round(b.centroid.x/gridSize), pApp.round(b.centroid.y/gridSize));
					if( t > UrbanMap.firstNumberOfObstacles) 
					{//this is in an obstacle
						solidObstacle o = obstacles.elementAt(t-UrbanMap.maximumNRbuildings);
						provisoryPolygone p = new provisoryPolygone(pApp);
						for(int j=0; j<o.vertexes.size();j++) {
							p.addVertex(o.vertexes.elementAt(j).x,o.vertexes.elementAt(j).y);
						}
						//					tobedeleted.add(o);
						inumerater++;		
						buildings.addElement(new Geometry.building(p, pApp, buildings, obstacles, pedestrians, urbanModel));
									
						System.out.println("tobedeleted");
						System.out.println("building added nr "+ buildings.size());
						obstacles.remove(o);
					}else {


						if(t <= -1)
						{//this point is in the middle of no building
							buildings.addElement(new Geometry.building(x, y, size1, pApp, buildings, obstacles, pedestrians, urbanModel));
							System.out.println("point is in the middle of no building");
							System.out.println("building added nr "+ buildings.size());
						}else 
						{//this is a building
							building bb = buildings.elementAt(t);
							provisoryPolygone p = new provisoryPolygone(pApp);
							for (int j = 0; j<bb.vertexes.size(); j++) {
								vertex v = bb.vertexes.elementAt(j);
								p.addVertex(v.x, v.y);
							}

							bToUpdate = new building(p, pApp, buildings, obstacles, pedestrians, urbanModel);
							if (bb.functions != null ) 
							{
								for (int j= 0; j<b.functions.size();j++) {
									String l = b.functions.elementAt(j);
									bb.functions.add(l);
								}

							}else 
							{ // doesn't have functions
								functions = b.functions;
								buildings.remove(bb);
								buildings.addElement( new Geometry.building(p,pApp,buildings, obstacles, pedestrians, functions, urbanModel));
								System.out.println("doesn't have function");
								System.out.println("building added nr "+ buildings.size());
							}
						}
					}
				}else {
					k++;
					System.out.println(k+"extrabuildings");
				}
			}
		}
		obstacles.remove(tobedeleted.iterator());

	}
}