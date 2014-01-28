package net.octacomm.sample.netty.usn.handler;

import javax.inject.Provider;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.springframework.beans.factory.annotation.Autowired;

public class UsnServerPipelineFactory implements ChannelPipelineFactory {

	@Autowired
	Provider<UsnServerHandler> usnServerHandlerProvider;
	
	@Override
	public ChannelPipeline getPipeline() {
		ChannelPipeline pipeline = Channels.pipeline();
		
		pipeline.addLast("encoder", new DefaultUsnServerEncoder());
		pipeline.addLast("decoder", new DefaultUsnServerDecoder());

		// business logic
		pipeline.addLast("hanlder", usnServerHandlerProvider.get());
		
		return pipeline;
	}

}
