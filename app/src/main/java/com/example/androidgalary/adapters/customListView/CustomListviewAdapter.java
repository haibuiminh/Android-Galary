package com.example.androidgalary.adapters.customListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidgalary.MainActivity;
import com.example.androidgalary.R;
import com.example.androidgalary.models.GallaryAlbumDetail;

import java.util.ArrayList;

// ***Custom List view dùng đề show danh sách album***//
public class CustomListviewAdapter extends ArrayAdapter<GallaryAlbumDetail> {
  Context context;
  ArrayList<GallaryAlbumDetail> mang;
  int layoutResource;

  public CustomListviewAdapter(Context context, ArrayList<GallaryAlbumDetail> mang, int layoutResource) {
    super(context, layoutResource, mang);
    this.context = context;
    this.mang = mang;
    this.layoutResource = layoutResource;
  }

  @Override
  public int getCount() {
    return mang.size();
  }

  @NonNull
  @Override
  public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    LayoutInflater layoutInflater = ((MainActivity) context).getLayoutInflater();

    final CustomListViewHolder viewHolder;
    // ***Xét điều kiện***//
    if (convertView == null) {
      viewHolder = new CustomListViewHolder();
      convertView = layoutInflater.inflate(layoutResource, null);

      viewHolder.ten = convertView.findViewById(R.id.tenalbum);
      viewHolder.soluong = convertView.findViewById(R.id.imageNumbers);
      viewHolder.imageView = convertView.findViewById(R.id.imgalbum);
      viewHolder.constraintLayout = convertView.findViewById(R.id.LNofitemalbum);
      viewHolder.checkBox = convertView.findViewById(R.id.Checkalbum);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (CustomListViewHolder) convertView.getTag();
    }
    viewHolder.checkBox.setChecked(false);
    viewHolder.ten.setText(mang.get(position).getName());
    viewHolder.soluong.setText(mang.get(position).getImageNumbers() + "");

    RequestOptions options =
        new RequestOptions()
            .centerCrop()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .priority(Priority.HIGH);

    Glide.with(context)
        .load(mang.get(position).getPath())
        .apply(options)
        .thumbnail(0.6f)
        .into(viewHolder.imageView);

    if (MainActivity.albumStatus == true) {
      viewHolder.constraintLayout.setVisibility(View.VISIBLE);
    } else {
      viewHolder.constraintLayout.setVisibility(View.INVISIBLE);
    }

    viewHolder.checkBox.setOnClickListener(
        v -> {
          if (viewHolder.checkBox.isChecked() == true) {
            MainActivity.collectedalbums.add(
                new GallaryAlbumDetail(
                    mang.get(position).getName(),
                    mang.get(position).getPath(),
                    mang.get(position).getImageNumbers()));
          } else {
            for (int i = 0; i < MainActivity.collectedalbums.size(); i++) {
              if (MainActivity.collectedalbums
                  .get(i)
                  .getName()
                  .equals(mang.get(position).getName())) {
                MainActivity.collectedalbums.remove(i);
              }
            }
          }
        });
    return convertView;
  }
}
