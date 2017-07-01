package ru.terra.universal.shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by terranz on 01.07.17.
 */
public class AccountInfo implements Serializable {
    private String login, pass;
    private Boolean banned;
    private Date lastLogin;
    private Long banTime;
    private Integer attemptsToLogin;
    private Long uid;

    public AccountInfo() {
    }

    public AccountInfo(String login, String pass, Boolean banned, Date lastLogin, Long banTime, Integer attemptsToLogin, Long uid) {
        this.login = login;
        this.pass = pass;
        this.banned = banned;
        this.lastLogin = lastLogin;
        this.banTime = banTime;
        this.attemptsToLogin = attemptsToLogin;
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Long getBanTime() {
        return banTime;
    }

    public void setBanTime(Long banTime) {
        this.banTime = banTime;
    }

    public Integer getAttemptsToLogin() {
        return attemptsToLogin;
    }

    public void setAttemptsToLogin(Integer attemptsToLogin) {
        this.attemptsToLogin = attemptsToLogin;
    }
}
