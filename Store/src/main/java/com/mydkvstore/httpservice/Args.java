/**
 * 
 */
package com.mydkvstore.httpservice;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.StringConverter;

/**
 * @author Pramod.Rathod
 *
 */
public class Args {
	
	@Parameter(names = {"--port", "-p"}, converter = StringConverter.class, required = false)
	String port = null;
	
	@Parameter(names = {"--listener", "-l"}, converter = StringConverter.class, required = false)
	String listener = null;
	 
	/**
	 * 
	 */
	public Args() {
		// TODO Auto-generated constructor stub
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getListener() {
		return listener;
	}

	public void setListener(String listener) {
		this.listener = listener;
	}
}

