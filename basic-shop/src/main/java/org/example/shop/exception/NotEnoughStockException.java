package org.example.shop.exception;

public class NotEnoughStockException extends RuntimeException {

	public NotEnoughStockException() {
		super();
	}

	public NotEnoughStockException(String message) {
		super(message);
	}

	public NotEnoughStockException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughStockException(Throwable cause) {
		super(cause);
	}

	protected NotEnoughStockException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}

/**
 * Exception 을 직접 만들고 싶은 경우, RuntimeException 상속 받아 상위 5개의 method overriding 해줌 이렇게 하는 이유는 오류 나는 경우, printStackTrace() 로 쭉 뽑아내기
 * 위함이고 근원적인 오류에 대해서도 계속해서 뽑기 위함
 */