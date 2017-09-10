package com.cn.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.dao.TaxiDao;
import com.cn.dao.TurningPointDao;
import com.cn.entity.Cluster_Point;
import com.cn.entity.Fragment;
import com.cn.entity.HierarchicalCluster;
import com.cn.entity.Taxi;
import com.cn.entity.TurningPoint;
import com.cn.entity.trajectoryCluster;

@Service("taxiService")
public class TaxiServiceImpl implements TaxiService {

	private TaxiDao taxiDao;
	
	private TurningPointDao turningPointDao;

	public TurningPointDao getTurningPointDao() {
		return turningPointDao;
	}

	@Resource
	public void setTurningPointDao(TurningPointDao turningPointDao) {
		this.turningPointDao = turningPointDao;
	}

	public TaxiDao getTaxiDao() {
		return taxiDao;
	}

	@Resource
	public void setTaxiDao(TaxiDao taxiDao) {
		this.taxiDao = taxiDao;
	}

	@Override
	public List<Taxi> search(String taxiId, String time) {
		List<Taxi> result = taxiDao.Search(taxiId, time);
		transform(result);
		return result;
	}
	@Override
	public List<Taxi> search_frequency(String taxiId, String time) {
		List<Taxi> result = taxiDao.SearchFrequency(taxiId, time);
		return result;
	}
	@Override
	public List<Taxi> getAll() {
		return taxiDao.getAll();
	}

	@Override
	public Set<String> getAllTaxiId( ) {
		return taxiDao.getAllTaxiId();
	}

	@Override
	public void transform(List<Taxi> list) {
		for(int i=0;i<list.size();i++)
		{
			double longtitude = Double.parseDouble(list.get(i).getLongtitude())+0.0045;
			list.get(i).setLongtitude(Double.toString(longtitude));
			double latitude = Double.parseDouble(list.get(i).getLatitude())-0.002;
			list.get(i).setLatitude(Double.toString(latitude));
		}
	}

	@Override
	public void GenerateTurningPoints(List<String> taxiIds) {
		List<TurningPoint> turningPoints = new ArrayList<TurningPoint>();
		System.out.println("ids:"+taxiIds.size());
		for(int i = 0; i<taxiIds.size();i++){
			List<Taxi> array = taxiDao.Search(taxiIds.get(i), null);
			//handle single trajectory of one car
			for(int j=1;j<array.size();j++){
				Taxi pre = array.get(j-1);
				Taxi aft = array.get(j);
				if (!pre.getState().equals(aft.getState())) {
					turningPoints.add(
							new TurningPoint(pre.getLongtitude(), pre.getLatitude(), pre.getState(), pre.getTime(), pre.getTaxi_Id(), pre.getSpeed(), null));
					turningPoints.add(
							new TurningPoint(aft.getLongtitude(), aft.getLatitude(), aft.getState(), aft.getTime(), aft.getTaxi_Id(), aft.getSpeed(), null));
				}
//				if (pre.getState().equals(aft.getState())&&pre.getLongtitude().equals(aft.getLongtitude()) && pre.getLatitude().equals(aft.getLatitude())
//						&& !pre.getTime().equals(aft.getTime()) && pre.getSpeed().equals("0")&& aft.getSpeed().equals("0")) {
//					turningPoints.add(
//							new TurningPoint(aft.getLongtitude(), aft.getLatitude(), aft.getState(), aft.getTime(), aft.getTaxi_Id(), aft.getSpeed()));
//					
//				}
			}
		}
		//save all turningpoints
		for(int i=0;i<turningPoints.size();i++){
			turningPointDao.save(turningPoints.get(i));
		}
		
	}

