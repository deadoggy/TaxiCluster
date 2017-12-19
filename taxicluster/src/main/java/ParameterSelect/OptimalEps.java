package ParameterSelect;

import org.tj.sse.taxicluster.entity.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OptimalEps {
 public HashMap<Double,Double> CalculateEPS(double[][]similarity,List<Fragment> list){
	 //������Ϣ��
	 //���ȼ���p(xi)��ÿ���켣�߶�����ĳ��ָ���
	 //��һ������ÿ���켣�ڲ�ͬ��EPS���������߶�����
	 //List<HashMap<Double,Integer>> listForAll= new ArrayList<HashMap<Double,Integer>>();
	 //����һ��hashmap<EPS,hashmap<id_Track,num_EPS>>���ڴ�����н��
	 //HashMap<Double,HashMap<Integer,Integer>> EPS_AllNum = new HashMap<Double,HashMap<Integer,Integer>>();
	 HashMap<Double,Double> EPS_HX = new HashMap<Double,Double>();//<eps,h(x)>
	 for(double eps=0;eps<45;eps=eps+0.01){
		// HashMap<Integer,Integer> id_num = new HashMap<Integer,Integer>();//���ڴ���ڲ�ͬ��id�µ�track������켣����
		 int All_length=1;//�����Ӧ�����߶ε������ڹ켣�߶�����,���Ϊ1�������߶α���
	 for(int i=0;i<list.size();i++){//����ÿ���켣���������ڹ켣��Ŀ
		// HashMap<Double,Integer> EPS_NUM = new HashMap<Double,Integer>();//��Ų�ͬEPS��ÿ���켣��Ӧ�������ڹ켣����
		 int indexI = list.get(i).getID();//����켣�����켣��
		 ArrayList<Integer> indexEps = new ArrayList<Integer>();//���ڼ�¼������֮�ڵĹ켣ID
		 for(int j=0;j<similarity[0].length;j++){
			 double distance= similarity[indexI][j];//��Ӧ��ÿ���켣�����켣֮��ľ���
			 if(distance<eps){
				 //��������eps֮��
				 if(j!=indexI){
					 //��Ҫ����j������켣��id��
					 indexEps.add(j);
				 }
			 }
		 }//��ѭ�����������ȷ������Ĺ켣��Ŀ
		 
		//�ж���һ���켣�����������ڹ켣�󣬽��丳ֵindex list��indexEps�ĳ��ȼ�Ϊÿ���켣������켣��Ŀ
		list.get(i).setIndexEps(indexEps);
		All_length=All_length+indexEps.size();//ÿ�����켣ȷ������󣬶����߶���Ŀ�ۼ�
		//EPS_NUM.put(eps, indexEps.size());
		//id_num.put(i, indexEps.size());//<ÿ�����켣��id��,��Ӧ�������ڹ켣��Ŀ>
		//list.get(i).setNumEps(EPS_NUM);
		//����P(Xi)
	 }
	// HashMap<Integer,Double> id_hx = new HashMap<Integer,Double>();//<id,h(x)>
	 double Reverse_hx =0;//����ÿ��eps,hx�ĳ�ʼֵΪ0;
	 for(int k=0;k<list.size();k++){
		 //����p(Xi)=ÿ���켣��������/���й켣��������Ŀ֮��
		 int each_TrackNum = list.get(k).GetindexEPS().size()+1;
		 double proportion = each_TrackNum/All_length;//��p(Xk)
		 Reverse_hx = Reverse_hx+ proportion*(Math.log(proportion)/Math.log((double)2));//�ۼ�
		// id_hx.put(k, hx);//<id,h(x)>
	 }
	 EPS_HX.put(eps, -Reverse_hx);//<eps,h(x)>
	 //EPS_AllNum.put(eps, id_num);//<eps,hashmap<id,num>
	 }
	 return EPS_HX;
 }
 public HashMap<Double, ArrayList<Integer>> testEPS(double[][]similarity,List<Fragment> list){
	 //������Ϣ��
	 //���ȼ���p(xi)��ÿ���켣�߶�����ĳ��ָ���
	 //��һ������ÿ���켣�ڲ�ͬ��EPS���������߶�����
	 //List<HashMap<Double,Integer>> listForAll= new ArrayList<HashMap<Double,Integer>>();
	 //����һ��hashmap<EPS,hashmap<id_Track,num_EPS>>���ڴ�����н��
	 //HashMap<Double,HashMap<Integer,Integer>> EPS_AllNum = new HashMap<Double,HashMap<Integer,Integer>>();
	 HashMap<Double,ArrayList<Integer>> EPS_HX = new HashMap<Double,ArrayList<Integer>>();//<eps,h(x)>
	 for(double eps=0;eps<45;eps=eps+0.01){
		// HashMap<Integer,Integer> id_num = new HashMap<Integer,Integer>();//���ڴ���ڲ�ͬ��id�µ�track������켣����
		 int All_length=1;//�����Ӧ�����߶ε������ڹ켣�߶�����,���Ϊ1�������߶α���
		 ArrayList<Integer> num_length= new ArrayList<Integer>();//indexΪ�켣id��,��Ӧ��Ԫ��Ϊ�����߶���
	 for(int i=0;i<list.size();i++){//����ÿ���켣���������ڹ켣��Ŀ
		// HashMap<Double,Integer> EPS_NUM = new HashMap<Double,Integer>();//��Ų�ͬEPS��ÿ���켣��Ӧ�������ڹ켣����
		 int indexI = list.get(i).getID();//����켣�����켣��
		 ArrayList<Integer> indexEps = new ArrayList<Integer>();//���ڼ�¼������֮�ڵĹ켣ID
		 for(int j=0;j<similarity[0].length;j++){
			 double distance= similarity[indexI][j];//��Ӧ��ÿ���켣�����켣֮��ľ���
			 if(distance<eps){
				 //��������eps֮��
				 if(j!=indexI){
					 //��Ҫ����j������켣��id��
					 indexEps.add(j);
				 }
			 }
		 }//��ѭ�����������ȷ������Ĺ켣��Ŀ
		 
		//�ж���һ���켣�����������ڹ켣�󣬽��丳ֵindex list��indexEps�ĳ��ȼ�Ϊÿ���켣������켣��Ŀ
		list.get(i).setIndexEps(indexEps);
		All_length=All_length+indexEps.size();//ÿ�����켣ȷ������󣬶����߶���Ŀ�ۼ�
		num_length.add(indexEps.size());
		//EPS_NUM.put(eps, indexEps.size());
		//id_num.put(i, indexEps.size());//<ÿ�����켣��id��,��Ӧ�������ڹ켣��Ŀ>
		//list.get(i).setNumEps(EPS_NUM);
		//����P(Xi)
	 }
	// HashMap<Integer,Double> id_hx = new HashMap<Integer,Double>();//<id,h(x)>
	 double Reverse_hx =0;//����ÿ��eps,hx�ĳ�ʼֵΪ0;
	 for(int k=0;k<list.size();k++){
		 //����p(Xi)=ÿ���켣��������/���й켣��������Ŀ֮��
		 int each_TrackNum = list.get(k).GetindexEPS().size()+1;
		 double proportion = each_TrackNum/All_length;//��p(Xk)
		 Reverse_hx = Reverse_hx+ proportion*(Math.log(proportion)/Math.log((double)2));//�ۼ�
		// id_hx.put(k, hx);//<id,h(x)>
	 }
	 EPS_HX.put(eps, num_length);//<eps,h(x)>
	 //EPS_AllNum.put(eps, id_num);//<eps,hashmap<id,num>
	 }
	 return EPS_HX;
 }
  
}
