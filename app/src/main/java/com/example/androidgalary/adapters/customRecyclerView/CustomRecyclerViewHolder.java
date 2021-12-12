package com.example.androidgalary.adapters.customRecyclerView;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidgalary.R;

public class CustomRecyclerViewHolder extends RecyclerView.ViewHolder {
  public ImageView imageView;
  public ConstraintLayout constraintLayout;
  public CheckBox checkBox;

  public CustomRecyclerViewHolder(View itemView) {
    super(itemView);
    imageView = itemView.findViewById(R.id.imgrv);
    constraintLayout = itemView.findViewById(R.id.LNofcardview);
    checkBox = itemView.findViewById(R.id.Check);
  }
}