	@Override
	public List<List<Taxi>> GetFragment(List<String> list) {
		List<List<Taxi>> result= new ArrayList<>();
		for(int i=0;i<list.size();i++)
		{
			List<Taxi> array = taxiDao.Search(list.get(i), null);
			//System.out.println(array);
			int trajectoryTotalSize = array.size();
			int fragmentSize = 0;
			List<Taxi> fragment = null;
			for(int j=0;j<trajectoryTotalSize;j++)
			{
				Taxi currentTaxi = array.get(j);
				double cos=1;
				if(j > 0 && j < trajectoryTotalSize - 1){
				double xA = Double.parseDouble(currentTaxi.getLatitude())-Double.parseDouble(array.get(j-1).getLatitude());
				double yA = Double.parseDouble(currentTaxi.getLongtitude())-Double.parseDouble(array.get(j-1).getLongtitude());
				double xB = Double.parseDouble(array.get(j+1).getLatitude())-Double.parseDouble(currentTaxi.getLatitude());
				double yB = Double.parseDouble(array.get(j+1).getLatitude())-Double.parseDouble(currentTaxi.getLatitude());
				cos = (xA*xB+yA*yB)/(Math.sqrt(xA*xA+yA*yA)*Math.sqrt(xB*xB+yB*yB));
				}
				//ignore no-load point
				if (currentTaxi.getState().equals("0"))
					continue;
				//start a new fragment
				else if ((j == 0 && currentTaxi.getState().equals("1"))
						|| (j > 0 && currentTaxi.getState().equals("1") && array.get(j - 1).getState().equals("0"))) {
					if (fragment!=null&&fragmentSize != 0&&fragment.size()>2)
					{
						transform(fragment);
						result.add(fragment);
					}
						
					fragment = new ArrayList<>();
					fragment.add(currentTaxi);
					fragmentSize++;
					continue;
				}
				//找到特征点
				else if (j > 0 && j < trajectoryTotalSize - 1 && currentTaxi.getState().equals("1")
						&& array.get(j - 1).getState().equals("1") && array.get(j + 1).getState().equals("1")
						&& cos <= 0) {
					fragment.add(currentTaxi);
					if(fragment!=null&&fragment.size()>2) result.add(fragment);
					fragment = new ArrayList<>();
					fragmentSize++;
					fragment.add(currentTaxi);
					continue;
				}
//				else if (j > 0
//						&& ((Integer.parseInt(currentTaxi.getAngle())
//								- Integer.parseInt(array.get(j - 1).getAngle())) >= 90)
//						&& currentTaxi.getState().equals("1") && array.get(j - 1).getState().equals("1")) {
//					fragment.add(currentTaxi);
//					if(fragment.size()>1) result.add(fragment);
//					fragment = new ArrayList<>();
//					fragmentSize++;
//					fragment.add(currentTaxi);
//					continue;
//				}
				else if (currentTaxi.getState().equals("1")) fragment.add(currentTaxi);
			}
			//add the last fragment
			if(fragment!=null&&fragment.size()>2) {
				transform(fragment);
				result.add(fragment);
			}
		}
		return result;
	}
	
//针对展示所有轨迹
	public List<List<Taxi>> GetFragmentAll(List<String>list) {
		List<List<Taxi>> result= new ArrayList<>();
		for(int i=0;i<list.size();i++)
		{
			List<Taxi> array = taxiDao.SearchAll(list.get(i), null);
			//System.out.println(array);
			int trajectoryTotalSize = array.size();
			int fragmentSize = 0;
			List<Taxi> fragment = null;
			for(int j=0;j<trajectoryTotalSize;j++)
			{
				Taxi currentTaxi = array.get(j);
				double cos=1;
				if(j > 0 && j < trajectoryTotalSize - 1){
				double xA = Double.parseDouble(currentTaxi.getLatitude())-Double.parseDouble(array.get(j-1).getLatitude());
				double yA = Double.parseDouble(currentTaxi.getLongtitude())-Double.parseDouble(array.get(j-1).getLongtitude());
				double xB = Double.parseDouble(array.get(j+1).getLatitude())-Double.parseDouble(currentTaxi.getLatitude());
				double yB = Double.parseDouble(array.get(j+1).getLatitude())-Double.parseDouble(currentTaxi.getLatitude());
				cos = (xA*xB+yA*yB)/(Math.sqrt(xA*xA+yA*yA)*Math.sqrt(xB*xB+yB*yB));
				}
				//ignore no-load point
				if (currentTaxi.getState().equals("0"))
					continue;
				//start a new fragment
				else if ((j == 0 && currentTaxi.getState().equals("1"))
						|| (j > 0 && currentTaxi.getState().equals("1") && array.get(j - 1).getState().equals("0"))) {
					if (fragment!=null&&fragmentSize != 0&&fragment.size()>2)
					{
						transform(fragment);
						result.add(fragment);
					}
						
					fragment = new ArrayList<>();
					fragment.add(currentTaxi);
					fragmentSize++;
					continue;
				}
				//找到特征点
				else if (j > 0 && j < trajectoryTotalSize - 1 && currentTaxi.getState().equals("1")
						&& array.get(j - 1).getState().equals("1") && array.get(j + 1).getState().equals("1")
						&& cos <= 0) {
					fragment.add(currentTaxi);
					if(fragment!=null&&fragment.size()>2) result.add(fragment);
					fragment = new ArrayList<>();
					fragmentSize++;
					fragment.add(currentTaxi);
					continue;
				}
//				else if (j > 0
//						&& ((Integer.parseInt(currentTaxi.getAngle())
//								- Integer.parseInt(array.get(j - 1).getAngle())) >= 90)
//						&& currentTaxi.getState().equals("1") && array.get(j - 1).getState().equals("1")) {
//					fragment.add(currentTaxi);
//					if(fragment.size()>1) result.add(fragment);
//					fragment = new ArrayList<>();
//					fragmentSize++;
//					fragment.add(currentTaxi);
//					continue;
//				}
				else if (currentTaxi.getState().equals("1")) fragment.add(currentTaxi);
			}
			//add the last fragment
			if(fragment!=null&&fragment.size()>2) {
				transform(fragment);
				result.add(fragment);
			}
		}
		
		return result;
	}
	@Override
	public List<Fragment> fragmentIndex(List<List<Taxi>> result){
		//为每条fragment赋值一个索引
			List<Fragment> fragmentList = new ArrayList<Fragment>();
				for(int i=0;i<result.size();i++){
					 Fragment frag_withIndex = new Fragment();
					 frag_withIndex.setfragment(result.get(i));//给对应的线段轨迹进行轨迹的赋值
					 frag_withIndex.setID(i);//给对应生成的线段轨迹进行轨迹id的赋值方便后面的索引查找
				     fragmentList.add(frag_withIndex);
				}
				return fragmentList;
	}
	//以上：针对展示所有轨迹
	@Override
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

