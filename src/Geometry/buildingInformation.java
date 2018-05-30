package Geometry;

import java.util.Vector;

import Importer.pair;
import MovingAgents.*;

public class buildingInformation {

	///////////////////////////////////////////////////////////////////
	//			Variables
	///////////////////////////////////////////////////////////////////


	public int numberOfPeopleSeen;
	public float averageAge;
	private float totaleAge;
	public Vector<function> functions;
	public int numberOfFunctions = 0;
	public int numberOfFloors = 0;
	public Vector<preference> demands;
	public Vector<pair> customersRace;  

	///////////////////////////////////////////////////////////////////
	//			Constructors
	///////////////////////////////////////////////////////////////////


	public buildingInformation() {
		functions = new Vector<function>();
		demands = new Vector<preference>();
		customersRace = new Vector<pair>();
	}

	public buildingInformation(int _numberOfFloors) {
		functions = new Vector<function>();
		numberOfFloors = _numberOfFloors;
		demands = new Vector<preference>();
		customersRace = new Vector<pair>();
	}

	public buildingInformation(function _function) {
		functions = new Vector<function>();
		functions.add(_function);
		demands = new Vector<preference>();
		customersRace = new Vector<pair>();
	}

	///////////////////////////////////////////////////////////////////
	//			Methods
	///////////////////////////////////////////////////////////////////

	public void addFunction(function e) {
		functions.add(e);
		for (int i = 0; i< functions.size();i++) {
			if (functions.elementAt(i).program != e.program) {
				numberOfFunctions++;
			}
		}
	}
	
	public void incrementAge(float age){
		totaleAge += age;
		numberOfPeopleSeen++;
		averageAge = totaleAge/numberOfPeopleSeen;
	}

	public void incrementDemand(socialInformation info){

		for (int i = 0; i < customersRace.size();i++){
			if(info.race == customersRace.elementAt(i).string){
				customersRace.elementAt(i).integer++;
			}
		}

		for (int i = 0; i< info.preferences.size(); i++){
			for (int j = 0; j< demands.size(); j++){

				//add 1 if this program is exist
				if (info.preferences.elementAt(i).program == demands.elementAt(j).program){
					demands.elementAt(j).preference++;
					break;
				}

				//ad this program to the demandList and set preference = 1
				if (j==demands.size()-1){
					demands.add(info.preferences.elementAt(i));
					demands.lastElement().preference=1;
				}
			}
		}
	}

}
