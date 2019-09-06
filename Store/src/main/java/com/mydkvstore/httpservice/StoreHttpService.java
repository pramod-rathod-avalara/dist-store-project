package com.mydkvstore.httpservice;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mydkvstore.core.DataListener;
import com.mydkvstore.core.KeyValue;
import com.mydkvstore.core.KeyValueStore;

import ro.pippo.core.Application;
import ro.pippo.core.RequestResponseFactory;
import ro.pippo.core.websocket.WebSocketContext;
import ro.pippo.core.websocket.WebSocketHandler;
import ro.pippo.session.SessionDataStorage;
import ro.pippo.session.SessionManager;
import ro.pippo.session.SessionRequestResponseFactory;
import ro.pippo.session.cookie.CookieSessionDataStorage;

/**
 * Store HTTP service to set and get value to and from a store. 
 *
 * @see com.mydkvstore.StoreHttpServiceLauncher#main(String[])
 */
public class StoreHttpService extends Application {
    
	private static final String NOT_FOUND = "Not found!";
	private static final String OK = "Ok!";
	private static final String ACTION_UPDATE = "update";
	private static final String ACTION_REMOVE = "remove";
	private static final String ACTION_INSERT = "insert";
	
	private final static Logger log = LoggerFactory.getLogger(StoreHttpService.class);
	private KeyValue<String, String> stringKeyValueStore = null;
	private Args appSettings;
	private StoreWebSocket storeWebSocket = null;
	private WebSocketClient storeWebSocketclient = null;
	private URI listenerURI = null;
	

    public StoreHttpService(Args appSettings) {
		this.appSettings = appSettings;
	}
    
    private void sendKeyValueMessage(String action, String key, String value) {
    	ObjectMapper keyValueObjectMapper = new ObjectMapper();
		try {
			String message = keyValueObjectMapper.writeValueAsString(new KeyValueMessage(action, key, value));
			log.debug("Sending insert request: %s ", message);
			storeWebSocket.sendMessage(message);
		} catch (JsonProcessingException e) {
			log.debug("JsonProcessingException", e);
		}
    }
    
