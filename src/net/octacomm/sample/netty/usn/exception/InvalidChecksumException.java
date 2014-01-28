package net.octacomm.sample.netty.usn.exception;


public class InvalidChecksumException extends RuntimeException {

	public InvalidChecksumException(int checksum) {
		super("checksum : " + checksum);
	}
}
