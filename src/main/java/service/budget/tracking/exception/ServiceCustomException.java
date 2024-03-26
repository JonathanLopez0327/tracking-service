package service.budget.tracking.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceCustomException extends RuntimeException {
    private String errorCode;
    private int status;

    public ServiceCustomException(String message, String erroCode) {
        super(message);
        this.errorCode = erroCode;
    }

    public ServiceCustomException(String message, String erroCode, int status) {
        super(message);
        this.errorCode = erroCode;
        this.status = status;
    }
}
