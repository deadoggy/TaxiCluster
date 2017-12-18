package org.tj.sse.taxicluster.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class showAllEntity {

    private Boolean isMapSim;

	private List<List<Taxi>> fragment;
	
	private HashMap<Integer,ArrayList<Double>> mapSimilarity;

	private double[][] doubleSimilarity;

	public Object getSimilarity(boolean type) {
	    return isMapSim ?  mapSimilarity : doubleSimilarity;
	}

	public void setSimilarity(HashMap<Integer,ArrayList<Double>> similarity) {
		this.mapSimilarity = similarity;
		isMapSim = true;
	}

	public void setSimilarity(double[][] similarity){
	    this.doubleSimilarity = similarity;
	    isMapSim = false;
	}

	public List<List<Taxi>> getFragment() {
		return fragment;
	}

	public void setFragment(List<List<Taxi>> fragment) {
		this.fragment = fragment;
	}
	
}
