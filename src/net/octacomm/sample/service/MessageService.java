package net.octacomm.sample.service;

import java.util.Locale;

import net.octacomm.logger.Log;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	@Log
	private Logger logger;
	
	@Autowired
	private MessageSource messageSource;
	
	@Scheduled(fixedRate = 100000)
	public void init() {
		logger.info("{}", messageSource.getMessage("test", new String[]{"김태영", "고형균"}, "No Message", Locale.KOREA));
		logger.info("{}", messageSource.getMessage("test", new String[]{"김태영", "고형균"}, "No Message", Locale.US));
	}
	
}
