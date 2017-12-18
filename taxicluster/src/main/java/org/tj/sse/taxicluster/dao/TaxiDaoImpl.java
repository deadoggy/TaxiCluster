package org.tj.sse.taxicluster.dao;


import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tj.sse.taxicluster.entity.Taxi;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("taxi")
public class TaxiDaoImpl extends HibernateDaoSupport implements TaxiDao{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	public List<Taxi> Search(String taxiId, String time) {
		if (time==null)
		{
			//org.hibernate.Session session = this.getSession();
			//Query query =session.createQuery("from Taxi where Taxi_Id='"+taxiId+"' order by Time") ;
		//	return this.getSession().createQuery("from Taxi where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
			return this.getSession().createQuery("from Taxi where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
		}
		return null;
	}

	public List<List<Taxi>> SearchForList(List<String> taxiList){
		List<List<Taxi>> all = new ArrayList<List<Taxi>>();
		for(int i=0;i<taxiList.size();i++){
			String taxiId = taxiList.get(i);
			List<Taxi> temp = this.getSession().createQuery("from Taxi where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
		    all.add(temp);
		}
		return all;
	}
	@SuppressWarnings("unchecked")
	public List<Taxi> SearchFrequency(String taxiId, String time) {
		if (time==null)
		{
			//org.hibernate.Session session = this.getSession();
			//Query query =session.createQuery("from Taxi where Taxi_Id='"+taxiId+"' order by Time") ;
			//return this.getSession().createQuery("count(*) from Taxi where Taxi_Id=?").setParameter(0, taxiId).list();
			return this.getSession().createQuery("count(*) from Taxi where Taxi_Id=?").setParameter(0, taxiId).list();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Taxi> SearchAll(String taxiId, String time) {
		if (time==null)
		{
			//org.hibernate.Session session = this.getSession();
			//Query query =session.createQuery("from Taxi where Taxi_Id='"+taxiId+"' order by Time") ;
			 return this.getSession().createQuery("from Taxi where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
			// return this.getSession().createQuery("from Taxi where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Taxi> getAll() {
		List<Taxi> resultAll =this.getSession().createQuery("from Taxi order by Time").list();
		System.out.println(resultAll);
		return resultAll;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<String> getAllTaxiId() {
		List<Taxi> array = this.getSession().createQuery("from Taxi").list();
		Set<String> result = new HashSet();
		for(int i=0;i<array.size();i++){
			result.add(array.get(i).getTaxi_Id());
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Set<String> GetTaxiIdByFuzzy(String fuzzyId){
		String fuzzy = "%"+fuzzyId;
		List<Taxi> array = this.getSession().createQuery("from Taxi where Taxi_Id like ?").setParameter(0, fuzzy).list();
		Set<String> result = new HashSet();
		for(int i=0;i<array.size();i++){
			result.add(array.get(i).getTaxi_Id());
		}
		return result;
	}

	@Transactional
	public void Delete(Taxi taxi) {
		this.getHibernateTemplate().delete(taxi);
	}
 


}
