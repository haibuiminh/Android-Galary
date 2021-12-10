package com.example.androidgalary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
  public PagerAdapter(@NonNull FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        AnhFragment anhFragment = new AnhFragment();
        return anhFragment;
      case 1:
        AlbumFragment albumFragment = new AlbumFragment();
        return albumFragment;
    }
    return null;
  }

  @Override
  public int getCount() {
    return 2;
  }
}
