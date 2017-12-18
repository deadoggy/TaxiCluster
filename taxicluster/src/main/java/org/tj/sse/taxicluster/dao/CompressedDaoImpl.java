package org.tj.sse.taxicluster.dao;


import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.tj.sse.taxicluster.entity.Taxi;

import javax.annotation.Resource;
import java.util.List;

@Repository("compression_dp")
public class CompressedDaoImpl extends HibernateDaoSupport implements CompressedDao{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Taxi> SearchCompressed(String taxiId, String time) {
		// TODO Auto-generated method stub
		if (time==null)
		{
			//org.hibernate.Session session = this.getSession();
			//Query query =session.createQuery("from Taxi where Taxi_Id='"+taxiId+"' order by Time") ;
		//	return this.getSession().createQuery("from Taxi where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
			List<Taxi> result = this.getSession().createQuery("from compression_dp where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
		//	return this.getSession().createQuery("from compression_dp where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
		    return result;
		}
		return null;
	}


}
