package org.tj.sse.taxicluster.datamining.DataMining;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestFileWriter {
    public void write() {
        FileWriter fw = null;
        try {
            fw = new FileWriter("G://data/data.txt",true);
            String c = "abs"+"\r\n";
            fw.write(c);
            fw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("д��ʧ��");
            System.exit(-1);
        }
    }
    public void filename() {
    	try{
    	File path=new File("G:\\data"); //�������ļ�·��
    	File dir=new File(path,"data.txt"); //�������ļ�·���´������ļ�������
    	if(!dir.exists()) //�ж��ļ��Ƿ��Ѿ�����
    	dir.createNewFile(); //��������ڵĻ��ʹ���һ���ļ�
    	}
    	catch(Exception e){ //������ھͻᱨ��
    	System.out.print("����ʧ��");//�������ʧ����Ϣ��Ҳ��֤����ǰҪ�������ļ��Ѿ����ڡ�
    	}
    	}
}