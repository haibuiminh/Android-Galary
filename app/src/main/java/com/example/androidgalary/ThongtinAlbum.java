package com.example.androidgalary;

public class ThongtinAlbum {
  String ten;
  String duongdan;
  Integer soluong;

  public ThongtinAlbum(String ten, String duongdan, Integer soluong) {
    this.ten = ten;
    this.duongdan = duongdan;
    this.soluong = soluong;
  }

  public String getTen() {
    return ten;
  }

  public String getDuongdan() {
    return duongdan;
  }

  public Integer getSoluong() {
    return soluong;
  }
}
