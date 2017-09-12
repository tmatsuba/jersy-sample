package org.gside.jersy.sample.exception;

/**
 * 許可のない操作を行った時の例外クラス
 * 
 * @author matsuba
 *
 */
public class UnAuthorizedException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public UnAuthorizedException(String message) {
		super(message);
	}
}
