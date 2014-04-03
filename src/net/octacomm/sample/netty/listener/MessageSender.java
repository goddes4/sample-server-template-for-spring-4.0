package net.octacomm.sample.netty.listener;

import org.springframework.util.concurrent.ListenableFuture;

public interface MessageSender<T> {
	
	boolean isConnected();
	
	boolean sendSyncMessage(T packet);
	
	ListenableFuture<Boolean> sendAsyncMessage(T packet);
	
}
