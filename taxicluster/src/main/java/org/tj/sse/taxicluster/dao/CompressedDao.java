package org.tj.sse.taxicluster.dao;


import org.tj.sse.taxicluster.entity.Taxi;
import java.util.List;

public interface CompressedDao {
	public List<Taxi> SearchCompressed(String taxiId, String time);
}
