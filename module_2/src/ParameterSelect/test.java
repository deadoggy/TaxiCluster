package ParameterSelect;

import java.util.*;

import com.cn.entity.*;
public class test {
	public static void main(String[] args){
       //给数据进行赋值
		List<List<Taxi>> trajectoryA = new ArrayList<>();//包含多条轨迹
		List<Taxi> listA = new ArrayList<>();//A号轨迹
		 
		List<Taxi> listB = new ArrayList<>();//B号轨迹
		for(int i=0;i<10;i++){
			Taxi taxiA = new Taxi();
			Taxi taxiB = new Taxi();
			String locA = String.valueOf(i+1);
			String locB = String.valueOf(i+4);
			taxiA.setLongtitude(locA);
			taxiA.setLatitude(locA+i);
			listA.add(taxiA);
			
			taxiB.setLongtitude(locB);
			taxiB.setLatitude(locB+4);
			listB.add(taxiB);
		}
		trajectoryA.add(listA);
		trajectoryA.add(listB);
		test testit = new test();
	double[][] Similarity = testit.GetSimilarity(trajectoryA);
	  System.out.println(Similarity[0][0]);
	} 
	public double[][] GetSimilarity(List<List<Taxi>> trajectory) {
		int size = trajectory.size();
		double[][] similarity = new double[size][size];
		for(int i=0;i<size;i++){
			for (int j = 0; j < size; j++) {
				similarity[i][j] = GetDirectionSensitiveHausdorffDistance(trajectory.get(i), trajectory.get(j));
				
			}
		}
		return similarity;
	}
	public double GetDirectionSensitiveHausdorffDistance(List<Taxi> TA, List<Taxi> TB){
		double scoreAB=0, scoreBA=0, closestDistanceSumAToB = 0, closestDistanceSumBToA = 0;
		int numOfTA = TA.size();
		int TASegmentSize = numOfTA-1;
		int numOfTB = TB.size();
		int TBSegmentSize = numOfTB-1;
		int closestIndex = 0;
		//each point in A to TB
		for(int i = 0;i<numOfTA;i++){
			double closestDistance = Double.MAX_VALUE;
			Taxi taxi = TA.get(i);
			for(int j = closestIndex;j<TBSegmentSize;j++){
				double x1 = Double.parseDouble(TB.get(j).getLatitude());
				double y1 = Double.parseDouble(TB.get(j).getLongtitude());
				double x2 = Double.parseDouble(TB.get(j+1).getLatitude());
				double y2 = Double.parseDouble(TB.get(j+1).getLongtitude());
				double x0 = Double.parseDouble(taxi.getLatitude());
				double y0 = Double.parseDouble(taxi.getLongtitude());
				double tempDistance = PointToLine(x1,y1,x2,y2,x0,y0);
				if(tempDistance<closestDistance){
					closestIndex = j;
					closestDistance = tempDistance;
				}
			}
			closestDistanceSumAToB = closestDistanceSumAToB + closestDistance;
		}
		scoreAB = closestDistanceSumAToB/numOfTA;
		
		//each point in B to TA
		closestIndex = 0;
		for(int i = 0;i<numOfTB;i++){
			double closestDistance = Double.MAX_VALUE;
			Taxi taxi = TB.get(i);
			for(int j = closestIndex;j<TASegmentSize;j++){
				double x1 = Double.parseDouble(TA.get(j).getLatitude());
				double y1 = Double.parseDouble(TA.get(j).getLongtitude());
				double x2 = Double.parseDouble(TA.get(j+1).getLatitude());
				double y2 = Double.parseDouble(TA.get(j+1).getLongtitude());
				double x0 = Double.parseDouble(taxi.getLatitude());
				double y0 = Double.parseDouble(taxi.getLongtitude());
				double tempDistance = PointToLine(x1,y1,x2,y2,x0,y0);
				if(tempDistance<closestDistance){
					closestIndex = j;
					closestDistance = tempDistance;
				}
			}
			closestDistanceSumBToA = closestDistanceSumBToA + closestDistance;
		}
		scoreBA = closestDistanceSumBToA/numOfTB;
		
		return (scoreAB+scoreBA)/2;
	}
	private double PointToLine(double x1, double y1, double x2, double y2, double x0, double y0){
		  double space = 0;
        double a, b, c;
        a = GetDistance(x1, y1, x2, y2);// 线段的长度
        b = GetDistance(x1, y1, x0, y0);// (x1,y1)到点的距离
        c = GetDistance(x2, y2, x0, y0);// (x2,y2)到点的距离
        if (c <= 0.000001 || b <= 0.000001) {
           space = 0;
           return space;
        }
        if (a <= 0.000001) {
           space = b;
           return space;
        }
        if (c * c >= a * a + b * b) {
           space = b;
           return space;
        }
        if (b * b >= a * a + c * c) {
           space = c;
           return space;
        }
        double p = (a + b + c) / 2;// 半周长
        double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// 海伦公式求面积
        space = 2 * s / a;// 返回点到线的距离（利用三角形面积公式求高）
        return space;
	}
	public static double GetDistance(double long1, double lat1, double long2, double lat2) {
		double a, b, R;
		R = 6378.137; // 地球半径km
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
		return d;
	}
}
