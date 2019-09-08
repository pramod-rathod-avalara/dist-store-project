package com.mydkvstore.httpservice;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

public class TestWebSocketClientConnection {
	
	@Test
	public void testWebSocketClientConnection() {
		
		System.out.println("Inside class");
		WebSocketClient storeWebSocketclient = new WebSocketClient();
		StoreWebSocket storeWebSocket = new StoreWebSocket();
		
		try {
			storeWebSocketclient.start();
			String socketURL = "ws://localhost:9339/ws/replicate";
			System.out.printf("Attemting to connect to ... %s ", socketURL);
			URI listenerURI = new URI(socketURL);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			storeWebSocketclient.connect(storeWebSocket, listenerURI, request);
			storeWebSocket.awaitClose(30, TimeUnit.SECONDS);
			System.out.printf("Connecting to : %s%n", listenerURI);
		} catch (Exception e) {
			System.out.printf("WebSocket Clinet Error: ", e);
		}
		
	}

}
