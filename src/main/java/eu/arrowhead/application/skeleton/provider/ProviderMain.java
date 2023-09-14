package eu.arrowhead.application.skeleton.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import eu.arrowhead.common.CommonConstants;

@SpringBootApplication
@ComponentScan(basePackages = {CommonConstants.BASE_PACKAGE, "ai.aitia"}) 
@EntityScan("ai.aitia.aims.db.entity")
@EnableJpaRepositories(basePackages = "ai.aitia.aims.db.repository")
public class ProviderMain {

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public static void main(final String[] args) {
		SpringApplication.run(ProviderMain.class, args);
	}	
}
