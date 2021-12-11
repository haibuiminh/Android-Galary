package com.example.androidgalary.adapters.customRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidgalary.MainActivity;
import com.example.androidgalary.R;
import com.example.androidgalary.image.GallaryImageActivity;
import com.example.androidgalary.image.GallaryImageFragment;
import com.example.androidgalary.models.GallaryImage;

import java.util.ArrayList;

// ***Custom các RecyclerView có trong Project***//
public class CustomRecyclerviewAdapter
    extends RecyclerView.Adapter<CustomRecyclerViewHolder> {
  private Context context;
  private ArrayList<GallaryImage> data;
  boolean loai; // ***album=true hoac kho ảnh=false***//
  private LayoutInflater inflater; // ***Layout muốn dán***//
  private int pos;

  public CustomRecyclerviewAdapter(Context context, ArrayList<GallaryImage> data, boolean loai, int pos) {
    this.context = context;
    this.data = data;
    inflater = LayoutInflater.from(context);
    this.loai = loai;
    this.pos = pos;
  }

  @NonNull
  @Override
  public CustomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = inflater.inflate(R.layout.custom_item_recyclerview, parent, false);
    CustomRecyclerViewHolder holder = new CustomRecyclerViewHolder(view);
    return holder;
  }

  @Override
  public void onBindViewHolder(
      @NonNull CustomRecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
    long start = System.currentTimeMillis();
    RequestOptions options =
        new RequestOptions()
            .centerCrop()
            .fitCenter()
            .override(250, 250)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .priority(Priority.HIGH);

    Glide.with(context)
        .load(data.get(position).getPath())
        .apply(options)
        .thumbnail(0.6f)
        .into(holder.imageView);
    ;

    // ***lưu ý trang thái được click của các ảnh trong trường hợp chuyển qua imageactivity rồi
    // quay lại***//
    for (int i = 0; i < MainActivity.collectedimgs.size(); i++) {
      if (MainActivity.collectedimgs.get(i).getPath().equals(data.get(position).getPath())) {
        holder.checkBox.setChecked(true);
      }
    }

    if (MainActivity.status) {
      holder.constraintLayout.setVisibility(View.VISIBLE);
    } else {
      holder.constraintLayout.setVisibility(View.INVISIBLE);
    }
    holder.imageView.setOnClickListener(
        v -> {
          // Chuyen man hinh image activity
          Intent intent = new Intent(context, GallaryImageActivity.class);
          intent.putExtra("vitri", data.get(position).getPath());
          intent.putExtra("loai", loai);
          context.startActivity(intent);
        });

    holder.checkBox.setOnClickListener(
        v -> {
          if (holder.checkBox.isChecked()) {
            MainActivity.collectedimgs.add(
                new GallaryImage(
                    data.get(position).getPath(),
                    data.get(position).getTenHinh(),
                    data.get(position).getAddDate()));
            if (!loai) {
              GallaryImageFragment.mangHinhDate.get(pos).get(position).setCheck(true);
            } else {
              MainActivity.mang.get(pos).get(position).setCheck(true);
            }
          } else {
            for (int i = 0; i < MainActivity.collectedimgs.size(); i++) {
              if (MainActivity.collectedimgs
                  .get(i)
                  .getPath()
                  .equals(data.get(position).getPath())) {
                MainActivity.collectedimgs.remove(i);
              }
            }
            if (!loai) GallaryImageFragment.mangHinhDate.get(pos).get(position).setCheck(false);
            else {
              MainActivity.mang.get(pos).get(position).setCheck(false);
            }
          }
        });

    holder.imageView.setOnLongClickListener(
        view -> {
          if (!loai) {
            if (((MainActivity) context).status == false) {
              ((MainActivity) context).status = true;
              GallaryImageActivity.position = pos;
              MainActivity.viewPager.setAdapter(MainActivity.pagerAdapter);

              // ***Show những lựa chọn cần thiết sau khi Select***//
              ((MainActivity) context).toolbar.getMenu().getItem(0).setVisible(false);
              ((MainActivity) context).toolbar.getMenu().getItem(1).setVisible(true);
              ((MainActivity) context).toolbar.getMenu().getItem(2).setVisible(true);
              ((MainActivity) context).toolbar.getMenu().getItem(3).setVisible(true);
              ((MainActivity) context).toolbar.getMenu().getItem(4).setVisible(true);
              ((MainActivity) context).toolbar.getMenu().getItem(5).setVisible(true);
            }
          }
          return false;
        });
  }

  @Override
  public int getItemCount() {
    return data.size();
  }
}
