package com.carbonai.cbam.exception;

/**
 * Runtime exception for domain-specific business errors.
 *
 * Beginner-friendly explanation:
 * This is used for problems that are not Java bugs, but business issues such as
 * "no default value was found" or "no emission factor exists for that input."
 */
public class BusinessException extends RuntimeException {

    private final String errorCode;

    /**
     * Creates a business exception with a machine-readable error code and a
     * human-readable message.
     */
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
