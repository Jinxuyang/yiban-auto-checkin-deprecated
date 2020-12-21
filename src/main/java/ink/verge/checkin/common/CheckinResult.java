package ink.verge.checkin.common;

import java.util.Date;

public class CheckinResult {
    private final Date date = new Date();
    private final long code;
    private final String message;
    private final String yibanMessage;

    public CheckinResult(long code, String message, String yibanMessage) {
        this.code = code;
        this.message = message;
        this.yibanMessage = yibanMessage;
    }

    public Date getDate() {
        return date;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getYibanMessage() {
        return yibanMessage;
    }

    @Override
    public String toString() {
        return "CheckinResult{" +
                "date=" + date +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", yibanMessage='" + yibanMessage + '\'' +
                '}';
    }
}
