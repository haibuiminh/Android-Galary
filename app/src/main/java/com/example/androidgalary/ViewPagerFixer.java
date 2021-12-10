package com.example.androidgalary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerFixer extends ViewPager {
  public ViewPagerFixer(@NonNull Context context) {
    super(context);
  }

  public ViewPagerFixer(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    try {
      return super.onInterceptTouchEvent(ev);
    } catch (IllegalArgumentException e) {

    } catch (ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
    return false;
  }
}
