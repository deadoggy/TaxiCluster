package com.cn.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.entity.CompareEntity;
import com.cn.entity.Fragment;
import com.cn.entity.SearchEntity;
import com.cn.entity.Taxi;
import com.cn.entity.showAllEntity;
import com.cn.service.TaxiService;

 
@Controller
@RequestMapping("/taxi")
public class TaxiController {

	private TaxiService taxiService;
	
	private List<String> taxiIdList ;

	public TaxiService getTaxiService() {
		return taxiService;
	}

	@Resource
	public void setTaxiService(TaxiService taxiService) {
		this.taxiService = taxiService;
	}
	@RequestMapping(value = "/frequency", method = RequestMethod.POST)
	public @ResponseBody List<Taxi> Frequency(@RequestBody SearchEntity searchEntity) {
		System.out.println("enter");
		System.out.println(searchEntity.getTaxiId());
		List<Taxi> result;
		if(searchEntity.getTaxiId()==null)result = taxiService.getAll();
		else result = taxiService.search_frequency(searchEntity.getTaxiId(), null);
		return result;
	}
	@RequestMapping(value = "/single", method = RequestMethod.POST)
	public @ResponseBody List<Taxi> ShowAllData(@RequestBody SearchEntity searchEntity) {
		System.out.println("enter");
		System.out.println(searchEntity.getTaxiId());
		List<Taxi> result;
		if(searchEntity.getTaxiId()==null)result = taxiService.getAll();
		else result = taxiService.search(searchEntity.getTaxiId(), null);
		return result;
	}
	
	@RequestMapping(value = "/taxiid", method = RequestMethod.POST)
	public @ResponseBody Set<String> GetTaxiId(@RequestBody SearchEntity searchEntity) {
		System.out.println("enter id,index:"+searchEntity.getIndex());
		int index = searchEntity.getIndex();
		if(index==0)
		{
			taxiIdList = new ArrayList<String>(taxiService.getAllTaxiId());
			Set<String> result = new HashSet<>();
			for(int i = 0;i<10;i++)
			{
				result.add(taxiIdList.get(index*10+i));
			}
			return result;
		}
		else
		{
			Set<String> result = new HashSet<>();
			for(int i = 0;i<10;i++)
			{
				result.add(taxiIdList.get(index*10+i));
			}
			return result;
		}
	}
	
	@RequestMapping(value = "/alltaxiid", method = RequestMethod.POST)
	public @ResponseBody Set<String> GetAllTaxiId() {
		return taxiService.getAllTaxiId();
	}
	
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	public @ResponseBody List<Taxi> GetAllTaxi()
	{
		System.out.println("enter all");
		return taxiService.getAll();
	}
	
