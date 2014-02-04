package net.octacomm.sample.netty.usn.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import net.octacomm.sample.netty.listener.MessageSender;
import net.octacomm.sample.netty.listener.UsnMessageListener;
import net.octacomm.sample.netty.usn.exception.InvalidDataSizeException;
import net.octacomm.sample.netty.usn.exception.NotSupprtedMessageIdException;
import net.octacomm.sample.netty.usn.msg.common.IncomingMessage;
import net.octacomm.sample.netty.usn.msg.common.OutgoingMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

/**
 * USN 과 연결되는 Channel을 통해서 데이터의 read, write 를 처리한다.
 * MessageSensor<MessagePacket> 은 본 채널을 통해서 전송하기 위해서 사용하는 인터페이스.
 * 
 * MessageSensor 를 구현한 핸들러는 GuiServerHandler, UsnServerHandler 총 2개가 되며,
 * @Autowired를 사용하기 위해 Qualifier 사용한다.
 * 
 * @author taeyo
 *
 */
@Qualifier("usn")
public class UsnServerHandler extends SimpleChannelInboundHandler<IncomingMessage> implements MessageSender<OutgoingMessage> {

	private static final int SYNC_MESSAGE_TIMEOUT_SEC = 2000;
	private static final int RETRY_COUNT = 3;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private BlockingQueue<IncomingMessage> recvLock = new SynchronousQueue<IncomingMessage>();
	private Channel channel;

	@Autowired
	private UsnMessageListener<IncomingMessage> listener;
	
	private String getChannelName() {
		return channel.remoteAddress().toString();
	}


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		channel = ctx.channel();
		logger.debug("{} is connected", getChannelName());
		listener.connectionStateChanged(true);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("{} was disconnected", getChannelName());
		listener.connectionStateChanged(false);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IncomingMessage packet)
			throws Exception {
		
		logger.info("[Incomming] {} {}", getChannelName(), packet);

		if (isAckMessage(packet)) {
			recvLock.offer(packet);
		} else {
			listener.messageReceived(packet);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		
		logger.error("{}", getChannelName(), cause);
		
		if (cause instanceof NotSupprtedMessageIdException
				|| cause instanceof InvalidDataSizeException) {
			ctx.channel().close();
		}
	}


	/**
	 * IncommingPacket 이면서 suffix가 Ack 인 클래스의 경우
	 * sendSyncMessage() 에서 보낸 메시지의 Ack로 인식할 수 있도록 함
	 * 
	 * 예) EnemyHitResult -> EnemyHitResultAck
	 * 
	 * @param packet
	 * @return
	 */
	private boolean isAckMessage(IncomingMessage packet) {
		return packet.getClass().getSimpleName().endsWith("Ack");
	}

    /**
     * 응답이 요구 되는 메시지의 경우 BlockingQueue를 사용하여, 
     * 응답메시지가 오기를 기다린후 응답시간 10000 msec 이 초과했을때 실패로 간주한다.
     * - 응답 메시지는 요청 메시지와 메시지 아이디가 같다.
     * - ACK가 오지 않을 경우 3회 재전송
     */
    @Override
    public boolean sendSyncMessage(OutgoingMessage packet) {
    	if (channel == null || !channel.isActive()) return false;
    	
    	int retryCount = 0;

    	// 3회 재전송
    	while(retryCount < RETRY_COUNT) {
    		if (send(packet)) {
    			return true;
    		}
    		retryCount++;
    	}
    	return false;
    }

	private boolean send(OutgoingMessage packet) {
		recvLock.clear();
		channel.writeAndFlush(packet);

		IncomingMessage recvMessage;
		try {
			recvMessage = recvLock.poll(SYNC_MESSAGE_TIMEOUT_SEC, TimeUnit.MILLISECONDS);

			if (recvMessage != null) {
				if ((packet.getMessageType().getId() + 0x80) == recvMessage.getMessageType().getId()) {
					return true;
				}
			}
		} catch (InterruptedException e) {
			logger.warn("{}", e.getMessage());
		}
    	
    	return false;
	}

    /**
     * 비동기로 메시지 전송 (executor의 ThreadPool 이용) 
     * Ack 메시지 사용
     * 
     */
    @Async("executor")
	@Override
	public Future<Boolean> sendAsyncMessage(OutgoingMessage packet) {
    	if (channel == null) return new AsyncResult<Boolean>(false);
    	
    	logger.info("{}", packet);
    	try {
    		channel.writeAndFlush(packet);
    	} catch (RuntimeException e) {
    		logger.error("{}", e);
			return new AsyncResult<Boolean>(false);
		}
    	return new AsyncResult<Boolean>(true);
	}

	@Override
	public boolean isConnected() {
		if (channel != null) {
			return channel.isActive();
		} else {
			return false;
		}
	}	
}
