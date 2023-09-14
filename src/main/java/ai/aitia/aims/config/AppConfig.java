package ai.aitia.aims.config;

import org.springframework.stereotype.Component;

@Component
public class AppConfig {

	boolean initialized = false;

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(final boolean initialized) {
		this.initialized = initialized;
	}
}
