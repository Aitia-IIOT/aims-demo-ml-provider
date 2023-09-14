package ai.aitia.aims.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

	@Bean(name = "processingQueue")
	public BlockingQueue<String> initJobQueue() {
		return new LinkedBlockingQueue<>();
	}
}