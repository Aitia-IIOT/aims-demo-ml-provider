package ai.aitia.aims.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import ai.aitia.aims.config.AppConfig;
import ai.aitia.aims.quartz.job.ImageJob;

@Configuration
@EnableAutoConfiguration
public class QuartzConfig {
	
	private final Logger logger = LogManager.getLogger(QuartzConfig.class);
	
	@Autowired
    private ApplicationContext applicationContext;
	
	@Autowired
    private AppConfig appConfig;
	
	@Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
	
	@Bean
    public SchedulerFactoryBean scheduler(Trigger trigger, JobDetail job) {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("application.properties"));

        logger.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);

        return schedulerFactory;
    }
	
	@Bean
    public JobDetailFactoryBean imageJobDetail() {

        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ImageJob.class);
        jobDetailFactory.setName("Image_Job_Detail");
        jobDetailFactory.setDescription("Invoke Image Job service...");
        jobDetailFactory.setDurability(true);
        return jobDetailFactory;
    }

	@Bean
    public SimpleTriggerFactoryBean trigger(JobDetail job) {

        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(job);

        logger.info("Configuring trigger to fire every {} seconds", appConfig.getReadInterval());
        trigger.setRepeatInterval(appConfig.getReadInterval());
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        trigger.setName("Quartz_Trigger");
        return trigger;
    }
}
