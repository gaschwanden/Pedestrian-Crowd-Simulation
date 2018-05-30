package UrbanModel;

import Geometry.vertex;

public class statisticPoints {
	public vertex origine;
	public vertex destination;
	public float pollution;
	public float distance;
	
	public statisticPoints() {
		
	}
	public statisticPoints(float _distance, float _pollution) {
		origine = new vertex(0, 0);
		destination = new vertex(0, 0);
		pollution = _pollution;
		distance = _distance;
	}

	public statisticPoints(float _pollution, float _distance, vertex _origine, vertex _destination) {
		origine = _origine;
		destination = _destination;
		pollution = _pollution;
		distance = _distance;
	}
}
