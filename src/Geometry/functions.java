package Geometry;

import java.util.ArrayList;
import java.util.List;

public class functions {
	ArrayList<String> functionsList= new ArrayList<String>();
	public functions(){
		
//		functionsList.add("Warship");
//		functionsList.add("Evening entertainment");
//		functionsList.add("Hotel");
//		functionsList.add("Restaurant");
//		functionsList.add("Beauty");
//		functionsList.add("Hardwarestore");
//		functionsList.add("Car Supply");
//		functionsList.add("Museum");
//		functionsList.add("Retail");
//		functionsList.add("Special");
	}
	public void add(String string) {
		functionsList.add(string);
	}
	public int size() {
		int x = functionsList.size();
		return x;
	}
	public String elementAt(int j) {
		String x = functionsList.get(j);
		return x;
	}
	
//	public void get(int i){
//		return functionsArray[i];
//	}
		
}
