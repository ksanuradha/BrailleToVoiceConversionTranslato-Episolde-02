package com.edu.msc.sliit.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class FindXCordinates {
	ArrayList<Double> dupicates = new ArrayList<>();// Store Y cordinates with Duplicates
	ArrayList<Double> unikeYcordinates = new ArrayList<>();// Unike Y cordinates for Each Dot

	ArrayList<Double> dupicatesX = new ArrayList<>();// Store x cordinates with Duplicates
	ArrayList<Double> unikeXcordinates = new ArrayList<>();// Unike x cordinates for Each Dot

	// Geting X and Y Cordinates with no duplicates and Assending Order
	public HashMap<Integer, ArrayList<Double>> findAverageXCordinates(HashMap<Integer, double[]> findRectangle) {
		HashMap<Integer, ArrayList<Double>> xyCordinates = new HashMap<Integer, ArrayList<Double>>();
		Iterator hmIterator = findRectangle.entrySet().iterator();
		while (hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry) hmIterator.next();
			double[] cordinates = ((double[]) mapElement.getValue());
			dupicates.add(cordinates[1]);// Getting Y cordinates
			dupicatesX.add(cordinates[0]);// Getting X cordinates
		}
		unikeYcordinates = removeDuplicates(dupicates);// Remove Y cordinates Duplicates
		unikeXcordinates = removeDuplicates(dupicatesX);// Remove X cordinates Duplicates
		Collections.sort(unikeYcordinates);// Sorting the Array
		Collections.sort(unikeXcordinates);// Sorting the Array
		// arrayListPrint(unikeXcordinates);
		xyCordinates.put(0, unikeXcordinates);// Draw top To Bottom
		xyCordinates.put(1, unikeYcordinates);// Draw left to Right
		return getAvegXYCordinates(xyCordinates);
		// return xyCordinates;
	}

	// Remove Duplicates
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
		// Create a new ArrayList
		ArrayList<T> newList = new ArrayList<T>();
		// Traverse through the first list
		for (T element : list) {
			// If this element is not present in newList
			// then add it
			if (!newList.contains(element)) {
				newList.add(element);
			}
		}
		// return the new list
		return newList;
	}

	// Getting X and Y Cordinates Averages
	public HashMap<Integer, ArrayList<Double>> getAvegXYCordinates(
			HashMap<Integer, ArrayList<Double>> findAverageXCordinates) {
		Iterator hmIterator = findAverageXCordinates.entrySet().iterator();
		int a = 0;
		HashMap<Integer, ArrayList<Double>> averageXYCordinates = new HashMap<Integer, ArrayList<Double>>();
		ArrayList<Double> avegXYCordinates = null;
		while (hmIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry) hmIterator.next();
			ArrayList<Double> cordinates = ((ArrayList<Double>) mapElement.getValue());// 1 St iteratin get X cordinates
																						// 2nd Y cordinates
			if (a == 0) { // a==0 for Y a==1 for x
				averageXYCordinates.put(0, getColumnAverage(getAvegXYCordinates(cordinates, 1.5)));// X Cordinates->||
																									// lines
			} else {
				averageXYCordinates.put(1, getAvegXYCordinates(cordinates, 2.9));// Y Cordinates ->-- lines
			}
			a++;
		}
		return averageXYCordinates;
	}

	private ArrayList<Double> getColumnAverage(ArrayList<Double> get) {
		double startElement = get.get(0);
		ArrayList<Double> out = new ArrayList<Double>();
		out.add(0, startElement);
		double dfifference = 0;
		for (int i = 0; i < get.size() - 1; i++) {
			dfifference = dfifference + (-1 * (get.get(i) - get.get(i + 1)));
		}
		double colAvgeSeparate = dfifference / (get.size() - 1);
		for (int a = 1; a < get.size(); a++) {
			startElement = startElement + colAvgeSeparate;
			out.add(a, startElement);
		}
		return out;
	}

	// Calculate Average
	private ArrayList<Double> getAvegXYCordinates(ArrayList<Double> cordinates, double nextIndex) {
		int startPosition = 0;
		double priviousValue = cordinates.get(0), sum = 0, avg = 0;
		ArrayList<Double> avgCordinates = new ArrayList<>();// store Avg Values
		for (int i = 0; i < cordinates.size(); i++) {
			sum = sum + cordinates.get(i); // Wrong
			startPosition++;
			// System.out.println("privious : "+priviousValue+" Current :
			// "+cordinates.get(i)+ " Difference : "+(cordinates.get(i)-priviousValue) );
			if ((priviousValue + nextIndex) < cordinates.get(i)) {
				// Do the Average Calculation
				sum = sum - cordinates.get(i);
				avg = sum / (startPosition - 1);
				avgCordinates.add(avg);
				avg = 0;
				startPosition = 1;// Start next Episode here
				sum = cordinates.get(i);// Start next Episode here
			}
			priviousValue = cordinates.get(i);
		}
		// Final iterations does not find priviousValue+2.8
		avg = sum / startPosition;
		avgCordinates.add(avg);
		return avgCordinates;
	}

	// Testing purpose prinyt Array List
	private void arrayListPrint(ArrayList<Double> cordinates) {
		for (int i = 0; i < cordinates.size(); i++) {
			System.out.print(cordinates.get(i) + ",");
		}
	}
}
