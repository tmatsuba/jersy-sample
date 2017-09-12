package org.gside.jersy.sample.exception;

/**
 * サービスクラス用の基底例外クラス
 * 
 * @author matsuba
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String message;
	public ServiceException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
