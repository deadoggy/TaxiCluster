package DataMining;

import org.tj.sse.taxicluster.entity.CPointCluster;
import org.tj.sse.taxicluster.entity.Taxi;

import java.io.*;
import java.sql.*;
import java.util.*;

public class GetData {
	  private int trajectory_num;
	  private String insert_sql;  
	    private String charset;  
	    private boolean debug;  
	  
	    private String connectStr;  
	    private String username;  
	    private String password;  
    static String sql = null;  
    static Database db1 = null;
    static ResultSet ret = null;  
    public void setTrajectory_num(int trajectory_num){
    	this.trajectory_num = trajectory_num;
    }
    public int getTrajectory_num(){
    	return trajectory_num;
    }
    public ArrayList<String> getHurricane(){
    	ArrayList<String>result = new ArrayList<String>();
    	 /* ��ȡ���� */
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("G:/data/hurricane.txt")),
                                                                         "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                result.add(lineTxt);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }
    	 
    	return result;
    }
    public void DbStoreHelper() {  
        connectStr = "jdbc:mysql://localhost:3306/geotag";  
        // connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
        insert_sql = "INSERT INTO compression_dp(GPS_ID,Taxi_id,Longtitude,Latitude,Speed,Angle,Time,State) VALUES (?,?,?,?,?,?,?,?)";  
       
        debug = true;  
        username = "root";  
         password = "195820";  
         // password = "changeme";  
    }  
    public void DbStoreHelperTraclus() {  
        connectStr = "jdbc:mysql://localhost:3306/geotag";  
        // connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
        insert_sql = "INSERT INTO traclus(idtraclus,latitude,longtitude,trajectory_num) VALUES (?,?,?,?)";  
       
        debug = true;  
        username = "root";  
        password = "195820";  
       // password = "changeme";  
    }  
    public void DbStoreHelperGTC() {  
        connectStr = "jdbc:mysql://localhost:3306/geotag";  
        // connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
        insert_sql = "INSERT INTO compression_GTC(GPS_ID,Taxi_id,Longtitude,Latitude,Speed,Angle,Time,State) VALUES (?,?,?,?,?,?,?,?)";  
       
        debug = true;  
        username = "root";  
        password = "195820";  
      //  password = "changeme";  
    }  
    public void DbStoreHelperDeer() {  
        connectStr = "jdbc:mysql://localhost:3306/geotag";  
        // connectStr += "?useServerPrepStmts=false&rewriteBatchedStatements=true";  
        insert_sql = "INSERT INTO deer1995(iddeer1995,Latitude,Longtitude,trajectory_num) VALUES (?,?,?,?)";  
       
        debug = true;  
        username = "root";  
        password = "195820";  
      //  password = "changeme";  
    }  
   
    public ArrayList<CharacteristicPoints.Point> getTracks(String taxi_id){
    	ArrayList<CharacteristicPoints.Point> Track = new ArrayList<>();
    	GetData gt = new GetData();
    	gt.DbStoreHelper();
    	List<Taxi>track_Taxi = gt.GetById(taxi_id);
    	for(int i=0;i<track_Taxi.size();i++){
    		Taxi taxiPoint = track_Taxi.get(i);
            String x = taxiPoint.getLatitude();
            String y = taxiPoint.getLongtitude();
            String gpsid= taxiPoint.getGPS_Id();
            String taxiid = taxiPoint.getTaxi_Id();
            String speed = taxiPoint.getSpeed();
            String state = taxiPoint.getState();
            String time_taxi = taxiPoint.getTime();
            String angle = taxiPoint.getAngle();
    		CharacteristicPoints.Point temp= new CharacteristicPoints.Point(x,y,gpsid,taxiid,speed,state,angle,time_taxi);
    	    Track.add(temp);
    	}
    	
    	return Track;
    }
    public List<Taxi> ReadFile(int track_col,String taxi_id){
    	//��ȡԭʼ�켣
    //	List<List<Taxi>> allTracks = getAllTracks_Original();
    //	List<Taxi> trajectory = allTracks.get(track_col);
    	//��ȡ�ļ����������ݿ�
    	List<Taxi> tracks = new ArrayList<Taxi>();
    	//String taxi_id = trajectory.get(0).getTaxi_Id();
    	 try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
             /* ����TXT�ļ� */  
             String pathname = "G:/data/�켣����/compression/UEF_compressionԴ��/Դ����/GPSTrajCompv0.2/"+track_col+"_dec.txt"; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
             File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
             InputStreamReader reader = new InputStreamReader(  
                     new FileInputStream(filename)); // ����һ������������reader  
             BufferedReader br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
             String line = "";  
             line = br.readLine();  
             int count = 0;
             while (line != null) {  
               //  line = br.readLine(); // һ�ζ���һ������  
                 if(line.indexOf(" ")>=0){
                 String[] info = line.split("\\s+");
                 Taxi temp = new Taxi();
                 temp.setGPS_Id(track_col+"00"+count);
                 count++;
                 temp.setTaxi_Id(taxi_id);
                 temp.setAngle("-1");
                 temp.setState("-1");
                 temp.setSpeed("-1");
                 temp.setLatitude(info[0]);
                 temp.setLongtitude(info[1]);
                 temp.setTime(info[2]+" "+info[3]);
                 tracks.add(temp);
                 line = br.readLine();  
                 }
             }
    	
    }catch (Exception e) {  
        e.printStackTrace();  
    }  
    	 return tracks;
    }
    public void insert(ArrayList<String> list)throws ClassNotFoundException, SQLException, IOException {
       Class.forName("com.mysql.jdbc.Driver");
       Connection conn = DriverManager.getConnection(connectStr, username,password); 
       conn.setAutoCommit(false); // �����ֶ��ύ  
       PreparedStatement psts = conn.prepareStatement(insert_sql);  
    	for(int i=0;i<list.size();i++){
    		String temp = list.get(i);
    		String[] all = temp.split("\\s+");
    		float latitude = Float.valueOf(all[0]);
    		float longtitude = Float.valueOf(all[1]);
    		int id = Integer.valueOf(all[2].substring(0, all[2].length()-1));
    	//��huccicane���ݲ������ݿ�
    	    psts.setFloat(1,latitude);  
            psts.setFloat(2, longtitude);  
            psts.setInt(3, id);  
            psts.addBatch();      
    }
    	psts.executeBatch(); // ִ����������  
    	 conn.commit();  // �ύ  
    	 conn.close();  
    }
    public void modifyMode(){
    	//
    	 sql = "SET SQL_SAFE_UPDATES = 0;";//SQL���  
         db1 = new Database(sql);//����DBHelper����
         try {  
             ret = db1.pst.executeQuery();//ִ����䣬�õ������
             ret.close();  
             db1.close();//�ر�����  
         } catch (SQLException e) {  
             e.printStackTrace();  
         }
    }
  public void Truncate(){
   	 sql = "truncate table geotag.compression_dp;";//SQL���  
        db1 = new Database(sql);//����DBHelper����
        try {  
            ret = db1.pst.executeQuery();//ִ����䣬�õ������
            ret.close();  
            db1.close();//�ر�����  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
  }
    public ArrayList<String> AllTaxiId(){
    	ArrayList<String> all_taxiId = new ArrayList<String>();
    	 sql = "select distinct(taxi_id) from taxi";//SQL���  
         db1 = new Database(sql);//����DBHelper����
         
         try {  
             ret = db1.pst.executeQuery();//ִ����䣬�õ������
             while (ret.next()) { 
                 String Taxi_id = ret.getString(1);  
               //  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
                all_taxiId.add(Taxi_id);
             }
           
             //��ʾ����  
             ret.close();  
             db1.close();//�ر�����  
         } catch (SQLException e) {  
             e.printStackTrace();  
         }
    	return all_taxiId;
    }
    public void AddIntoDB(ArrayList<Compress.Point>allPoints)throws ClassNotFoundException,SQLException,IOException{
    	//��ѹ����ĵ�������ݿ�����֮���չʾ
    	
    	 Class.forName("com.mysql.jdbc.Driver");
			  Connection conn =DriverManager.getConnection(connectStr, username,password);
			conn.setAutoCommit(false);
			String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
			String sql2="truncate table geotag.compression_dp;";
			Statement stmt = conn.createStatement();
			stmt.addBatch(sql1);
			stmt.addBatch(sql2);
			stmt.executeBatch();
			 PreparedStatement psts = conn.prepareStatement(insert_sql); 
			
      	for(int i=0;i<allPoints.size();i++){
      		Compress.Point tempPoint = allPoints.get(i);
      		String latitude = String.valueOf(tempPoint.getX());
      		String longtitude = String.valueOf(tempPoint.getY());
            String gps_id = tempPoint.getGPS_ID();
            String taxi_id = tempPoint.getTaxi_id();
            String angle = tempPoint.getAngle();
            String speed = tempPoint.getSpeed();
            String state = tempPoint.getState();
            String time = tempPoint.getTime();
      		//����õ�taxi�����ݲ������ݿ�
      	    psts.setString(1,gps_id);  
              psts.setString(2, taxi_id);  
              psts.setString(3, longtitude);  
              psts.setString(4, latitude);  
              psts.setString(5, speed);  
              psts.setString(6,angle);  
              psts.setString(7, time);  
              psts.setString(8, state);  
              psts.addBatch();      
      	}
      	psts.executeBatch(); // ִ����������  
      	 conn.commit();  // �ύ  
      	 conn.close();  
		
    }
    public List<Taxi> GetById(String taxi_id) {  
        sql = "select * from taxi  where taxi_id ='"+taxi_id+"' order by time";//SQL���  
        db1 = new Database(sql);//����DBHelper����
        
        List<Taxi> taxi = new ArrayList<Taxi>();
        try {  
            ret = db1.pst.executeQuery();//ִ����䣬�õ������
            while (ret.next()) { 
            	Taxi temp = new Taxi();
                String GPS_id = ret.getString(1);  
                String Taxi_id = ret.getString(2);  
                String Longitude = ret.getString(3);  
                String Latitude= ret.getString(4);  
                String Speed = ret.getString(5);  
                String Angle = ret.getString(6);  
                String Time  = ret.getString(7);  
                String State = ret.getString(8);  
              //  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
                temp.setGPS_Id(GPS_id);
                temp.setTaxi_Id(Taxi_id);
                temp.setLongtitude(Longitude);
                temp.setLatitude(Latitude);
                temp.setSpeed(Speed);
                temp.setAngle(Angle);
                temp.setTime(Time);
                temp.setState(State);
                taxi.add(temp);
            }
          
            //��ʾ����  
            ret.close();  
            db1.close();//�ر�����  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
        return taxi;
    }  
 
    public List<Taxi> getCompressed(String id){
    	//�����ݿ���ȡ��Ӧid�����е㲢����ʱ���������list����
    	  sql = "select * from compression_dp  where taxi_id ='"+id+"'";//SQL���  
          db1 = new Database(sql);//����DBHelper����
          
          List<Taxi> taxi = new ArrayList<Taxi>();
          try {  
              ret = db1.pst.executeQuery();//ִ����䣬�õ������
              while (ret.next()) { 
              	Taxi temp = new Taxi();
                  String GPS_id = ret.getString(1);  
                  String Taxi_id = ret.getString(2);  
                  String Longitude = ret.getString(3);  
                  String Latitude= ret.getString(4);  
                  String Speed = ret.getString(5);  
                  String Angle = ret.getString(6);  
                  String Time  = ret.getString(7);  
                  String State = ret.getString(8);  
                //  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
                  temp.setGPS_Id(GPS_id);
                  temp.setTaxi_Id(Taxi_id);
                  temp.setLongtitude(Longitude);
                  temp.setLatitude(Latitude);
                  temp.setSpeed(Speed);
                  temp.setAngle(Angle);
                  temp.setTime(Time);
                  temp.setState(State);
                  taxi.add(temp);
              }
            
              //��ʾ����  
              ret.close();  
              db1.close();//�ر�����  
          } catch (SQLException e) {  
              e.printStackTrace();  
          }
          return taxi;
    }
public List<Taxi> getCompressedGTC(String id){
		//�����ݿ���ȡ��Ӧid�����е㲢����ʱ���������list����
		  sql = "select * from compression_gtc  where taxi_id ='"+id+"'";//SQL���  
	      db1 = new Database(sql);//����DBHelper����
	      
	      List<Taxi> taxi = new ArrayList<Taxi>();
	      try {  
	          ret = db1.pst.executeQuery();//ִ����䣬�õ������
	          while (ret.next()) { 
	          	Taxi temp = new Taxi();
	              String GPS_id = ret.getString(1);  
	              String Taxi_id = ret.getString(2);  
	              String Longitude = ret.getString(3);  
	              String Latitude= ret.getString(4);  
	              String Speed = ret.getString(5);  
	              String Angle = ret.getString(6);  
	              String Time  = ret.getString(7);  
	              String State = ret.getString(8);  
	            //  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
	              temp.setGPS_Id(GPS_id);
	              temp.setTaxi_Id(Taxi_id);
	              temp.setLongtitude(Longitude);
	              temp.setLatitude(Latitude);
	              temp.setSpeed(Speed);
	              temp.setAngle(Angle);
	              temp.setTime(Time);
	              temp.setState(State);
	              taxi.add(temp);
	          }
	        
	          //��ʾ����  
	          ret.close();  
	          db1.close();//�ر�����  
	      } catch (SQLException e) {  
	          e.printStackTrace();  
	      }
	      return taxi;
	}
public List<List<Taxi>> getFragment(List<String> list){//��ȡѹ��������compression_dp�����ݲ���List<List<Taxi>>����ʽ����
	List<List<Taxi>> result = new ArrayList<List<Taxi>>();
    for(int i=0;i<list.size();i++){
    	List<Taxi> array = getCompressed(list.get(i));
    	result.add(array);
    }
	return result;
}
public List<List<Taxi>> getFragmentGTC(List<String> list){//��ȡѹ��������compression_dp�����ݲ���List<List<Taxi>>����ʽ����
	List<List<Taxi>> result = new ArrayList<List<Taxi>>();
    for(int i=0;i<list.size();i++){
    	List<Taxi> array = getCompressedGTC(list.get(i));
    	result.add(array);
    }
	return result;
}
public List<List<Taxi>> getAllTracks_Original(){
	List<List<Taxi>> allTracks = new ArrayList<List<Taxi>>();
	ArrayList<String> id_list = AllTaxiId();//���еĵ�taxi_id
	for(int i=0;i<id_list.size();i++){
		List<Taxi> array = GetById(id_list.get(i));
		allTracks.add(array);
	}
	return allTracks;
}

public void writeFile() throws IOException{
	//���ǵ�MOPSI��GTC�㷨��Ҫÿ���켣��Ϊһ���ļ������м��㣬���Դ洢��ʱ��ÿ���켣��Ϊһ��txt
	//д���ʽΪlat lon yyyy-mm-dd HH:mm:ss
	List<List<Taxi>>allTracks = getAllTracks_Original();//���г��⳵id��ԭʼ�켣
	ArrayList<String> allList = AllTaxiId();//���г��⳵id��10��12��
	  for (int i = 0; i < allTracks.size(); i++) {
	  List<Taxi> temp = allTracks.get(i);
	  FileWriter fileWriter=new FileWriter("G:/data/�켣����/compression/UEF_compressionԴ��/myData/"+(i+1)+".txt");
	  for(int j=0;j<temp.size();j++){
		  Taxi point = temp.get(j);
		  String lat = point.getLatitude();
		  String lon = point.getLongtitude();
		  String taxi_id = allList.get(i);
		  String[] time = point.getTime().split("\\s+");//������  ʱ����
		  
		  String str = lat +" " + lon + " " +time[0].trim()+ " " + time[1].trim();
	       fileWriter.write(str+" \r\n");
		}
		fileWriter.flush();
		fileWriter.close();
	  }

}
public void insertCompressedGTC(List<Taxi>trajectory) throws ClassNotFoundException,SQLException,IOException{
	 Class.forName("com.mysql.jdbc.Driver");
	  Connection conn =DriverManager.getConnection(connectStr, username,password);
	conn.setAutoCommit(false);
	String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
	//String sql2="truncate table geotag.compression_GTC;";
	Statement stmt = conn.createStatement();
	stmt.addBatch(sql1);
//	stmt.addBatch(sql2);
	stmt.executeBatch();
	 PreparedStatement psts = conn.prepareStatement(insert_sql); 
	
	for(int i=0;i<trajectory.size();i++){
		Taxi tempPoint = trajectory.get(i);
		String latitude = tempPoint.getLatitude();
		String longtitude = tempPoint.getLongtitude();
   String gps_id = tempPoint.getGPS_Id();
   String taxi_id = tempPoint.getTaxi_Id();
   String angle = tempPoint.getAngle();
   String speed = tempPoint.getSpeed();
   String state = tempPoint.getState();
   String time = tempPoint.getTime();
		//����õ�taxi�����ݲ������ݿ�
	    psts.setString(1,gps_id);  
     psts.setString(2, taxi_id);  
     psts.setString(3, longtitude);  
     psts.setString(4, latitude);  
     psts.setString(5, speed);  
     psts.setString(6,angle);  
     psts.setString(7, time);  
     psts.setString(8, state);  
     psts.addBatch();      
	}
	psts.executeBatch(); // ִ����������  
	 conn.commit();  // �ύ  
	 conn.close();  

}
  @SuppressWarnings("unchecked")
public static void main(String args[]) throws IOException, ClassNotFoundException, SQLException{
	/*  GetData gt = new GetData();
	  gt.DbStoreHelper();
	  try {
		ArrayList<String> hurricane = gt.getHurricane();
		gt.insert(hurricane);
	} catch (ClassNotFoundException | SQLException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	  //��ȡhurricane���ݲ���֮����Mysql���ݿ�֮�����MDL������㣬TRACLUS��representative����
	  GetData gt = new GetData();
	  gt.DbStoreHelper();
	 Set set= gt.getAllTaxiId();
	 gt.insert100TaxiData(set);
	//  gt.writeFile();
  }
private Set getAllTaxiId() {
	// TODO Auto-generated method stub
	List<String> array = new ArrayList<String>();
	 sql = "select * from taxi";//SQL���  
    db1 = new Database(sql);//����DBHelper����
    Set<String> result = new HashSet(); 
    try {  
        ret = db1.pst.executeQuery();//ִ����䣬�õ������
        while (ret.next()) { 
            String Taxi_id = ret.getString(2);  
            System.out.println(Taxi_id );  
            array.add(Taxi_id);
        }
    	for(int i=0;i<array.size();i++){
    		result.add(array.get(i));
    	}
       System.out.println("size of id :"+result.size());
      
        //��ʾ����  
        ret.close();  
        db1.close();//�ر�����  
    } catch (SQLException e) {  
        e.printStackTrace();  
    }
	return result;
}
public void insert100TaxiData(Set<String> idList) throws ClassNotFoundException, IOException, SQLException{
	Iterator it = idList.iterator();
	int count=0;
	List<List<Taxi>> list = new ArrayList<List<Taxi>>();
	while(it.hasNext()){
		if(count<100){
			String id = String.valueOf(it.next());
            List<Taxi> temp =  GetById(id);
            list.add(temp);
			count++;
		}else{
			break;
		}
	}
	System.out.println("size of 100 list is:"+list.size());
	//store
	  AddIntoDb_100Taxi(list);
}
public void AddIntoDb_100Taxi(List<List<Taxi>>data) throws IOException,ClassNotFoundException,SQLException{
	 
	 Class.forName("com.mysql.jdbc.Driver");
	  Connection conn =DriverManager.getConnection(connectStr, username,password);
	conn.setAutoCommit(false);
	String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
	String sql2="truncate table geotag.taxi100top;";
	Statement stmt = conn.createStatement();
	//stmt.addBatch(sql1);
	//stmt.addBatch(sql2);
	//stmt.executeBatch();
	String insert = "INSERT INTO taxi100top(GPS_ID,Taxi_id,Longtitude,Latitude,Speed,Angle,Time,State) VALUES (?,?,?,?,?,?,?,?)";  
	 PreparedStatement psts = conn.prepareStatement(insert); 
	for(int k=0;k<data.size();k++){
		List<Taxi> points = data.get(k);
	for(int i=0;i<points.size();i++){
		 Taxi temp= points.get(i);
		String latitude = temp.getLatitude();
		String longtitude = temp.getLongtitude();
     String gps_id = temp.getGPS_Id();
   String taxi_id = temp.getTaxi_Id();
   String angle = temp.getAngle();
   String speed = temp.getSpeed();
   String state = temp.getState();
   String time = temp.getTime();
		//����õ�taxi�����ݲ������ݿ�
	    psts.setString(1,gps_id);  
     psts.setString(2, taxi_id);  
     psts.setString(3, longtitude);  
     psts.setString(4, latitude);  
     psts.setString(5, speed);  
     psts.setString(6,angle);  
     psts.setString(7, time);  
     psts.setString(8, state);  
     psts.addBatch();      
	}
}
	psts.executeBatch(); // ִ����������  
	 conn.commit();  // �ύ  
	 conn.close();  
}
public void AddIntoDB_deer(ArrayList<Trajectory> rTrajectory) throws SQLException,ClassNotFoundException {
	// TODO Auto-generated method stub
     int count = 1;
	 Class.forName("com.mysql.jdbc.Driver");
		  Connection conn =DriverManager.getConnection(connectStr, username,password);
		conn.setAutoCommit(false);
		String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
		String sql2="truncate table geotag.deer1995;";
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql1);
		stmt.addBatch(sql2);
		stmt.executeBatch();
		 PreparedStatement psts = conn.prepareStatement(insert_sql); 
		for(int k=0;k<rTrajectory.size();k++){
			List<Point> allPoints = rTrajectory.get(k).getPoints();
 	      for(int i=0;i<allPoints.size();i++){
 		    Point tempPoint = allPoints.get(i);
 		    String latitude = String.valueOf(tempPoint.getX()/10);
 		    String longtitude = String.valueOf(tempPoint.getY()/10);
    
 		//����õ�taxi�����ݲ������ݿ�
 	        psts.setInt(1,count++);  
            psts.setString(2, latitude);  
            psts.setString(3, longtitude);  
            psts.setInt(4, k);  
       
            psts.addBatch();      
 	}
}
 	psts.executeBatch(); // ִ����������  
 	 conn.commit();  // �ύ  
 	 conn.close();  
}
public List<List<Taxi>> getFragmentdeer(List<String> comparedTaxiIds) {
	// TODO Auto-generated method stub
	List<List<Taxi>> result = new ArrayList<List<Taxi>>();
    for(int i=0;i<trajectory_num;i++){
    	List<Taxi> array = getCompresseddeer(i);
    	result.add(array);
    }
	return result;
}
public List<List<Taxi>> getFragmentTraclus(List<String> comparedTaxiIds) {
	// TODO Auto-generated method stub
	List<List<Taxi>> result = new ArrayList<List<Taxi>>();
    for(int i=0;i<3;i++){
    	List<Taxi> array = getCompressedTraclus(i);
    	result.add(array);
    }
	return result;
}
private List<Taxi> getCompressedTraclus(int i) {
	 sql = "select * from traclus  where trajectory_num ='"+i+"'";//SQL���  
	 // sql = "select * from deer1995 ";//SQL���  
     db1 = new Database(sql);//����DBHelper����
     
     List<Taxi> taxi = new ArrayList<Taxi>();
     try {  
         ret = db1.pst.executeQuery();//ִ����䣬�õ������
         while (ret.next()) { 
         	Taxi temp = new Taxi();
             String Latitude = ret.getString(2);  
             String Longtitude= ret.getString(3);  
             temp.setLongtitude(Longtitude);
             temp.setLatitude(Latitude);
             taxi.add(temp);
         }
         //��ʾ����  
         ret.close();  
         db1.close();//�ر�����  
     } catch (SQLException e) {  
         e.printStackTrace();  
     }
     return taxi;
}
public List<Taxi> getCompresseddeer(int id){
	 sql = "select * from deer1995  where trajectory_num ='"+id+"'";//SQL���  
      db1 = new Database(sql);//����DBHelper����
      
      List<Taxi> taxi = new ArrayList<Taxi>();
      try {  
          ret = db1.pst.executeQuery();//ִ����䣬�õ������
          while (ret.next()) { 
          	Taxi temp = new Taxi();
              String Latitude = ret.getString(2);  
              String Longtitude= ret.getString(3);  
              temp.setLongtitude(Longtitude);
              temp.setLatitude(Latitude);
              taxi.add(temp);
          }
        
          //��ʾ����  
          ret.close();  
          db1.close();//�ر�����  
      } catch (SQLException e) {  
          e.printStackTrace();  
      }
      return taxi;
}
public void AddIntoDB_Traclus(ArrayList<Trajectory> rTrajectory) throws ClassNotFoundException,SQLException{
	// TODO Auto-generated method stub
    int count = 1;
	 Class.forName("com.mysql.jdbc.Driver");
		  Connection conn =DriverManager.getConnection(connectStr, username,password);
		conn.setAutoCommit(false);
		String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
		String sql2="truncate table geotag.traclus;";
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql1);
		stmt.addBatch(sql2);
		stmt.executeBatch();
		 PreparedStatement psts = conn.prepareStatement(insert_sql); 
		for(int k=0;k<rTrajectory.size();k++){
			List<Point> allPoints = rTrajectory.get(k).getPoints();
 	      for(int i=0;i<allPoints.size();i++){
 		    Point tempPoint = allPoints.get(i);
 		    String latitude = String.valueOf(tempPoint.getX());
 		    String longtitude = String.valueOf(tempPoint.getY());
    
 		//����õ�taxi�����ݲ������ݿ�
 	        psts.setInt(1,count++);  
            psts.setString(2, latitude);  
            psts.setString(3, longtitude);  
            psts.setInt(4, k);  
       
            psts.addBatch();      
 	}
}
 	psts.executeBatch(); // ִ����������  
 	 conn.commit();  // �ύ  
 	 conn.close();  
}
public List<Taxi> getTraclusCharacters() {
	// ����characteristic��õ�����Ϊtraclus�ĵ�����չʾ
	// sql = "select * from characteristics  where Algor_type = 'traclus' ";//SQL���  
	sql = "select * from taxi  where taxi_id='89457' ";
	db1 = new Database(sql);//����DBHelper����
     
     List<Taxi> taxi = new ArrayList<Taxi>();
     try {  
         ret = db1.pst.executeQuery();//ִ����䣬�õ������
         while (ret.next()) { 
         	Taxi temp = new Taxi();
             String Latitude = ret.getString(2);  
             String Longtitude= ret.getString(3);  
             temp.setLongtitude(Longtitude);
             temp.setLatitude(Latitude);
             taxi.add(temp);
         }
       
         //��ʾ����  
         ret.close();  
         db1.close();//�ر�����  
     } catch (SQLException e) {  
         e.printStackTrace();  
     }
     return taxi;
}
public List<List<Taxi>> getPointForCharacter() {
	// ����taxi����ԭʼ�켣��
	ArrayList<String> idlist = AllTaxiId();
	ArrayList<String> partList = new ArrayList<String>();
	for(int i=0;i<100;i++){
		partList.add(idlist.get(i));//��100��taxi id���뵽partlist����
	}
	List<List<Taxi>> result = new ArrayList<List<Taxi>>();
	int count =0;
	for(int j=0;j<partList.size();j++){
		String id = partList.get(j);
		List<Taxi> listTaxi = GetById(id);
		count +=listTaxi.size();
		result.add(GetById(id));
	}
	System.out.println("100taxis has :"+count+" points");
	return result;
}
public void AddIntoCharactersSGA(ArrayList<CharacteristicPoints.Point> points,String type) throws SQLException,ClassNotFoundException{
		 Class.forName("com.mysql.jdbc.Driver");
		  Connection conn =DriverManager.getConnection(connectStr, username,password);
		conn.setAutoCommit(false);
	/*	String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
		String sql2="truncate table geotag.compression_dp;";
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql1);
		stmt.addBatch(sql2);
		stmt.executeBatch();
		*/
		String sql = "INSERT INTO char_sga(GPS_ID,Taxi_id,Longtitude,Latitude,Speed,Angle,Time,State,Algor_type) VALUES (?,?,?,?,?,?,?,?,?)";  
	       
		 PreparedStatement psts = conn.prepareStatement(sql); 
		
	for(int i=0;i<points.size();i++){
	  CharacteristicPoints.Point temp_point =  points.get(i);
	   //start��
	   String latitudeS = String.valueOf(temp_point.getX());
	   String longtitudeS = String.valueOf(temp_point.getY());
      String gps_idS = temp_point.getGPS_ID();
      String taxi_idS = temp_point.getTaxi_id();
      String angleS = temp_point.getAngle();
      String speedS = temp_point.getSpeed();
      String stateS = temp_point.getState();
      String timeS = temp_point.getTime();
      
		//����õ�taxi�����ݲ������ݿ�
	     psts.setString(1,gps_idS);  
        psts.setString(2, taxi_idS);  
        psts.setString(3, longtitudeS);  
        psts.setString(4, latitudeS);  
        psts.setString(5, speedS);  
        psts.setString(6,angleS);  
        psts.setString(7, timeS);  
        psts.setString(8, stateS);  
        psts.setString(9, type);
        psts.addBatch();      
        //end��
     
	}
	psts.executeBatch(); // ִ����������  
	 conn.commit();  // �ύ  
	 conn.close();  
	
}

public void AddIntoCharactersTraclus(List<Line> lines,String type) throws SQLException,ClassNotFoundException{
	// ��ĳһ���켣��ȡ��������������뵽characteristics���ݱ���
	if(type.equals("traclus")){
		 Class.forName("com.mysql.jdbc.Driver");
		  Connection conn =DriverManager.getConnection(connectStr, username,password);
		conn.setAutoCommit(false);
	/*	String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
		String sql2="truncate table geotag.compression_dp;";
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql1);
		stmt.addBatch(sql2);
		stmt.executeBatch();
		*/
		String sql = "INSERT INTO char_traclus(GPS_ID,Taxi_id,Longtitude,Latitude,Speed,Angle,Time,State,Algor_type) VALUES (?,?,?,?,?,?,?,?,?)";  
	       
		 PreparedStatement psts = conn.prepareStatement(sql); 
		
 	for(int i=0;i<lines.size();i++){
 	   Line temp_line = lines.get(i);
 	  if(i!=lines.size()-1){//��i����ĳ���߶��зֺ�õ��߶μ��ϵ����һ���߶���ֻ��S��
 	   //start��
 	   String latitudeS = String.valueOf(temp_line.getS().getX()/100);
 	   String longtitudeS = String.valueOf(temp_line.getS().getY()/100);
       String gps_idS = temp_line.getS().getGPS_ID();
       String taxi_idS = temp_line.getS().getTaxi_id();
       String angleS = temp_line.getS().getAngle();
       String speedS = temp_line.getS().getSpeed();
       String stateS = temp_line.getS().getState();
       String timeS = temp_line.getS().getTime();
     
 		//����õ�taxi��S�˼��зֺ�켣��������ݲ������ݿ���Ϊ������
 	     psts.setString(1,gps_idS);  
         psts.setString(2, taxi_idS);  
         psts.setString(3, longtitudeS);  
         psts.setString(4, latitudeS);  
         psts.setString(5, speedS);  
         psts.setString(6,angleS);  
         psts.setString(7, timeS);  
         psts.setString(8, stateS);  
         psts.setString(9, type);
         psts.addBatch();  
       }
 	  else{//��iΪ�߶μ������һ���з��߶Σ���������յ�Ҳ���뵽�����㼯��
         //end��
 		   String latitudeS = String.valueOf(temp_line.getS().getX()/100 );
 	 	   String longtitudeS = String.valueOf(temp_line.getS().getY()/100 );
 	       String gps_idS = temp_line.getS().getGPS_ID();
 	       String taxi_idS = temp_line.getS().getTaxi_id();
 	       String angleS = temp_line.getS().getAngle();
 	       String speedS = temp_line.getS().getSpeed();
 	       String stateS = temp_line.getS().getState();
 	       String timeS = temp_line.getS().getTime();
 	     
 	 		//����õ�taxi��S�˼��зֺ�켣��������ݲ������ݿ���Ϊ������
 	 	     psts.setString(1,gps_idS);  
 	         psts.setString(2, taxi_idS);  
 	         psts.setString(3, longtitudeS);  
 	         psts.setString(4, latitudeS);  
 	         psts.setString(5, speedS);  
 	         psts.setString(6,angleS);  
 	         psts.setString(7, timeS);  
 	         psts.setString(8, stateS);  
 	         psts.setString(9, type);
 	         psts.addBatch();  
         String latitudeE = String.valueOf(temp_line.getE().getX()/100 );
   	   String longtitudeE = String.valueOf(temp_line.getE().getY()/100 );
         String gps_idE = temp_line.getE().getGPS_ID();
         String taxi_idE = temp_line.getE().getTaxi_id();
         String angleE = temp_line.getE().getAngle();
         String speedE = temp_line.getE().getSpeed();
         String stateE = temp_line.getE().getState();
         String timeE = temp_line.getE().getTime();
   		//����õ�taxi�����ݲ������ݿ�
   	       psts.setString(1,gps_idE);  
           psts.setString(2, taxi_idE);  
           psts.setString(3, longtitudeE);  
           psts.setString(4, latitudeE);  
           psts.setString(5, speedE);  
           psts.setString(6,angleE);  
           psts.setString(7, timeE);  
           psts.setString(8, stateE);  
           psts.setString(9, type); 
           psts.addBatch();      
 	  }
 	}
 	psts.executeBatch(); // ִ����������  
 	 conn.commit();  // �ύ  
 	 conn.close();  
	}
}
public List<Taxi> getCharacterResult(String type) {
	// �����ȡ�������㣬������traclus��õ������㼯��
	 String sql ;
	if(type.equals("traclus")){
	 sql = "select * from characteristics  where Algor_type ='traclus'";//SQL���  :TRACLUS�õ�������������
	}else{
		 sql = "select *from characteristics  where Algor_type ='SGA'";//SQL���  ��SGA�õ�������������
	}
     db1 = new Database(sql);//����DBHelper����
     
     List<Taxi> taxi = new ArrayList<Taxi>();
     try {  
         ret = db1.pst.executeQuery();//ִ����䣬�õ������
         while (ret.next()) { 
         	Taxi temp = new Taxi();
             String GPS_id = ret.getString(1);  
             String Taxi_id = ret.getString(2);  
             String Longitude = ret.getString(3);  
             String Latitude= ret.getString(4);  
             String Speed = ret.getString(5);  
             String Angle = ret.getString(6);  
             String Time  = ret.getString(7);  
             String State = ret.getString(8);  
           //  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
             temp.setGPS_Id(GPS_id);
             temp.setTaxi_Id(Taxi_id);
             temp.setLongtitude(Longitude);
             temp.setLatitude(Latitude);
             temp.setSpeed(Speed);
             temp.setAngle(Angle);
             temp.setTime(Time);
             temp.setState(State);
             taxi.add(temp);
         }
       System.out.println("taxi___size:"+taxi.size());
         //��ʾ����  
         ret.close();  
         db1.close();//�ر�����  
     } catch (SQLException e) {  
         e.printStackTrace();  
     }
	return taxi;
}
public void AddCluster_rawPoint(List<CPointCluster>cluster,String type, String cp_type) throws SQLException,ClassNotFoundException{
	 Class.forName("com.mysql.jdbc.Driver");
	  Connection conn =DriverManager.getConnection(connectStr, username,password);
	  conn.setAutoCommit(false);
	  String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
		String sql2="truncate table geotag.cp_raw;";//ȫ�����
		Statement stmt = conn.createStatement();
		stmt.addBatch(sql1);
		stmt.addBatch(sql2);
		stmt.executeBatch();
	  insert_sql = "INSERT INTO cp_raw VALUES (?,?,?,?,?,?,?,?,?,?,?)"; 
	  PreparedStatement psts = conn.prepareStatement(insert_sql); 
	 
	
// �����������Ľ���������ݿ�,clusterΪ���༯��
    
	 
      for(int i=0;i<cluster.size();i++){
   	    CPointCluster cp_cluster = cluster.get(i);
   //	String central_lat = String.valueOf(cp_cluster.getCenterLatitude());//����γ��
   //	String central_lon = String.valueOf(cp_cluster.getCenterLongtitude());//����γ��
   	   List<Taxi> list = cp_cluster.getTurningPoints();//����þ����ԭʼ�㼯
       String cluster_id=String.valueOf(i); //ÿ�����༯�϶�Ӧһ��cluster_id
   	   for(int j=0;j<list.size();j++){
   		Taxi temp = list.get(j);
   		String gpsid = temp.getGPS_Id();
   		String taxi_id = temp.getTaxi_Id();
   		String latitude = temp.getLatitude();
   		String longtitude= temp.getLongtitude();
   		String speed = temp.getSpeed();
   		String angle = temp.getAngle();
   		String state = temp.getState();
   		String time = temp.getTime();
   	     psts.setString(1,gpsid);  
         psts.setString(2, taxi_id);  
         psts.setString(3, longtitude);  
         psts.setString(4, latitude);  
         psts.setString(5, speed);
         psts.setString(6, angle);
         psts.setString(7, time);
         psts.setString(8, state);
         psts.setString(9, cluster_id);
         psts.setString(10,type);
         psts.setString(11, cp_type);
         psts.addBatch(); 
   	}
   }
	psts.executeBatch(); // ִ����������  
	 conn.commit();  // �ύ  
	 conn.close();  
 
}
public void AddIntoCluster(List<CPointCluster> cluster,String type,String cp_type) throws SQLException,ClassNotFoundException {
	      Class.forName("com.mysql.jdbc.Driver");
		  Connection conn =DriverManager.getConnection(connectStr, username,password);
		  conn.setAutoCommit(false);
		  String sql1= "SET SQL_SAFE_UPDATES = 0;";//SQL���
			String sql2="truncate table geotag.cluster_result;";//ȫ�����
			Statement stmt = conn.createStatement();
			stmt.addBatch(sql1);
			stmt.addBatch(sql2);
			stmt.executeBatch();
		  insert_sql = "INSERT INTO cluster_result VALUES (?,?,?,?,?)"; //������õ��Ľ���������ݿ�
		  PreparedStatement psts = conn.prepareStatement(insert_sql); 
		 
	// �����������Ľ���������ݿ�,clusterΪ���༯��
	      
		//���෽��ΪKmeans
           for(int i=0;i<cluster.size();i++){
        	CPointCluster cp_cluster = cluster.get(i);
        	String central_lat = String.valueOf(cp_cluster.getCenterLatitude());//����γ��
        	String central_lon = String.valueOf(cp_cluster.getCenterLongtitude());//����γ��
        	List<Taxi> list = cp_cluster.getTurningPoints();//����þ����ԭʼ��
        	String cluster_id=String.valueOf(i); 
        	  psts.setString(1,cluster_id);  
              psts.setString(2, central_lat);  
              psts.setString(3, central_lon);  
              psts.setString(4, type);  
              psts.setString(5, cp_type);//���������Ԫ���ݵ���ȡ���ͣ�traclus����SGA
              psts.addBatch();      
        }
    	psts.executeBatch(); // ִ����������  
    	 conn.commit();  // �ύ  
    	 conn.close();  
	 
	
}
public List<CPointCluster> getCluster(String string, String string2) {
	// ��ȡ�����������ĵ�,����Ϊ��partition_type, clustering_type,k,iterator
	   sql = "select *from cluster_result  where cluster_type='"+string2+"' and cp_type='"+string+"'";//SQL���  
       db1 = new Database(sql);//����DBHelper����
       
       List<CPointCluster> taxi = new ArrayList<CPointCluster>();
       try {  
           ret = db1.pst.executeQuery();//ִ����䣬�õ������
           while (ret.next()) { 
           	   CPointCluster temp = new CPointCluster();
               String cluster_id = ret.getString(1);  
               String center_latitude = ret.getString(2);  
               String center_longtitude = ret.getString(3);  
               String cluster_type= ret.getString(4);  
               String cp_type= ret.getString(5);  
             
             //  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
               temp.setId(Integer.valueOf(cluster_id));
               temp.setCenterLatitude(Double.valueOf(center_latitude));
               temp.setCenterLongtitude(Double.valueOf(center_longtitude));
               temp.setCluster_type(cluster_type);
               temp.setCP_type(cp_type);
             
               taxi.add(temp);
           }
         
           //��ʾ����  
           ret.close();  
           db1.close();//�ر�����  
       } catch (SQLException e) {  
           e.printStackTrace();  
       }
       return taxi;
}
public List<Taxi> getRawData(String type, String cp) throws SQLException,ClassNotFoundException {
	// ��ȡ���ھ����ԭʼ���ݵ�
	System.out.println(type+":"+cp);
    Class.forName("com.mysql.jdbc.Driver");
		  Connection conn =DriverManager.getConnection(connectStr, username,password);
	   String  raw_sql1 = "select *from cp_raw  where cluster_type=? and cp_type=?";//SQL���  
	   PreparedStatement pt= conn.prepareStatement(raw_sql1);  
	   pt.setString(1,type);  
	   pt.setString(2, cp);
	  // db1 = new Database(raw_sql1);//����DBHelper����  
       
       List<Taxi> taxi = new ArrayList<Taxi>();
       try {  
           ret = pt.executeQuery();//ִ����䣬�õ������
           while (ret.next()) { 
           	   Taxi temp = new Taxi();
               String gps_id = ret.getString(1);  
               String taxiid = ret.getString(2);  
               String lon = ret.getString(3);  
               String lat= ret.getString(4);  
               String speed= ret.getString(5);  
               String angle = ret.getString(6);  
               String time = ret.getString(7);  
               String state = ret.getString(8);  
               String cluster_id= ret.getString(9);  
              // String cluster_type= ret.getString(10);  
              // String cp_type= ret.getString(10);  
             //  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate );  
                temp.setGPS_Id(gps_id);
                temp.setTaxi_Id(taxiid);
                temp.setLongtitude(lon);
                temp.setLatitude(lat);
                temp.setSpeed(speed);
                temp.setAngle(angle);
                temp.setTime(time);
                temp.setState(state);
                temp.setCluster_id(Integer.valueOf(cluster_id));
                taxi.add(temp);
           }
         
           //��ʾ����  
           ret.close();  
           conn.close();//�ر�����  
       } catch (SQLException e) {  
           e.printStackTrace();  
       }
       System.out.println("detected :"+taxi.size());
       return taxi;
	 
}
 
}  