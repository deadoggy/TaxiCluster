package org.tj.sse.taxicluster.dao;


import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tj.sse.taxicluster.entity.CPointCluster;
import org.tj.sse.taxicluster.entity.ClusterResultEntity;

import javax.annotation.Resource;
import java.util.List;

@Repository("cluster_result")
public class ClusterDaoImpl extends HibernateDaoSupport implements ClusterDao{

	@Resource
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}
	@Transactional
	@Override
	public void Store(List<CPointCluster> result, String cluster_type, String cp_type) {
		// TODO Auto-generated method stub
		//������õ��Ľ�������ش���cluster_result ����
		for(int i=0;i<result.size();i++){
			CPointCluster temp = result.get(i);//tempΪһ��CPointCluster
			String clusterId = String.valueOf(temp.getId());
			String centerLatitude = String.valueOf(temp.getCenterLatitude());
			String centerLongtitude = String.valueOf(temp.getCenterLongtitude());
			ClusterResultEntity clusterEntity = new ClusterResultEntity();
			clusterEntity.setClusterId(clusterId);
			clusterEntity.setCenter_Latitude(centerLatitude);
			clusterEntity.setCenter_Longtitude(centerLongtitude);
			clusterEntity.setCluster_type(cluster_type);
			clusterEntity.setCp_type(cp_type);
			clusterEntity.setId(i);
			//���ú�entity����֮�󼴿ɽ���save����
			save(clusterEntity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClusterResultEntity> Search() {
			//org.hibernate.Session session = this.getSession();
			//Query query =session.createQuery("from Taxi where Taxi_Id='"+taxiId+"' order by Time") ;
		//	return this.getSession().createQuery("from Taxi where Taxi_Id=? order by Time").setParameter(0, taxiId).list();
			return this.getSession().createQuery("from ClusterResultEntity").list();
		 
	}
	@Transactional
	@Override
	public void Delete() {
	List<ClusterResultEntity> list   =   this.Search();//��cluster_result�����ȡ���м�¼
        if(list.size()>0){  
           this.getHibernateTemplate().deleteAll(list);
         }
	}
	@Transactional
	@Override
	public void save(ClusterResultEntity clusterEntity) {
		this.getHibernateTemplate().save(clusterEntity);
	}
	@Transactional
	@Override
	public void merge(ClusterResultEntity clusterEntity) {
		this.Delete();
		this.save(clusterEntity);
		
	}
 
	@SuppressWarnings("unchecked")
	@Override
	public List<ClusterResultEntity> SearchForMSE() {
		return this.getSession().createQuery("from ClusterResultEntity").list();
		 
	}

}
