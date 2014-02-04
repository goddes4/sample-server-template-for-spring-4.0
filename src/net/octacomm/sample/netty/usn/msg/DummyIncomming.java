package net.octacomm.sample.netty.usn.msg;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.octacomm.sample.netty.usn.msg.common.AbstractMessage;
import net.octacomm.sample.netty.usn.msg.common.IncomingMessage;
import net.octacomm.sample.netty.usn.msg.common.MessageHeader;

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
	public void decode(ByteBuf buffer) {
		dummyData = (buffer.readUnsignedByte());
	}
}
