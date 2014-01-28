package net.octacomm.sample.service;

import net.octacomm.logger.Log;
import net.octacomm.sample.netty.listener.MessageSender;
import net.octacomm.sample.netty.listener.MessageSenderAware;
import net.octacomm.sample.netty.listener.UsnMessageListener;
import net.octacomm.sample.netty.msg.NotifyMessageUpdateRequestMessage;
import net.octacomm.sample.netty.msg.RequestMessage;
import net.octacomm.sample.netty.usn.msg.common.IncomingMessage;
import net.octacomm.sample.netty.usn.msg.common.MessageHelper;
import net.octacomm.sample.netty.usn.msg.common.OutgoingMessage;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class UsnMessageProcessor implements
		UsnMessageListener<IncomingMessage>, MessageSenderAware<RequestMessage> {

	@Log private Logger logger;
	
	@Autowired
	@Qualifier("usn")
	private MessageSender<OutgoingMessage> usnMessageSender;
	private MessageSender<RequestMessage> guiMessageSensor;

	@Scheduled(fixedDelay = 10000)
	public void messageTest() {
		usnMessageSender.sendSyncMessage(MessageHelper.makeDummyOutgoing(33));
	}
	
	@Override
	public void messageReceived(IncomingMessage packet) {
		switch (packet.getMessageType()) {

			case DUMMY_INCOMMING:
				usnMessageSender.sendAsyncMessage(MessageHelper.makeDummyIncommingAck());
					break;
			
			default:
					break;
		}
	}

	@Override
	public void connectionStateChanged(boolean isConnected) {
		sendNotifyRouterStatusMsg(isConnected);
	}
	
	private void sendNotifyRouterStatusMsg(boolean isConnected) {
		if (guiMessageSensor != null) {
			guiMessageSensor.sendSyncMessage(new NotifyMessageUpdateRequestMessage(""));
		}
	}
	
	@Override
	public void setMessageSender(MessageSender<RequestMessage> messageSender) {
		this.guiMessageSensor = messageSender;
	}

}