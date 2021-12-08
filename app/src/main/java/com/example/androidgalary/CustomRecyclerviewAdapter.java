package com.example.androidgalary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

//***Custom các RecyclerView có trong Project***//
public class CustomRecyclerviewAdapter extends RecyclerView.Adapter<CustomRecyclerviewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Hinh> data;
    boolean loai; //***album=true hoac kho ảnh=false***//
    private LayoutInflater inflater; //***Layout muốn dán***//
    private int pos;

    public CustomRecyclerviewAdapter(Context context, ArrayList<Hinh> data, boolean loai, int pos) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.loai = loai;
        this.pos = pos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_item_recyclerview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        long start = System.currentTimeMillis();
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .override(250, 250)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH);

        Glide.with(context).load(data.get(position).getDuongdan())
                .apply(options).thumbnail(0.6f)
                .into(holder.imageView);;

        holder.checkBox.setChecked(data.get(position).check);

        if (MainActivity.status) {
            holder.linearLayout.setVisibility(View.VISIBLE);
        } else {
            holder.linearLayout.setVisibility(View.INVISIBLE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyen man hinh image activity
                Intent intent = new Intent(context, ImageActivity.class);
                intent.putExtra("vitri", data.get(position).getDuongdan());
                intent.putExtra("loai", loai);
                context.startActivity(intent);
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()) {
                    MainActivity.collectedimgs.add(
                        new Hinh(
                            data.get(position).getDuongdan(),
                            data.get(position).getTenHinh(),
                            data.get(position).getAddDate()
                        )
                    );
                    if (loai == false) {
                        AnhFragment.mangHinhDate.get(pos).get(position).setCheck(true);
                    } else if (loai) {
                        MainActivity.mang.get(pos).get(position).setCheck(true);
                    }
                } else {
                    for (int i = 0; i < MainActivity.collectedimgs.size(); i++) {
                        if (MainActivity.collectedimgs.get(i).getDuongdan().equals(data.get(position).duongdan)) {
                            MainActivity.collectedimgs.remove(i);
                        }
                    }
                    if (loai == false)
                        AnhFragment.mangHinhDate.get(pos).get(position).setCheck(false);
                    else if (loai) {
                        MainActivity.mang.get(pos).get(position).setCheck(false);
                    }
                }
            }
        });

        MainActivity.funcExecuteTime.put("onBindViewHolder CustomRecyclerviewAdapter", System.currentTimeMillis() - start);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout linearLayout;
        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imgrv);
            linearLayout = itemView.findViewById(R.id.LNofcardview);
            checkBox = itemView.findViewById(R.id.Check);
        }
    }
}
