package com.example.androidgalary.image;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.androidgalary.R;
import com.example.androidgalary.models.GallaryImage;
import java.io.File;
import java.util.ArrayList;
import uk.co.senab.photoview.PhotoView;

public class ImagePagerAdapter extends PagerAdapter {
  ArrayList<GallaryImage> mang;
  Context context;
  LayoutInflater layoutInflater;

  public ImagePagerAdapter(ArrayList<GallaryImage> mang, Context context) {
    this.mang = mang;
    this.context = context;
    this.layoutInflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return mang.size();
  }

  @Override
  public boolean isViewFromObject(View view, @NonNull Object object) {
    return view.equals(object);
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    View view = layoutInflater.inflate(R.layout.custom_item_viewpager, container, false);
    PhotoView img;
    img = view.findViewById(R.id.imgofimgactivity);
    File file = new File(mang.get(position).getPath());
    RequestOptions options =
        new RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).priority(Priority.HIGH);

    Glide.with(context).load(file).apply(options).thumbnail(0.6f).into(img);
    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }
}
