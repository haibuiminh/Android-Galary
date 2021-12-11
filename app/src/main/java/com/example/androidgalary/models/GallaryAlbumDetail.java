package com.example.androidgalary.models;

public class GallaryAlbumDetail {
  String name;
  String path;
  Integer imageNumbers;

  public GallaryAlbumDetail(String name, String path, Integer imageNumbers) {
    this.name = name;
    this.path = path;
    this.imageNumbers = imageNumbers;
  }

  public String getName() {
    return name;
  }

  public String getPath() {
    return path;
  }

  public Integer getImageNumbers() {
    return imageNumbers;
  }
}
