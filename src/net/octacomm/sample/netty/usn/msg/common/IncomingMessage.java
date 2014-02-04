package net.octacomm.sample.netty.usn.msg.common;

import io.netty.buffer.ByteBuf;

public interface IncomingMessage {
	
	void decode(ByteBuf buffer);

	int checksum();

	MessageType getMessageType();
	
}
