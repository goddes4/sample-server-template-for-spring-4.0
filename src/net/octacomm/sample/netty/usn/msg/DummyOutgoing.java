package net.octacomm.sample.netty.usn.msg;

import lombok.Getter;
import lombok.ToString;
import net.octacomm.sample.netty.usn.msg.common.AbstractMessage;
import net.octacomm.sample.netty.usn.msg.common.MessageHeader;
import net.octacomm.sample.netty.usn.msg.common.MessageType;
import net.octacomm.sample.netty.usn.msg.common.OutgoingMessage;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * request가 true이면 TimeSync 요청 메시지 이고
 *           false 이면 NotifyTimeSync 메시지의 Ack 이다.
 * 
 * @author taeyo
 *
 */
@Getter
@ToString(callSuper = true)
public class DummyOutgoing extends AbstractMessage implements OutgoingMessage {

	private int dummyData;

	public DummyOutgoing(int dummyData) {
		super(new MessageHeader(MessageType.DUMMY_OUTGOING));		
		this.dummyData = dummyData;
	}

	@Override
	public void encode(ChannelBuffer buffer) {
		buffer.writeByte(dummyData);
	}

	@Override
	public int bodyDataSum() {
		return dummyData;
	}
}
