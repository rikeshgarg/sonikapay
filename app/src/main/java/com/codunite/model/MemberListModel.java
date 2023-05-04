package com.codunite.model;

public class MemberListModel {
    private String memberId, name, walBal, status, userCode, wall_bal;

    public MemberListModel(String memberId, String name, String walBal, String status, String userCode, String wall_bal) {
        this.memberId = memberId;
        this.name = name;
        this.walBal = walBal;
        this.status = status;
        this.userCode = userCode;
        this.wall_bal = wall_bal;
    }

    public String getWall_bal() {
        return wall_bal;
    }

    public void setWall_bal(String wall_bal) {
        this.wall_bal = wall_bal;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWalBal() {
        return walBal;
    }

    public void setWalBal(String walBal) {
        this.walBal = walBal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
