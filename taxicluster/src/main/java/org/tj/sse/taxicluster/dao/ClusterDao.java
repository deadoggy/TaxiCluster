package org.tj.sse.taxicluster.dao;



import org.tj.sse.taxicluster.entity.CPointCluster;
import org.tj.sse.taxicluster.entity.ClusterResultEntity;

import java.util.List;

public interface ClusterDao {

	void Store(List<CPointCluster> result, String string2, String cp);

	List<ClusterResultEntity> Search();

	void save(ClusterResultEntity clusterEntity);

	void Delete();

	void merge(ClusterResultEntity clusterEntity);

	List<ClusterResultEntity> SearchForMSE();

}
