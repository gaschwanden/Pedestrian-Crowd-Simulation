package UrbanModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Geometry.*;


public class statisticBins {

	public float average;
	public float variance;
	public float standartDeviation;
	List<statisticPoints> pollutions;
	private float totalPollution;
	private float totalvariancePollution;
	
	public statisticBins() {
		average = 0;
		variance = 0;
		standartDeviation= 0;
		pollutions = new Vector<statisticPoints>();
		totalPollution = 0;
		totalvariancePollution = 0;
	}

	public void addElement(statisticPoints statisticPoints) {
		pollutions.add(statisticPoints);
	}

	public void calculateStatistics() {
		totalPollution = 0;
		totalvariancePollution = 0;
		for (int i = 0; i <pollutions.size(); i++) {
			totalPollution = totalPollution + pollutions.get(i).pollution;
			if(i==pollutions.size()-1) {
				average = totalPollution/pollutions.size();
			}
		}
		
		for (int i = 0; i <pollutions.size(); i++) {
//			System.out.println("pollutions - average = "+pollutions.get(i).y+" - "+average);
			totalvariancePollution =  totalvariancePollution + (pollutions.get(i).pollution-average)*(pollutions.get(i).pollution-average);
			if(i==pollutions.size()-1) {
				variance = totalvariancePollution/pollutions.size();
//				System.out.println("totalvariancePollutions / variance "+totalvariancePollution+" / "+variance);
				standartDeviation = (float) Math.sqrt(variance);
			}
		}
	}
}
