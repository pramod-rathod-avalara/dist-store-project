package com.mydkvstore.core;

import java.util.Map;

public interface KeyValue<K, V> extends DataListenerProvider<String, String> {
	
    public V put(K key, V value);
    
    public void putAll(Map<? extends K, ? extends V> map);

    public V get(K key);
    
    public Map<? extends K, ? extends V> getAll();

    public V remove(K key);
    
    public boolean hasKey(K key);
    
    public int size();
    
}