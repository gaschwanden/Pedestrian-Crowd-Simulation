package Importer;
import java.util.ArrayList;
import processing.core.*;



public class EZLink_loader extends PApplet{

	String[] data;
	Trip[] tripsTrip;
	TripContainer[] tripContainerArray;
	ArrayList<Station> DestinationStations;
	ArrayList<Station> OrigineStations;
	ArrayList<String> OriginieNames;
	ArrayList<String> DestinationNames;
	PApplet p;

	int binSize = 900; //sec
	
	int timeBin = (60*60*24)/binSize;

	public EZLink_loader(PApplet p)
	{
		tripContainerArray = new TripContainer[timeBin];
		OriginieNames = new ArrayList<String>();
		DestinationNames = new ArrayList<String>();
		DestinationStations = new ArrayList<Station>();
		OrigineStations = new ArrayList<Station>();

	}

	public class TripContainer {

		int id;
		ArrayList<Trip> TripArrayList;

		TripContainer(int id) {
			this.id = id;
			TripArrayList = new ArrayList<Trip>();
		}
	}

	public void loadTrips() {
		data = loadStrings("/Users/GA/Documents/workspace/UrbanModel_Playground/data/ez_link_trips.csv");

		if(data == null) {
			System.out.println("data is null");
		}
		tripsTrip = new Trip[data.length];
		for (int i = 0; i < data.length; i++) {
			String dataString = data[i];
			parseTrip(dataString);

			//			if (i%1000 == 0)System.out.println(i);
		}

		System.out.println("Destination");
		for (int i = 0; i < DestinationStations.size(); i++) {
			Station s = DestinationStations.get(i);
			System.out.print(s.Name);
			for (int j = 0; j < s.timeBin.length; j++) {
				//				if(s.timeBin[j]>0) {
//				System.out.print(j);
				System.out.print(","+ s.timeBin[j]);
				//				}
			}
			System.out.println("");
		}
		System.out.println("Origine");
		for (int i = 0; i < OrigineStations.size(); i++) {
			Station s = OrigineStations.get(i);
			System.out.print(s.Name);
			for (int j = 0; j < s.timeBin.length; j++) {
				//				if(s.timeBin[j]>0) {
//				System.out.println(j);
				System.out.print(","+ s.timeBin[j]);
				//				}
			}
			System.out.println("");
		}
	}

	public void parseTrip(String dataString) {
		// Break the line into pare the be parsed
		String[] pieces = split(dataString, ',');
		// Crate a Trip Object from all Data String public Trip
		// public parseTrip(String dataString) { // Column number in the CSV
		// file

		// int CARD_ID = 0;
		// int PassengerType = 1;
		// int TRAVEL_MODE = 2;
		int ORIGIN_NAME = 3; // ORIGIN_LOCATION
		int DESTINATION_NAME = 4;// DESTINATION_LOCATION
		// int JOURNEY_START_DATE = 5;
		int JOURNEY_START_TIME = 6;
		int JOURNEY_DISTANCE = 7;
		// int START_TIME = 8; //
		int JOURNEY_TIME = 8; // Journey_Time(min)
		// int JOURNEY_TIME_FULL = 9;
		// int Journey_End_Time = 10;
		int ORIGIN_LAT = 11; // start_lat
		int ORIGIN_LON = 12; // start_lon
		int DESTINATION_LAT = 13; // end_lat
		int DESTINATION_LON = 14; // end_lon
		int Origin_StopID = 15;
		// int Destination_StopID = 16;
		// int TripID = 17;

		//		float x = 	 PApplet.parseFloat(pieces[ORIGIN_LAT]) ;

		//		System.out.println(x);
		// Return the new Trip object
		String noHyphen = pieces[ORIGIN_NAME].substring(1, pieces[ORIGIN_NAME].length()-1);
		String destnoHyphen = pieces[DESTINATION_NAME].substring(1, pieces[DESTINATION_NAME].length()-1);
		if(		 
//				pieces[DESTINATION_NAME].equals("\"STN Raffles Place\"") 
//				||
//				pieces[ORIGIN_NAME].equals("\"STN Meridian\"") ||
//				pieces[ORIGIN_NAME].equals("\"STN Cove\"") ||
//				pieces[ORIGIN_NAME].equals("\"STN Oasis\"") ||
//				pieces[ORIGIN_NAME].equals("\"STN Coral Edge\"") ||
//				pieces[ORIGIN_NAME].equals("\"STN Riviera\"") 


								PApplet.parseFloat(pieces[ORIGIN_LAT])	>= 2.303999
//								&& 
//								PApplet.parseFloat(pieces[ORIGIN_LAT]) 	<= 1.318415 
//								&& 
//								PApplet.parseFloat(pieces[ORIGIN_LON]) 	>= 103.845177
//								&& 
//								PApplet.parseFloat(pieces[ORIGIN_LON]) 	<= 103.860111

				) {
			if( OriginieNames.contains(noHyphen))
			{
				for (int i = 0; i < OrigineStations.size(); i++) {
					Station s = OrigineStations.get(i);
					String t = s.Name;
					if(t.equals(noHyphen)) {
						int time = round(convertToSeconds(pieces[JOURNEY_START_TIME])/binSize);
						s.timeBin[time]++;
					}
				}
			}else {
				System.out.println("new Station " + noHyphen);
				OriginieNames.add(noHyphen);
				Station s = new Station(noHyphen, timeBin);
				int time = round(convertToSeconds(pieces[JOURNEY_START_TIME])/binSize);
				s.timeBin[time]++;				
				OrigineStations.add(s);


			}
		}
		if(		 
				pieces[DESTINATION_NAME].equals("\"STN Raffles Place\"") 
				||
				pieces[DESTINATION_NAME].equals("\"STN Tanjong Pagar\"")
				||
				pieces[DESTINATION_NAME].equals("\"STN City Hall\"") ||
				pieces[DESTINATION_NAME].equals("\"STN Orchard Rd\"") 
//				||
//				pieces[DESTINATION_NAME].equals("\"STN Coral Edge\"") ||
//				pieces[DESTINATION_NAME].equals("\"STN Riviera\"") 

			
								
//				PApplet.parseFloat(pieces[DESTINATION_LAT])	>= 1.303999
//				&& 
//				PApplet.parseFloat(pieces[DESTINATION_LAT]) 	<= 1.318415 
//				&& 
//				PApplet.parseFloat(pieces[DESTINATION_LON]) 	>= 103.845177
//				&& 
//				PApplet.parseFloat(pieces[DESTINATION_LON]) 	<= 103.860111
				) {
			if( DestinationNames.contains(destnoHyphen))
			{
				for (int i = 0; i < DestinationStations.size(); i++) {
					Station s = DestinationStations.get(i);
					String t = s.Name;
					if(t.equals(destnoHyphen)) {
						int time = round(convertToSeconds(pieces[JOURNEY_START_TIME])/binSize);
						s.timeBin[time]++;
					}
				}
			}else {
				System.out.println("new destStation " + destnoHyphen);
				DestinationNames.add(destnoHyphen);
				Station s = new Station(destnoHyphen, timeBin);
				int time = round(convertToSeconds(pieces[JOURNEY_START_TIME])/binSize);
				s.timeBin[time]++;				
				DestinationStations.add(s);


			}
		}
	}

