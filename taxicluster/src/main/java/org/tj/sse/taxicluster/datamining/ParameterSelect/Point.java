package org.tj.sse.taxicluster.datamining.ParameterSelect;

public class Point {
	public double x;
	public double y;
	int order = -1;
	//�����ţ������켣����ţ�
	int num = -1;
	public Point(){
		
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Point(double x , double y){
		this.x = x;
		this.y = y;
	}
	
	public int getNum() {
		return num;
	}
	/**
	 * @description ���ù켣���
	 * @param num
	 */
	public void setNum(int num) {
		this.num = num;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	@Override
	public String toString() {
		return x + "\t" + y;
	}
	
}