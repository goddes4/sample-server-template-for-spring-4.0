package net.octacomm.sample.netty.usn.handler;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

import net.octacomm.sample.netty.usn.msg.common.OutgoingMessage;

public class DefaultUsnServerEncoder extends AbstractUsnServerEncoder {

	@Override
	public void encode(OutgoingMessage message, ByteBuf out) {
		out.order(ByteOrder.BIG_ENDIAN);
		
		out.writeByte(message.getMessageType().getId());
		out.writeByte(message.getHeader().getSeq());
		out.writeByte(message.getMessageType().getSize());
		message.encode(out);
	}

}
