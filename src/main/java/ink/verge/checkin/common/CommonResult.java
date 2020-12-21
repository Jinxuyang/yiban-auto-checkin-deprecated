package ink.verge.checkin.common;

public class CommonResult {
    private long code;
    private String message;
    private Object data;

    public CommonResult() {
    }

    public CommonResult(long code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功消息
     * @return
     */
    public static CommonResult success(){
        return new CommonResult(ResultType.SUCCESS.getCode(),ResultType.SUCCESS.getMessage(),null);
    }

    /**
     * 返回成功消息
     * @param message 消息
     * @return
     */
    public static CommonResult success(String message){
        return new CommonResult(ResultType.SUCCESS.getCode(),message,null);
    }

    /**
     * 返回成功消息带数据
     * @param data 数据
     * @return
     */
    public static CommonResult success(Object data){
        return new CommonResult(ResultType.SUCCESS.getCode(),ResultType.SUCCESS.getMessage(),data);
    }

    /**
     * 返回成功消息带数据
     * @param message 消息
     * @param data 数据
     * @return
     */
    public static CommonResult success(String message,Object data){
        return new CommonResult(ResultType.SUCCESS.getCode(),message,data);
    }

    /**
     * 失败返回消息
     * @return
     */
    public static CommonResult failed(){
        return new CommonResult(ResultType.FAILED.getCode(),ResultType.FAILED.getMessage(),null);
    }

    /**
     * 失败返回消息
     * @param message 消息
     * @return
     */
    public static CommonResult failed(String message){
        return new CommonResult(ResultType.FAILED.getCode(),message,null);
    }

    /**
     * 失败返回消息带数据
     * @param data 数据
     * @return
     */
    public static CommonResult failed(Object data){
        return new CommonResult(ResultType.FAILED.getCode(),ResultType.FAILED.getMessage(),data);
    }

    /**
     * 失败返回消息带数据
     * @param message 消息
     * @param data 数据
     * @return
     */
    public static CommonResult failed(String message,Object data){
        return new CommonResult(ResultType.FAILED.getCode(),message,data);
    }

    /**
     * 失败返回消息
     * @param errorType 错误类型
     * @return
     */
    public static CommonResult failed(IErrorType errorType){
        return new CommonResult(errorType.getCode(),errorType.getMessage(),null);
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
