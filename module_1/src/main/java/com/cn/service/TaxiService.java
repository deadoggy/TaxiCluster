package com.cn.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.cn.entity.HierarchicalCluster;
import com.cn.entity.InfoEntity;
import com.cn.entity.RawEntity;
import com.cn.entity.RawForVoronoi;
import com.cn.entity.RawRepre;
import com.cn.entity.Taxi;
import com.cn.entity.TraclusRepEntity;
import com.cn.entity.trajectoryCluster;

import DataMining.Line;
import DataMining.TraClus;
import DataMining.Trajectory;

import com.cn.entity.CPointCluster;
import com.cn.entity.ClusterResultEntity;
import com.cn.entity.Fragment;

public interface TaxiService {
    public int getClusterNum();
	public List<Taxi> search(String taxiId, String time);
	public List<Taxi> getAll();
	public Set<String> getAllTaxiId( );
	public void transform(List<Taxi> list);
	public void GenerateTurningPoints(List<String> taxiIds);
	public List<List<Taxi>> GetFragment(List<String> list);
	public List<List<Taxi>> GetFragmentAfterCompressed(List<String> list);
	public double[][] GetSimilarity(List<List<Taxi>> trajectory);
	public double getHausdorffDistance(List<Taxi> t1,List<Taxi> t2);
	public void DeleteDuplicateTaxi(List<String> id);
	public List<List<Taxi>> GetFragmentAll(List<String> list);
	public List<List<Taxi>> clusterTrajectory(List<List<Taxi>> trajectory,double[][] similarity);
	public HashMap<Integer, ArrayList<Integer>>  TRACLUS(List<List<Taxi>> list,double[][] similarity);
	public List<trajectoryCluster> InitialCluster(List<List<Taxi>> list);
	public void CalculateCenter(List<trajectoryCluster> resultList);
	public List<trajectoryCluster> MergeCluster(List<trajectoryCluster> list, int index1, int index2);
	public List<List<Taxi>> backToListTracks(List<trajectoryCluster> list);
	public List<Taxi> search_frequency(String taxiId, String time);
	public List<Fragment> fragmentIndex(List<List<Taxi>> result);
	public List<List<Taxi>> GetFragmentOriginal(List<String> list);
	public HashMap<Integer, ArrayList<Double>> GetSimilarityForShowAll(List<List<Taxi>> trajectory);
	public List<Taxi> GetTraclusCharacteristic();
	public List<CPointCluster> Kmeans(String string, String string2, int i, int j);
	public List<Taxi> ClusterRawData();
	public List<List<RawEntity>> ClusterRawDataToLine(String cp, String cluster_type);
	public List<CPointCluster> Dbscan(String string, String string2, double eps, int minlns);
	List<CPointCluster> TEST();
	public List<CPointCluster> Hierarchical_Agnes(String string, String string2, int n);
	public List<CPointCluster> Hierarchical_Grid(String string, String string2, int m, int nx, int ny);
	public double MSE(String cluster_type,String cp_type);
	public List<List<RawEntity>> GetPointsByClusterId();
	public void SGA(double minAngle, double minStopDuration, double minDis, double maxDis) throws IOException;
	public InfoEntity SetInfo(List<RawEntity> track );
	public List<ClusterResultEntity> GetClusterResult();
	public List<CPointCluster> ConvertResultEntityToCP(List<ClusterResultEntity> cluster_points);
	public List<RawForVoronoi> GetRawData();
	public List<DataMining.Trajectory> TraclusRep(String cp, double eps,int minlns);
	public RawRepre TraclusRepRaw(String type,int order,double eps,int minlns, String cluster_type);
	public List<CPointCluster> Traclus_Dbscan(String string, String string2, double eps, int minpts);
	public List<Trajectory> GeneralRep(String cluster_type, String cp);
	//List<List<RawEntity>> ClusterRawDataToLineSeg(String cp, String cluster_type);
	HashMap<Integer, ArrayList<Line>> Traclus_Experiment(TraClus tra,List<List<Taxi>> data, double eps, int minlns);
	public ArrayList<Line> ShowSeg_cluster(String partition_type, double eps, int minlns);
	public ArrayList<Line> ShowSeg_clusterKmeans(String string, int i, int j);
	ArrayList<Line> ShowSeg_clusterHierarchical_Agnes(String partition_type, int n);
	public ArrayList<Line> getTrajectoryForRepreSentative();
	public ArrayList<DataMining.Trajectory> getReKmeansSeg(String partition_type,int k, int iterator);
	public ArrayList<DataMining.Trajectory> getReDbscanSeg(String partition_type,double eps, int minlns);
	public ArrayList<DataMining.Trajectory> getReHierarchicalSeg(String partition,int n);
}