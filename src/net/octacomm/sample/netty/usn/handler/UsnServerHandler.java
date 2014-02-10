package net.octacomm.sample.netty.usn.handler;

import net.octacomm.sample.netty.common.handler.AbstractServerHandler;
import net.octacomm.sample.netty.listener.MessageListener;
import net.octacomm.sample.netty.usn.msg.common.UsnIncomingMessage;
import net.octacomm.sample.netty.usn.msg.common.UsnOutgoingMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Qualifier("usn")
public class UsnServerHandler extends AbstractServerHandler<UsnIncomingMessage, UsnOutgoingMessage> {

	@Autowired
	@Qualifier("usn")
	@Override
	public void setListener(MessageListener<UsnIncomingMessage> listener) {
		super.listener = listener;
	}
}