	private int convertToSeconds(String time) {

		int h = Integer.parseInt(time.substring(1, time.indexOf(":")));
		int m = Integer.parseInt(time.substring((time.indexOf(":") + 1),
				time.indexOf(":") + 3));
		int s = Integer.parseInt(time.substring((time.indexOf(":") + 5),
				(time.indexOf(":") + 6)));

		// Convert into Seconds
		int totalSeconds = ((h * 3600) + (m * 60) + s);

		// Return the Value
		return totalSeconds;

	}

	public class Trip {

		// TRIP ATTRIBUTEs
		int start_time;
		String origin_name;
		String destination_name;

		// Screen position
		float origin_x;
		float origin_y;
		float destination_x;
		float destination_y;
		float origin_lon;
		float origin_lat;
		float destination_lon;
		float destination_lat;
		float traveleddistance;
		float TotalTraveledTime;
		float traveltime;
		float travelspeed;

		// Animation variables
		float angleTravelled; // The angle taken by the path
		float speed = 0.5f; // Speed at which to animate paths
		float angle; // Angle of rotation around y axis to align with
		// origin-dest line

		// Constructor
		Trip(String start_time, String origin_name, String destination_name,
				String destination_lon, String destination_lat,
				String origin_lon, String origin_lat, String traveleddistance,
				String traveltime) {
			this.start_time = convertToSeconds(start_time);
			this.origin_name = origin_name;
			this.destination_name = destination_name;
			this.origin_lon = PApplet.parseFloat(origin_lon);
			this.origin_lat = PApplet.parseFloat(origin_lat);
			this.destination_lon = PApplet.parseFloat(destination_lon);
			this.destination_lat = PApplet.parseFloat(destination_lat);
			this.traveleddistance = PApplet.parseFloat(traveleddistance);
			this.traveltime = PApplet.parseFloat(traveltime);


			this.setBounds();// set the Bounderies
		}

		public void setBounds() {

			float w_start = 103.570862f; // Longitude start (largest longitude value
			float w_end = 104.071803f;
			float h_start = 1.509072f;
			float h_end = 1.193304f;
			//			float w_start = 103.577781f; // Longitude start (largest longitude value
			//			float w_end = 104.077520f;
			//			float h_start = 1.539417f;
			//			float h_end = 1.174656f;
			int width = 1024;
			int height = 768;

			origin_x = map(origin_lon, w_start, w_end, 0, width);
			origin_y = map(origin_lat, h_start, h_end, 0, height);
			destination_x = map(destination_lon, w_start, w_end, 0, width);
			destination_y = map(destination_lat, h_start, h_end, 0, height);
		}

		private int convertToSeconds(String time) {

			int h = Integer.parseInt(time.substring(1, time.indexOf(":")));
			int m = Integer.parseInt(time.substring((time.indexOf(":") + 1),
					time.indexOf(":") + 3));
			int s = Integer.parseInt(time.substring((time.indexOf(":") + 5),
					(time.indexOf(":") + 6)));

			// Convert into Seconds
			int totalSeconds = ((h * 3600) + (m * 60) + s);

			// Return the Value
			return totalSeconds;

		}
	}
}