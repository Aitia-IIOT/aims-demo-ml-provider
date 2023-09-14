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
	
	//=================================================================================================
	// methods

	public boolean isInitialized() { return initialized; }
	public String getInputFolderPrefix() { return inputFolderPrefix; }
	public String getWorkingFolderPrefix() { return workingFolderPrefix; }
	public String getProcessingToolPath() { return processingToolPath; }
	
	public void setInitialized(final boolean initialized) { this.initialized = initialized; }

}
