package ai.aitia.aims.quartz.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import ai.aitia.aims.quartz.QuartzConfig;

@Component
public class ImageJob implements Job {

	private final Logger logger = LogManager.getLogger(QuartzConfig.class);
	
	@Override
	public void execute(final JobExecutionContext context) throws JobExecutionException {
		logger.info("job executed");		
	}

}
