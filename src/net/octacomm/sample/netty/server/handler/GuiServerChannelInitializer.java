package net.octacomm.sample.netty.server.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;

public class GuiServerChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	@Autowired
	Provider<GuiServerHandler> guiServerHandlerProvider;
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		// Add the text line codec combination first.
		pipeline.addLast("encoder", new ObjectEncoder());
		pipeline.addLast("decoder", new ObjectDecoder(10 * 1024 * 1024, ClassResolvers.softCachingResolver(null)));
		
		// and then business logic.
		pipeline.addLast("handler", guiServerHandlerProvider.get());
	}

}
