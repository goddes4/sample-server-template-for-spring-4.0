package net.octacomm.sample.netty.server.handler;

import javax.inject.Provider;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.springframework.beans.factory.annotation.Autowired;

public class GuiServerPipelineFactory implements ChannelPipelineFactory {
	
	@Autowired
	Provider<GuiServerHandler> guiServerHandlerProvider;
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// Create a default pipeline implementation.
		ChannelPipeline pipeline = Channels.pipeline();
		
		// Add the text line codec combination first.
		pipeline.addLast("encoder", new ObjectEncoder());
		pipeline.addLast("decoder", new ObjectDecoder(10 * 1024 * 1024, ClassResolvers.softCachingResolver(null)));
		
		// and then business logic.
		pipeline.addLast("handler", guiServerHandlerProvider.get());
		
		return pipeline;
	}

}
