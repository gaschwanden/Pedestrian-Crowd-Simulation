package UrbanModel;

import java.util.Vector;

import processing.core.PApplet;

import Geometry.*;
import Importer.*;




public class Evaluation {
	private Vector<building> tempBuildings;
	Vector<solidObstacle> tempSolidObstacles;
	Vector<solidObstacle> defSolidObstacles;
	private Vector<building> buildings;
	Vector<solidObstacle> solidObstacles;
	private PApplet p;
	private solidObstacle nextObject;
	private float xCoordinate;
	private float yCoordinate;
	private float distance = 2500;
	private UrbanModel urbanModel;
	Vector<solidObstacle> testedObjects;
	Vector<Thread> Threads;

	public Evaluation(Vector<building> _buildings, Vector<solidObstacle> _obstacles, Vector<softObstacle> obstaclesS, Vector<softObstacle> streets, PApplet _p, UrbanModel _urbanModel) {
		p = _p;
		buildings = _buildings;
		solidObstacles = _obstacles;
		tempBuildings = new Vector<building>();
		tempSolidObstacles = new Vector<solidObstacle>();
		defSolidObstacles = new Vector<solidObstacle>();
		urbanModel = _urbanModel;

	}

	public void total() {
		System.out.println("Wollt ihr die Totale Evaluation");
		System.out.println("JA!!");
		System.out.println("solidObstacles = "+ solidObstacles.size());
		System.out.println("buildings = "+ buildings.size());

		// Test one, to be deleted if new one is working

		
		mergeSolidANDBuildingTOSolid(tempSolidObstacles,solidObstacles,buildings);
		System.out.println("tempSolidObstacles = "+ tempSolidObstacles.size());
		findNextElement(tempSolidObstacles);
		findBuildingsInVicinity(nextObject,distance);
		urbanModel.UrbanMap.metricDistanceMap(tempBuildings, 0);	
		tempSolidObstacles = null;

		
		// A.0. Preparation
		// A.0.1 load obstacles, streets, buildings without projection
		// A.0.2 find maximal diameter and generate global maps
		// A.0.3 
		

		// iterate through the hole vector until empty

		// B.1. map buildings and obstacles in reference to nextObject building
		// B.1.1 map vertexes and find mapping of occupation map to global map 
		// B.1.2 make obstruction map
		// B.1.3 make soft Walkobstruction
		// B.1.4 make metric distance map
		// B.2. run pedestrian simulation
		// B.2.1 emit x amount of pedestrians from central to each of the other buildings
		// B.2.2 after time or nr of agents arrived remove all pedestrians
		
		
		// B.3. put information into global 
		// B.3.1 map footfall to gloablfootfall
		
		// B.4. clean up
		// B.4.1 put buildings back to obstacles
		//
		
		// C.1 Export
		// C.1.1 export image
		// C.1.1 export CSV of maps
		



	}

	private void findBuildingsInVicinity(solidObstacle centreBuilding, float dist) {
		for (int i = 0; i < tempSolidObstacles.size(); i++) {
			solidObstacle s = tempSolidObstacles.elementAt(i);
			float distanceToCentre = (float) Math.sqrt((s.centroid.x-centreBuilding.centroid.x)*(s.centroid.x-centreBuilding.centroid.x)+(s.centroid.y-centreBuilding.centroid.y)*(s.centroid.y-centreBuilding.centroid.y));
			if(distanceToCentre<dist) {
				provisoryPolygone provPoly = new provisoryPolygone(p);
				for (int j = 0; j < s.vertexes.size(); j++) {
					provPoly.addVertex(s.vertexes.elementAt(j));
				}
				
				tempBuildings.addElement(new building(provPoly,p, buildings, tempSolidObstacles, urbanModel));
				tempSolidObstacles.remove(s);

			}
		}
	}

	private void findNextElement(Vector<solidObstacle> tempSolidObstacles2) {
		xCoordinate = 99999999;
		yCoordinate = 99999999;
		for (int i = 0; i < tempSolidObstacles.size(); i++) {
			solidObstacle s = tempSolidObstacles.elementAt(i);
			if (testedObjects.contains(s))break;
			if (s.centroid.x < xCoordinate) {
				xCoordinate = s.centroid.x;
				if(s.centroid.y<nextObject.centroid.y) {
					nextObject = s;
					
				} else {

				}
			}
		}
		testedObjects.addElement(nextObject);
		tempSolidObstacles.remove(nextObject);
	}

	private void mergeSolidANDBuildingTOSolid(
			Vector<solidObstacle> tempSolidObstacles2,
			Vector<solidObstacle> solidObstacles2, Vector<building> buildings2) {

		tempSolidObstacles2 = solidObstacles2;
		for (int i = 0; i < buildings2.size(); i++) {
			building b = buildings2.elementAt(i);
			provisoryPolygone provPoly = new provisoryPolygone(p);
			for (int j = 0; j<b.vertexes.size();j++) {
				vertex v = b.vertexes.elementAt(j);
				provPoly.addVertex(v);
			}
			solidObstacle s = new solidObstacle(provPoly, p);
			tempSolidObstacles2.addElement(s);
		}

		tempSolidObstacles = tempSolidObstacles2;

	}

}
