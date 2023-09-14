package ai.aitia.aims.worker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ai.aitia.aims.db.service.DetectionDbService;

@Component
public class ImageProcessingWorker extends Thread {
	
	//=================================================================================================
	// members
	
	@Autowired
	private DetectionDbService detectionDbService;
	
	@Resource(name = "processingQueue")
	private BlockingQueue<String> processingQueue;
	
	@Value("${confidence_threshold}")
	private int confidenceThreshold;
	
	@Value("${processing_tool_path}")
	private String processingToolPath;
	
	private boolean doWork = false;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void run() {
		if (doWork) {
			throw new UnsupportedOperationException("Image processing worker is already started");
		}		
		setName(ImageProcessingWorker.class.getSimpleName());
		
		doWork = true;
		while (doWork) {
			try {
				final String path = processingQueue.take();
				handleImageProcessing(path);
			} catch (final InterruptedException ex) {
				if (doWork) {
					interrupt();
				}
			} catch (final Exception ex) {
				System.err.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void interrupt() {
		doWork = false;
		super.interrupt();
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void handleImageProcessing(final String path) throws IOException, InterruptedException {
		final Process process = Runtime.getRuntime().exec(processingToolPath + " " + path);
		
		final BufferedReader stdOut = new BufferedReader(new InputStreamReader(process.getInputStream()));
		final BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		
		final List<String> output = new ArrayList<>();

	    // read the output from the command
		String line = null;
	    while ((line = stdOut.readLine()) != null) {
	    	output.add(line);
	    }
	            
	    // read any errors from the attempted command
        while ((line = stdError.readLine()) != null) {
        	System.err.println(line);
	    }
		
		process.waitFor();
		stdOut.close();
		stdError.close();
		
		if (output.size() > 0) {
			processOutput(path, output);
		}
	}

	//-------------------------------------------------------------------------------------------------
	private void processOutput(final String path, final List<String> output) {
		if (output.size() > 0 && output.get(0).trim().toLowerCase().startsWith("y")) {
			final String[] parts = output.get(0).trim().split(" ");
			final int confidence = Integer.parseInt(parts[1]);
			if (confidence > confidenceThreshold) {
				final File file = new File(path);
				final String name = file.getName();
				final String location = file.getParentFile().getName();
				final String timestampStr = name.substring(0, name.lastIndexOf('.'));
				final long timestamp = Long.parseLong(timestampStr);

				detectionDbService.addDetection(timestamp, location);
			}
		}
	}
}