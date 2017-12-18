package org.tj.sse.taxicluster.datamining.ParameterSelect;

import java.util.ArrayList;

public class Tracks {
	public static void main(String[] args){
		//���Խ�ÿ���켣����һ������
		ArrayList<ArrayList<Integer>> ClusterAll = new ArrayList<ArrayList<Integer>>();//���еľ���
	    for(int i = 0;i<10;i++){
	    	ArrayList<Integer> Cluster = new ArrayList<Integer>();//ÿ���켣��Ӧһ������
	    	if(Cluster.size()<1){
	    	Cluster.add(i);
	    	}
	    	ClusterAll.add(Cluster);	
	    }
	}
}
