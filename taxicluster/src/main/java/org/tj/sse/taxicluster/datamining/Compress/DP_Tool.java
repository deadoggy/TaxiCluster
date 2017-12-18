package org.tj.sse.taxicluster.datamining.Compress;

import DataMining.GetData;
import com.cn.entity.Taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DP_Tool {  
    private String filePath;  
    private double threshold;  
    private ArrayList<Point> pointList;  
    private ArrayList<Double>thresholdList;
    private ArrayList<String[]> listTemp;//��Ŵ��ļ��ж��������  
    private ArrayList<Line> lineList = new ArrayList<Line>();  
    private ArrayList<Line> resultLine = new ArrayList<Line>();//�洢һ������ѹ����ɺ�Ľ��  
    private ArrayList<ArrayList<Line>> compressionCollection = new ArrayList<ArrayList<Line>>();//�洢���б�ѹ����ɵ��߶�;  
    private ArrayList<Point> allPoints = new ArrayList<Point>();//����ԭʼ�켣����������Ѿ�ѹ���õĵ�
    public DP_Tool(List<List<Taxi>> trajectory,double threshold) throws ParseException{  
      //  this.filePath = filePath;  
        this.thresholdList = getThreshold(trajectory);  
       // readDataFile();  
    	this.threshold = threshold;
    	initPointAndLine(trajectory);  
    }  
    private void readDataFile() {  
        File file = new File(filePath);  
         listTemp = new ArrayList<String[]>();  
        try{  
            BufferedReader in = new BufferedReader(new FileReader(file));  
            String str;  
            String[] strTemp;  
            while((str=in.readLine())!=null){  
                strTemp = str.split(" ");  
                listTemp.add(strTemp);  
            }  
            in.close();  
        }catch(IOException e){  
            e.printStackTrace();  
        }  
    }  
    private void initPointAndLine(List<List<Taxi>>trajectory){  
           Line line;  
        for(int i=0;i<trajectory.size();i++){   
             pointList = new ArrayList<Point>();  
            Point point;  
          List<Taxi> temp_tracks = trajectory.get(i);//line
           // String[] str = listTemp.get(i);  
            for(int j=0;j<temp_tracks.size();j++){  
            	Taxi temp = temp_tracks.get(j);//point
                point = new Point(temp.getLatitude(),temp.getLongtitude(),temp.getGPS_Id(),temp.getAngle(),temp.getSpeed(),temp.getTime(),temp.getState(),temp.getTaxi_Id());  
                pointList.add(point);  
            }  
            line = new Line(pointList);  
            lineList.add(line);  
        }  
    }  
    public void compressLine(Line line){  
        Line lineTemp;  
       // threshold = thresholdList.get(col);
        if(line.getDistance()<threshold){  
            //˵���������������߶�����������  
            lineTemp = new Line(line.getStart(),line.getEnd());  
            resultLine.add(lineTemp);  
        }  
        else{  
            ArrayList<Point> pointsTemp1 = new ArrayList<Point>();  
            ArrayList<Point> pointsTemp2 = new ArrayList<Point>();  
            for(int i=0;i<=line.getIndex();i++){  
                pointsTemp1.add(line.getLinePoints().get(i));  
            }  
            for(int j=line.getIndex();j<line.getLinePoints().size();j++){  
                pointsTemp2.add(line.getLinePoints().get(j));  
            }  
            compressLine(new Line(pointsTemp1));//�����ν���ѹ�����ֶε�λ�ü�Ϊ�������ĵ�  
            compressLine(new Line(pointsTemp2));  
        }  
        compressionCollection.add(resultLine);  
    }  
    public ArrayList<Line> startCompression(){  
        for(int i=0;i<lineList.size();i++){  
        	ArrayList<Point> pointsCompressed = new ArrayList<Point>();//ÿһ���߶ζ�Ӧһ���㼯
            lineList.get(i).printLine();  
            resultLine.clear();//���︺��������ǰһ������ѹ�����߶�  
            compressLine(lineList.get(i));  
            ArrayList<Line> resultShow = resultLine;
            
            System.out.print("�ڽ�����������߱�ѹ����"+resultLine.size()+"��");  
            //ÿѹ��һ����������ʼ��һ��resultLine���������ӡ����  
            for(Line line:resultLine){  
                line.printLine();  
            }  
            System.out.println();  
            pointsCompressed = getDistinctPoints(resultLine);
            allPoints.addAll(pointsCompressed);
        }  
        return resultLine;
    }  
    public ArrayList<Point> getDistinctPoints(ArrayList<Line> resultLine){
    	ArrayList<Point> temp = new ArrayList<Point>();
    	//ͨ��ԭ������������ļ����߶Σ�������ǵĵ�
    	for(int i=0;i<resultLine.size();i++){
    		//temp.addAll(resultLine.get(i).getLinePoints());
    		ArrayList<Point> temp_points = resultLine.get(i).getLinePoints();
    		for(int j=0;j<1;j++){//�߶ε������յ�
    			Point start = temp_points.get(0);
    			Point end = temp_points.get(1);
    			if(i==resultLine.size()-1){//���ʵ����һ���߶�
    				temp.add(start);
    				temp.add(end);
    			}else{//j==0
    				temp.add(start);
    			}
    		}
    	}
    	//ȥ�ظ�
    	/*for(int i=0;i<temp.size()-1;i++){
    		if(temp.get(i).getGPS_ID().equals(temp.get(i+1).getGPS_ID())){
    			temp.remove(i);
    			i--;
    		}
    	}*/
    	return temp;
    }
    public Date convertTime(String str) throws ParseException{
    	Date date = null;
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	date = sdf.parse(str); 
    	return date;
    }
    public ArrayList<Double> getThreshold(List<List<Taxi>> trajectory) throws ParseException{
    	ArrayList<Double> thresholdList = new ArrayList<Double>();
    	for(int i=0;i<trajectory.size();i++){//����ÿ���켣
    		//�����ֵ
    		List<Taxi> temp = trajectory.get(i);//��i���켣
    		Taxi first = temp.get(0);
    		double x_first = Double.parseDouble(first.getLatitude());
    		double y_first = Double.parseDouble(first.getLongtitude());
    		Date first_time =  convertTime(first.getTime());
    		Taxi last =temp.get(temp.size()-1);
    		double x_last = Double.parseDouble(last.getLatitude());
    		double y_last = Double.parseDouble(last.getLongtitude());
    		Date last_time =  convertTime(last.getTime());
    		Taxi second = temp.get(1);
    		double x_second = Double.parseDouble(second.getLatitude());
    	    double y_second = Double.parseDouble(second.getLongtitude());
    		Date second_time = convertTime(second.getTime());
    		long deta_e =Math.abs(last_time.getTime()-first_time.getTime());
    		long data_i = Math.abs(second_time.getTime()-first_time.getTime());
    		double latiApprox_i = x_second + (data_i/deta_e)*(x_last - x_first);
    		double longtiApprox_i = y_second + (data_i/deta_e)*(y_last - y_first);
    		//�ָ���ֵ
    		double threshold = Math.sqrt((latiApprox_i - x_second)*(latiApprox_i-x_second)+(longtiApprox_i - y_second)*(longtiApprox_i - y_second));
    	    thresholdList.add(threshold);
    	}
    	return thresholdList;
    }
  public ArrayList<Line> distinct(ArrayList<Line> rawLines){
    	//�������߶�����ĵ㼯�е��ظ���ȥ��
	  ArrayList<Point> allPoints = new ArrayList<Point>();//������еĵ�
	   for(int i=0;i<rawLines.size();i++){
		   Line line = rawLines.get(i);
		   ArrayList<Point> points = line.getLinePoints();
		   for(int j=0;j<points.size()-1;j++){
			   if(points.get(j).getGPS_ID().equals(points.get(j+1).getGPS_ID())){
				   points.remove(j);
				   j--;
			   }
			  allPoints.addAll(points);//�����еĵ㶼���뵽�㼯��
		   }
		   line.setLinePoints(points);//��ȥ�ظ���ĵ㼯���¸����߶�
		   rawLines.set(i,line);
	   }
	   //�߶����߶�֮��Ҳ�����Դ���ͬGPS_ID�ĵ�
	   for(int i=0;i<rawLines.size();i++){
		   Line lineNext = rawLines.get(i);
		   ArrayList<Point> pointsNext = lineNext.getLinePoints();
		   for(int j=0;j<pointsNext.size();j++){
			   if(Duplicate(pointsNext.get(j),pointsNext)){
				   pointsNext.remove(j);
				   j--;
			   }
		   }
		   lineNext.setLinePoints(pointsNext);
		   rawLines.set(i,lineNext);
	   }
	  
	  return rawLines;
    }
  public boolean Duplicate(Point point,ArrayList<Point>points){
	  //�ж�GPS_ID��Ӧ�ĵ��Ƿ����ظ����ظ��򷵻�true
	  boolean flag = false;
	  for(int i=0;i<points.size();i++){
		  if(point.getGPS_ID().equals(points.get(i).getGPS_ID())){
			  flag = true;
		  }
	  }
	  return flag;
  }
  public ArrayList<Point> getAllPoint(){
	  return allPoints;
  }
  public int NumOriginalPoints(List<List<Taxi>> all_tracks){
	  int all = 0;
	  for(int i=0;i<all_tracks.size();i++){
		all+=all_tracks.get(i).size();  
	  }
	  return all;
  }
    public static void main(String args[]) throws ParseException,SQLException,IOException,ClassNotFoundException{
    	//��ȡԭʼ��
    	GetData getData = new GetData();
    	ArrayList<String> taxi_id = getData.AllTaxiId();//10��12�����г��⳵id
    	List<List<Taxi>> all_tracks = new ArrayList<List<Taxi>>();
    	//for(int i=0;i<taxi_id.size();i++){
    	for(int i=0;i<taxi_id.size();i++){
    		List<Taxi> temp = getData.GetById(taxi_id.get(i));
    		all_tracks.add(temp);
    	}
    	List<List<Taxi>>test = new ArrayList<List<Taxi>>();
    	List<Taxi> temp_test = new ArrayList<Taxi>();
    	Taxi start = new Taxi();
    	start.setLatitude("31.25");
    	start.setLongtitude("121.4475");
    	temp_test.add(start);
    	Taxi end = new Taxi();
    	end.setLatitude("31.2905");
    	end.setLongtitude("121.4466");
    	temp_test.add(end);
    	test.add(temp_test);
    	//�ڻ�ȡ���еĹ켣֮��������й켣����ֵ
    	//DP_Tool tool = new DP_Tool(all_tracks,0.001);//filename �� threshold
    	DP_Tool tool = new DP_Tool(all_tracks,0.001);
    	System.out.println(tool.NumOriginalPoints(all_tracks));
    	ArrayList<Line> list =tool.startCompression();
        getData.DbStoreHelper();
     	getData.AddIntoDB(tool.getAllPoint());//��������ȥ�ظ�����뵽���ݿ�������֮���չʾ
    }
}  