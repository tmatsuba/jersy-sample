package org.gside.jersy.sample.exception;

/**
 * 入力が不正であった場合の例外
 * 
 * @author matsuba
 *
 */
public class IllegalInputException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public IllegalInputException(String message) {
		super(message);
	}

}
