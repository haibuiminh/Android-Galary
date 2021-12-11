package com.example.androidgalary.models;

import androidx.exifinterface.media.ExifInterface;
import java.io.IOException;

public class GallaryImage {
  String path;
  String name;
  Integer addDate;
  boolean check;
  ExifInterface exif;

  public GallaryImage(String path, String name, Integer addDate) {
    this.path = path;
    this.name = name;
    this.addDate = addDate;
    this.check = false;
    try {
      exif = new ExifInterface(path);
    } catch (IOException e) {
      exif = null;
    }
  }

  public String getPath() {
    return path;
  }

  public String getTenHinh() {
    return name;
  }

  public Integer getAddDate() {
    return addDate;
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
    GallaryImage target = (GallaryImage) obj;
    return (this.name.equals(target.name)
        && this.path.equals(target.path)
        && this.addDate.equals(target.addDate));
  }

  public ExifInterface getExif() {
    return exif;
  }

  public void setExif(ExifInterface exif) {
    this.exif = exif;
  }
}
