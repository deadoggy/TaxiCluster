package org.tj.sse.taxicluster.entity;

public class InfoEntity {
  public String taxiId;
  public double maxSpeed;
  public double minSpeed;
  public double totalDis;//���г�
  public int CpointNum;//�����������
  public double avgSpeed;//ƽ���ٶ�
  public double totalTime;//��ʱ��
  
  public String getTaxiId(){
	  return taxiId;
  }
  public void setTaxiId(String id){
	  this.taxiId = id;
  }
  public double getMaxSpeed(){
	  return maxSpeed;
  }
  public void setMaxSpeed(double max){
	  this.maxSpeed = max;
  }
  public double getMinSpeed(){
	  return minSpeed;
  }
  public void setMinSpeed(double min){
	  this.minSpeed = min;
  }
  public double getAvgSpeed(){
	  return avgSpeed;
  }
  public void setAvgSpeed(double avg){
	  this.avgSpeed = avg;
  }
  public int getCpointNum(){
	  return CpointNum;
  }
  public void setCpointNum(int num){
	  this.CpointNum = num;
  }
  public double getTotalTime(){
	  return totalTime;
  }
  public void setTotalTime(double time){
	  this.totalTime = time;
  }
  public double getTotalDis(){
	  return totalDis;
  }
  public void setTotalDis(double dis){
	  this.totalDis = dis;
  }
}
