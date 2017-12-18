package org.tj.sse.taxicluster.entity;

import java.util.ArrayList;
import java.util.List;

public class Fragment {
 int id ;
 List<Taxi> fragment;
 double[][] similarity;//轨迹之间相似度
 ArrayList<Integer> indexEPS ;//在邻域内的轨迹ID
 int Num_eps;//邻域内有多少条轨迹
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
 //设置主轨迹对应的邻域轨迹的id号组成的list
 public void setIndexEps(ArrayList<Integer> list){
	 indexEPS= list;
 }
  
  
}
