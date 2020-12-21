package ink.verge.checkin.mbg.model;

import java.io.Serializable;

public class User implements Serializable {
    private Integer uid;

    private String account;

    private String password;

    private String address;

    private String mail;

    private Boolean morstatus;

    private Boolean noonstatus;

    private String openid;

    private static final long serialVersionUID = 1L;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Boolean getMorstatus() {
        return morstatus;
    }

    public void setMorstatus(Boolean morstatus) {
        this.morstatus = morstatus;
    }

    public Boolean getNoonstatus() {
        return noonstatus;
    }

    public void setNoonstatus(Boolean noonstatus) {
        this.noonstatus = noonstatus;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", uid=").append(uid);
        sb.append(", account=").append(account);
        sb.append(", password=").append(password);
        sb.append(", address=").append(address);
        sb.append(", mail=").append(mail);
        sb.append(", morstatus=").append(morstatus);
        sb.append(", noonstatus=").append(noonstatus);
        sb.append(", openid=").append(openid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}