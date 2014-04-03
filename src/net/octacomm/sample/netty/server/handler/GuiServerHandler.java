package net.octacomm.sample.netty.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import net.octacomm.network.ChannelGroup;
import net.octacomm.sample.domain.Domain;
import net.octacomm.sample.domain.User;
import net.octacomm.sample.netty.client.exception.ServiceNotFoundException;
import net.octacomm.sample.netty.listener.MessageSender;
import net.octacomm.sample.netty.listener.MessageSenderAware;
import net.octacomm.sample.netty.msg.BooleanResponseMessage;
import net.octacomm.sample.netty.msg.ExceptionResponseMessage;
import net.octacomm.sample.netty.msg.PDU;
import net.octacomm.sample.netty.msg.RequestMessage;
import net.octacomm.sample.netty.msg.ResponseMessage;
import net.octacomm.sample.netty.msg.domain.DomainCRUDRequestMessage;
import net.octacomm.sample.netty.msg.login.LoginRequestMessage;
import net.octacomm.sample.netty.msg.login.LoginResponseMessage;
import net.octacomm.sample.netty.msg.login.LoginResult;
import net.octacomm.sample.service.LoginService;
import net.octacomm.sample.service.crud.CRUDService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * Spring에 빈으로 등록되며, Scope는 Prototype이다.
 * 즉 채널마다 인스턴스가 생성된다..
 * 
 * @author tykim
 *
 */
public class GuiServerHandler extends SimpleChannelInboundHandler<RequestMessage> implements MessageSender<RequestMessage> {

	private static final int SYNC_MESSAGE_TIMEOUT_SEC = 1000;
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoginService loginService;
	
	@Resource(name = "guiChannelGroup")
	private ChannelGroup channelGroup;

	/**
	 * MessageSenderRegistry 는 GUI의 경우 Session이 새로 생성이 되면
	 * prototype으로 핸들러가 빈으로 생성되기 때문에 초기화 시점에서 @Autowired로 DI 할 수 없다.
	 * Connection이 이루어진 다음에 MessageSender를 전달해야 한다.
	 * 
	 * 즉, 본 핸들러의 channelConnected 메소드를 통해서 전달한다.
	 */
	@Autowired
	private List<MessageSenderAware<RequestMessage>> messageSenderRegistries;
	
	@Autowired(required = false)
	private CRUDService<Domain> crudService;
	
	private BlockingQueue<PDU> recvLock = new SynchronousQueue<PDU>();
	
	private Channel channel;
	private String userId;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestMessage request) throws Exception {

		logger.debug("{} Recv Message : {}", channel, request);

		ResponseMessage response;
		try {
			response = process(request);
		} catch (RuntimeException ex) {
			response = new ExceptionResponseMessage(ex);
		}
		sendResponseMessage(response);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		channel = ctx.channel();
		logger.info("channelActive : {}", channel);
		for (MessageSenderAware<RequestMessage> messageSenderRegistry : messageSenderRegistries) {
			messageSenderRegistry.setMessageSender(this);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channelInactive : {}", channel.remoteAddress());
		
		if (userId != null) {
			channelGroup.removeChannel(userId);
			loginService.logout(userId);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("", cause);
	}

	private void sendResponseMessage(ResponseMessage res) {
		channel.writeAndFlush(res).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);;
		logger.debug("{} Send Message : {}", channel.remoteAddress(), res);
	}

	public ResponseMessage process(RequestMessage request) {
		switch (request.getMessageType()) {

			case LOGIN_REQUEST:
				LoginRequestMessage req = (LoginRequestMessage) request;
				LoginResponseMessage loginResponseMessage = req.process(loginService);
				
				if (loginResponseMessage.getResult() == LoginResult.SUCCESS) {
					userId = req.getId();
					channelGroup.addChannel(userId, channel);
				}
				
				return loginResponseMessage;
				
			case DOMAIN_CRUD_REQUEST:
				DomainCRUDRequestMessage domainCRUDRequestMessage = (DomainCRUDRequestMessage) request;
				return domainCRUDRequestMessage.process((CRUDService<?>) getCRUDService(domainCRUDRequestMessage.getDomainClass()));
				
			default:
				throw new UnsupportedOperationException(request.getMessageType() + " is not supported.");
		}
	}

	private Object getCRUDService(Class<? extends Domain> domainClass) {
		if (domainClass == User.class) {
			return null;
		} else {
			throw new ServiceNotFoundException();
		}
	}

	@Override
	public boolean sendSyncMessage(RequestMessage packet) {
		if (channel == null || !channel.isActive()) return false;
    	
		logger.info("send : {}", packet);
		
    	recvLock.clear();
		channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);;

		BooleanResponseMessage response;
		try {
			response = (BooleanResponseMessage) recvLock.poll(
					SYNC_MESSAGE_TIMEOUT_SEC, TimeUnit.MILLISECONDS);

			if (response != null) {
				return response.isResult();
			}
		} catch (InterruptedException e) {
			logger.warn("{}", e.getMessage());
		}
    	
    	return false;		
	}

	@Override
	public ListenableFuture<Boolean> sendAsyncMessage(RequestMessage packet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isConnected() {
		return channel.isActive();
	}
}
