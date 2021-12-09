package com.example.androidgalary;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;

public class ImagePagerAdapter extends PagerAdapter {

    ArrayList<Hinh> mang;
    Context context;
    LayoutInflater layoutInflater;


    public ImagePagerAdapter(ArrayList<Hinh> mang, Context context) {
        this.mang = mang;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mang.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=layoutInflater.inflate(R.layout.custom_item_viewpager,container,false);
        PhotoView img;
        img = (PhotoView) view.findViewById(R.id.imgofimgactivity);
        File file = new File(mang.get(position).getDuongdan());
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH);

        Glide.with(context).load(file).apply(options).thumbnail(0.6f)
                .into(img);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



}
