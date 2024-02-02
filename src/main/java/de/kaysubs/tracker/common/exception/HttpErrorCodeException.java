package de.kaysubs.tracker.common.exception;

public class HttpErrorCodeException extends HttpException {
    private final int errorCode;

    public HttpErrorCodeException(int errorCode) {
        this(errorCode, "Errorcode " + errorCode);
    }

    public HttpErrorCodeException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpErrorCodeException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public HttpErrorCodeException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public HttpErrorCodeException(int errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
