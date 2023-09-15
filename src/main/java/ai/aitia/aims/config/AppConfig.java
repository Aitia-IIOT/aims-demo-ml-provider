package ai.aitia.aims.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
	
	//=================================================================================================
	// members

	private boolean initialized = false;
	
	@Value("${input_path_prefix}")
	private String inputFolderPrefix;
	
	@Value("${working_path_prefix}")
	private String workingFolderPrefix;	

	@Value("${processing_tool_path}")
	private String processingToolPath;
	
	@Value("${read_interval:1000}")
	private int readInterval;
	
	@Value("${confidence_threshold:80}")
	private int confidenceThreshold;
	
	@Value("${locations}")
	private String[] locations;
	
	//=================================================================================================
	// methods

	public boolean isInitialized() { return initialized; }
	public String getInputFolderPrefix() { return inputFolderPrefix; }
	public String getWorkingFolderPrefix() { return workingFolderPrefix; }
	public String getProcessingToolPath() { return processingToolPath; }
	public int getReadInterval() { return readInterval; }	
	public int getConfidenceThreshold() { return confidenceThreshold; }
	public String[] getLocations() { return locations; }
	public void setInitialized(final boolean initialized) { this.initialized = initialized; }
}
