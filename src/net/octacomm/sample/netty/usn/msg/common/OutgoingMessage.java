package net.octacomm.sample.netty.usn.msg.common;

import io.netty.buffer.ByteBuf;

public interface OutgoingMessage {

	void encode(ByteBuf buffer);

	int checksum();
	
	MessageType getMessageType();
	
	MessageHeader getHeader();

}
