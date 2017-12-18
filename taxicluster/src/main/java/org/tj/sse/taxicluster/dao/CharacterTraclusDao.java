package org.tj.sse.taxicluster.dao;


import org.tj.sse.taxicluster.entity.char_TraclusEntity;

import java.util.List;
import java.util.Set;

public interface CharacterTraclusDao {
    void save(char_TraclusEntity ce);
	List<char_TraclusEntity> Search();
	void Delete();
	void Store(List<char_TraclusEntity> result);
	Set<String> getAllTaxiId();
	List<char_TraclusEntity> SearchById(String taxiId);
}
