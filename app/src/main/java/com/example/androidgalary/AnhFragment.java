package com.example.androidgalary;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

//Fragment dùng để show tất cả ảnh trong điện thoại theo ngày chụp
public class AnhFragment extends Fragment implements FragmentCallbacks {
    Context context = null;
    static final Uri Image_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    static ArrayList<Hinh> mangHinh = new ArrayList<>();

    static ArrayList<ArrayList<Hinh>> mangHinhDate = new ArrayList<>();
    static Map<Integer, ArrayList<Hinh>> mapImage = new TreeMap<>(Collections.<Integer>reverseOrder());
    boolean hasNewChanged = false;

    RecyclerView listView;
    CustomListviewImageAdapter customListviewImageAdapter;

    View view;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();

        view = inflater.inflate(R.layout.anh_layout, container, false);
        listView = view.findViewById(R.id.lvimg);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //***Khởi tạo các mảng***//
        Map<Integer, ArrayList<Hinh>> mapImage = new TreeMap<>(Collections.<Integer>reverseOrder());
        mangHinh = new ArrayList<>();
        ContentResolver contentResolver = getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(Image_URI, null, null, null, null);
        cursor.moveToLast();

        while (!cursor.isBeforeFirst()) {
            @SuppressLint("Range") final String duongdanhinhanh = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            @SuppressLint("Range") String tenhinh = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));

            File TempFiles = new File(duongdanhinhanh);
            Date lastModDate = new Date(TempFiles.lastModified());
            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
            Integer dateText = Integer.valueOf(df2.format(lastModDate));

            if (TempFiles.exists()) {
                Hinh currentImage = new Hinh(duongdanhinhanh, tenhinh, dateText);
                mangHinh.add(currentImage);
                if (mapImage.containsKey(dateText)) {
                    mapImage.get(dateText).add(currentImage);
                } else {
                    ArrayList<Hinh> temp = new ArrayList<>();
                    temp.add(currentImage);
                    mapImage.put(dateText, temp);
                }
            }
            cursor.moveToPrevious();
        }

        cursor.close();

        mangHinhDate.clear();
        mangHinhDate.addAll(mapImage.values());

        customListviewImageAdapter = new CustomListviewImageAdapter(context, mangHinhDate, R.layout.custom_item_listview_img);
        listView.setAdapter(customListviewImageAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onMsgFromMainToFragment(String strValue) {
    }
}
