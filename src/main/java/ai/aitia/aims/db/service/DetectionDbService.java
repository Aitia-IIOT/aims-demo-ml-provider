package ai.aitia.aims.db.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ai.aitia.aims.db.entity.Detection;
import ai.aitia.aims.db.repository.DetectionRepository;
import ai.aitia.aims.dto.DetectionDTO;
import eu.arrowhead.common.exception.ArrowheadException;

@Service
public class DetectionDbService {

	//=================================================================================================
	// members
	
	@Autowired
	private DetectionRepository detectionRepository;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public List<DetectionDTO> getDetectionsFrom(final long timestamp) {
		try {
			final List<Detection> records = detectionRepository.findByTimestampGreaterThan(timestamp * 1000);
			return records.stream().map(r -> new DetectionDTO(r.getTimestamp() / 1000, r.getLocation())).collect(Collectors.toList()); 
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			
			throw new ArrowheadException("Database exception");
		}
		
	}
	
	//-------------------------------------------------------------------------------------------------
	@Transactional(rollbackFor = ArrowheadException.class)
	public void addDetection(final long timestamp, final String location) {
		try {
			final Detection detection = new Detection(timestamp, location);
			detectionRepository.saveAndFlush(detection);
		} catch (final Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			
			throw new ArrowheadException("Database exception");
		}
	}
}
