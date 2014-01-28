package net.octacomm.sample.netty.usn.msg.common;

import org.jboss.netty.buffer.ChannelBuffer;

public interface IncomingMessage {
	
	void decode(ChannelBuffer buffer);

	int checksum();

	MessageType getMessageType();
	
}
