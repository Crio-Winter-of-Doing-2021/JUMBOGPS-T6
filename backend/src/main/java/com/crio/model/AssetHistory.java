package com.crio.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "asset_history")
public class AssetHistory {
	
	private Integer pkAssetHistoryDetailId;
	private AssetDetail fkAssetId;
	private Date timeOfTracking;
	private Double latitude;
	private Double longitude;
	
	@Id 
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="pk_asset_history_detail_id", unique = true, nullable = false)
	public Integer getPkAssetHistoryDetailId() {
		return pkAssetHistoryDetailId;
	}
	public void setPkAssetHistoryDetailId(Integer pkAssetHistoryDetailId) {
		this.pkAssetHistoryDetailId = pkAssetHistoryDetailId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_asset_id", nullable = false, referencedColumnName = "pk_asset_id")
	public AssetDetail getFkAssetId() {
		return fkAssetId;
	}
	public void setFkAssetId(AssetDetail fkAssetId) {
		this.fkAssetId = fkAssetId;
	}
	
	@Column(name = "time_of_tracking")
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getTimeOfTracking() {
		return timeOfTracking;
	}
	public void setTimeOfTracking(Date timeOfTracking) {
		this.timeOfTracking = timeOfTracking;
	}
	
	@Column(name = "latitude")
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@Column(name = "longitude")
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

}
