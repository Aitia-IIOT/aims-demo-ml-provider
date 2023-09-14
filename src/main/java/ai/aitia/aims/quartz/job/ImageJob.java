package ai.aitia.aims.quartz.job;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ai.aitia.aims.config.AppConfig;
import ai.aitia.aims.quartz.QuartzConfig;

@Component
@DisallowConcurrentExecution
public class ImageJob implements Job {
	
	//=================================================================================================
	// members
	
	@Autowired
	private AppConfig config;
	
	@Autowired
	private BlockingQueue<String> processingJobQueue;

	private final Logger logger = LogManager.getLogger(QuartzConfig.class);
	
	//=================================================================================================
	// methods
	
	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		if (!config.isInitialized()) {
			return;
		}
		
		logger.debug("job start");
		for (final String location : config.getLocations()) {
			final File inputfolder = Path.of(config.getInputFolderPrefix(), location).toFile();
			if (inputfolder.exists()) {
				final File[] imgFiles = inputfolder.listFiles();
				for (final File img : imgFiles) {
					logger.debug("File found in {}: {}", location, img.getName());
					final Path workingFolderPath = Path.of(config.getWorkingFolderPrefix(), location);
					createFolderIfNotExists(workingFolderPath);
					final Path renamedImg = Path.of(workingFolderPath.toString(), String.valueOf(System.currentTimeMillis()));
					try {
						Files.move(Path.of(img.getAbsolutePath()), renamedImg, StandardCopyOption.REPLACE_EXISTING);
						logger.debug("File moved: {}" + renamedImg.toString());
						processingJobQueue.add(renamedImg.toString());
					} catch (final IOException ex) {
						logger.error("Error occured when moving image file");
						throw new JobExecutionException(ex.getMessage());
					}
				}
			}
		}
		
		logger.debug("job finish");	
	}

	//=================================================================================================
	// assistants
	
	private void createFolderIfNotExists(final Path path) {
		final File folder = path.toFile();
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}
}
