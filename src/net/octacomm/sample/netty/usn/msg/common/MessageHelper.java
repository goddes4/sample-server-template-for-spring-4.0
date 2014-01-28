package net.octacomm.sample.netty.usn.msg.common;


import net.octacomm.sample.netty.usn.msg.DummyIncommingAck;
import net.octacomm.sample.netty.usn.msg.DummyOutgoing;

/**
 * 송신 메시지를 만들어 주는 헬퍼 클래스
 * 
 * @author taeyo
 *
 */
public class MessageHelper {
	
	
	public static OutgoingMessage makeDummyOutgoing(int dummyData) {
		return new DummyOutgoing(dummyData);
	}

	public static OutgoingMessage makeDummyIncommingAck() {
		return new DummyIncommingAck();
	}
}
