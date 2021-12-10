package com.example.androidgalary;

import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;

public class Hinh {
  String duongdan;
  String tenhinh;
  Integer addDate;
  boolean check;
  ExifInterface exif = null;

  public Hinh(String duongdan, String tenhinh, Integer addDate) {
    this.duongdan = duongdan;
    this.tenhinh = tenhinh;
    this.addDate = addDate;
    this.check = false;
    try {
      exif = new ExifInterface(duongdan);
    } catch (IOException e) {
      exif = null;
    }
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

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    Hinh target = (Hinh) obj;
    return (this.tenhinh.equals(target.tenhinh)
        && this.duongdan.equals(target.duongdan)
        && this.addDate.equals(target.addDate));
  }
}
