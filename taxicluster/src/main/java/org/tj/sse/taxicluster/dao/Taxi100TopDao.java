package org.tj.sse.taxicluster.dao;

import org.tj.sse.taxicluster.entity.Taxi;

import java.util.List;
import java.util.Set;

public interface Taxi100TopDao {

	public List<Taxi> Search(String taxiId, String time);
	public List<Taxi> getAll();
	public Set<String> getAllTaxiId();
	public void Delete(Taxi taxi);
	public Set<String> GetTaxiIdByFuzzy(String fuzzyId);
	public List<Taxi> SearchAll(String taxiId, String time);
	List<Taxi> SearchFrequency(String taxiId, String time);
}
