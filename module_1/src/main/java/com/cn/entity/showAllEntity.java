package com.cn.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class showAllEntity {

	private List<List<Taxi>> fragment;
	
	private HashMap<Integer,ArrayList<Double>> similarity;

	public HashMap<Integer,ArrayList<Double>> getSimilarity() {
		return similarity;
	}

	public void setSimilarity(HashMap<Integer,ArrayList<Double>> similarity) {
		this.similarity = similarity;
	}

	public List<List<Taxi>> getFragment() {
		return fragment;
	}

	public void setFragment(List<List<Taxi>> fragment) {
		this.fragment = fragment;
	}
	
}
