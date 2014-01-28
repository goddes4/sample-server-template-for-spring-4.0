package net.octacomm.sample.netty.usn.handler;

import java.nio.ByteOrder;

import net.octacomm.sample.netty.usn.msg.common.MessageHeader;
import net.octacomm.sample.netty.usn.msg.common.OutgoingMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class DefaultUsnServerEncoder extends AbstractUsnServerEncoder {

	@Override
	public ChannelBuffer encode(OutgoingMessage message) {
		
		int bufferLength = MessageHeader.getRequiredHeaderSize() + message.getMessageType().getSize();
		ChannelBuffer writeBuffer = ChannelBuffers.directBuffer(ByteOrder.BIG_ENDIAN, bufferLength);
		
		writeBuffer.writeByte(message.getMessageType().getId());
		writeBuffer.writeByte(message.getHeader().getSeq());
		writeBuffer.writeByte(message.getMessageType().getSize());
		message.encode(writeBuffer);

		return writeBuffer;
	}

}
