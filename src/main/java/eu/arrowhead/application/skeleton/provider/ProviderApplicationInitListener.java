package eu.arrowhead.application.skeleton.provider;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import ai.aitia.aims.config.AppConfig;
import ai.aitia.aims.worker.ImageProcessingWorker;
import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.arrowhead.application.library.config.ApplicationInitListener;
import ai.aitia.arrowhead.application.library.util.ApplicationCommonConstants;
import eu.arrowhead.application.skeleton.provider.security.ProviderSecurityConfig;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.core.CoreSystem;
import eu.arrowhead.common.exception.ArrowheadException;

@Component
public class ProviderApplicationInitListener extends ApplicationInitListener {
	
	//=================================================================================================
	// members
	
	@Autowired
	private ArrowheadService arrowheadService;
	
	@Autowired
	private ProviderSecurityConfig providerSecurityConfig;
	
	@Value(ApplicationCommonConstants.$TOKEN_SECURITY_FILTER_ENABLED_WD)
	private boolean tokenSecurityFilterEnabled;
	
	@Value(CommonConstants.$SERVER_SSL_ENABLED_WD)
	private boolean sslEnabled;
	
	@Autowired
	private AppConfig config;
	
	@Autowired
	private ImageProcessingWorker worker;
	
	private final Logger logger = LogManager.getLogger(ProviderApplicationInitListener.class);
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	@Override
	protected void customInit(final ContextRefreshedEvent event) {
		checkConfiguration();
		
		//Checking the availability of necessary core systems
		checkCoreSystemReachability(CoreSystem.SERVICEREGISTRY);
		if (sslEnabled && tokenSecurityFilterEnabled) {
			checkCoreSystemReachability(CoreSystem.AUTHORIZATION);			

			//Initialize Arrowhead Context
			arrowheadService.updateCoreServiceURIs(CoreSystem.AUTHORIZATION);			
		
			setTokenSecurityFilter();
		
		}else {
			logger.info("TokenSecurityFilter in not active");
		}		
		
		validateProcessingToolPath();
		prepareImageFolders();
		prepareLocationFolders();
		worker.start();
		config.setInitialized(true);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void customDestroy() {
		worker.interrupt();
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void checkConfiguration() {
		if (!sslEnabled && tokenSecurityFilterEnabled) {			 
			logger.warn("Contradictory configuration:");
			logger.warn("token.security.filter.enabled=true while server.ssl.enabled=false");
		}
	}

	//-------------------------------------------------------------------------------------------------
	private void setTokenSecurityFilter() {
		final PublicKey authorizationPublicKey = arrowheadService.queryAuthorizationPublicKey();
		if (authorizationPublicKey == null) {
			throw new ArrowheadException("Authorization public key is null");
		}
		
		KeyStore keystore;
		try {
			keystore = KeyStore.getInstance(sslProperties.getKeyStoreType());
			keystore.load(sslProperties.getKeyStore().getInputStream(), sslProperties.getKeyStorePassword().toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException ex) {
			throw new ArrowheadException(ex.getMessage());
		}			
		final PrivateKey providerPrivateKey = Utilities.getPrivateKey(keystore, sslProperties.getKeyPassword());
		
		providerSecurityConfig.getTokenSecurityFilter().setAuthorizationPublicKey(authorizationPublicKey);
		providerSecurityConfig.getTokenSecurityFilter().setMyPrivateKey(providerPrivateKey);

	}
	
	//-------------------------------------------------------------------------------------------------
	private void prepareImageFolders()  {
		if (config.getInputFolderPrefix() == null || config.getInputFolderPrefix().isBlank()) {
			throw new RuntimeException("input_path_prefix is not configured");
		}
		if (config.getWorkingFolderPrefix() == null || config.getWorkingFolderPrefix().isBlank()) {
			throw new RuntimeException("working_path_prefix is not configured");
		}
		
		
		try {
			final File inputFolder = new File(config.getInputFolderPrefix());
			if (!inputFolder.exists()) {
				if (!inputFolder.mkdirs()) {
					throw new RuntimeException("Could not create folder: " + config.getInputFolderPrefix());
				}
			}
			if (!inputFolder.canRead()) {
				throw new RuntimeException("Have no rights to read folder: " + config.getInputFolderPrefix());
			}
			if (!inputFolder.canWrite()) {
				throw new RuntimeException("Have no rights to write folder: " + config.getInputFolderPrefix());
			}
		} catch (InvalidPathException ex) {
			throw new RuntimeException("input_path_prefix syntax error: " + ex.getMessage());
		}
		
		try {
			final File workingFolder = new File(config.getWorkingFolderPrefix());
			if (!workingFolder.exists()) {
				if (!workingFolder.mkdirs()) {
					throw new RuntimeException("Could not create folder: " + config.getWorkingFolderPrefix());
				}
			}
			if (!workingFolder.canRead()) {
				throw new RuntimeException("Have no rights to read folder: " + config.getWorkingFolderPrefix());
			}
			if (!workingFolder.canWrite()) {
				throw new RuntimeException("Have no rights to write folder: " + config.getWorkingFolderPrefix());
			}
		} catch (InvalidPathException ex) {
			throw new RuntimeException("working_path_prefix syntax error: " + ex.getMessage());
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private void prepareLocationFolders() {
		final File inputFolder = new File(config.getInputFolderPrefix());
		final File workingFolder = new File(config.getWorkingFolderPrefix());
		if (!inputFolder.exists() || !workingFolder.exists()) {
			throw new RuntimeException("Cannot create location folders, because image folders haven't been prepared yet.");
		}
		
		for (String location : config.getLocations()) {
			final File inputLocationFolder = Path.of(config.getInputFolderPrefix(), location).toFile();
			final File workingLocationFolder = Path.of(config.getWorkingFolderPrefix(), location).toFile();
			if (!inputLocationFolder.exists()) {
				if (!inputLocationFolder.mkdirs()) {
					throw new RuntimeException("Could not create folder: " + inputLocationFolder);
				}
			}
			if (!workingLocationFolder.exists()) {
				if (!workingLocationFolder.mkdirs()) {
					throw new RuntimeException("Could not create folder: " + inputLocationFolder);
				}
			}
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private void validateProcessingToolPath()  {
		if (config.getProcessingToolPath() == null || config.getProcessingToolPath().isBlank()) {
			throw new RuntimeException("processing_tool_path is not configured");
		}
		
		try {
			final File processingTool = new File(config.getProcessingToolPath());
			if (!processingTool.exists()) {
				throw new RuntimeException("processing_tool_path not exists: " + config.getProcessingToolPath());
			}
			if (!processingTool.canExecute()) {
				throw new RuntimeException("Have no rights to execute: " + config.getProcessingToolPath());
			}
		} catch (InvalidPathException ex) {
			throw new RuntimeException("processing_tool_path syntax error: " + ex.getMessage());
		}
	}
	
}
