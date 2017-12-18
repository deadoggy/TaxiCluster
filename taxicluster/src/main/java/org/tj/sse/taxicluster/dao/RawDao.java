package org.tj.sse.taxicluster.dao;



import org.tj.sse.taxicluster.entity.CPointCluster;
import org.tj.sse.taxicluster.entity.RawEntity;
import org.tj.sse.taxicluster.entity.Taxi;

import java.util.List;
import java.util.Set;

public interface RawDao {

	public void Store(List<CPointCluster> result, String cluster_type, String cp_type);

	public List<RawEntity> Search(String cluster_type, String cp_type);

	public void DeleteRaw();

	public void saveRaw(RawEntity rawEntity);

	public void merge(RawEntity rawEntity);

	public List<RawEntity> SearchAll();

	void Traclus_DbscanStore(List<List<Taxi>> data, String cluster_type, String cp_type);

	Set getClusterId();
	 
}