	@Override
	public double getHausdorffDistance(List<Taxi> t1, List<Taxi> t2) {
		double HD=0.0;
		double hAB=0.0;
		double hBA=0.0;
		int lenA=t1.size();
		int lenB=t2.size();
		double[]  minHA= new double[lenA];
		double[]  minHB = new double[lenB];
		
		for(int i=0;i<lenA;i++)
		{
			minHA[i] = GetDistance(Double.parseDouble(t1.get(i).getLongtitude()),
					Double.parseDouble(t1.get(i).getLatitude()), Double.parseDouble(t2.get(0).getLongtitude()),
					Double.parseDouble(t2.get(0).getLatitude()));

			for (int j = 1; j < lenB; j++)
			{
				double distance = GetDistance(Double.parseDouble(t1.get(i).getLongtitude()),
						Double.parseDouble(t1.get(i).getLatitude()), Double.parseDouble(t2.get(j).getLongtitude()),
						Double.parseDouble(t2.get(j).getLatitude()));
				if(distance<minHA[i])
					minHA[i]=distance;
			}
		}
		hAB=minHA[0];
		for(int i=1;i<minHA.length;i++)
		{
			if(minHA[i]>hAB)
				hAB=minHA[i];
				
		}
		
		for(int i=0;i<lenB;i++)
		{
			minHB[i]=GetDistance(Double.parseDouble(t2.get(i).getLongtitude()),
					Double.parseDouble(t2.get(i).getLatitude()), Double.parseDouble(t1.get(0).getLongtitude()),
					Double.parseDouble(t1.get(0).getLatitude()));
			for(int j=1;j<lenA;j++)
			{
				double distance = GetDistance(Double.parseDouble(t2.get(i).getLongtitude()),
						Double.parseDouble(t2.get(i).getLatitude()), Double.parseDouble(t1.get(j).getLongtitude()),
						Double.parseDouble(t1.get(j).getLatitude()));
				if(distance<minHB[i])
					minHB[i]=distance;
			}
		}
		hBA=minHB[0];
		for(int i=1;i<minHB.length;i++)
		{
			if(minHB[i]>hBA)
				hBA=minHB[i];
				
		}
		
		if(hAB<hBA)
			HD=hBA;
		else
			HD=hAB;
	
		return HD;
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
	
	//point to line shortest distance
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
	
	/** 
	 * 计算地球上任意两点(经纬度)距离 
	 *  
	 * @param long1 
	 *            第一点经度 
	 * @param lat1 
	 *            第一点纬度 
	 * @param long2 
	 *            第二点经度 
	 * @param lat2 
	 *            第二点纬度 
	 * @return 返回距离 单位：米 
	 */  
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

	@Override
	public void DeleteDuplicateTaxi(List<String> id) {
		for(int i = 0;i<id.size();i++){
			List<Taxi> array = taxiDao.Search(id.get(i), null);
			int trajectorySize = array.size();
			for(int j = 1;j<trajectorySize;j++){
				Taxi pre = array.get(j-1);
				Taxi aft = array.get(j);
				
				if(pre.getTaxi_Id().equals(aft.getTaxi_Id())
						&&pre.getTime().equals(aft.getTime())) taxiDao.Delete(pre);
				else if (pre.getTaxi_Id().equals(aft.getTaxi_Id())
						&&pre.getAngle().equals(aft.getAngle())
						&&pre.getLatitude().equals(aft.getLatitude())
						&&pre.getLongtitude().equals(aft.getLongtitude())
						&&pre.getSpeed().equals(aft.getSpeed())
						&&pre.getState().equals(aft.getState())) {
					taxiDao.Delete(pre);
				}
			}
			
		}
		for(int i = 0;i<id.size();i++){
			List<Taxi> array = taxiDao.Search(id.get(i), null);
			if(array.size()<3){
				for(int j = 0;j<array.size();j++) taxiDao.Delete(array.get(j));
			}
		}
	}
	
	@Override
	public List<trajectoryCluster> InitialCluster(List<List<Taxi>> list) {
		
		List<trajectoryCluster> originalClusters=new ArrayList<trajectoryCluster>();
         for(int i=0;i<list.size();i++){
           
        	//List<trajectoryCluster> tempDataTracks=new ArrayList<trajectoryCluster>();
        	//tempDataTracks.add((trajectoryCluster) list.get(i));
            List<List<Taxi>> tempTrackList = new ArrayList<List<Taxi>>();
            tempTrackList.add(list.get(i));
            trajectoryCluster tempCluster=new trajectoryCluster();
          //  tempCluster.setTurningPoints(tempDataTracks);
            tempCluster.setTrajectoryList(tempTrackList);
            originalClusters.add(tempCluster);
        }
            System.out.println("the original cluster is "+originalClusters.size());
        return originalClusters;
	}

	//对相似度进行轨迹聚类
	@Override
	public List<trajectoryCluster> Trajectory_HierarchicalCluster(List<List<Taxi>> list, int num) {
		//System.out.println(num);
		List<trajectoryCluster> resultList = new ArrayList<>();
		List<trajectoryCluster> originalList = InitialCluster(list);
		resultList = originalList;
		CalculateCenter(resultList);
		while (resultList.size() > num) {
			double min = Double.MAX_VALUE;
			int mergeIndexA = 0;
			int mergeIndexB = 0;
			System.out.println("start cluster");
			for (int i = 0; i < resultList.size(); i++) {
				for (int j = 0; j < resultList.size(); j++) {
					if(i!=j){
						 trajectoryCluster  clusterA = resultList.get(i);
						 trajectoryCluster  clusterB = resultList.get(j);
						//double tempDis = GetDistance(clusterA.getCenterLatitude(), clusterA.getCenterLongtitude(), clusterB.getCenterLatitude(), clusterB.getCenterLongtitude());
						double tempDis = GetDirectionSensitiveHausdorffDistance(clusterA.getCenterTracks(), clusterB.getCenterTracks());
						 if(tempDis<min){
							min = tempDis;
							mergeIndexA = i;
							mergeIndexB = j;
						}
					}
				}
			}
			resultList=MergeCluster(resultList,mergeIndexA,mergeIndexB);
			//System.out.println(resultList.size());
			if(resultList.size()==80){
				CalculateCenter(resultList);
				for(int i = 0;i<80;i++)
				{
					int numberOfTracks = resultList.get(i).getTrajectoryList().size();
					trajectoryCluster cluster = resultList.get(i);
					resultList.get(i).setNumberOfCluster(80);
			
				}
			}
			if(resultList.size()==50){

				CalculateCenter(resultList);
				for(int i = 0;i<50;i++)
				{
					int numberOfTracks = resultList.get(i).getTrajectoryList().size();
					trajectoryCluster cluster = resultList.get(i);
					resultList.get(i).setNumberOfCluster(50);
				}
			
			}
		}
		//end while loop
		CalculateCenter(resultList);
		for(int i = 0;i<resultList.size();i++)
		{
			int numberOfTracks = resultList.get(i).getTrajectoryList().size();
			trajectoryCluster cluster = resultList.get(i);
			resultList.get(i).setNumberOfCluster(num);
			System.out.println(numberOfTracks);
		}
		return resultList;
	}
	//计算中心点,这里应为计算中心轨迹
	@Override
	public void CalculateCenter(List<trajectoryCluster> resultList) {
		//将每个轨迹的GPS点个数统一，少的加0，首先获取最长轨迹的GPS点数
		int  maxlength=0;
		List<trajectoryCluster> unifiedCluster = new ArrayList<trajectoryCluster>();
		for(int i=0;i<resultList.size();i++){
			maxlength=0;
			trajectoryCluster tempCluster = resultList.get(i);
			List<List<Taxi>> tracks = tempCluster.getTrajectoryList();
			maxlength=tracks.get(0).size();//第一个轨迹的点数
			for(int j =1;j<tracks.size();j++){
				if(maxlength<tracks.get(j).size()){
					maxlength=tracks.get(j).size();
				}
			}
			resultList.get(i).setMaxLength(maxlength);
			//System.out.println("maxlength is"+resultList.get(i).getMaxlength());
		}
		
		//获取最大长度也就是最大点数之后对其余参与计算中心点的轨迹进行补0
		for(int i=0;i<resultList.size();i++){
			trajectoryCluster tempCluster = resultList.get(i);
			List<List<Taxi>> tracks = tempCluster.getTrajectoryList();
			for(int j=0;j<tracks.size();j++){
				List<Taxi> tempTrack = tracks.get(j);//获取每条轨迹
				Taxi lastTaxi = tempTrack.get(tempTrack.size()-1);//获取每条轨迹的最后一个GPS点
				String lastLat = lastTaxi.getLatitude();//最后一个数据点的纬度
				String lastLon = lastTaxi.getLongtitude();//最后一个数据点的经度
				for(int k=0;k<(maxlength-tracks.size());k++){
					Taxi tempTaxi = new Taxi();
					//tempTaxi.setLatitude("0");
					//tempTaxi.setLongtitude("0");
					tempTaxi.setLatitude(lastLat);
					tempTaxi.setLongtitude(lastLon);
					tracks.get(j).add(tempTaxi);
				}
			}//对每个轨迹都补充完毕后设置set
			tempCluster.setTrajectoryList(tracks);
			unifiedCluster.add(tempCluster);
		}
		//get each cluster
	
		for(int i =0;i<unifiedCluster.size();i++){
			List<Taxi> averageTracks = new ArrayList<Taxi>();//用于存放每条轨迹计算得到的均值轨迹
			//get list of tracks
			List<List<Taxi>> Tracks = resultList.get(i).getTrajectoryList();
			double totalLatitude=0, totalLongtitude=0;
			int SizeOfTracks = Tracks.size();
			//get each tracks
			for(int k=0;k<maxlength;k++){
			for(int j = 0;j<SizeOfTracks;j++){
				//for(int k=0;k<Tracks.get(j).size();k++){
				double tempLat = Double.parseDouble(Tracks.get(j).get(k).getLatitude());
				double tempLon = Double.parseDouble(Tracks.get(j).get(k).getLongtitude());
				totalLatitude=totalLatitude+tempLat;
				totalLongtitude=totalLongtitude+tempLon;
				//}
				//counter++;
			}
			Taxi tempAvgTaxi = new Taxi();
			tempAvgTaxi.setLatitude(String.valueOf(totalLatitude/SizeOfTracks));
			tempAvgTaxi.setLongtitude(String.valueOf(totalLongtitude/SizeOfTracks));
			averageTracks.add(tempAvgTaxi);
		}
			unifiedCluster.get(i).setCenterTracks(averageTracks); 
			//unifiedCluster.get(i).setCenterLongtitude(totalLongtitude/SizeOfTracks);
		}
		resultList=unifiedCluster;
	}
	//以上计算中心轨迹
//合并
	@Override
	public List<trajectoryCluster> MergeCluster(List<trajectoryCluster> list, int index1, int index2) {
		if(index1 !=index2){
			System.out.println("index1:"+index1+"  index2:"+index2);
			trajectoryCluster cluster1 = list.get(index1);
			trajectoryCluster cluster2 = list.get(index2);
			List<List<Taxi>> dataSet1 = cluster1.getTrajectoryList();
			List<List<Taxi>> dataSet2 = cluster2.getTrajectoryList();
			for(int i = 0;i<dataSet2.size();i++){
				dataSet1.add(dataSet2.get(i));
			}
			cluster1.setTrajectoryList(dataSet1);
			
			list.set(index1, cluster1);
			list.remove(index2);
		}
		return list;
	}
	@Override
	public List<List<Taxi>> backToListTracks(List<trajectoryCluster> list) {
		List<List<Taxi>> tracks = new ArrayList<List<Taxi>>();
		if(list!=null){
		 for(int i=0;i<list.size();i++){
			 List<List<Taxi>> tempTracks = list.get(i).getTrajectoryList();
			 for(int j=0;j<tempTracks.size();j++){
				 tracks.add(tempTracks.get(j));
			 }
		 }}
		else{
			System.out.println("list is empty");
		}
		return tracks;
	}
//controller调用的聚类方法
	@Override
	public List<List<Taxi>> clusterTrajectory(List<List<Taxi>> trajectory) {
		// 该方法用于将传入的原始获得的轨迹进行聚类后传出来用于js进行绘制
		//double[][] similarity = GetSimilarity(trajectory);//获取了轨迹的相似度
		//根据相似度进行聚类
	   List<trajectoryCluster> resultCluster = Trajectory_HierarchicalCluster(trajectory,50);
	   List<List<Taxi>> resultTracks = backToListTracks(resultCluster);
		return resultTracks;
	}
	//使用DBSCAN变种的方法进行聚类，eps=1.6,minlns=11
	
}
