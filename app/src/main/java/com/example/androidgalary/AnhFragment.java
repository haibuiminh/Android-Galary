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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//Activity dùng để show tất cả ảnh trong điện thoại theo ngày chụp
public class AnhFragment extends Fragment implements FragmentCallbacks {
    Context context = null;
    Uri Image_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    static ArrayList<Hinh> mangHinh;

    static ArrayList<Integer> mangDate;

    static ArrayList<ArrayList<Hinh>> mangHinhDate;
    //listview chua nhieu recycleview
    ListView listView;
    CustomListviewImageAdapter customListviewImageAdapter;

    View view;

    @Nullable
    @Override
    public void onResume() {
        long start = System.currentTimeMillis();
        super.onResume();
        Log.e("test", "AnhFragment");
        Log.e("test", "" + Image_URI.toString());
        //***Khởi tạo các mảng***//
        mangHinh = new ArrayList<Hinh>();
        //mang luu so luong hinh trong 1 ngày
        mangDate = new ArrayList<Integer>();
        mangHinhDate = new ArrayList<ArrayList<Hinh>>();

        ContentResolver contentResolver = getActivity().getContentResolver();

        Cursor cursor = contentResolver.query(Image_URI, null, null, null, null);
        cursor.moveToLast();
        while (!cursor.isBeforeFirst()) {
            @SuppressLint("Range") String duongdanhinhanh = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            @SuppressLint("Range") String tenhinh = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            File TempFiles = new File(duongdanhinhanh);
            Date lastModDate = new Date(TempFiles.lastModified());
            SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMdd");
            Integer dateText = Integer.valueOf(df2.format(lastModDate));
            if (TempFiles.exists()) {
                mangHinh.add(new Hinh(duongdanhinhanh, tenhinh, dateText));
            }
            cursor.moveToPrevious();
        }
        //Collections.reverse(mangHinh);
        //mang luu so luong hinh trong 1 ngày
        mangDate.clear();
        mangHinhDate.clear();
        //tao ra mang Date
        int dem = 0;
        for (int i = 0; i < mangHinh.size(); i++) {
            if (i == 0) {
                dem++;
            } else if ((i == mangHinh.size() - 1)) {
                if (dem != 1) {
                    dem++;
                }

                mangDate.add(dem);
            } else {
                Integer x = mangHinh.get(i).getAddDate();
                Integer y = mangHinh.get(i - 1).getAddDate();
                if ((x.intValue() != y.intValue())) {
                    mangDate.add(dem);
                    dem = 1;
                } else {
                    dem++;
                }
            }
        }
        //***Tạo ra mangHinhDate***//
        int tmp = 0;
        for (int i = 0; i < mangDate.size(); i++) {
            if (i == 0) {
                tmp = 0;
                mangHinhDate.add(new ArrayList<Hinh>(mangHinh.subList(tmp, tmp + mangDate.get(i))));
            } else {
                tmp += mangDate.get(i - 1);
                mangHinhDate.add(new ArrayList<Hinh>(mangHinh.subList(tmp, tmp + mangDate.get(i))));
            }
        }

        customListviewImageAdapter = new CustomListviewImageAdapter(context, mangHinhDate, R.layout.custom_item_listview_img);
        listView.setAdapter(customListviewImageAdapter);
        listView.setDivider(null);
        listView.setFastScrollEnabled(true);
        MainActivity.funcExecuteTime.put("onResume AnhFragment", System.currentTimeMillis() - start);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Log.e("test", "AnhFragmentOnCreate");
        context = getActivity();

        view = inflater.inflate(R.layout.anh_layout, container, false);
        listView = (ListView) view.findViewById(R.id.lvimg);


        return view;
    }


    @Override
    public void onMsgFromMainToFragment(String strValue) {
    }
}
