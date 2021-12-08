package com.example.androidgalary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


// Custom list view trong AnhFragment
//***Show Ảnh theo thời gian***//
public class CustomListviewImageAdapter extends ArrayAdapter<ArrayList<Hinh>> {
    Context context;

    //***Mảng lưu số lượng ảnh trong 1 ngày nào đó***//
//    ArrayList<Integer> mang;
    ArrayList<ArrayList<Hinh>> mang;
    //***Layout muốn dán***//
    int layoutResource;

    public CustomListviewImageAdapter(Context context, ArrayList<ArrayList<Hinh>> mang, int layoutResource) {
        super(context, layoutResource, mang);
        this.context = context;
        this.mang = mang;
        this.layoutResource = layoutResource;
    }

    @Override
    public int getCount() {
        return mang.size();
    }

    //***ViewHolder***//
    public class ViewHolder {
        TextView textView;
        RecyclerView recyclerView;

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        long start = System.currentTimeMillis();
        LayoutInflater inf = ((MainActivity) context).getLayoutInflater();
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inf.inflate(layoutResource, null);
            viewHolder.textView = convertView.findViewById(R.id.tvdate);
            viewHolder.recyclerView = convertView.findViewById(R.id.recyclerView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //***Thêm hình từ mangHinh vào temp, số lượng tùy vào mang.get(position)***//
        ArrayList<Hinh> temp;

        //viewHolder.textView.setText(mang.get(position).get(0).getAddDate().toString());
        String date = mang.get(position).get(0).getAddDate().toString();
        viewHolder.textView.setText(String.format("%s-%s-%s", date.substring(0, 4), date.substring(4, 6), date.substring(6, 8)));
        CustomRecyclerviewAdapter customRecyclerviewAdapter = new CustomRecyclerviewAdapter(context, mang.get(position), false, position);
        viewHolder.recyclerView.setAdapter(customRecyclerviewAdapter);
        viewHolder.recyclerView.setHasFixedSize(true);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(context, 4);

        viewHolder.recyclerView.setLayoutManager(mGridLayoutManager);

        MainActivity.funcExecuteTime.put("getView CustomListviewImageAdapter", System.currentTimeMillis() - start);

        return convertView;
    }
}
