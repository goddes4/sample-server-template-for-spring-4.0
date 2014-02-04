package net.octacomm.sample.netty.usn.handler;

import io.netty.buffer.ByteBuf;
import net.octacomm.sample.netty.usn.msg.common.IncomingMessage;
import net.octacomm.sample.netty.usn.msg.common.MessageHeader;
import net.octacomm.sample.netty.usn.msg.common.MessageType;

public class DefaultUsnServerDecoder extends AbstractUsnServerDecoder {

	@Override
	public MessageHeader makeMessageHeader(ByteBuf buffer) {
		int msgId = buffer.readUnsignedByte();
		int seq = buffer.readUnsignedByte();
		int size = buffer.readUnsignedByte();
		MessageHeader header = new MessageHeader(MessageType.valueOf(msgId));
		header.setSeq(seq);
		header.setSize(size);
		return header;
	}

	@Override
	public void discardBufferByFailHeader(ByteBuf buffer) {
		buffer.readBytes(buffer.readableBytes());
	}

	@Override
	public void discardBufferByFailBody(ByteBuf buffer, MessageHeader header) {
		buffer.readBytes(header.getRequiredBodySize());
	}

	@Override
	public boolean processChecksum(ByteBuf buffer,
			IncomingMessage incomingMessage) {
		return true;
	}

}
