package org.tj.sse.taxicluster.dao;



import org.tj.sse.taxicluster.entity.char_SGAEntity;

import java.util.List;

public interface CharacterSGADao {
    void save(char_SGAEntity ce);
	List<char_SGAEntity> Search();
	void Delete();
	void Store(List<char_SGAEntity> result);
	void saveAll(List<char_SGAEntity> ce);
}
