package org.tj.sse.taxicluster.dao;



import org.tj.sse.taxicluster.entity.HierarchicalCluster;

import java.util.List;

public interface HierarchicalClusterDao {
	public int save(HierarchicalCluster cluster);
	public void update(HierarchicalCluster cluster);
	public List<HierarchicalCluster> GetStartPointAtTen();
	public List<HierarchicalCluster> GetStartPointAtTenByKMeans();
	public List<HierarchicalCluster> GetStartPointAtTenByAGNES();
	public List<HierarchicalCluster> GetStartPointAtTenByDBSCAN();
	public HierarchicalCluster GetClusterById(int id);
	public List<HierarchicalCluster> GetClusterByParentId(int parentId);
}
