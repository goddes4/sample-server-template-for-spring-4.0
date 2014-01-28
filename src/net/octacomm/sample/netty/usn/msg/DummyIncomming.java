package net.octacomm.sample.netty.usn.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.octacomm.sample.netty.usn.msg.common.IncomingMessage;
import net.octacomm.sample.netty.usn.msg.common.MessageHeader;
import net.octacomm.sample.netty.usn.msg.common.AbstractMessage;

import org.jboss.netty.buffer.ChannelBuffer;

@Setter
@Getter
@ToString(callSuper = true)
public class DummyIncomming extends AbstractMessage implements IncomingMessage {

	private int dummyData;
	
	public DummyIncomming(MessageHeader header) {
		super(header);
	}

	@Override
	public int bodyDataSum() {
		return dummyData;
	}

	@Override
	public void decode(ChannelBuffer buffer) {
		dummyData = (buffer.readUnsignedByte());
	}
}
