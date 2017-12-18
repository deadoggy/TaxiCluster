package org.tj.sse.taxicluster.datamining.DataMining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class kmeansTest {
	public abstract class KMeansClustering <T>{  
	    private List<T> dataArray;//�������ԭʼֵ  
	    private int K = 3;//��Ҫ�ֳɵ�������  
	    private int maxClusterTimes = 500;//����������  
	    private List<List<T>> clusterList;//����Ľ��  
	    private List<T> clusteringCenterT;//����  
	    
	     public int getK() {  
	        return K;  
	    }  
	    public void setK(int K) {  
	        if (K < 1) {  
	            throw new IllegalArgumentException("K must greater than 0");  
	        }  
	        this.K = K;  
	    }  
	    public int getMaxClusterTimes() {  
	        return maxClusterTimes;  
	    }  
	    public void setMaxClusterTimes(int maxClusterTimes) {  
	        if (maxClusterTimes < 10) {  
	            throw new IllegalArgumentException("maxClusterTimes must greater than 10");  
	        }  
	        this.maxClusterTimes = maxClusterTimes;  
	    }  
	    public List<T> getClusteringCenterT() {  
	    
	     return clusteringCenterT;  
	    }  
	    /** 
	     * @return 
	     * @Author:lulei   
	     * @Description: �����ݽ��о��� 
	     */  
	    public List<List<T>> clustering() {  
	        if (dataArray == null) {  
	            return null;  
	        }  
	        //��ʼK����Ϊ�����е�ǰK����  
	        int size = K > dataArray.size() ? dataArray.size() : K;  
	        List<T> centerT = new ArrayList<T>(size);  
	        //�����ݽ��д���  
	        Collections.shuffle(dataArray);  
	        for (int i = 0; i < size; i++) {  
	            centerT.add(dataArray.get(i));  
	        }  
	        
	         clustering(centerT, 0);  
	        return clusterList;  
	    }  
	      
	    /** 
	     * @param preCenter 
	     * @param times 
	     * @Author:lulei   
	     * @Description: һ�־��� 
	     */  
	    private void clustering(List<T> preCenter, int times) {  
	        if (preCenter == null || preCenter.size() < 2) {  
	            return;  
	        }  
	        //�������ĵ�˳��  
	        Collections.shuffle(preCenter);  
	        List<List<T>> clusterList =  getListT(preCenter.size());  
	        for (T o1 : this.dataArray) {  
	            //Ѱ�������Ƶ�����  
	            int max = 0;  
	            double maxScore = similarScore(o1, preCenter.get(0));  
	            
	              for (int i = 1; i < preCenter.size(); i++) {  
	                if (maxScore < similarScore(o1, preCenter.get(i))) {  
	                    maxScore = similarScore(o1, preCenter.get(i));  
	                    max = i;  
	                }  
	            }  
	            clusterList.get(max).add(o1);  
	        }  
	        //���㱾�ξ�����ÿ����������  
	        List<T> nowCenter = new ArrayList<T> ();  
	        for (List<T> list : clusterList) {  
	            nowCenter.add(getCenterT(list));  
	        }  
	        //�Ƿ�ﵽ����������  
	        if (times >= this.maxClusterTimes || preCenter.size() < this.K) {  
	            this.clusterList = clusterList;  
	            return;  
	        }  
	        this.clusteringCenterT = nowCenter;  
	        //�ж������Ƿ����ƶ������û���ƶ����������ξ��࣬���������һ��  
	        
	            
	        if (isCenterChange(preCenter, nowCenter)) {  
	            clear(clusterList);  
	            clustering(nowCenter, times + 1);  
	        } else {  
	            this.clusterList = clusterList;  
	        }  
	    }  
	      
	    /** 
	     * @param size 
	     * @return 
	     * @Author:lulei   
	     * @Description: ��ʼ��һ�������� 
	     */  
	    private List<List<T>> getListT(int size) {  
	        List<List<T>> list = new ArrayList<List<T>>(size);  
	        for (int i = 0; i < size; i++) {  
	            list.add(new ArrayList<T>());  
	        }  
	        return list;  
	        
	          }  
	      
	    /** 
	     * @param lists 
	     * @Author:lulei   
	     * @Description: ����������� 
	     */  
	    private void clear(List<List<T>> lists) {  
	        for (List<T> list : lists) {  
	            list.clear();  
	        }  
	        lists.clear();  
	    }  
	      
	    /** 
	     * @param value 
	     * @Author:lulei   
	     * @Description: ��ģ������Ӽ�¼ 
	     */  
	    public void addRecord(T value) {  
	    
	     if (dataArray == null) {  
	            dataArray = new ArrayList<T>();  
	        }  
	        dataArray.add(value);  
	    }  
	      
	    /** 
	     * @param preT 
	     * @param nowT 
	     * @return 
	     * @Author:lulei   
	     * @Description: �ж������Ƿ����ƶ� 
	     */  
	    private boolean isCenterChange(List<T> preT, List<T> nowT) {  
	        if (preT == null || nowT == null) {  
	            return false;  
	        }  
	        for (T t1 : preT) {  
	            boolean bol = true;  
	            for (T t2 : nowT) {  
	            
	             if (equals(t1, t2)) {//t1��t2������ȵģ���Ϊ������δ�ƶ�  
	                    bol = false;  
	                    break;  
	                }  
	            }  
	            //��һ�����ķ����ƶ�����Ϊ��Ҫ������һ�μ���  
	            if (bol) {  
	                return bol;  
	            }  
	        }  
	        return false;  
	    }  
	      
	    /** 
	     * @param o1 
	     * @param o2 
	     * @return 
	     * @Author:lulei   
	     * @Description: o1 o2֮������ƶ� 
	     */  
	     
	                 
	    public abstract double similarScore(T o1, T o2);  
	      
	    /** 
	     * @param o1 
	     * @param o2 
	     * @return 
	     * @Author:lulei   
	     * @Description: �ж�o1 o2�Ƿ���� 
	     */  
	    public abstract boolean equals(T o1, T o2);  
	      
	    /** 
	     * @param list 
	     * @return 
	     * @Author:lulei   
	     * @Description: ��һ�����ݵ����� 
	     */  
	    public abstract T getCenterT(List<T> list);  
	}   
}
