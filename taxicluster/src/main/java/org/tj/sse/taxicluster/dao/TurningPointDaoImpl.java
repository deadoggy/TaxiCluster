package org.tj.sse.taxicluster.dao;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tj.sse.taxicluster.entity.TurningPoint;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("turningPointDao")
public class TurningPointDaoImpl  extends HibernateDaoSupport implements TurningPointDao{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@Transactional
	public void save(TurningPoint turningPoint) {
		this.getHibernateTemplate().save(turningPoint);
	}
	
	@Transactional
	public void delete(TurningPoint turningPoint) {
		this.getHibernateTemplate().delete(turningPoint);
	}

	@SuppressWarnings("unchecked")
	public List<TurningPoint> GetAllByTime() {
		return this.getSession().createQuery("from TurningPoint order by time").list();
	}

	@SuppressWarnings("unchecked")
	public List<TurningPoint> GetAllByTaxiId() {
		return this.getSession().createQuery("from TurningPoint order by taxi_id").list();
	}

	@SuppressWarnings("unchecked")
	public List<TurningPoint> GetById(String id) {
		return this.getSession().createQuery("from TurningPoint where taxi_id=? order by time").setParameter(0, id)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<String> GetAllIds() {
		List<TurningPoint> array = this.getSession().createQuery("from TurningPoint").list();
		Set<String> resultSet = new HashSet<String>();
		for(int i=0;i<array.size();i++){
			resultSet.add(array.get(i).getTaxiId());
		}
		List<String> result = new ArrayList<String>();
		for (String str : resultSet) {  
		      result.add(str);
		}  
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<TurningPoint> GetStartPoint() {
		String start="start";
		return this.getSession().createQuery("from TurningPoint where type=?").setParameter(0, start)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<TurningPoint> GetDropPoint() {
		String drop="drop";
		return this.getSession().createQuery("from TurningPoint where type=?").setParameter(0, drop).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TurningPoint> GetStartPointAtTen(){
		String start="start";
		return this.getSession().createQuery("from TurningPoint where type=? and time like '2007-10-12 10%'").setParameter(0, start)
				.list();
	}

	@SuppressWarnings("unchecked")
	public List<TurningPoint> GetByCluster(int clusterId) {
		Query query=this.getSession().createQuery("from TurningPoint as t"
				+ " where t.id in"
				+" (select cp.cp_turningPoint.id from Cluster_Point as cp"
				+ " where cp.cp_hierarchicalCluster.id=?)");
		query.setParameter(0, clusterId);
		List<TurningPoint> list = query.list();
		return list;
	}

	@Transactional
	public void merge(TurningPoint temp, TurningPoint pre, TurningPoint aft) {
		this.getHibernateTemplate().save(temp);
		this.getHibernateTemplate().delete(pre);
		this.getHibernateTemplate().delete(aft);
	}

}
