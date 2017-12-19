package DataMining;

import org.tj.sse.taxicluster.entity.Taxi;
import org.tj.sse.taxicluster.service.TaxiServiceImpl;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DataMining {
//�ֳ������֣�partition��clustering��representative
public List<List<Taxi>> Partition_trajectory(List<Taxi> trajectory) throws ParseException{
	List<List<Taxi>> partition = new ArrayList<List<Taxi>>();
	//���ȿ��ǵ������ʱ����16�룬�Ӷ�̫���ܼ��޷����зָ����Ƚ���ѹ��
	List<Taxi> compress = getCompressed(Compression(trajectory,trajectory.size()), trajectory);
	TaxiServiceImpl impl = new TaxiServiceImpl();
	partition = impl.Partition(compress);//���зָ�
	return partition;
 }
public Date convertTime(String str) throws ParseException{
	Date date = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	date = sdf.parse(str); 
	return date;
}
public HashMap<Integer,Double> getMaxDistance(List<Taxi>taxi){
	HashMap<Integer,Double> map = new HashMap<Integer,Double>();
	double max = 0 ;
	int max_num = 0;;
	Taxi first = taxi.get(0);
	double x_first = Double.parseDouble(first.getLatitude());
	double y_first = Double.parseDouble(first.getLongtitude());
	Taxi last = taxi.get(taxi.size()-1);
	double x_last = Double.parseDouble(last.getLatitude());
	double y_last = Double.parseDouble(last.getLongtitude());
	for(int i=1;i<taxi.size()-1;i++){
		//���ȼ���ÿ���㵽�����յ����ߵĴ�ֱ����
	Taxi temp = taxi.get(i);
	double x = Double.parseDouble(temp.getLatitude());
	double y = Double.parseDouble(temp.getLongtitude());
    double distance_Per = Math.abs((x - x_first)*(y_first - y_last)-(x_first-x_last)*(y - y_first))/Math.sqrt((x_first - x_last)*(x_first - x_last) + (y_first -y_last)*(y_first -y_last));                                                
    if(distance_Per > max){
    	max = distance_Per;
    	max_num = i;
    }
	}
	map.put(max_num,max);
	return map;
}
public Double getMaxDistance_max(List<Taxi>taxi){
	double max = 0 ;
	int max_num = 0;;
	Taxi first = taxi.get(0);
	double x_first = Double.parseDouble(first.getLatitude());
	double y_first = Double.parseDouble(first.getLongtitude());
	Taxi last = taxi.get(taxi.size()-1);
	double x_last = Double.parseDouble(last.getLatitude());
	double y_last = Double.parseDouble(last.getLongtitude());
	for(int i=1;i<taxi.size()-1;i++){
		//���ȼ���ÿ���㵽�����յ����ߵĴ�ֱ����
	Taxi temp = taxi.get(i);
	double x = Double.parseDouble(temp.getLatitude());
	double y = Double.parseDouble(temp.getLongtitude());
    double distance_Per = Math.abs((x - x_first)*(y_first - y_last)-(x_first-x_last)*(y - y_first))/Math.sqrt((x_first - x_last)*(x_first - x_last) + (y_first -y_last)*(y_first -y_last));                                                
    if(distance_Per > max){
    	max = distance_Per;
    	max_num = i;
    }
	}
	return max;
}
public List<Taxi> getCompressed(ArrayList<Integer>breakPoint,List<Taxi> trajectory){
	List<Taxi> taxi = new ArrayList<Taxi>();
	for(int i= 0;i<breakPoint.size();i++){
		int col= breakPoint.get(i);
		Taxi temp = trajectory.get(col);
		taxi.add(temp);
	}
	return taxi;
}
public double getMaxDistance_amongSub(List<Taxi>taxi,ArrayList<Integer> breakPoint){//Ѱ���ӹ켣������ŷ�Ͼ���
	double max= 0.0;
	List<List<Taxi>> sub = subTrajectory(breakPoint, taxi);//����ӹ켣
	for(int i=0;i<sub.size();i++){
		double temp = getMaxDistance_max(sub.get(i));//��i���ӹ켣�е����ŷ�Ͼ���
		if(temp>max){
			max = temp;
		}
	}
	return max;
}
public double getThreshold(List<Taxi> trajectory) throws ParseException{
	//�����ֵ
	Taxi first = trajectory.get(0);
	double x_first = Double.parseDouble(first.getLatitude());
	double y_first = Double.parseDouble(first.getLongtitude());
	Date first_time =  convertTime(first.getTime());
	
	Taxi last = trajectory.get(trajectory.size()-1);
	double x_last = Double.parseDouble(last.getLatitude());
	double y_last = Double.parseDouble(last.getLongtitude());
	Date last_time =  convertTime(last.getTime());
	Taxi second = trajectory.get(1);
	double x_second = Double.parseDouble(second.getLatitude());
    double y_second = Double.parseDouble(second.getLongtitude());
	Date second_time = convertTime(second.getTime());
	long deta_e =Math.abs(last_time.getTime()-first_time.getTime());
		long data_i = Math.abs(second_time.getTime()-first_time.getTime());
		double latiApprox_i = x_second + (data_i/deta_e)*(x_last - x_first);
		double longtiApprox_i = y_second + (data_i/deta_e)*(y_last - y_first);
		//�ָ���ֵ
		double threshold = Math.sqrt((latiApprox_i - x_second)*(latiApprox_i)+(longtiApprox_i - y_second)*(longtiApprox_i - y_second));
		return threshold;
	
}
List<List<Taxi>> subTrajectory(ArrayList<Integer> num_track, List<Taxi> taxi){
	List<List<Taxi>> subTracks = new ArrayList<List<Taxi>>();
	for(int i=0;i<num_track.size()-1;i++){
		List<Taxi> sub = new ArrayList<Taxi>();
		int start = num_track.get(i);
		int end = num_track.get(i+1);
		if(i!= num_track.size()-2){
		for(int j = start;j<end;j++){
			sub.add(taxi.get(j));
		}}else{
			for(int j = start;j<=end;j++){
				sub.add(taxi.get(j));
			}
		}
		subTracks.add(sub);//���ӹ켣���뵽list����
	}
	return subTracks;
}
private ArrayList<Integer> Compression(List<Taxi> trajectory,int original_size) throws ParseException {
	// ʹ��TD-TR �㷨���й켣ѹ���������ܶ�
	ArrayList<Integer> breakPoint = new ArrayList<Integer>();//��¼���ĸ�����зָ�
	HashMap<Integer,Double>map = getMaxDistance(trajectory);//�켣Ϊtrajectoryʱ���м�㵽�˵����ߵ����ֵ��ź�������
	double max=0.0;//������
	int max_col = -1;//�������Ӧ�ĵ��
	 Set<Map.Entry<Integer, Double>> set=map.entrySet();    
     for (Iterator <Map.Entry<Integer, Double>> iterator = set.iterator(); iterator.hasNext();) {  
         Map.Entry<Integer, Double> entry = (Map.Entry<Integer, Double>) iterator.next();  
         max_col=entry.getKey();  
         max=entry.getValue();  
     }  
	double max_sub = 0.0;
	double threshold = getThreshold(trajectory);//�켣Ϊtrajectoryʱ����ֵ�������ǲ��õڶ���������Ƶ㵽�����ľ�����Ϊ��ֵ
	//�����ж�max>threshold?����ǵĻ�����breakpoint���ָ���������켣���ֱ����
	if(max > threshold){
		max_sub = getMaxDistance_amongSub(trajectory,breakPoint);
		if((original_size - breakPoint.size())!=2&&(max_sub>=threshold)){
		  breakPoint.add(max_col);
		  List<List<Taxi>> subTracks = subTrajectory(breakPoint, trajectory);//�ָ���ȡ�ӹ켣
		  for(int i=0;i<subTracks.size();i++){
		   ArrayList<Integer> temp = Compression(subTracks.get(i),original_size);
		   breakPoint.addAll(temp);
		}
	}
	}
	return breakPoint;
}

public void transform(List<Taxi> list) {
	for(int i=0;i<list.size();i++)
	{
		double longtitude = Double.parseDouble(list.get(i).getLongtitude())+0.0045;
		list.get(i).setLongtitude(Double.toString(longtitude));
		double latitude = Double.parseDouble(list.get(i).getLatitude())-0.002;
		list.get(i).setLatitude(Double.toString(latitude));
	}
}
public List<List<Taxi>> GetFragment(List<String>list) {
	List<List<Taxi>> result= new ArrayList<>();
	for(int i=0;i<list.size();i++)
	{
		DataMining.GetData getData = new DataMining.GetData();
		List<Taxi> array = getData.GetById(list.get(i));
		int trajectoryTotalSize = array.size();
		int fragmentSize = 0;
		List<Taxi> fragment = null;
		for(int j=0;j<trajectoryTotalSize;j++)
		{
			Taxi currentTaxi = array.get(j);
			double cos=1;
			if(j > 0 && j < trajectoryTotalSize - 1){
			double xA = Double.parseDouble(currentTaxi.getLatitude())-Double.parseDouble(array.get(j-1).getLatitude());
			double yA = Double.parseDouble(currentTaxi.getLongtitude())-Double.parseDouble(array.get(j-1).getLongtitude());
			double xB = Double.parseDouble(array.get(j+1).getLatitude())-Double.parseDouble(currentTaxi.getLatitude());
			double yB = Double.parseDouble(array.get(j+1).getLatitude())-Double.parseDouble(currentTaxi.getLatitude());
			cos = (xA*xB+yA*yB)/(Math.sqrt(xA*xA+yA*yA)*Math.sqrt(xB*xB+yB*yB));
			}
			//ignore no-load point
			if (currentTaxi.getState().equals("0"))
				continue;
			//start a new fragment
			else if ((j == 0 && currentTaxi.getState().equals("1"))
					|| (j > 0 && currentTaxi.getState().equals("1") && array.get(j - 1).getState().equals("0"))) {
				if (fragment!=null&&fragmentSize != 0&&fragment.size()>2)
				{
					transform(fragment);
					result.add(fragment);
				}
					
				fragment = new ArrayList<>();
				fragment.add(currentTaxi);
				fragmentSize++;
				continue;
			}
			//�ҵ�������
			else if (j > 0 && j < trajectoryTotalSize - 1 && currentTaxi.getState().equals("1")
					&& array.get(j - 1).getState().equals("1") && array.get(j + 1).getState().equals("1")
					&& cos <= 0) {
				fragment.add(currentTaxi);
				if(fragment!=null&&fragment.size()>2) result.add(fragment);
				fragment = new ArrayList<>();
				fragmentSize++;
				fragment.add(currentTaxi);
				continue;
			}
			else if (currentTaxi.getState().equals("1")) fragment.add(currentTaxi);
		}
		//add the last fragment
		if(fragment!=null&&fragment.size()>2) {
			transform(fragment);
			result.add(fragment);
		}
	}
	return result;
}

public double GetDirectionSensitiveHausdorffDistance(List<Taxi> TA, List<Taxi> TB){
	double scoreAB=0, scoreBA=0, closestDistanceSumAToB = 0, closestDistanceSumBToA = 0;
	int numOfTA = TA.size();
	int TASegmentSize = numOfTA-1;
	int numOfTB = TB.size();
	int TBSegmentSize = numOfTB-1;
	int closestIndex = 0;
	//each point in A to TB
	for(int i = 0;i<numOfTA;i++){
		double closestDistance = Double.MAX_VALUE;
		Taxi taxi = TA.get(i);
		for(int j = closestIndex;j<TBSegmentSize;j++){
			double x1 = Double.parseDouble(TB.get(j).getLatitude());
			double y1 = Double.parseDouble(TB.get(j).getLongtitude());
			double x2 = Double.parseDouble(TB.get(j+1).getLatitude());
			double y2 = Double.parseDouble(TB.get(j+1).getLongtitude());
			double x0 = Double.parseDouble(taxi.getLatitude());
			double y0 = Double.parseDouble(taxi.getLongtitude());
			double tempDistance = PointToLine(x1,y1,x2,y2,x0,y0);
			if(tempDistance<closestDistance){
				closestIndex = j;
				closestDistance = tempDistance;
			}
		}
		closestDistanceSumAToB = closestDistanceSumAToB + closestDistance;
	}
	scoreAB = closestDistanceSumAToB/numOfTA;
	
	//each point in B to TA
	closestIndex = 0;
	for(int i = 0;i<numOfTB;i++){
		double closestDistance = Double.MAX_VALUE;
		Taxi taxi = TB.get(i);
		for(int j = closestIndex;j<TASegmentSize;j++){
			double x1 = Double.parseDouble(TA.get(j).getLatitude());
			double y1 = Double.parseDouble(TA.get(j).getLongtitude());
			double x2 = Double.parseDouble(TA.get(j+1).getLatitude());
			double y2 = Double.parseDouble(TA.get(j+1).getLongtitude());
			double x0 = Double.parseDouble(taxi.getLatitude());
			double y0 = Double.parseDouble(taxi.getLongtitude());
			double tempDistance = PointToLine(x1,y1,x2,y2,x0,y0);
			if(tempDistance<closestDistance){
				closestIndex = j;
				closestDistance = tempDistance;
			}
		}
		closestDistanceSumBToA = closestDistanceSumBToA + closestDistance;
	}
	scoreBA = closestDistanceSumBToA/numOfTB;
	
	return (scoreAB+scoreBA)/2;
}
public static double GetDistance(double long1, double lat1, double long2, double lat2) {
	double a, b, R;
	R = 6378.137; // ����뾶km
	lat1 = lat1 * Math.PI / 180.0;
	lat2 = lat2 * Math.PI / 180.0;
	a = lat1 - lat2;
	b = (long1 - long2) * Math.PI / 180.0;
	double d;
	double sa2, sb2;
	sa2 = Math.sin(a / 2.0);
	sb2 = Math.sin(b / 2.0);
	d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
	return d;
}
private double PointToLine(double x1, double y1, double x2, double y2, double x0, double y0){
	  double space = 0;
    double a, b, c;
    a = GetDistance(x1, y1, x2, y2);// �߶εĳ���
    b = GetDistance(x1, y1, x0, y0);// (x1,y1)����ľ���
    c = GetDistance(x2, y2, x0, y0);// (x2,y2)����ľ���
    if (c <= 0.000001 || b <= 0.000001) {
       space = 0;
       return space;
    }
    if (a <= 0.000001) {
       space = b;
       return space;
    }
    if (c * c >= a * a + b * b) {
       space = b;
       return space;
    }
    if (b * b >= a * a + c * c) {
       space = c;
       return space;
    }
    double p = (a + b + c) / 2;// ���ܳ�
    double s = Math.sqrt(p * (p - a) * (p - b) * (p - c));// ���׹�ʽ�����
    space = 2 * s / a;// ���ص㵽�ߵľ��루���������������ʽ��ߣ�
    return space;
}
public HashMap<Integer,ArrayList<Double>> GetSimilarity_data(List<List<Taxi>> trajectory) {
	HashMap<Integer,ArrayList<Double>> similarity = new HashMap<Integer,ArrayList<Double>>();//key��Ӧ���ǵڼ����켣��value��Ӧ���Ǵ�ź�ÿ���߶ε����ƶȵ�list
	int size = trajectory.size();//һ�������켣
	for(int i=0;i<size;i++){
		ArrayList<Double> list = new ArrayList<Double>();//ÿ���켣��Ӧ�����ƶ�List
		for (int j = 0; j < size; j++) {
			double temp_similarity =  GetDirectionSensitiveHausdorffDistance(trajectory.get(i), trajectory.get(j));
			 list.add(temp_similarity);
		}
		similarity.put(i, list);
	}
	return similarity;
}

public HashMap<Integer, ArrayList<Integer>>  TRACLUS_data(List<List<Taxi>> list, HashMap<Integer,ArrayList<Double>> similarity){

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
	 System.out.println("��"+i+"���켣");
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
Date d = new Date();
System.out.println(d);
return ClusterAll;
}
public ArrayList<Integer> EPS_NUM(int numTra,HashMap<Integer,ArrayList<Double>> similarity,double eps){
	ArrayList<Integer> epsList = new ArrayList<Integer>();
	for(int i=0;i<similarity.get(numTra).size();i++){
		double sim = similarity.get(numTra).get(i);
		if(sim <= eps){
			epsList.add(i);//������켣���кż�¼����
		}
	}
	return epsList ;// ����켣��
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
public void writeObject(HashMap<Integer,ArrayList<Integer>>map){
    try {
        List<HashMap<Integer, ArrayList<Integer>>> list=new ArrayList<HashMap<Integer, ArrayList<Integer>>>();
        list.add(map);
        
        FileOutputStream outStream = new FileOutputStream("G:/data/1.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                outStream);

        objectOutputStream.writeObject(list);
        outStream.close();
        System.out.println("successful");
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void readObject(){
    FileInputStream freader;
    try {
        freader = new FileInputStream("G:/data/1.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(freader);
        HashMap<String,Object> map = new HashMap<String,Object>();
        
        List<Map<String, Object>> list=(List<Map<String, Object>>)objectInputStream.readObject();
        for (Map<String, Object> map2 : list) {
            System.out.println(map2.toString());
        }
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
public List<Taxi> washTrajectory(List<Taxi> taxi){
	//��ԭʼ���ݽ�����ϴ��Ȼ�󷵻���ϴ���ֵ
	List<Taxi> result = new ArrayList<Taxi>();
	if(taxi.size()>2){
		return null;
	}
	for(int i=0;i<taxi.size();i++){
	    //�Թ켣�����ÿһ��������жϣ����
		return result;
	}
	return result;
}
public static void main(String args[]) throws ParseException{
	List<List<Taxi>> list_tra = new ArrayList<List<Taxi>>();
	DataMining.GetData getData = new DataMining.GetData();
	List<Taxi> taxi = getData.GetById("81421");//���⳵�ƺ�Ϊ87553�ĳ��⳵��10��12�ŵĹ켣
	DataMining.DataMining datamining = new DataMining.DataMining();
	ArrayList<Integer> breakPoint = datamining.Compression(taxi,taxi.size());//�켣ѹ��
	System.out.println(breakPoint.size());
	//����partition_trajectory����
//	List<String> id = getData.AllTaxiId();
//	list_tra = datamining.GetFragment(id);
	//����traclus��������impl���棩
//	System.out.println(list_tra.size());
	//����representation��������Impl���棩
//	HashMap<Integer, ArrayList<Integer>> map = datamining.TRACLUS_data(list_tra, datamining.GetSimilarity_data(list_tra));
	//д���ļ�
//	datamining.writeObject(map);
//	System.out.println(map.size());
	//�����ǰʱ��
//	Date date = new Date();
//	System.out.println(date);
}
}
