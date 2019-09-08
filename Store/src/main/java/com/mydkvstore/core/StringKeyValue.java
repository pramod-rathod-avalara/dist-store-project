/**
 * 
 */
package com.mydkvstore.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Pramod.Rathod
 *
 */
public class StringKeyValue implements KeyValue<String, String>, DataListenerProvider<String, String> {
	
	private static final String DEFAULT_VALUE = null;
	
	private ConcurrentMap<String, String> map = null;
	private List<DataListener<String, String>> dataListeners = null; 
	
	public StringKeyValue() {
		this.map = new ConcurrentHashMap<String, String>(); 
		this.dataListeners = new CopyOnWriteArrayList<DataListener<String,String>>();
	}
	
	public StringKeyValue(Map<String,String> map) {
		this.map = new ConcurrentHashMap<String, String>(map);
	}

	@Override
	public String put(String key, String value) {
		boolean hasKey = map.containsKey(key);
		String prevValue = map.put(key, value);
		for(DataListener<String, String> listener: this.dataListeners) {
			if(hasKey) {
				listener.onUpdate(key, prevValue, value);
			}
			else {
				listener.onInsert(key, value);
			}
		}
		return prevValue;
	}
	

	@Override
	public String get(String key) {
		return map.getOrDefault(key, DEFAULT_VALUE);
	}

	@Override
	public String remove(String key) {
		String prevValue = null;
		
		if(map.containsKey(key)) {
			prevValue = map.remove(key);
			for(DataListener<String, String> listener: this.dataListeners) {
				listener.onRemove(key, prevValue);
			}
		}
		
		return prevValue;
	}

	
	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		 for (Entry<? extends String, ? extends String> e : m.entrySet()) {
			 this.put(e.getKey(), e.getValue());
		 }
	}

	@Override
	public int size() {
		return map.size();
	}


	@Override
	public Map<? extends String, ? extends String> getAll() {
		return new HashMap<String, String>(this.map);
	}


	@Override
	public void addDataListener(DataListener<String, String> dataListener) {
		this.dataListeners.add(dataListener);
	}

	@Override
	public void removeDataListener(DataListener<String, String> dataListener) {
		this.dataListeners.remove(dataListener);
		
	}

	@Override
	public List<DataListener<String, String>> getListeners() {
		return this.dataListeners;
	}

	@Override
	public boolean hasKey(String key) {
		return map.containsKey(key);
	}

}
