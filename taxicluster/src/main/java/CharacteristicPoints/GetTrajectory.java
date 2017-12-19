package CharacteristicPoints;

import DataMining.GetData;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class GetTrajectory {
      /* public List<List<Point>> getTracksAll(){
    	   GetData gt = new GetData();
    	   List<List<Point>> list_point = new ArrayList<List<Point>>();
    	  List<List<Taxi>> list = gt.getPointForCharacter();//�õ�100���켣
    	  //Ϊ�����ã���taxi������ת��Ϊpoint
    	  
       }*/
	public ArrayList<Point> getTracks(String taxi_id){
		GetData gt = new GetData();
		return gt.getTracks(taxi_id);
	}
     public static void main(String args[]){
    	 List<Integer> list = new ArrayList<Integer>();
    	 for(int i=0;i<100;i++){
    		 int temp = i;
    		 list.add(temp);
    	 }
    	 for(int i=0;i<100;i++){
    		 list.add(i);
    	 }
    	 TreeSet<String> oldIds = new TreeSet<String>();
         if(list !=null){// ���ݿ��Ѿ����ڵ�����
             for (int i = 0; i < list.size(); i++){
                  oldIds.add(list.get(i).toString());
             }
         }
        // Collections.sort(oldIds);
         System.out.println(oldIds.size());
     }
	
}
