package net.octacomm.network;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Taeyoung,Kim
 */
public class NioTcpServer extends SimpleChannelUpstreamHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    // Spring DI
    private String localIP;
    private int localPort;
    private ByteOrder byteOrder;
    private ChannelPipelineFactory pipelineFactory;
    private ChannelGroup channelGroup;
    
    private	Channel channel;
    private	ServerBootstrap bootstrap;

    @Override
    public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        logger.info(e.getChannel() + " NioTcpServer is bound and started to accept incoming connections .");
    }

    @Override
    public void childChannelOpen(ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
        logger.info(e.getChildChannel() + " Client is connected.");
        if (channelGroup != null) {
            channelGroup.addChannel(e.getChildChannel());
        }
    }

    @Override
    public void childChannelClosed(ChannelHandlerContext ctx, ChildChannelStateEvent e) throws Exception {
        logger.info(e.getChildChannel() + " Client is disconnected.");
        if (channelGroup != null) {
            channelGroup.removeChannel(e.getChildChannel());
        }
    }

    public void setLocalIP(String serverIP) {
        this.localIP = serverIP;
    }

    public void setLocalPort(int serverPort) {
        this.localPort = serverPort;
    }

    public void setPipelineFactory(ChannelPipelineFactory pipelineFactory) {
        this.pipelineFactory = pipelineFactory;
    }

    public void setChannelGroup(ChannelGroup channelGroup) {
        this.channelGroup = channelGroup;
    }
    
    public void setByteOrder(String byteOrder) {
    	if (byteOrder.equals("LITTLE")) {
    		this.byteOrder = ByteOrder.LITTLE_ENDIAN;
    	} else if (byteOrder.equals("BIG")) {
    		this.byteOrder = ByteOrder.BIG_ENDIAN; 
    	}
    }

    public void init() {
        // Configure the server.
        bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        // Configure the pipeline factory.
        bootstrap.setParentHandler(this);
        bootstrap.setPipelineFactory(pipelineFactory);

        logger.debug("---------- Netty Option ----------");
        bootstrap.setOption("child.tcpNoDelay", true);
        logger.debug(" tcpNoDelay is true");
        bootstrap.setOption("child.keepAlive", true);
        logger.debug(" keepAlive is true");

        /*
        bootstrap.setOption("bufferFactory", 
        		HeapChannelBufferFactory.getInstance(ChannelBuffers.LITTLE_ENDIAN));
        		*/
        logger.debug(" Native byte order is {}", ByteOrder.nativeOrder());
        if (byteOrder != null) {
        	logger.debug(" Byte order is changed into {}", byteOrder);
        	bootstrap.setOption("child.bufferFactory", new HeapChannelBufferFactory(byteOrder));
        }

        // Bind and start to accept incoming connections.
        if (localPort == 0) {
            // Set the options
            bootstrap.setOption("localAddress", new InetSocketAddress(10000));
            channel = bootstrap.bind();
        } else if (localIP == null || localIP.isEmpty()) {
        	channel = bootstrap.bind(new InetSocketAddress(localPort));
        } else {
        	channel = bootstrap.bind(new InetSocketAddress(localIP, localPort));
        }
    }
    
    public void close() {
    	if (bootstrap != null) {
    		if (channelGroup != null) {
    			channelGroup.removeAllChannels();
    		}
    		channel.close();
    		logger.info("releaseExternalResources");
    		bootstrap.releaseExternalResources();
    	}
    }
}
