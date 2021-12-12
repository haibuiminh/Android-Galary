package com.example.androidgalary.adapters.customListImageView;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.androidgalary.R;

public class CustomListViewImageHolder extends RecyclerView.ViewHolder {
  public TextView textView;
  public RecyclerView recyclerView;

  public CustomListViewImageHolder(View itemView) {
    super(itemView);
    textView = itemView.findViewById(R.id.tvdate);
    recyclerView = itemView.findViewById(R.id.recyclerView);
  }
}