    private void procesKeyValueMessage(String message) {
    	
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			KeyValueMessage keyValueMessage = mapper.readValue(message, KeyValueMessage.class);
			String action = keyValueMessage.getAction();
			String prevValue = null;
			switch(action) {
				case ACTION_UPDATE:
				case ACTION_INSERT:	
					prevValue = stringKeyValueStore.put(keyValueMessage.getKey(), keyValueMessage.getValue());
					log.debug("Action %s: Key = %s, Previous Value  = %s,  Current Value  = %s  ", action, keyValueMessage.getKey(), prevValue, keyValueMessage.getValue());
					break;
				case ACTION_REMOVE:
					prevValue = stringKeyValueStore.remove(keyValueMessage.getKey());
					log.debug("Action %s: Key = %s, Previous Value  = %s ", action, keyValueMessage.getKey(), prevValue);
					break;
			}
		} catch (IOException e) {
			log.debug("ProcesKeyValueMessageException", e);
		}
    	
    }


	@Override
    protected void onInit() {
		
		log.debug("Entry: init() ");
    	
    	stringKeyValueStore = KeyValueStore.stringKeyValueStore();
    	
    	String listener = this.getPippoSettings().getString("listener", appSettings.listener);
    	
    	if(listener != null && !listener.isEmpty()) {
    		log.debug("Found listener config.");
    		log.debug("Attemting to connect to ... %s ", listener);
    		String[] listenerHostPort = listener.split(":", 2);
    		
    		if(listenerHostPort.length == 2) {
    			
    			storeWebSocketclient = new WebSocketClient();
        		storeWebSocket = new StoreWebSocket();
        		
        		try {
					storeWebSocketclient.start();
					String socketURL = new StringBuilder("ws://").append(listener).append("/ws/replicate").toString();
					log.debug("Attemting to connect to ... %s ", socketURL);
					listenerURI = new URI(socketURL);
					ClientUpgradeRequest request = new ClientUpgradeRequest();
					storeWebSocketclient.connect(storeWebSocket, listenerURI, request);
					log.debug("Connecting to : %s%n", listenerURI);
				} catch (Exception e) {
					log.debug("WebSocket Clinet Error: ", e);
				}
    			
    			stringKeyValueStore.addDataListener( new DataListener<String, String>() {
        			
        			@Override
        			public void onUpdate(String key, String previousValue, String currentValue) {
        				log.debug("Updated: key=%s, previousValue=%s,  currentValue=%s", key, previousValue, currentValue);
        				sendKeyValueMessage(ACTION_UPDATE, key, currentValue);
        			}
        			
        			@Override
        			public void onRemove(String key, String value) {
        				log.debug("Removed: key=%s, value=%s", key, value);
        				sendKeyValueMessage(ACTION_REMOVE, key, value);
        			}
        			
        			@Override
        			public void onInsert(String key, String value) {
        				log.debug("Inserted: key=%s, value=%s", key, value);
        				sendKeyValueMessage(ACTION_INSERT, key, value);
        			}
        		});
    			
    		}
    	}
		    	
        // send a template as response
        GET("/get/{key}", routeContext -> {
        	log.debug("GET /get/{key}");
        	// retrieve key parameters from request
            String key = routeContext.getParameter("key").toString();
            String value = NOT_FOUND;
            if (key != null && stringKeyValueStore.hasKey(key)) {
            	value = stringKeyValueStore.get(key);
            } 
            routeContext.json().status(HttpStatus.OK_200).send(value);
        });
        
        POST("/set/{key}", routeContext -> {
        	log.debug("POST /set/{key}");
            String key = routeContext.getParameter("key").toString();
            log.debug("Key: "+key);
	        String value = routeContext.createEntityFromBody(String.class);
	        log.debug("Value: "+value);
	        stringKeyValueStore.put(key, value);
	        routeContext.json().status(HttpStatus.OK_200).send(OK);
        });
        
        
        addWebSocket("/ws/replicate", new WebSocketHandler() {
			
			@Override
			public void onMessage(WebSocketContext webSocketContext, String message) {
				log.debug("StoreHttpServiceWebSocket.onMessage");
				log.debug("message = " + message);
		        procesKeyValueMessage(message);
			}
			
			@Override
		    public void onOpen(WebSocketContext webSocketContext) {
		        log.debug("StoreHttpServiceWebSocket.onOpen");
		    }

		    @Override
		    public void onClose(WebSocketContext webSocketContext, int closeCode, String message) {
		        log.debug("StoreHttpServiceWebSocket.onClose");
		    }

		    @Override
		    public void onTimeout(WebSocketContext webSocketContext) {
		        log.debug("StoreHttpServiceWebSocket.onTimeout");
		    }

		    @Override
		    public void onError(WebSocketContext webSocketContext, Throwable t) {
		        log.debug("StoreHttpServiceWebSocket.onError");
		    }
		});
        
        log.debug("Exit: init() ");
        
    }
	
	@Override
    protected RequestResponseFactory createRequestResponseFactory() {
        SessionDataStorage sessionDataStorage = new CookieSessionDataStorage(getPippoSettings());
        SessionManager sessionManager = new SessionManager(sessionDataStorage);
        return new SessionRequestResponseFactory(this, sessionManager);
    }
	
	@Override
	protected void onDestroy() {
		log.debug("Entry: onDestroy() ");		
		if(storeWebSocket != null) {
			log.debug("Closing socket session...");
			storeWebSocket.closeSession();
			log.debug("Closing socket session... Done!");
			// wait for closed socket connection.
			try {
				storeWebSocket.awaitClose(5, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				log.debug(e.getMessage(), e);
			}
			finally {
				try {
					log.debug("Stopping socket client...");
					storeWebSocketclient.stop();
					stringKeyValueStore = null;
					log.debug("Stopping socket client... Done!");
				} catch (Exception e) {
					log.debug(e.getMessage(), e);
				}
			}
		}
			
		log.debug("Exit: onDestroy() ");
	}

}
