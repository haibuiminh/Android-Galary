package com.example.androidgalary.image;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidgalary.FragmentCallbacks;
import com.example.androidgalary.R;
import com.example.androidgalary.adapters.customListImageView.CustomListviewImageAdapter;
import com.example.androidgalary.models.GallaryImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

// Fragment dùng để show tất cả ảnh trong điện thoại theo ngày chụp
public class GallaryImageFragment extends Fragment implements FragmentCallbacks {
  Context context = null;
  public static final Uri Image_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
  public static ArrayList<GallaryImage> mangHinh = new ArrayList<>();
  public static ArrayList<ArrayList<GallaryImage>> mangHinhDate = new ArrayList<>();
  public static LinearLayoutManager linearLayoutManager = null;
  public static RecyclerView listView;
  CustomListviewImageAdapter customListviewImageAdapter;
  View view;

  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    context = getActivity();

    linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    view = inflater.inflate(R.layout.anh_layout, container, false);
    listView = view.findViewById(R.id.lvimg);

    return view;
  }

  @Override
  public void onResume() {
    super.onResume();

    // ***Khởi tạo các mảng***//
    Map<Integer, ArrayList<GallaryImage>> mapImage =
        new TreeMap<>(Collections.<Integer>reverseOrder());
    mangHinh = new ArrayList<>();
    ContentResolver contentResolver = getActivity().getContentResolver();

    Cursor cursor = contentResolver.query(Image_URI, null, null, null, null);
    cursor.moveToLast();
    while (!cursor.isBeforeFirst()) {
      @SuppressLint("Range")
      final String duongdanhinhanh =
          cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
      @SuppressLint("Range")
      String tenhinh =
          cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

      File TempFiles = new File(duongdanhinhanh);
      Date lastModDate = new Date(TempFiles.lastModified());
      SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
      Integer dateText = Integer.valueOf(df2.format(lastModDate));
      if (TempFiles.exists()) {
        GallaryImage currentImage = new GallaryImage(duongdanhinhanh, tenhinh, dateText);
        mangHinh.add(currentImage);
        if (mapImage.containsKey(dateText)) {
          {
            mapImage.get(dateText).add(currentImage);
          }
        } else {
          ArrayList<GallaryImage> temp = new ArrayList<>();
          temp.add(currentImage);
          mapImage.put(dateText, temp);
        }
      }
      cursor.moveToPrevious();
    }

    cursor.close();

    mangHinhDate.clear();
    mangHinhDate.addAll(mapImage.values());

    customListviewImageAdapter =
        new CustomListviewImageAdapter(context, mangHinhDate, R.layout.custom_item_listview_img);
    listView.setAdapter(customListviewImageAdapter);
    listView.setLayoutManager(linearLayoutManager);
  }

  @Override
  public void onMsgFromMainToFragment(String strValue) {}
}
