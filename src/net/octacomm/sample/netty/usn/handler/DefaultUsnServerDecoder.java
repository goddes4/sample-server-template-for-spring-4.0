package net.octacomm.sample.netty.usn.handler;

import net.octacomm.sample.netty.usn.msg.common.IncomingMessage;
import net.octacomm.sample.netty.usn.msg.common.MessageHeader;
import net.octacomm.sample.netty.usn.msg.common.MessageType;

import org.jboss.netty.buffer.ChannelBuffer;

public class DefaultUsnServerDecoder extends AbstractUsnServerDecoder {

	@Override
	public MessageHeader makeMessageHeader(ChannelBuffer buffer) {
		int msgId = buffer.readUnsignedByte();
		int seq = buffer.readUnsignedByte();
		int size = buffer.readUnsignedByte();
		MessageHeader header = new MessageHeader(MessageType.valueOf(msgId));
		header.setSeq(seq);
		header.setSize(size);
		return header;
	}

	@Override
	public void discardBufferByFailHeader(ChannelBuffer buffer) {
		buffer.readBytes(buffer.readableBytes());
	}

	@Override
	public void discardBufferByFailBody(ChannelBuffer buffer, MessageHeader header) {
		buffer.readBytes(header.getRequiredBodySize());
	}

	@Override
	public boolean processChecksum(ChannelBuffer buffer,
			IncomingMessage incomingMessage) {
		return true;
	}

}
