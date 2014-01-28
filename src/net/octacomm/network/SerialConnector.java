package net.octacomm.network;

import gnu.io.SerialPort;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import net.octacomm.sample.netty.listener.MessageSender;
import net.octacomm.sample.netty.usn.msg.common.OutgoingMessage;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import de.uniluebeck.itm.nettyrxtx.RXTXChannelConfig.Databits;
import de.uniluebeck.itm.nettyrxtx.RXTXChannelConfig.Paritybit;
import de.uniluebeck.itm.nettyrxtx.RXTXChannelConfig.Stopbits;
import de.uniluebeck.itm.nettyrxtx.RXTXChannelFactory;
import de.uniluebeck.itm.nettyrxtx.RXTXDeviceAddress;

@Qualifier("remocon")
public class SerialConnector implements MessageSender<OutgoingMessage> {

	private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private int stopbits = SerialPort.STOPBITS_1;
    private int databits = SerialPort.DATABITS_8;
    private int paritybit = SerialPort.PARITY_NONE;

	private String deviceAddress;
    private int baudrate;
    private ChannelPipelineFactory pipelineFactory;
    private Channel channel;

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public void setPipelineFactory(ChannelPipelineFactory pipelineFactory) {
        this.pipelineFactory = pipelineFactory;
    }

    public boolean start() {
        final ClientBootstrap bootstrap = new ClientBootstrap(
                new RXTXChannelFactory(Executors.newCachedThreadPool()));

        // Configure the event pipeline factory.
        bootstrap.setPipelineFactory(pipelineFactory);
        bootstrap.setOption("baudrate", baudrate);
        bootstrap.setOption("stopbits", Stopbits.ofValue(stopbits));
        bootstrap.setOption("databits", Databits.ofValue(databits));
        bootstrap.setOption("paritybit", Paritybit.ofValue(paritybit));

        // Make a new connection.
        ChannelFuture future = bootstrap.connect(new RXTXDeviceAddress(deviceAddress));
        future.awaitUninterruptibly();
        if (!future.isSuccess()) {
        	logger.error("Remocon channel is disconnected", future.getCause());
        	return false;
        } else {
        	logger.info("Remocon channel is connected");
        }
        
        channel = future.getChannel();
        
        future.getChannel().getCloseFuture().addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture cf) throws Exception {
                // An Executor cannot be shut down from the thread acquired from itself.  
                // Please make sure you are not calling releaseExternalResources() from an I/O worker thread.
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                    	channel = null;
                    	logger.info("Channel is closed.");
                        bootstrap.releaseExternalResources();
                    }
                }).start();
            }
        });
        return true;
    }
    
    public boolean stop() {
    	if (channel != null && channel.isConnected()) {
    		channel.close();
    		return true;
    	} else {
    		return false;
    	}
    }

	@Override
	public boolean isConnected() {
		if (channel != null) {
			return channel.isConnected();
		} else {
			return false;
		}
	}

	@Override
	public boolean sendSyncMessage(OutgoingMessage packet) {
		channel.write(packet, new RXTXDeviceAddress(deviceAddress));
		return true;
	}

	@Override
	public Future<Boolean> sendAsyncMessage(OutgoingMessage packet) {
		throw new UnsupportedOperationException();
	}

	public void forceClose() {
		if (channel != null) {
			channel.close();
		}
	}

    private static final Logger log = LoggerFactory.getLogger(SerialConnector.class);

    public static void main(String[] args) {
    	SerialConnector connector = new SerialConnector();
        connector.setDeviceAddress("COM3");
        connector.setBaudrate(19200);
        connector.setPipelineFactory(new ChannelPipelineFactory() {

            @Override
            public ChannelPipeline getPipeline() throws Exception {
                DefaultChannelPipeline pipeline = new DefaultChannelPipeline();
                pipeline.addLast("loggingHandler", new SimpleChannelHandler() {

                    @Override
                    public void messageReceived(final ChannelHandlerContext ctx, final MessageEvent e)
                            throws Exception {
                        log.debug("{}", Arrays.toString(((ChannelBuffer) e.getMessage()).array()));
                        super.messageReceived(ctx, e);
                    }
                });
                return pipeline;
            }
        });
        connector.start();
    }

}
