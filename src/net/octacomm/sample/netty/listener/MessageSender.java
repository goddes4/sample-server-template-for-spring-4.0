package net.octacomm.sample.netty.listener;

import java.util.concurrent.Future;

public interface MessageSender<T> {
	
	boolean isConnected();
	
	boolean sendSyncMessage(T packet);
	
	Future<Boolean> sendAsyncMessage(T packet);
	
}
