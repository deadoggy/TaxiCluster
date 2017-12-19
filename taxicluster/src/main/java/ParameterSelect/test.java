package ParameterSelect;

import org.tj.sse.taxicluster.entity.Taxi;

import java.util.*;

public class test{
	 //���չʾ���й켣
		
		/** 
		 * �����������������(��γ��)���� 
		 *  
		 * @param long1 
		 *            ��һ�㾭�� 
		 * @param lat1 
		 *            ��һ��γ�� 
		 * @param long2 
		 *            �ڶ��㾭�� 
		 * @param lat2 
		 *            �ڶ���γ�� 
		 * @return ���ؾ��� ��λ���� 
		 */  
		
		//�����ƶȽ��й켣����,ʹ�õ���TRACLUS���෽��
	
		public HashMap<Integer, ArrayList<Integer>>  TRACLUS(List<List<Taxi>> list, double[][] similarity){
	 
		ArrayList<Integer> Queue = new ArrayList<Integer>();//����Q
		ArrayList<Integer> Noise = new ArrayList<Integer>();//����
		double eps = 5 ; double minlns = 3;
		int clusterId = 0;
		ArrayList<Integer> TraClusterId = new ArrayList<Integer>();//��¼ÿ���켣��clusterId
		ArrayList<Integer> TraClassified = new ArrayList<Integer>();//��¼ÿ���켣�Ƿ񱻹��࣬0Ϊû�б����࣬1Ϊ��������
		int num_tra = list.size();//һ���м����켣
		//�����еĹ켣�ľ����������Ϊ-1
		for(int i=0;i<num_tra;i++){
			TraClusterId.add(-1);
		}
		for(int i = 0 ; i<num_tra;i++){
			TraClassified.add(0);//�����й켣ȫ�����Ϊδ������
		}
		for(int i = 0;i<num_tra;i++){//����ÿ���켣
			if(TraClassified.get(i)==0){
			//	TraClassified.set(i,2);//���Ϊ������
				//����켣iδ������
				//���ȼ���ÿ���켣�����������������similarity����С�ڵ���eps�Ĺ켣����
				ArrayList<Integer> eps_col  = EPS_NUM(i, similarity, eps);//��i���켣������켣��
				int numOfEps = eps_col.size();//����켣��
				if(numOfEps+1>=minlns){//����Ǻ����߶�
					TraClusterId.set(i,clusterId);//�����߶μ��뵽clustering��
					//��clusterId��������������еĹ켣
				     for(int j=0;j<eps_col.size();j++){
				    	 TraClusterId.set(j, clusterId);
				    	 TraClassified.set(j, 2);//�����߶α��Ϊ������
				    	 //������켣��������켣�ļ��ϲ��뵽����Q
				    	 if(i!=j){//��������켣
				    		 Queue.add(eps_col.get(j));//����켣���к�
				    	 }
				     }
				    //STEP2:ExpandCluster
				 	while( Queue.size()>0){//queue�ǿ�
						int Track_col  = Queue.get(0);//Queue����ĵ�һ���켣M
						//����M��Ӧ������
						ArrayList<Integer> EPS_M =  EPS_NUM(Track_col, similarity, eps);//��M���켣������
						if(EPS_M.size()>=minlns){//ֱ���ܶȿɴ�
							for(int j = 0;j<EPS_M.size();j++){
								int col_num = EPS_M.get(j);//�����߶ε��к�
								if(TraClassified.get(col_num)==0||TraClassified.get(col_num)==1){
									TraClusterId.set(col_num, clusterId);
									TraClassified.set(col_num, 2);
								}
								int X = EPS_M.get(j);
								if(TraClassified.get(j)==0 ){
								    //��Queue��������Ƿ���X�Ź켣����û�������
									boolean find = false;
									for(int k=0;k<Queue.size();k++){
										int tra_num=Queue.get(k);
										if(tra_num==X){
											find = true;//queue�����Ѿ����˶�Ӧ�Ĺ켣
											break;
										}
									}if(find==false){
										//queue����û���߶�X
										Queue.add(X);
									}
								}
							}
						}
						Queue.remove(0);//��Queue�еĵ�һ���켣ȥ��
					}
				    clusterId++;
				}else{
					//���켣L���Ϊ����
					Noise.add(i);
					TraClassified.set(i, 1);
				}
			}
			 
		}
		//STEP 3 : �����й켣���䵽�������ľ���
		HashMap<Integer,ArrayList<Integer>> ClusterAll = new HashMap<Integer,ArrayList<Integer>>();//���о���ļ���
		ArrayList<Integer> copyOfClusterid=new ArrayList<Integer>();
		copyOfClusterid.addAll(TraClusterId);
		ArrayList<Integer> Cluster;
		ArrayList<Integer> uniqueClusterId = uniqueClusterId(copyOfClusterid);
	    for(int i = 0;i<uniqueClusterId.size();i++){ 
	    	Cluster = new ArrayList<Integer>();//ÿ���켣��Ӧһ������
	    	int clu_num = uniqueClusterId.get(i);
	    	for(int j=0;j<TraClusterId.size();j++){//j��Ӧ���ǹ켣���к�
	    	if(TraClusterId.get(j)==clu_num){//��J���켣�ľ������ƥ���
	    	Cluster.add(j);
	    	}	
	    	}
	    	ClusterAll.put(i, Cluster);
	    }
		//���켣�Ļ���
	    for(int j = 0;j<ClusterAll.size();j++){
	    	int numOfTracks = 0;
	    	ArrayList<Integer> ClusterTemp = ClusterAll.get(j);//��Ӧ�ľ���
	    	numOfTracks = ClusterTemp.size();//�����ڵĹ켣��
	    	if(numOfTracks+1<minlns){//�������Ӧ�Ĺ켣��ĿС��Minlns
	    		//��������ܾ��༯����ȥ��
	    	    ClusterAll.remove(j);
	             }
	     }
	    return ClusterAll;
		}
		public ArrayList<Integer> uniqueClusterId(ArrayList<Integer> list){//ȥ�ظ�
			ArrayList<Integer> Unique_list = list;
			for  ( int  i  =   0 ; i  <  Unique_list.size()  -   1 ; i ++ )   { 
			    for  ( int  j  =  Unique_list.size()  -   1 ; j  >  i; j -- )   { 
			      if  (Unique_list.get(j).equals(Unique_list.get(i)))   { 
			    	  Unique_list.remove(j); 
			      } 
			    } 
			  } 
			return Unique_list;
		}
		 
