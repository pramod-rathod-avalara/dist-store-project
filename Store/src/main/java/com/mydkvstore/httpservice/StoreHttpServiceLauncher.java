package com.mydkvstore.httpservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import ro.pippo.core.Pippo;

/**
 * Run application from here.
 */
public class StoreHttpServiceLauncher {
	
	private final static Logger log = LoggerFactory.getLogger(StoreHttpServiceLauncher.class);

    public static void main(String[] args) {
    	log.info(">> Program arguments { ");
		for (String s : args) {
			log.info("\t" + s);
		}
		log.info("} ");
		
		Args appSettings = new Args();
		JCommander.newBuilder()
		  .addObject(appSettings)
		  .build()
		  .parse(args);
		
		String listener = System.getProperty("storeListener", appSettings.listener);
		log.info("Listener: %s",listener);
		StoreHttpService storeService = new StoreHttpService(appSettings);
        Pippo pippo = new Pippo(storeService);
        String port = System.getProperty("storeHost", appSettings.port);
        if(port == null) {
        	log.info("Using default port settings");
        	pippo.start();
        } else {
        	log.info("Using custom port settings: %s", port);
        	pippo.start(new Integer(port));
        }
        
    }

}
