package eu.arrowhead.application.skeleton.provider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.aims.db.service.DetectionDbService;
import ai.aitia.aims.dto.DetectionDTO;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.exception.BadPayloadException;

@RestController
@RequestMapping("/ml-provider")
public class ProviderController {
	
	//=================================================================================================
	// members

	@Autowired
	private DetectionDbService detectionDbService;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@GetMapping(path = CommonConstants.ECHO_URI)
	public String echoService() {
		return "Got it!";
	}

	//-------------------------------------------------------------------------------------------------
	@GetMapping(path = "/detections", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody public List<DetectionDTO> getDetections(@RequestParam(name = "from", required = true) final Long timestamp) {
		if (timestamp == null || timestamp.longValue() < 0) {
			throw new BadPayloadException("Invalid from value.");
		}
		
		return detectionDbService.getDetectionsFrom(timestamp);
	}
}
