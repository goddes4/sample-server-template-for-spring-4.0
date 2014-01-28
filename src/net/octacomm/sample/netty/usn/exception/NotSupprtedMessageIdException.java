package net.octacomm.sample.netty.usn.exception;

public class NotSupprtedMessageIdException extends RuntimeException {
	
	public NotSupprtedMessageIdException(int messageId) {
		super(messageId + " is invalid.");
	}
}
