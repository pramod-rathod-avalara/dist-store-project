/**
 * 
 */
package com.mydkvstore.core;

import java.util.List;

/**
 * @author Pramod.Rathod
 *
 */
public interface DataListenerProvider<K, V> {
	
	public void addDataListener(DataListener<K, V> dataListener);
	
	public void removeDataListener(DataListener<K, V> dataListener);
	
	public List<DataListener<K, V>> getListeners();

}
