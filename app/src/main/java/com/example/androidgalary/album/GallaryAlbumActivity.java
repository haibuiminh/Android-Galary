package com.example.androidgalary.album;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.androidgalary.MainActivity;
import com.example.androidgalary.R;
import com.example.androidgalary.adapters.customRecyclerView.CustomRecyclerviewAdapter;
import com.example.androidgalary.image.GallaryImageActivity;
import com.example.androidgalary.models.GallaryImage;
import com.example.androidgalary.slideShow.GallaryImageSlideShowActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GallaryAlbumActivity extends Activity {
  Toolbar toolbar;
  int pos; // *** lưu thứ tự album sẽ được show trong Mainactivity.mang***//
  String ten;
  RecyclerView recyclerView; // *** Hiện thị ảnh bằng recycler view***//
  CustomRecyclerviewAdapter customRecyclerviewAdapter;

  @Override
  // *** Hàm giúp cập nhật thông tin khi chuyển Activity***//
  protected void onPostResume() {
    super.onPostResume();
    Log.d("TAG", "onPostResume: ");
    // *****************************************************************************************************//
    // * Xét trạng thái của GallaryImageActivity.co:
    //           *//
    // *  + False: MainActivity.mang(pos) còn tồn tại => show album
    //           *//
    // *  + True: MainActivity.mang(pos) đã bị xóa => đóng GallaryAlbumActivity vì không cần show
    // ảnh
    // của Album đó*//
    // *****************************************************************************************************//
    if (!GallaryImageActivity.co) {
      StaggeredGridLayoutManager mStaggeredVerticalLayoutManager =
          new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
      customRecyclerviewAdapter =
          new CustomRecyclerviewAdapter(
              GallaryAlbumActivity.this, MainActivity.mang.get(pos), true, pos);
      recyclerView.setAdapter(customRecyclerviewAdapter);
      recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
    } else {
      finish();
      GallaryImageActivity.co = false;
    }
  }

  @Override
  // ***Xử lí khi click back***//
  public void onBackPressed() {
    if (MainActivity.status) {
      MainActivity.collectedimgs.clear();
      MainActivity.status = false;
      // ***Show ảnh không checkbox***//
      StaggeredGridLayoutManager mStaggeredVerticalLayoutManager =
          new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
      for (int i = 0; i < MainActivity.mang.get(pos).size(); i++) {
        MainActivity.mang.get(pos).get(i).setCheck(false);
      }
      customRecyclerviewAdapter =
          new CustomRecyclerviewAdapter(
              GallaryAlbumActivity.this, MainActivity.mang.get(pos), true, pos);
      recyclerView.setAdapter(customRecyclerviewAdapter);
      recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
      toolbar.getMenu().getItem(0).setVisible(true);
      toolbar.getMenu().getItem(1).setVisible(false);
      toolbar.getMenu().getItem(2).setVisible(false);
      toolbar.getMenu().getItem(3).setVisible(false);
    } else {
      finish();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album);
    // ***Bắt các dữ liệu cần thiết được gửi qua từ AlbumFragment***//
    Intent intent = getIntent();
    // Số thứ tự của album
    pos = intent.getIntExtra("thutu", 0);
    // tên của album
    ten = intent.getStringExtra("ten");
    // ************************************************************//

    // ***Ánh xạ***//
    recyclerView = (RecyclerView) findViewById(R.id.recyclerViewAlbum);
    toolbar = findViewById(R.id.toolbaralbum);
    // *****************************************************************//

    // ***Cài đặt Toolbar***//
    // Đặt tên Toolbar
    toolbar.setTitle(ten);
    // Dán R.menu.menu_album vào Toolbar
    toolbar.inflateMenu(R.menu.menu_album);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbar.setNavigationOnClickListener(v -> onBackPressed());

    // ***Cài đặt RecyclerView***//
    StaggeredGridLayoutManager mStaggeredVerticalLayoutManager =
        new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
    if (MainActivity.mang.get(pos) != null) {
      customRecyclerviewAdapter =
          new CustomRecyclerviewAdapter(
              GallaryAlbumActivity.this, MainActivity.mang.get(pos), true, pos);
      recyclerView.setAdapter(customRecyclerviewAdapter);
      recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager);
    } else {
      finish();
    }
    // ******************************************************************//

    // ***bat su kien cho menu của Toolbar***//
    toolbar.setOnMenuItemClickListener(
        item -> {
          int i = item.getItemId();
          // ***Sự kiện chọn ảnh trong album***//
          if (i == R.id.selectAlbum) {
            // ***Cập nhật MainActivity.status để hiện ra checkbox trong từng ảnh để
            // người dùng click***//
            MainActivity.status = true;

            // ***Xóa những ảnh đã được chọn trước đó => Đảm bảo không bị xóa
            // nhầm***//
            MainActivity.collectedimgs.clear();

            // ***Show lại ảnh của album, lúc này sẽ xuất hiện check box
            StaggeredGridLayoutManager mStaggeredVerticalLayoutManager1 =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            customRecyclerviewAdapter =
                new CustomRecyclerviewAdapter(
                    GallaryAlbumActivity.this, MainActivity.mang.get(pos), true, pos);
            recyclerView.setAdapter(customRecyclerviewAdapter);
            recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager1);

            // ***Cập nhật menu Toolbar để show ra những lựa chọn cần thiết khi
            // Select xong***//
            toolbar.getMenu().getItem(0).setVisible(false);
            toolbar.getMenu().getItem(1).setVisible(true);
            toolbar.getMenu().getItem(2).setVisible(true);
            toolbar.getMenu().getItem(3).setVisible(true);

          }
          // ***Sự kiện delete các ảnh đã chọn***//
          else if (i == R.id.deleteAlbum) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GallaryAlbumActivity.this);
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có muốn xóa không?");
            builder.setCancelable(false);
            builder.setPositiveButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                  }
                });
            builder.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    // ***Gắn cờ để xem size của
                    // Album**********************************//
                    // *  True: size=0 và bị remove=> không tồn tại trong
                    // Activity.mang*//
                    // *  False: size>0            => còn tồn tại trong
                    // Activity.mang  *//
                    // *****************************************************************//
                    boolean flag = false;

                    // ***Xét điều kiện trước khi xóa***//
                    // ***Nếu không có ảnh thì Toast thông báo***//
                    if (MainActivity.collectedimgs.size() == 0) {
                      // Toast.makeText(getApplicationContext(), "Chua
                      // chon anh", //Toast.LENGTH_SHORT).show();
                    } else {
                      // ***Cập nhật lại MainActivity.mang và lưu trạng
                      // thái size của Album***//
                      flag = refreshfAlbum(MainActivity.collectedimgs, pos);
                    }
                    // ***Cập nhật MainActivity.status để show ảnh không còn
                    // checkbox***//
                    MainActivity.status = false;

                    // ***Album không còn tồn tại=> đóng Activity
                    if (flag == true) {
                      toolbar.getMenu().getItem(0).setVisible(true);
                      toolbar.getMenu().getItem(1).setVisible(false);
                      toolbar.getMenu().getItem(2).setVisible(true);
                      toolbar.getMenu().getItem(3).setVisible(false);
                      finish();
                    }
                    // ***Album còn tồn tại => show lại ảnh còn lại sau khi xóa***//
                    else {
                      // *** Show ảnh không checkbox ***//
                      StaggeredGridLayoutManager mStaggeredVerticalLayoutManager1 =
                          new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
                      customRecyclerviewAdapter =
                          new CustomRecyclerviewAdapter(
                              GallaryAlbumActivity.this, MainActivity.mang.get(pos), true, pos);
                      recyclerView.setAdapter(customRecyclerviewAdapter);
                      recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager1);

                      // ***Show những lựa chọn cần thiết khi xóa xong***//
                      toolbar.getMenu().getItem(0).setVisible(true);
                      toolbar.getMenu().getItem(1).setVisible(false);
                      toolbar.getMenu().getItem(2).setVisible(true);
                      toolbar.getMenu().getItem(3).setVisible(false);
                    }
                    dialogInterface.dismiss();
                  }
                });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            // *** Sự kiện back trên menu Toolbar***//
            // *** Sử dụng khi không muốn chọn ảnh***//
          } else if (i == R.id.backAlbum) {
            MainActivity.collectedimgs.clear();
            MainActivity.status = false;
            // ***Show ảnh không checkbox***//
            StaggeredGridLayoutManager mStaggeredVerticalLayoutManager1 =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            for (int i1 = 0; i1 < MainActivity.mang.get(pos).size(); i1++) {
              MainActivity.mang.get(pos).get(i1).setCheck(false);
            }
            customRecyclerviewAdapter =
                new CustomRecyclerviewAdapter(
                    GallaryAlbumActivity.this, MainActivity.mang.get(pos), true, pos);
            recyclerView.setAdapter(customRecyclerviewAdapter);
            recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager1);
            toolbar.getMenu().getItem(0).setVisible(true);
            toolbar.getMenu().getItem(1).setVisible(false);
            toolbar.getMenu().getItem(2).setVisible(true);
            toolbar.getMenu().getItem(3).setVisible(false);

          } else if (i == R.id.createSlideshowAlbum) {
            Intent intent1 = new Intent(getBaseContext(), GallaryImageSlideShowActivity.class);
            toolbar.getMenu().getItem(0).setVisible(true);
            toolbar.getMenu().getItem(1).setVisible(false);
            toolbar.getMenu().getItem(3).setVisible(false);
            ArrayList<String> data = new ArrayList<String>();
            if (MainActivity.collectedimgs.size() != 0) {
              for (GallaryImage c : MainActivity.collectedimgs) {
                data.add(c.getPath());
              }
            } else {
              for (GallaryImage c : MainActivity.mang.get(pos)) {
                data.add(c.getPath());
              }
            }
            intent1.putExtra("data", data);
            startActivity(intent1);
            MainActivity.status = false;

            // ***Show ảnh không checkbox***//
            StaggeredGridLayoutManager mStaggeredVerticalLayoutManager1 =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
            customRecyclerviewAdapter =
                new CustomRecyclerviewAdapter(
                    GallaryAlbumActivity.this, MainActivity.mang.get(pos), true, pos);
            recyclerView.setAdapter(customRecyclerviewAdapter);
            recyclerView.setLayoutManager(mStaggeredVerticalLayoutManager1);
          }
          return true;
        });
  }

  // ***Hàm cập nhật MainActivity.mang, ghi lại vào bộ nhớ,remove những Album có size=0***//
  public boolean refreshfAlbum(ArrayList<GallaryImage> collectedimgs, int pos) {
    // ***Gắn cờ để xem size của Album**********************************//
    // *  True: size=0 và bị remove=> không tồn tại trong Activity.mang*//
    // *  False: size>0            => còn tồn tại trong Activity.mang  *//
    // *****************************************************************//
    boolean flag = false;

    // ***Cập nhật MainActivity.mang.get(pos) tức là xóa ảnh được chọn***//
    for (int i = 0; i < collectedimgs.size(); i++) {
      for (int j1 = 0; j1 < MainActivity.mang.get(pos).size(); j1++) {
        if (MainActivity.mang.get(pos).get(j1).getPath().equals(collectedimgs.get(i).getPath())) {
          MainActivity.mang.get(pos).remove(j1);
        }
      }
    }
    // ***Remove Album có size=0***//
    for (int i = 0; i < MainActivity.mang.size(); i++) {
      if (MainActivity.mang.get(i).size() == 0) {

        MainActivity.mang.remove(i);
        MainActivity.MangTen.remove(i);
        if (pos == i) {
          flag = true;
        }
      }
    }
    // ***Cập nhật bộ nhớ***//
    ghivaobonhotrongtenalbum();
    ghivaobonhotrong();
    return flag;
  }

  public void ghivaobonhotrong() {
    try {
      File duongdan = getCacheDir();
      File taptin = new File(duongdan, "imgofalbum.txt");
      Log.d("lienket", taptin + "");
      FileOutputStream out = new FileOutputStream(taptin);
      String buffer = new String();
      for (int i = 0; i < MainActivity.mang.size(); i++) {
        for (int j = 0; j < MainActivity.mang.get(i).size(); j++) {
          if (j == MainActivity.mang.get(i).size() - 1) {
            buffer =
                buffer
                    + MainActivity.mang.get(i).get(j).getPath().toString()
                    + "#"
                    + MainActivity.mang.get(i).get(j).getTenHinh().toString()
                    + "#"
                    + MainActivity.mang.get(i).get(j).getAddDate().toString();
          } else {
            buffer =
                buffer
                    + MainActivity.mang.get(i).get(j).getPath().toString()
                    + "#"
                    + MainActivity.mang.get(i).get(j).getTenHinh().toString()
                    + "#"
                    + MainActivity.mang.get(i).get(j).getAddDate().toString()
                    + "#";
          }
        }
        buffer += "%";
      }

      out.write(buffer.getBytes());
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void ghivaobonhotrongtenalbum() {
    File duongdan = getCacheDir();
    File file = new File(duongdan, "nameofalbum.txt");
    if (file.exists()) file.delete();

    file = new File(duongdan, "nameofalbum.txt");

    try {
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      String buffer = new String();
      for (int i = 0; i < MainActivity.MangTen.size(); i++) {
        if (i != MainActivity.MangTen.size() - 1) buffer += MainActivity.MangTen.get(i) + "#";
        else buffer += MainActivity.MangTen.get(MainActivity.MangTen.size() - 1);
      }

      fileOutputStream.write(buffer.getBytes());
      fileOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
