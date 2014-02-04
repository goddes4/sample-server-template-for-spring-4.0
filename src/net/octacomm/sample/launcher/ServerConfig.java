package net.octacomm.sample.launcher;

import java.util.concurrent.ThreadPoolExecutor;

import net.octacomm.logger.LoggerBeanPostProcessor;
import net.octacomm.network.ChannelGroup;
import net.octacomm.network.DefaultChannelGroup;
import net.octacomm.network.NioTcpServer;
import net.octacomm.sample.netty.server.handler.GuiServerHandler;
import net.octacomm.sample.netty.server.handler.GuiServerChannelInitializer;
import net.octacomm.sample.netty.usn.handler.UsnServerHandler;
import net.octacomm.sample.netty.usn.handler.UsnServerChannelInitializer;
import net.octacomm.sample.service.LoginServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@PropertySource("file:resources/bootstrap.properties")
@Import({MyBatisConfig.class})
@ComponentScan(basePackageClasses = LoginServiceImpl.class)
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class ServerConfig {

	@Bean
	public BeanPostProcessor loggerPostProcessor() {
		return new LoggerBeanPostProcessor();
	}
	
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("file:resources/message");
		messageSource.setCacheSeconds(3);
		return messageSource;
	}
	
	@Bean(destroyMethod = "shutdown")
	public TaskScheduler scheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(1);

		return scheduler;
	}

	@Bean(destroyMethod = "shutdown")
	public TaskExecutor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setQueueCapacity(10);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		
		return executor;
	}

	@Configuration
	static class GuiServerConfig {

		@Autowired
		private Environment env;
		
		@Bean
		public ChannelGroup guiChannelGroup() {
			return new DefaultChannelGroup();
		}
		
		@Bean
		@Scope("prototype")
		public GuiServerHandler guiServerHandler() {
			return new GuiServerHandler();
		}

		@Bean
		public GuiServerChannelInitializer guiServerPipelineFactory() {
			return new GuiServerChannelInitializer();
		}
		
		@Bean(destroyMethod = "close")
		public NioTcpServer guiTcpServer() {
			NioTcpServer tcpServer = new NioTcpServer();
			tcpServer.setLocalIP(env.getProperty("tcp.local.ip"));
			tcpServer.setLocalPort(env.getProperty("tcp.local.gui.port", int.class));
			tcpServer.setChannelInitializer(guiServerPipelineFactory());
			tcpServer.init();
			return tcpServer;
		}
	}

	@Configuration
	static class UsnServerConfig {

		@Autowired
		private Environment env;
		
		@Bean
		public UsnServerHandler usnServerHandler() {
			return new UsnServerHandler();
		}

		@Bean
		public UsnServerChannelInitializer usnServerChannelInitializer() {
			return new UsnServerChannelInitializer();
		}
		
		@Bean(destroyMethod = "close")
		public NioTcpServer usnTcpServer() {
			NioTcpServer tcpServer = new NioTcpServer();
			tcpServer.setLocalIP(env.getProperty("tcp.local.ip"));
			tcpServer.setLocalPort(env.getProperty("tcp.local.usn.port", int.class));
			tcpServer.setChannelInitializer(usnServerChannelInitializer());
			tcpServer.init();
			return tcpServer;
		}
	}	
}
