/**
 * 
 */
package com.mydkvstore.httpservice;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;


/**
 * @author Pramod.Rathod
 *
 */
@WebSocket(maxTextMessageSize = 1024 * 1024)
public class StoreWebSocket {
	
	private CountDownLatch closeLatch;
	private Session session;

	/**
	 * 
	 */
	public StoreWebSocket() {
		this.closeLatch = new CountDownLatch(1);
	}
	
	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException
    {
        return this.closeLatch.await(duration, unit);
    }
	
	@OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown(); // trigger latch
    }
	
	@OnWebSocketConnect
    public void onConnect(Session session)
    {
        System.out.printf("Got connect: %s%n", session);
        this.session = session;
    }
	
	 @OnWebSocketMessage
	 public void onMessage(String msg)
	 {
	    System.out.printf("Got msg: %s%n", msg);
	 }

    @OnWebSocketError
    public void onError(Throwable cause)
    {
        System.out.print("WebSocket Error: ");
        cause.printStackTrace(System.out);
    }
    
    public void sendMessage(String message) {
    	try
        {
            //Future<Void> fut;
            //fut = 
    		session.getRemote().sendString(message);
            //fut.get(100, TimeUnit.MILLISECONDS); // wait for send to complete.
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }
    
    public void closeSession() {
    	session.close(StatusCode.NORMAL, "Done!");
    }

}