	@RequestMapping(value="/compare", method = RequestMethod.POST)
	public @ResponseBody CompareEntity ComparePath(@RequestBody SearchEntity searchEntity){
		System.out.println("enter compare");
		CompareEntity result = new CompareEntity();
		List<List<Taxi>> trajectory = taxiService.GetFragment(searchEntity.getComparedTaxiIds());
		result.setFragment(trajectory);
		result.setSimilarity(taxiService.GetSimilarity(trajectory));
		return result;
		
	}
	//用于展示所有轨迹
	@RequestMapping(value="/showAll", method = RequestMethod.POST)
	public @ResponseBody showAllEntity showAllEntity(@RequestBody SearchEntity searchEntity){
		System.out.println("show all");
		showAllEntity result = new showAllEntity();
		List<String> idList = new ArrayList<String>();//set转化为list
		//taxiService
		List<String> testList = new ArrayList<String>(taxiService.getAllTaxiId());
		for(int i=0;i<testList.size();i++){
			System.out.println(testList.get(i));
		}System.out.println("length:"+testList.size());
	  for(int i=0;i<20;i++){//将20改为1从而查看数据点是否出现了问题
		 idList.add(testList.get(i));
		 System.out.println("the id used for check is"+testList.get(i));
		 }
	 
	   List<List<Taxi>> trajectory = taxiService.GetFragmentAll(idList);
		 //获取轨迹列表之后进行相似度计算并进行层次聚类生成新的list
		// List<List<Taxi>> resultTracks = taxiService.clusterTrajectory(trajectory);
		  //修改是以上
		  result.setFragment(trajectory);
		  result.setSimilarity(taxiService.GetSimilarity(trajectory));
		  double[][] outputSimilarity = result.getSimilarity();
		  //将相似度写入txt文件用于metlab的参数选择
		  // SimilarityTXT similar = new SimilarityTXT();
		  //similar.writeTXT(outputSimilarity);
		  //对获得的轨迹进行索引赋值并写入txt
		 List<Fragment> listFrag= taxiService.fragmentIndex(trajectory);
       //  OptimalEps optEps = new OptimalEps();
        // similar.testwriteTrackHx(optEps.testEPS(outputSimilarity, listFrag));
        // similar.writeTXT(outputSimilarity);//将相似度写入txt文件
         System.out.println(outputSimilarity.length+":"+outputSimilarity[0].length);
		 //给定不同EPS求ENTROPY的值并写入txt
	/*	 ArrayList<Double> list = new ArrayList<Double>();
		  for(int outI=0;outI<outputSimilarity.length;outI++){
			 // System.out.print("第"+outI+"条，");
			  for(int outJ=1;outJ<outputSimilarity[0].length;outJ++){
				  //System.out.println("第"+outI+"条对应第"+outJ+"条,距离："+outputSimilarity[outI][outJ]);
                  list.add(outputSimilarity[outI][outJ]);
}
			  Collections.sort(list);
			  System.out.println("sort"+list.get(0)+";"+list.get(list.size()-1));
		  }
	   */
		 
		// System.out.println("the size of trajectories is:"+trajectory.size());
		// System.out.println("the size of trajectories is:"+resultTracks.size());
		// result.setFragment(resultTracks);
		// result.setSimilarity(taxiService.GetSimilarity(resultTracks));
		return result;
		
	}
	//以上用于展示所有轨迹
	//从taxi获取数据之后进行层次聚类结果得到聚类后的轨迹
	/*
	@RequestMapping(value="/trajectoryCluster", method = RequestMethod.POST)
	public @ResponseBody showAllEntity trajectoryCluster(@RequestBody SearchEntity searchEntity){
		System.out.println("enter all");
		showAllEntity result = new showAllEntity();
		List<String> idList = new ArrayList<String>();//set转化为list
		//taxiService
		List<String> testList = new ArrayList<String>(taxiService.getAllTaxiId());
		for(int i=0;i<testList.size();i++){
			System.out.println(testList.get(i));
		}System.out.println("length:"+testList.size());
	  for(int i=0;i<20;i++){//将20改为1从而查看数据点是否出现了问题
		 idList.add(testList.get(i));
		 System.out.println("the id used for check is"+testList.get(i));
		 }
	 
		//List<List<Taxi>> trajectory = taxiService.GetFragment(searchEntity.getComparedTaxiIds());
		List<List<Taxi>> trajectory = taxiService.GetFragmentAll(idList);
	 //以上数据是20个taxi得到的所有轨迹数据，下面进行聚类
		  List<List<Taxi>> afterCluster_trajectory = taxiService.clusterTrajectory(trajectory);
		// result.setFragment(trajectory);
		result.setFragment(afterCluster_trajectory);
		// result.setSimilarity(taxiService.GetSimilarity(trajectory));
		  result.setSimilarity(taxiService.GetSimilarity(afterCluster_trajectory));
		return result;
		
	}
	*/
	//修改是以上
	@RequestMapping(value="/test/generate")
	public @ResponseBody String Test()
	{
		taxiService.GenerateTurningPoints(taxiIdList);  
		//taxiService.deleteDuplicate();
		return "hello";
	}
	
	@RequestMapping(value="/test/delete", method = RequestMethod.POST)
	public @ResponseBody String DeleteDuplicateTaxi(){
		taxiService.DeleteDuplicateTaxi(taxiIdList);
		return "hello";
	} 
}
