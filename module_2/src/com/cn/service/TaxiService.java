package com.cn.service;

import java.util.List;
import java.util.Set;

import com.cn.entity.HierarchicalCluster;
import com.cn.entity.Taxi;
import com.cn.entity.trajectoryCluster;
import com.cn.entity.Fragment;

public interface TaxiService {

	public List<Taxi> search(String taxiId, String time);
	public List<Taxi> getAll();
	public Set<String> getAllTaxiId( );
	public void transform(List<Taxi> list);
	public void GenerateTurningPoints(List<String> taxiIds);
	public List<List<Taxi>> GetFragment(List<String> list);
	public double[][] GetSimilarity(List<List<Taxi>> trajectory);
	public double getHausdorffDistance(List<Taxi> t1,List<Taxi> t2);
	public void DeleteDuplicateTaxi(List<String> id);
	public List<List<Taxi>> GetFragmentAll(List<String> list);
	public List<List<Taxi>> clusterTrajectory(List<List<Taxi>> trajectory);
	public List<trajectoryCluster> Trajectory_HierarchicalCluster(List<List<Taxi>> list, int num);
	public List<trajectoryCluster> InitialCluster(List<List<Taxi>> list);
	public void CalculateCenter(List<trajectoryCluster> resultList);
	public List<trajectoryCluster> MergeCluster(List<trajectoryCluster> list, int index1, int index2);
	public List<List<Taxi>> backToListTracks(List<trajectoryCluster> list);
	public List<Taxi> search_frequency(String taxiId, String time);
	public List<Fragment> fragmentIndex(List<List<Taxi>> result);
}
