/**
 * 
 */
package com.mydkvstore.core;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mydkvstore.core.DataListener;
import com.mydkvstore.core.StringKeyValue;


/**
 * @author Pramod.Rathod
 *
 */
public class StringKeyValueTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#put(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPut() {
		StringKeyValue kvStore = new StringKeyValue();
		assertEquals(null, kvStore.put("1", "One"));
		assertEquals("One", kvStore.get("1"));
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		StringKeyValue kvStore = new StringKeyValue();
		kvStore.put("1", "One");
		kvStore.put("2", "Two");
		assertEquals("One", kvStore.get("1"));
		assertEquals("Two", kvStore.get("2"));
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#remove(java.lang.String)}.
	 */
	@Test
	public void testRemove() {
		StringKeyValue kvStore = new StringKeyValue();
		kvStore.put("1", "One");
		kvStore.put("2", "Two");
		assertEquals("One", kvStore.remove("1"));
		assertEquals("Two", kvStore.remove("2"));
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#putAll(java.util.Map)}.
	 */
	@Test
	public void testPutAll() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "One");
		map.put("2", "Two");
		StringKeyValue kvStore = new StringKeyValue();
		kvStore.putAll(map);
		assertEquals("One", kvStore.get("1"));
		assertEquals("Two", kvStore.get("2"));
		assertEquals(2, kvStore.size());
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#size()}.
	 */
	@Test
	public void testSize() {
		StringKeyValue kvStore = new StringKeyValue();
		kvStore.put("1", "One");
		kvStore.put("2", "Two");
		assertEquals(2, kvStore.size());
		assertEquals("Two", kvStore.remove("2"));
		assertEquals(1, kvStore.size());
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#getAll()}.
	 */
	@Test
	public void testGetAll() {
		StringKeyValue kvStore = new StringKeyValue();
		kvStore.put("1", "One");
		kvStore.put("2", "Two");
		Map<? extends String, ? extends String> map = kvStore.getAll();
		assertNotNull(map);
		assertEquals("One", kvStore.get("1"));
		assertEquals("Two", kvStore.get("2"));
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#addDataListener(com.mydkvstore.core.DataListener)}.
	 */
	@Test
	public void testAddDataListener() {
		StringKeyValue kvStore = new StringKeyValue();
		kvStore.addDataListener(new DataListener<String, String>() {
			
			@Override
			public void onUpdate(String key, String previousValue, String currentValue) {
				assertEquals("1", key);
				assertEquals("One", previousValue);	
				assertEquals("DigitOne", currentValue);
			}
			
			@Override
			public void onInsert(String key, String value) {
				assertEquals("1", key);	
				assertEquals("One", value);
				
			}
			
			@Override
			public void onRemove(String key, String value) {
				assertEquals("1", key);	
				assertEquals("DigitOne", value);
				
			}
		});
		
		kvStore.put("1", "One");
		kvStore.put("1", "DigitOne");
		kvStore.remove("1");
	}

	/**
	 * Test method for {@link com.mydkvstore.core.StringKeyValue#removeDataListener(com.mydkvstore.core.DataListener)}.
	 */
	@Test
	public void testRemoveDataListener() {
		StringKeyValue kvStore = new StringKeyValue();
		DataListener<String, String> listener = new DataListener<String, String>() {
			
			@Override
			public void onUpdate(String key, String previousValue, String currentValue) {
				//Do nothing
			}
			
			@Override
			public void onInsert(String key, String value) {
				//Do nothing
				
			}
			
			@Override
			public void onRemove(String key, String value) {
				//Do nothing
				
			}
		};
		kvStore.addDataListener(listener);
		kvStore.removeDataListener(listener);
		assertEquals(0, kvStore.getListeners().size());
	}

}
