package service.budget.tracking.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceCustomException extends RuntimeException {
    private String errorCode;
    private int status;

    public ServiceCustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceCustomException(String message, String errorCode, int status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