		//����ÿ���켣��EPS�ڹ켣 
	 	public ArrayList<Integer> EPS_NUM(int numTra,double[][] similarity,double eps){
			ArrayList<Integer> epsList = new ArrayList<Integer>();
			 
			for(int i=0;i<similarity[numTra].length;i++){
				if(similarity[numTra][i]<=eps){
					epsList.add(i);//������켣���кż�¼����
				}
			}
			return epsList ;// ����켣��
		}
		//�������ĵ�,����ӦΪ�������Ĺ켣
		 
	//����representative trajectory
		@SuppressWarnings("unchecked")
		public  List<Taxi>  Representative( ArrayList<Integer>  cluster, List<List<Taxi>> trajectory,double minlns){
			double smooth = 0.0002;
			List<List<Taxi>> Re_all = new ArrayList<List<Taxi>>();
			 List<Taxi>  representative = new ArrayList< Taxi >();//representative �켣
			List<List<Taxi>> clusterTracks = new ArrayList<List<Taxi>>();//���������й켣
			List<List<Taxi>> clusterRotate = new ArrayList<List<Taxi>>();//������������ת��켣
			//����ƽ����������
			double x = 0;
			double y = 0;
			for(int i = 0 ;i<cluster.size();i++){
			 int track_number = cluster.get(i);//��������Ĺ켣��
			 List<Taxi> tracks =  trajectory.get(track_number);//��track_number���켣
			 clusterTracks.add(tracks);
			 double start_x = Double.parseDouble(tracks.get(0).getLatitude());//��һ��GPS��ľ�����Ϊ����ϵ��x
			 double start_y = Double.parseDouble(tracks.get(0).getLongtitude());//��һ��GPS���γ����Ϊ����ϵ��y
			 double end_x = Double.parseDouble(tracks.get(tracks.size()-1).getLatitude());//���һ��GPS��ľ�����Ϊ����ϵ��x
			 double end_y = Double.parseDouble(tracks.get(tracks.size()-1).getLongtitude());//���һ��GPS���γ����Ϊ����ϵ��y
			 //����������(x2-x1,y2-y1)
				double temp_x = end_x-start_x;
				double temp_y = end_y-start_y;
				x = x + temp_x;
				y = y + temp_y;
			}
			  
			double[] average_vector = {x/cluster.size(),y/cluster.size()};
			double x_average = average_vector[0];
			double y_average = average_vector[1];
		
			//��ת������ʹ��x����ƽ������ƽ��
			//���ȼ�����ת�Ƕȣ�ͨ���������
			double average_length = Math.sqrt(x_average*x_average+y_average*y_average);
					
			double cos_angle =  x_average/average_length;
			double sin_angle =  y_average/average_length;
			 
			//�����sin �� cos֮�����x'��y'
			for(int i=0;i<clusterTracks.size();i++){
				List<Taxi> temp = clusterTracks.get(i);//ԭʼ�켣
				List<Taxi> temp_rotate =  new ArrayList<Taxi>();//���ڴ����ת��켣
				for(int j=0;j<temp.size();j++){
				Taxi point = temp.get(j);//ԭʼ��
				Taxi rotate_point = new Taxi();//���ڴ����ת��ĵ�
		         double	x_rotate =  cos_angle * (Double.parseDouble(point.getLatitude())) + sin_angle* (Double.parseDouble(point.getLongtitude()));//cos*x+sin*y
		         double	y_rotate =  (-1)*sin_angle* (Double.parseDouble(point.getLatitude())) + cos_angle * (Double.parseDouble(point.getLongtitude()));//-1*sin*x+cos*y
				 rotate_point.setLatitude(String.valueOf(x_rotate));//��ת��ľ���
				 rotate_point.setLongtitude(String.valueOf(y_rotate));//��ת���γ��
				 temp_rotate.add(rotate_point);
				}
			    clusterRotate.add(temp_rotate);
			}
			ArrayList<Taxi> P =  new ArrayList<Taxi>();//P�Ǿ���������յ�ļ���
			for(int i=0;i<clusterRotate.size();i++){
		    List<Taxi> tempTracks = clusterRotate.get(i); 
			Taxi p_start = new Taxi();
		    p_start = tempTracks.get(0);//���
			Taxi p_end = new Taxi();
		    p_end = tempTracks.get(tempTracks.size()-1);//�յ�
			P.add(p_start);
			P.add(p_end);
			}
	 
			MyComparator mc = new MyComparator();
			Collections.sort(P,mc);//��P�ڵĵ���и���X'��ֵ��С���������
			//����ת��������߶ν������򣺸����߶�������С��X'ֵ���бȽ�
			try{
				Collections.sort(clusterRotate, new Comparator<List<Taxi>>(){
					@Override
					public int compare(List<Taxi>l1, List<Taxi> l2) {
						double v1_start =Double.parseDouble(l1.get(0).getLatitude());
						double v1_end =Double.parseDouble(l1.get(l1.size()-1).getLatitude());
						double v2_start = Double.parseDouble(l2.get(0).getLatitude());
						double v2_end = Double.parseDouble(l2.get(l2.size()-1).getLatitude());
						double min_v1=0.0;
						double min_v2=0.0;
						if(v1_start<v1_end){
							min_v1 = v1_start;
						}else{
							min_v1 = v1_end;
						}
						if(v2_start<v2_end){
							min_v2 = v2_start;
						}else{
							min_v2 = v2_end;
						}
						if(min_v1 > min_v2)
							return 1;
						if(min_v1 < min_v2)
							return -1;
						return 0;
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
	 
			double preX = 0.0;
			boolean mark = false;
			for(int i=0;i<P.size();i++){
				Taxi p_sweep=P.get(i);
				CalcBean neighbor = getNeighbor(p_sweep, clusterRotate);
				if(!mark&&neighbor.getCount()>minlns){
					double avgy = neighbor.getSumy();
					double x_before = Double.parseDouble(p_sweep.getLatitude())*cos_angle-avgy*sin_angle;
					double y_before = (Double.parseDouble(p_sweep.getLatitude())+sin_angle*avgy*cos_angle-Double.parseDouble(p_sweep.getLatitude())*cos_angle*cos_angle)/sin_angle;
				    Taxi insert = new Taxi();
				    insert.setLatitude(String.valueOf(x_before));
				    insert.setLongtitude(String.valueOf(y_before));
					representative.add(insert);
					preX = Double.parseDouble(p_sweep.getLatitude());
					mark = true;
				}
				else if(neighbor.getCount() >minlns && mark){
					double p_x = Double.parseDouble(p_sweep.getLatitude());
					if(p_x - preX>=smooth){
						double avgy = neighbor.getSumy()/neighbor.getCount();
						double x_before_else = p_x *cos_angle - avgy*sin_angle;
						double y_before_else =(p_x+avgy*sin_angle*cos_angle-p_x*cos_angle*cos_angle)/sin_angle;
						Taxi insert_else = new Taxi();
						insert_else.setLatitude(String.valueOf(x_before_else));
						insert_else.setLongtitude(String.valueOf(y_before_else));
						representative.add(insert_else);
						preX = p_x;
					}
				}
			}
	       Re_all.add(representative);
	 
			return representative;
		}
		 
		private CalcBean getNeighbor(Taxi p, List<List<Taxi>>tracluster){
			CalcBean bean = new CalcBean();
			for(int i = 0 ; i < tracluster.size() ; i++){
				List<Taxi> l = tracluster.get(i);
				double max = Double.parseDouble(l.get(0).getLatitude());
				double min = Double.parseDouble(l.get(l.size()-1).getLatitude());
				if(max < min){
					double temp = max ;
					max = min;
					min = temp;
				}
				if(min > Double.parseDouble(p.getLatitude()))
					break;
				if(min < Double.parseDouble(p.getLatitude()) && max > Double.parseDouble(p.getLatitude())){
					bean.incrementCount();
					bean.incrementSumY( getY(l , p));
				}
			}
			return bean;
	}	
		private double getY(List<Taxi> l , Taxi p){
			double start_x = Double.parseDouble( l.get(0).getLatitude());
			double start_y = Double.parseDouble( l.get(0).getLongtitude());
			double end_x = Double.parseDouble( l.get(l.size()-1).getLatitude());
			double end_y = Double.parseDouble( l.get(l.size()-1).getLongtitude());
			double p_x = Double.parseDouble(p.getLatitude());
			double disX = (start_x- end_x);
			if(disX == 0){
				return (end_y - start_y) / 2;
			}
			//б��
			double k = (start_y - end_y) / disX;
			return (start_y + k * (p_x - start_x));
	}
	    public  List<List<Taxi>> Partition(List<Taxi> TR){//��һ��ԭʼ�켣���ֳ��߶�
	    ArrayList<Integer> CP = new ArrayList<Integer>();
	    List<List<Taxi>> Line = new ArrayList<List<Taxi>>();//����и��ļ����߶�
	    int startIndex = 0;
	    int length = 1;
	    int len_i = TR.size();//TR����һ���м�����
	    int count = 0;//CP�ļ������
	    Taxi p1= TR.get(0);//�켣�ĵ�һ����
	    CP.add(0);//����һ�������CP�У�0�������ԭʼ�켣���к�
	    int currIndex;
	    while(startIndex+length<=len_i-1){
	    	currIndex = startIndex + length;
	        Taxi p_startIndex = TR.get(startIndex);
	        Taxi p_currIndex = TR.get(currIndex);
	        double Cost_par = MDL(1,TR,startIndex,currIndex);
	        double Cost_nopar = MDL(0,TR,startIndex,currIndex);
	        
	        if(Cost_par>Cost_nopar){
	        	count = count + 1;
	        	CP.add(currIndex-1);//����ǰ���ǰһ�������CP
	        	startIndex = currIndex - 1;
	        	length = 1;
	        }else{
	        	length = length + 1;
	        }
	    }
	    count = count + 1;
	    CP.add(TR.size()-1);//���켣�����һ������кż��뵽CP
	    return null;
	    }
		public List<Taxi> CutTrajectory(int start,int end,List<Taxi> single_trajectory,ArrayList<Integer>CP){
			List<Taxi> cutTra = new ArrayList<Taxi>();
			if(start != CP.get(0)){
				start = start + 1;
			}
			for(int i=start;i<=end;i++){
				cutTra.add(single_trajectory.get(i));
			}
			return cutTra;
		}
	    public double distance(Taxi p1,Taxi p2){
	    	double distance = 0.0;
	    	 double p1_x = Double.parseDouble(p1.getLatitude());
	         double p1_y = Double.parseDouble(p1.getLongtitude());
	         double p2_x = Double.parseDouble(p2.getLatitude());
	         double p2_y = Double.parseDouble(p2.getLongtitude());
	         
	         distance  =  Math.sqrt((p1_x-p2_x)*(p1_x-p2_x)+(p1_y-p2_y)*(p1_y-p2_y));
	         return distance;
	    }
	    public double distance(List<Taxi> trajectory){
	    	double distance = 0.0;
	    	double start_x = Double.parseDouble(trajectory.get(0).getLatitude());
	        double start_y = Double.parseDouble(trajectory.get(0).getLongtitude());
	        double end_x = Double.parseDouble(trajectory.get(trajectory.size()-1).getLatitude());
	        double end_y = Double.parseDouble(trajectory.get(trajectory.size()-1).getLongtitude());
	    	
	        distance = Math.sqrt((start_x- end_x)*(start_x - end_x)+(start_y - end_y)*(start_y - end_y));
	    	return distance ;
	    }
		private double MDL( int mdl,List<Taxi> trajectory,int startIndex,int end) {
	        //LH�����������ŷ����þ���
			double LH = 0.0;
			double LDH = 0.0;
			//�����кŶ�TR���н�ȡ
			List<Taxi> partition = new ArrayList<Taxi>();
				for(int i = startIndex;i<=end;i++){
					Taxi temp = trajectory.get(i);
					partition.add(temp);
				}
			if(mdl ==1){//partition
			Taxi start = trajectory.get(startIndex);
			double x_start = Double.parseDouble(start.getLatitude());
			double y_start = Double.parseDouble(start.getLongtitude());
			Taxi endTra = trajectory.get(end);
			double x_end = Double.parseDouble(endTra.getLatitude());
			double y_end = Double.parseDouble(endTra.getLongtitude());
			double x = x_start - x_end;
			double y = y_start - y_end;
			LH = Math.sqrt(x*x+y*y);
			
			
			//����LDH
			Taxi startPoint = partition.get(0);
			Taxi endPoint =partition.get(partition.size()-1);
			double Perpen_distance = 0.0;
			double angle_distance = 0.0;
			double x_par_start = Double.parseDouble(startPoint.getLatitude());
			double y_par_start = Double.parseDouble(startPoint.getLongtitude());
			double x_par_end = Double.parseDouble(endPoint.getLatitude());
			double y_par_end = Double.parseDouble(endPoint.getLongtitude());
			if(partition.size()>2){
				//���p1��p2�������ڵ�������
				for(int i=1;i<partition.size()-1;i++){//i��partition��2����������2����
					Taxi point_TR = partition.get(i);
					Taxi point_next = partition.get(i+1);
					double x_S = Double.parseDouble(point_TR.getLatitude());
					double y_S = Double.parseDouble(point_TR.getLongtitude());
					double x_E = Double.parseDouble(point_next.getLatitude());
					double y_E = Double.parseDouble(point_next.getLongtitude());
					if(x_par_start==x_par_end&&y_par_start==y_par_end){
						LDH = 0.0;
					}else{
						double distanceSE = Math.abs((x_S - x_par_start)*(y_par_start - y_par_end)-(x_par_start-x_par_end)*(y_S - y_par_start))/Math.sqrt((x_par_start - x_par_end)*(x_par_start - x_par_end) + (y_par_start -y_par_end)*(y_par_start -y_par_end));                                                
						double distance_par = Math.abs((x_E - x_par_start)*(y_par_start - y_par_end)-(x_par_start-x_par_end)*(y_E - y_par_start))/Math.sqrt((x_par_start - x_par_end)*(x_par_start - x_par_end) + (y_par_start -y_par_end)*(y_par_start -y_par_end));                                       
					    //��ֱ����
						Perpen_distance = Perpen_distance + (distanceSE*distanceSE + distance_par*distance_par)/(distanceSE + distance_par);
						//�ǶȾ���
						double Li_length = Math.sqrt((x_par_end - x_par_start)*(x_par_end - x_par_start)+(y_par_end - y_par_start)*(y_par_end - y_par_start));
					    double Pi_length = Math.sqrt((x_E -x_S)*(x_E - x_S) + (y_E - y_S)* (y_E - y_S));
					    double cosValue =( (x_par_end - x_par_start) * (x_E - x_S) + (y_par_end - y_par_start) * (y_E - y_S))/(Li_length * Pi_length);
					    angle_distance = angle_distance + Math.sqrt((1-cosValue*cosValue)*((x_par_end - x_par_start)* (x_par_end - x_par_start)+(y_par_end - y_par_start)* (y_par_end - y_par_start)));
					    
					}
				}
				LDH = (Math.log(Perpen_distance)+Math.log(angle_distance))/Math.log(2);
			}else{
				LDH = 0.0;
			}
			}
			else{
				//nopar
				if(partition.size()>1){
				for(int i = 0;i<partition.size()-1;i++){
					double x1= Double.parseDouble(partition.get(i).getLatitude());
					double y1= Double.parseDouble(partition.get(i).getLongtitude());
					double x2= Double.parseDouble(partition.get(i+1).getLatitude());
					double y2= Double.parseDouble(partition.get(i+1).getLongtitude());
					LH  = LH + Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
				}}
				else{
					LH = 0.0;
				}
			}
	       double result  = LH+LDH;
			return result;
		}
	    private double distance_perpen(Taxi si,Taxi ei,Taxi sj,Taxi ej) {
	        double sj_x = Double.parseDouble(sj.getLatitude());
	        double si_x = Double.parseDouble(si.getLatitude());
	        double ei_x = Double.parseDouble(ei.getLatitude());
	        double ej_x = Double.parseDouble(ej.getLatitude());
	        
	        double sj_y = Double.parseDouble(sj.getLongtitude());
	        double si_y = Double.parseDouble(si.getLongtitude());
	        double ei_y = Double.parseDouble(ei.getLongtitude());
	        double ej_y = Double.parseDouble(ej.getLongtitude());
	    	double x1=sj_x-si_x;
			double y1=sj_y-si_y;
			
			double x2=ei_x-si_x;
			double y2=ei_y-si_y;
			
			double x3=ej_x-si_x;
			double y3=ej_y-si_y;
			
			double x=Math.pow(x2,2)+Math.pow(y2,2);
			
			double u1=(x1*x2+y1*y2)/x;
			double u2=(x3*x2+y3*y2)/x;
		    
		    double psx=si_x+u1*x2;
			double psy=si_y+u1*y2;
			
			double pex=si_x+u2*x2;
			double pey=si_y+u2*y2;
			
			double Lper1=Math.sqrt(Math.pow(psx-sj_x,2)+Math.pow(psy-sj_y,2));
			double Lper2=Math.sqrt(Math.pow(pex-ej_x,2)+Math.pow(pey-ej_y,2));
			double d_perpen;
			
			if(Lper1==0&&Lper2==0)
			{
				d_perpen=0;
			}
			else
			{
				d_perpen=(Math.pow(Lper1,2)+Math.pow(Lper2,2))/(Lper1+Lper2);
			}
	      return d_perpen;
		}

		public List<Taxi> newLine(List<Taxi> trajectory,int start, int end){
	    	List<Taxi> newLine = new ArrayList<Taxi>();
	    	for(int i=start;i<=end;i++){
	    		newLine.add(trajectory.get(start));
	    	}
	    	return newLine;
	    }
		private double distance_angle(List<Taxi> l1,List<Taxi> l2) {
			//�ǶȾ���
		    double len = distance(l2);
		    double cos = cos_angle(l1,l2);
		    double sin = Math.sqrt(1-cos*cos);
		    double angle = len*sin;
		    return angle;
		}
	    public double cos_angle(List<Taxi> l1,List<Taxi> l2){
	    	double start1_x = Double.parseDouble(l1.get(0) .getLatitude());
			double start1_y = Double.parseDouble(l1.get(0).getLongtitude());
			double end1_x = Double.parseDouble(l1.get(l1.size()-1).getLatitude());
			double end1_y = Double.parseDouble(l1.get(l1.size()-1).getLongtitude());
			double start2_x = Double.parseDouble(l2.get(0).getLatitude());
	        double start2_y = Double.parseDouble(l2.get(0) .getLongtitude());
	        double end2_x = Double.parseDouble(l2.get(l2.size()-1) .getLatitude());
	        double end2_y = Double.parseDouble(l2.get(l2.size()-1) .getLongtitude());
	        double x1= end1_x - start1_x;
	        double y1= end1_y - start1_y;
	        double x2= end2_x - start2_x;
	        double y2= end2_y - start2_y;
	        double length1 = Math.sqrt(x1*x1+y1*y1);
	        double length2 = Math.sqrt(x2*x2+y2*y2);
	        if(length1 ==0 || length2 ==0){
	        	return 0;
	        }
	        else{
	        	double cos = (x1*x2+y1*y2)/(length1*length2);
	        	return cos;
	        }
	    }
		private double PerDistance(Taxi start, Taxi end, Taxi p1, Taxi p2) {
			// TODO Auto-generated method stub
			//��ֱ����,start���end���ǵײ����߶ε����˵�,��ʽ��L=|(x0-x1)(y1-y2)-(x1-x2)(y0-y1)|/��[(x1-x2)^2+(y1-y2)^2]
			double perDistance = 0.0;
			double start_x = Double.parseDouble(start .getLatitude());
			double start_y = Double.parseDouble(start.getLongtitude());
			double end_x = Double.parseDouble(end.getLatitude());
			double end_y = Double.parseDouble(end.getLongtitude());
			double p1_x = Double.parseDouble(p1 .getLatitude());
	        double p1_y = Double.parseDouble(p1 .getLongtitude());
	        double p2_x = Double.parseDouble(p2 .getLatitude());
	        double p2_y = Double.parseDouble(p2 .getLongtitude());
	        if(start_x!=end_x || start_y!=end_y){
	        double dis1 = Math.abs((p1_x-start_x)*(start_y-end_y)-(start_x - end_x)*(p1_y - start_y))/Math.sqrt((start_x-end_x)*(start_x-end_x)+(start_y-end_y)*(start_y-end_y));
			double dis2 = Math.abs((p2_x-start_x)*(start_y-end_y)-(start_x - end_x)*(p2_y - start_y))/Math.sqrt((start_x-end_x)*(start_x-end_x)+(start_y-end_y)*(start_y-end_y));
			if((dis1+dis2)!=0)
			{
				perDistance = (dis1*dis1+dis2*dis2)/(dis1+dis2);
			}
			else{
				perDistance = 0.0;
			}
	        }
			return perDistance;
		}

		//controller���õľ��෽��
		
		public List<List<Taxi>> clusterTrajectory(List<List<Taxi>> trajectory,double[][] similarity) {
			double minlns = 2;
			List<List<Taxi>> partition= new ArrayList<List<Taxi>>();
			//for(int i=0;i<trajectory.size();i++){
				//List<List<Taxi>> temp = Partition(trajectory.get(i));//��ֵõ��ļ����߶�
			    //partition.addAll(temp);
			//}
			
			//tra.setParameter(5, 3);
			//tra.setMDL_COST_ADVANTAGE(10);
			 HashMap<Integer, ArrayList<Integer>> resultCluster = TRACLUS(trajectory,similarity);
	         List<List<Taxi>> resultRepre = new ArrayList<List<Taxi>>();
		   for(int i=0;i<resultCluster.size();i++){
		    //	 for(int i=1;i<2;i++){
		    	List<Taxi> temp = Representative(resultCluster.get(i), trajectory, minlns);
		    	for(int j=0;j<temp.size();j++){
		    		double longi= Double.parseDouble(temp.get(j).getLongtitude());
		    		double lati = Double.parseDouble(temp.get(j).getLatitude());
		    		if(longi>180||longi<(-1)*180){
		    			temp.remove(j);
		    		}
		    		if(lati>90||lati<(-1)*90){
		    			temp.remove(j);
		    		}
		    	}
		    	resultRepre.add(temp);
		    } 
		  
			return resultRepre;
		   
		}
		//ʹ��DBSCAN���ֵķ������о��࣬eps=1.6,minlns=11

	public static  void main(String[] args){
		Taxi a1 = new Taxi();
		a1.setLongtitude("4");
		a1.setLatitude("5");
		Taxi a2 = new Taxi();
		a2.setLongtitude("2");
		a2.setLatitude("4");
		Taxi a3 = new Taxi();
		a3.setLongtitude("3");
		a3.setLatitude("5");
		Taxi a4 = new Taxi();
		a4.setLongtitude("1");
		a4.setLatitude("40");
		List<Taxi> A = new ArrayList<>();
	    A.add(a1);
	    A.add(a2);
	    A.add(a3);
	    A.add(a4);
	    //B
	    Taxi b1 = new Taxi();
		b1.setLongtitude("40");
		b1.setLatitude("5");
		Taxi b2 = new Taxi();
		b2.setLongtitude("24");
		b2.setLatitude("4");
		Taxi b3 = new Taxi();
		b3.setLongtitude("39");
		b3.setLatitude("5");
		Taxi b4 = new Taxi();
		b4.setLongtitude("10");
		b4.setLatitude("40");
		Taxi b5= new Taxi();
		b5.setLongtitude("15");
		b5.setLatitude("40");
		Taxi b6 = new Taxi();
		b6.setLongtitude("80");
		b6.setLatitude("44");
		List<Taxi> 	B = new ArrayList<>();
	    B.add(b1);
	    B.add(b2);
	    B.add(b3);
	    B.add(b4);
	    B.add(b5);
	    B.add(b6);
	    //C
	    //B
	    Taxi c1 = new Taxi();
		c1.setLongtitude("14");
		c1.setLatitude("15");
		Taxi c2 = new Taxi();
		c2.setLongtitude("22");
		c2.setLatitude("42");
		Taxi c3 = new Taxi();
		c3.setLongtitude("43");
		c3.setLatitude("55");
		Taxi c4 = new Taxi();
		c4.setLongtitude("12");
		c4.setLatitude("40");
		Taxi c5= new Taxi();
		c5.setLongtitude("51");
		c5.setLatitude("40");
		Taxi c6 = new Taxi();
		c6.setLongtitude("40");
		c6.setLatitude("84");
		List<Taxi> 	C = new ArrayList<>();
	    C.add(c1);
	    C.add(c2);
	    C.add(c3);
	    C.add(c4);
	    C.add(c5);
	    C.add(c6);
		//MyComparator mc = new MyComparator();
		//Collections.sort(list,mc);
		//for(int i=0;i<list.size();i++){
		//	System.out.println(list.get(i).getLongtitude());
		//}
	    List<List<Taxi>> list = new ArrayList<List<Taxi>>();
	    list.add(A);
	    list.add(B);
	    list.add(C);
	    List<List<Taxi>> partition = new ArrayList<List<Taxi>>();
	    test t = new test();
	    	partition = 	t.Partition(C);
	     System.out.println(partition.size());
	}
	
}