/**
 * 
 */
package com.mydkvstore.core;


/**
 * @author Pramod.Rathod
 *
 */
public interface DataListener<K, V> {
	
	public void onInsert(K key, V value);
	
	public void onUpdate(K key, V previousValue, V currentValue);
	
	public void onRemove(K key, V value);
	
}
