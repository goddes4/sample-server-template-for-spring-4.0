package net.octacomm.sample.netty.usn.msg.common;

import lombok.Getter;
import net.octacomm.sample.netty.usn.exception.NotSupprtedMessageIdException;
import net.octacomm.sample.netty.usn.msg.DummyIncomming;
import net.octacomm.sample.netty.usn.msg.DummyOutgoingAck;

@Getter
public enum MessageType {
	DUMMY_OUTGOING(0x01, 1),
	DUMMY_OUTGOING_ACK(0x81, DummyOutgoingAck.class),
	DUMMY_INCOMMING(0x02, DummyIncomming.class),
	DUMMY_INCOMMING_ACK(0x82, 0)
	;
	
	private Class<? extends IncomingMessage> incomingClass;
	@Getter	private int id;
	@Getter	private int size;
	private String desc;

	private MessageType(int id, int size) {
		this(id, size, null);
	}
	
	private MessageType(int id, Class<? extends IncomingMessage> incomingClass) {
		this(id, 0, incomingClass);
	}
	
	private MessageType(int id, int size, Class<? extends IncomingMessage> incomingClass) {
		this.id = id;
		this.size = size;
		this.incomingClass = incomingClass;
		
		desc = name() + String.format("{ id:%02X, size:%d, class:%s }", id, size, (incomingClass == null ? null : incomingClass.getSimpleName()));
	}
	
	public String toString() {
		return desc;
	}
	
	public IncomingMessage newInstance(MessageHeader header) throws ReflectiveOperationException {
		return incomingClass.getConstructor(MessageHeader.class).newInstance(header);
	}

	public static MessageType valueOf(int id) throws NotSupprtedMessageIdException {
		for (MessageType msg : MessageType.values()) {
			if (msg.id == id && msg.incomingClass != null) {
				return msg;
			}
		}
		throw new NotSupprtedMessageIdException(id);
	}
	
}
