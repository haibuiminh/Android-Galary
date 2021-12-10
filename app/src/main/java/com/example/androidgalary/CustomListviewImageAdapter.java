package com.example.androidgalary;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomListviewImageAdapter
    extends RecyclerView.Adapter<CustomListviewImageAdapter.MyViewHolder> {

  Context context;

  // ***Mảng lưu số lượng ảnh trong 1 ngày nào đó***//
  ArrayList<ArrayList<Hinh>> mang;
  // ***Layout muốn dán***//
  int layoutResource;

  public CustomListviewImageAdapter(
      Context context, ArrayList<ArrayList<Hinh>> mang, int layoutResource) {
    this.context = context;
    this.mang = mang;
    this.layoutResource = layoutResource;
  }

  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = ((Activity) context).getLayoutInflater().inflate(layoutResource, parent, false);
    MyViewHolder holder = new MyViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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

  class MyViewHolder extends RecyclerView.ViewHolder {
    TextView textView;
    RecyclerView recyclerView;

    public MyViewHolder(View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.tvdate);
      recyclerView = itemView.findViewById(R.id.recyclerView);
    }
  }
}
