package com.example.androidgalary.adapters.customListImageView;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidgalary.adapters.customRecyclerView.CustomRecyclerviewAdapter;
import com.example.androidgalary.MainActivity;
import com.example.androidgalary.R;
import com.example.androidgalary.models.GallaryImage;

import java.util.ArrayList;

public class CustomListviewImageAdapter
    extends RecyclerView.Adapter<CustomListViewImageHolder> {

  Context context;

  // ***Mảng lưu số lượng ảnh trong 1 ngày nào đó***//
  ArrayList<ArrayList<GallaryImage>> mang;
  // ***Layout muốn dán***//
  int layoutResource;

  public CustomListviewImageAdapter(
      Context context, ArrayList<ArrayList<GallaryImage>> mang, int layoutResource) {
    this.context = context;
    this.mang = mang;
    this.layoutResource = layoutResource;
  }

  @Override
  public CustomListViewImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = ((Activity) context).getLayoutInflater().inflate(layoutResource, parent, false);
    CustomListViewImageHolder holder = new CustomListViewImageHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull CustomListViewImageHolder holder, int position) {
    long start = System.currentTimeMillis();
    String date = mang.get(position).get(0).getAddDate().toString();
    holder.textView.setText(
        String.format(
            "%s-%s-%s", date.substring(0, 4), date.substring(4, 6), date.substring(6, 8)));
    CustomRecyclerviewAdapter customRecyclerviewAdapter =
        new CustomRecyclerviewAdapter(context, mang.get(position), false, position);
    holder.recyclerView.setAdapter(customRecyclerviewAdapter);
    holder.recyclerView.setHasFixedSize(true);
    GridLayoutManager mGridLayoutManager = new GridLayoutManager(context, 4);

    holder.recyclerView.setLayoutManager(mGridLayoutManager);

    MainActivity.funcExecuteTime.put(
        "getView CustomListviewImageAdapter", System.currentTimeMillis() - start);
  }

  @Override
  public int getItemCount() {
    return mang.size();
  }
}
