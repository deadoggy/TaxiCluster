package org.tj.sse.taxicluster.entity;

import java.util.ArrayList;
import java.util.List;

public class Fragment {
 int id ;
 List<Taxi> fragment;
 double[][] similarity;//�켣֮�����ƶ�
 ArrayList<Integer> indexEPS ;//�������ڵĹ켣ID
 int Num_eps;//�������ж������켣
 public void setID(int id){
	this.id = id;
 }
 public void setfragment(List<Taxi> fragment){
	 this.fragment = fragment;
 }
 public int getID(){
	 return id;
 }
 public List<Taxi> getFragment(){
	 return fragment;
 }
 public void setSimilarity(double[][] similarity){
	 this.similarity= similarity;
 }
 public double[][] getSimilarity(){
	 return similarity;
 }
 public ArrayList<Integer> GetindexEPS(){
	 return indexEPS;
 }
 //�������켣��Ӧ������켣��id����ɵ�list
 public void setIndexEps(ArrayList<Integer> list){
	 indexEPS= list;
 }
  
  
}
