/**
 * 
 */
package com.mydkvstore.core;

/**
 * @author Pramod.Rathod
 *
 */
public final class KeyValueStore {
	
	private static KeyValue<String, String> stringKeyValueStore = null;

	/**
	 * 
	 */
	private KeyValueStore() { 
		//Do nothing
	}

	public static synchronized KeyValue<String, String> stringKeyValueStore() {
		if(stringKeyValueStore == null) {
			stringKeyValueStore = new StringKeyValue();
		}
		return stringKeyValueStore;
	}

}
