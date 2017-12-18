package org.tj.sse.taxicluster.entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hierarchical_cluster")
public class HierarchicalCluster {
	
	private int id;
	
	private List<TurningPoint> turningPoints = new ArrayList<>();
	
	private double centerLatitude;
	
	private double centerLongtitude;
	
	private List<Cluster_Point> cluster_Points;
	
	//num of turning point
	private int size;
	
	//initialize number
	private int numberOfCluster;
	
	private String name;
	
	//start or stop or all
	private String divide;
	
	private String method;
	
	@OneToMany(mappedBy="cp_hierarchicalCluster",cascade=CascadeType.REMOVE)
	@LazyCollection(LazyCollectionOption.EXTRA)
	public List<Cluster_Point> getCluster_Points() {
		return cluster_Points;
	}

	public void setCluster_Points(List<Cluster_Point> cluster_Points) {
		this.cluster_Points = cluster_Points;
	}

	@Column(name="method")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name="divide")		
	public String getDivide() {
		return divide;
	}

	public void setDivide(String divide) {
		this.divide = divide;
	}

	@Column(name="name")	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="numberOfCluster")
	public int getNumberOfCluster() {
		return numberOfCluster;
	}
	
	public void setNumberOfCluster(int numberOfCluster) {
		this.numberOfCluster = numberOfCluster;
	}
	
	@Column(name="size")
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Transient
	public List<TurningPoint> getTurningPoints() {
		return turningPoints;
	}
	
	public void setTurningPoints(List<TurningPoint> turningPoints) {
		this.turningPoints = turningPoints;
	}
	
	@Column(name="centerLatitude")
	public double getCenterLatitude() {
		return centerLatitude;
	}
	
	public void setCenterLatitude(double centerLatitude) {
		this.centerLatitude = centerLatitude;
	}
	
	@Column(name="centerLongtitude")
	public double getCenterLongtitude() {
		return centerLongtitude;
	}
	
	public void setCenterLongtitude(double centerLongtitude) {
		this.centerLongtitude = centerLongtitude;
	}
	
}
