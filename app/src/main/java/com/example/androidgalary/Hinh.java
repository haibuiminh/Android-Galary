package com.example.androidgalary;

public class Hinh {
    String duongdan;
    String tenhinh;
    Integer addDate;
    boolean check;

    public Hinh(String duongdan, String tenhinh, Integer addDate) {
        this.duongdan = duongdan;
        this.tenhinh = tenhinh;
        this.addDate = addDate;
        this.check = false;
    }

    public String getDuongdan() {
        return duongdan;
    }

    public void setDuongdan(String duongdan) {
        this.duongdan = duongdan;
    }

    public String getTenHinh() {
        return tenhinh;
    }

    public void setTenHinh(String tenhinh) {
        this.tenhinh = tenhinh;
    }

    public Integer getAddDate() {
        return addDate;
    }

    public void setAddDate(Integer addDate) {
        this.addDate = addDate;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
