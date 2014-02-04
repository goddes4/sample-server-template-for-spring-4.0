package net.octacomm.sample.netty.usn.msg;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.ToString;
import net.octacomm.sample.netty.usn.msg.common.AbstractMessage;
import net.octacomm.sample.netty.usn.msg.common.MessageHeader;
import net.octacomm.sample.netty.usn.msg.common.MessageType;
import net.octacomm.sample.netty.usn.msg.common.OutgoingMessage;

/**
 * 
 * @author taeyo
 *
 */
@Getter
@ToString(callSuper = true)
public class DummyIncommingAck extends AbstractMessage implements OutgoingMessage {

	public DummyIncommingAck() {
		super(new MessageHeader(MessageType.DUMMY_INCOMMING_ACK));		
	}

	@Override
	public void encode(ByteBuf buffer) {
	}

	public int bodyDataSum() {
		return 0;
	}
}
