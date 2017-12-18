package org.tj.sse.taxicluster.dao;


import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tj.sse.taxicluster.entity.Cluster_Point;

import javax.annotation.Resource;

@Repository("clusterPointDao")
public class ClusterPointDaoImpl extends HibernateDaoSupport implements ClusterPointDao{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	@Transactional
	public void save(Cluster_Point cp) {
		this.getHibernateTemplate().save(cp);
		
	}

}
