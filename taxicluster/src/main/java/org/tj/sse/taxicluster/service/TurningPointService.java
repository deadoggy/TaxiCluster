package org.tj.sse.taxicluster.service;


import org.tj.sse.taxicluster.entity.TurningPoint;

import java.util.List;

public interface TurningPointService {
	public List<TurningPoint> GetAllTurningPoint();
	public void Transform(List<TurningPoint> list);
	public void deleteDuplicate();
	public void SmallRegion();
	public List<TurningPoint> GetAllStartPoint();
	public List<TurningPoint> GetAllDropPoint();
	public List<TurningPoint> GetAllStartPointAtTen();
	public List<TurningPoint> GetByCluster(int clusterId);
}
