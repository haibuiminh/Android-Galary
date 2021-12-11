package com.example.androidgalary;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.androidgalary.album.AlbumFragment;
import com.example.androidgalary.image.GallaryImageFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {
  public PagerAdapter(@NonNull FragmentManager fm) {
    super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
  }

  @NonNull
  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0:
        GallaryImageFragment gallaryImageFragment = new GallaryImageFragment();
        return gallaryImageFragment;
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
