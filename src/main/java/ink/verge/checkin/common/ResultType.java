package ink.verge.checkin.common;

/**
 * 返回类型
 * @Author Verge
 * @Date 2020/9/24 20:50
 */
public enum ResultType implements IErrorType {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败");

    private long code;
    private String message;

    ResultType(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
