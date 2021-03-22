package com.crio.jumbogps.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.crio.jumbogps.model.AssetDetail;
import com.crio.jumbogps.model.AssetHistory;
import com.crio.jumbogps.repository.AssetDetailRepository;
import com.crio.jumbogps.repository.AssetHistoryRepository;

@RestController
public class AssetController {
	
	@Autowired  
	private AssetHistoryRepository assetHistoryRepository;
	
	@Autowired  
	private AssetDetailRepository assetDetailRepository;
	
	@GetMapping("/location/list")
	public List<AssetHistory> getAllAssets() {
		getCurrentLocationOfAsset();
		return assetHistoryRepository.findDistinctByFkAssetIdIn();
	}
	
	@PostMapping("/location/currentList")
	private void getCurrentLocationOfAsset() {
		List<AssetDetail> assetDetailList = assetDetailRepository.findAll();
		for(AssetDetail assetDetail : assetDetailList) {
		AssetHistory assetHistory = new AssetHistory();
		double longitude = Math.random() * Math.PI * 2;
		double latitude = Math.acos(Math.random() * 2 - 1);
		
		assetHistory.setFkAssetId(assetDetail);
		assetHistory.setLatitude(latitude);
		assetHistory.setLongitude(longitude);
		assetHistory.setTimeOfTracking(LocalDateTime.now());
		assetHistoryRepository.save(assetHistory);
		}
	}
	
	@GetMapping("/location/type")
	public List<AssetHistory> getAAssetsHistoryByType(@RequestParam("type") int assetType) {
		return assetHistoryRepository.getAssetDetailByType(assetType);
	}
	
	@GetMapping("/location/activeTodayList")
	public List<AssetHistory> getAAssetsHistoryActiveToday() {
		return assetHistoryRepository.getAssetDetailSinceLastDay();	
	}
	
	
	@GetMapping("/location/id")
	public List<Map<String,Object>> getAAssetsHistoryById(@RequestParam("id") int assetId) {
		Optional<AssetDetail> assetDetailOptional = assetDetailRepository.findById(assetId);
		List<Map<String,Object>> assetHistoryListMap = new ArrayList<Map<String,Object>>();
		
		if(assetDetailOptional.isPresent()) {
			try {
				List<AssetHistory> assetHistoryList = assetHistoryRepository.getAssetDetailByIdSinceLastDay(assetDetailOptional.get().getPkAssetId());
				for(AssetHistory assetHistory : assetHistoryList) {
					Map<String,Object> assetHistoryMap = new HashMap<String,Object>();
					assetHistoryMap.put("pkAssetHistoryDetailId", assetHistory.getPkAssetHistoryDetailId());
					assetHistoryMap.put("timeOfTracking", assetHistory.getTimeOfTracking());
					assetHistoryMap.put("latitude", assetHistory.getLatitude());
					assetHistoryMap.put("longitude", assetHistory.getLongitude());
					assetHistoryListMap.add(assetHistoryMap);
				}
				}catch(Exception e) {
					e.printStackTrace();
				}
			return assetHistoryListMap;
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not found");
		}	
	}
	
	@GetMapping("/location/time")
	public List<AssetHistory> getAssetsHistoryByTime(@RequestParam("startTime") String startTime , @RequestParam("endTime") String endTime) {
		
		try {
			LocalDateTime start = LocalDateTime.parse(startTime);
			LocalDateTime end = LocalDateTime.parse(endTime);
			return assetHistoryRepository.getAssetDetailsByTime(start,end);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;	
	}
	
}