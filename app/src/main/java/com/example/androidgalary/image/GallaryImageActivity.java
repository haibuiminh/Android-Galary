package com.example.androidgalary.image;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import com.example.androidgalary.BuildConfig;
import com.example.androidgalary.ExifUtility;
import com.example.androidgalary.MainActivity;
import com.example.androidgalary.R;
import com.example.androidgalary.ViewPagerFixer;
import com.example.androidgalary.album.AlbumFragment;
import com.example.androidgalary.editImage.EditImageActivity;
import com.example.androidgalary.models.GallaryImage;
import com.theartofdev.edmodo.cropper.CropImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.UUID;

public class GallaryImageActivity extends Activity {
  Toolbar toolbar;
  ViewPagerFixer viewPager;
  Button btnDelete;
  Button btnShare;
  Button btnCrop;
  Button btnResize;
  Button btnEdit;

  public static int position = 0;
  String diachi;
  boolean loai;
  public static boolean co = false;
  public static GallaryImage currentImage = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image);

    // nhan thong tin tu activity truoc
    final Intent intent = getIntent();
    diachi = intent.getStringExtra("vitri");
    loai = intent.getBooleanExtra("loai", false);

    Anhxa();
    toolbar.setNavigationOnClickListener(v -> finish());
    viewPager.setCurrentItem(position);

    toolbar.setOnMenuItemClickListener(
        item -> {
          int i = item.getItemId();
          if (i == R.id.compress) {
            compressImage();
          } else if (i == R.id.metadata) {
            showMetadata();
          }
          return false;
        });

    // Sự kiện crop ảnh
    btnCrop.setOnClickListener(v -> cropImage());

    // Xử lý sự kiện Share ảnh
    btnShare.setOnClickListener(
        v -> {
          int tmp = viewPager.getCurrentItem();
          GallaryImage choosenImage = GallaryImageFragment.mangHinh.get(tmp);
          try {
            File photoFile = new File(choosenImage.getPath());
            Uri imageUri =
                FileProvider.getUriForFile(
                    GallaryImageActivity.this,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    photoFile);
            final Intent intent1 = new Intent(Intent.ACTION_SEND);
            intent1.setType("image/*");
            intent1.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(intent1, "Share via"));
          } catch (Exception e) {
            e.printStackTrace();
          }
        });

    // Sự kiện resize ảnh
    btnResize.setOnClickListener(v -> resizeImage());

    // *************************************************
    // * Xử lí sự kiện xóa ảnh trong hai trường hợp    *
    // *     I:  ảnh của Anhfragment                   *
    // *     II: ảnh của Album                         *
    // *************************************************
    btnDelete.setOnClickListener(
        v -> {
          final Dialog confirmDialog = new Dialog(GallaryImageActivity.this);
          confirmDialog.setTitle("Xác nhận xóa ảnh");
          confirmDialog.setContentView(R.layout.delete_dialog);
          confirmDialog.show();
          Button btnOK = confirmDialog.findViewById(R.id.btnDeleteOk);
          Button btnCancel = confirmDialog.findViewById(R.id.btnDeleteCancel);

          btnCancel.setOnClickListener(v1 -> confirmDialog.dismiss());

          btnOK.setOnClickListener(
              v12 -> {
                deleteImage();
                confirmDialog.dismiss();
              });
        });
    final Intent editIntent = new Intent(getApplicationContext(), EditImageActivity.class);

    btnEdit.setOnClickListener(
        v -> {
          currentImage = GallaryImageFragment.mangHinh.get(viewPager.getCurrentItem());
          startActivity(editIntent);
        });
  }

  private void showMetadata() {
    try {
      currentImage = GallaryImageFragment.mangHinh.get(viewPager.getCurrentItem());
      ListView listView = new ListView(this);

      ExifUtility exif = new ExifUtility();
      String[] items = exif.getExif(currentImage.getExif()).toArray(new String[0]);

      ArrayAdapter<String> adapter =
          new ArrayAdapter<>(this, R.layout.listview_metadata, R.id.txtitem, items);
      listView.setAdapter(adapter);

      AlertDialog.Builder builder = new AlertDialog.Builder(GallaryImageActivity.this);
      builder.setCancelable(true);
      builder.setPositiveButton("OK", null);
      builder.setView(listView);

      AlertDialog dialog = builder.create();
      dialog.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressLint({"SetWorldReadable", "SetWorldWritable"})
  private void editBitmap(String filePath) {
    try {
      File file = new File(filePath);
      file.setReadable(true, false);
      file.setWritable(true, false);

      CropImage.activity(Uri.fromFile(file)).start(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressLint({"SetWorldReadable", "SetWorldWritable"})
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      CropImage.ActivityResult result = CropImage.getActivityResult(data);
      if (resultCode == RESULT_OK) {
        Uri resultUri = result.getUri();
        Bitmap bm = null;
        InputStream is;
        String url = resultUri.toString();
        BufferedInputStream bis;

        try {
          URLConnection conn = new URL(url).openConnection();
          conn.connect();
          is = conn.getInputStream();
          bis = new BufferedInputStream(is, 8192);
          bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
          e.printStackTrace();
        }
        File file = new File(resultUri.getPath());

        try {
          FileOutputStream out = new FileOutputStream(file);
          file.setReadable(true, false);
          file.setWritable(true, false);
          if (bm != null) {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
          }
          out.flush();
          out.close();
          MediaStore.Images.Media.insertImage(
              getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        Exception error = result.getError();
        error.printStackTrace();
      }
    }
  }

  @Override
  public void onBackPressed() {
    for (int i = 0; i < GallaryImageFragment.mangHinhDate.size(); i++) {
      for (int j = 0; j < GallaryImageFragment.mangHinhDate.get(i).size(); j++) {
        if (GallaryImageFragment.mangHinhDate
            .get(i)
            .get(j)
            .getPath()
            .equals(GallaryImageFragment.mangHinh.get(viewPager.getCurrentItem()).getPath())) {
          position = i;
          break;
        }
      }
    }
    super.onBackPressed();
  }

  private void Anhxa() {
    toolbar = findViewById(R.id.toolbarofimgactivity);

    btnDelete = findViewById(R.id.delete_btn);
    btnShare = findViewById(R.id.share_btn);
    btnCrop = findViewById(R.id.crop_btn);
    btnResize = findViewById(R.id.Resize_btn);
    btnEdit = findViewById(R.id.edit_btn);

    // Tạo nút back trên toolbar
    toolbar.inflateMenu(R.menu.menu_image);
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
    toolbar.setNavigationOnClickListener(v -> onBackPressed());

    viewPager = findViewById(R.id.viewpagerofimgactivity);

    if (!loai) {
      // ********************************************
      // *Xử lí trường hợp: Xuất ảnh của Anhfragment*
      // ********************************************
      for (int i = 0; i < GallaryImageFragment.mangHinh.size(); i++) {
        if (diachi.equals(GallaryImageFragment.mangHinh.get(i).getPath())) {
          position = i;
          break;
        }
      }
      ImagePagerAdapter imagePagerAdapter =
          new ImagePagerAdapter(GallaryImageFragment.mangHinh, this);
      viewPager.setAdapter(imagePagerAdapter);
    } else {
      // ****************************************
      // *Xử lí trường hợp: Xuất ảnh của 1 album*
      // ****************************************
      for (int i = 0; i < MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size(); i++) {
        if (diachi.equals(
            MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(i).getPath())) {
          position = i;
          break;
        }
      }
      ImagePagerAdapter imagePagerAdapter =
          new ImagePagerAdapter(MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum), this);
      viewPager.setAdapter(imagePagerAdapter);
    }
  }

  // *** Hàm dùng để cập nhật mảng chứa các album sau khi xóa và ghi lại vào bộ nhớ***//
  public void refreshfAlbum(ArrayList<GallaryImage> collectedimgs) {
    // ***Các ảnh cần xóa nằm trong collectedimgs***//                                         *
    for (int i = 0; i < collectedimgs.size(); i++) {
      for (int j = 0; j < MainActivity.mang.size(); j++) {
        for (int j1 = 0; j1 < MainActivity.mang.get(j).size(); j1++) {
          if (MainActivity.mang.get(j).get(j1).getPath().equals(collectedimgs.get(i).getPath())) {
            MainActivity.mang.get(j).remove(j1);
          }
        }
      }
    }
  }

  // *** Hàm dùng để ghi lại danh sách album vào bộ nhớ***//
  public void ghivaobonhotrong() {
    try {
      // ***Lấy đường dẫn bộ nhớ Cache***//
      File duongdan = getCacheDir();
      // ***Đặt tên file chứa thông tin album***//
      File taptin = new File(duongdan, "imgofalbum.txt");
      // ***Tạo luồng ghi***//
      FileOutputStream out = new FileOutputStream(taptin);
      // *** Tạo buffer để chứa dự liệu cần ghi vào File "imgofalbum.txt" ***//
      StringBuilder buffer = new StringBuilder();
      // *** Tiến hành ghi dữ liệu vào buffer***//

      // **********************************************************************************************************************
      // * Một album được ghi dưới dạng:
      //                                *
      // *
      //                                *
      // *
      // "duongdan[1]"#"tenhinh[1]"#"date[1]"#"duongdan[2]"#"tenhinh[2]"#"date[2]"#"duongdan[3]"#"tenhinh[4]"#"date[3]"...% *
      // *
      //                                *
      // * Kết thúc 1 album là dấu (  %  )
      //                                *
      // **********************************************************************************************************************
      for (int i = 0; i < MainActivity.mang.size(); i++) {
        for (int j = 0; j < MainActivity.mang.get(i).size(); j++) {
          if (j == MainActivity.mang.get(i).size() - 1) {
            // Trường hợp ảnh cuối cùng của Album
            // Không có dấu (  #  ) ở cuối
            buffer
                .append(MainActivity.mang.get(i).get(j).getPath())
                .append("#")
                .append(MainActivity.mang.get(i).get(j).getTenHinh())
                .append("#")
                .append(MainActivity.mang.get(i).get(j).getAddDate().toString());
          } else {
            // Trường hợp bình thường
            // Có dấu (  #  ) ở cuối
            buffer
                .append(MainActivity.mang.get(i).get(j).getPath())
                .append("#")
                .append(MainActivity.mang.get(i).get(j).getTenHinh())
                .append("#")
                .append(MainActivity.mang.get(i).get(j).getAddDate().toString())
                .append("#");
          }
        }
        buffer.append("%");
      }
      // *** ghi vào bộ nhớ ***//
      out.write(buffer.toString().getBytes());
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
      StringBuilder buffer = new StringBuilder();
      for (int i = 0; i < MainActivity.MangTen.size(); i++) {
        if (i != MainActivity.MangTen.size() - 1)
          buffer.append(MainActivity.MangTen.get(i)).append("#");
        else buffer.append(MainActivity.MangTen.get(MainActivity.MangTen.size() - 1));
      }
      Log.d("ALBUM", "ALBUMNAME= GallaryImageActivity" + buffer);
      fileOutputStream.write(buffer.toString().getBytes());
      fileOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressLint({"SetWorldReadable", "SetWorldWritable"})
  private void resizeImage() {
    int tmp = viewPager.getCurrentItem();
    final GallaryImage choosenImage = GallaryImageFragment.mangHinh.get(tmp);
    final Dialog dialog1;
    dialog1 = new Dialog(GallaryImageActivity.this);
    dialog1.setTitle("Nhập kích thước: ");
    dialog1.setContentView(R.layout.resize_dialog);
    dialog1.show();
    final EditText Width, Height;
    Button btnOK, btnCancel;
    Width = dialog1.findViewById(R.id.editText_Width);
    Height = dialog1.findViewById(R.id.editText_Height);
    btnOK = dialog1.findViewById(R.id.btnOk);
    btnCancel = dialog1.findViewById(R.id.btnCancel);
    btnOK.setOnClickListener(
        v -> {
          try {
            int width = Integer.parseInt(Width.getText().toString());
            int height = Integer.parseInt(Height.getText().toString());

            File imgFile = new File(choosenImage.getPath());
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap resBitmap = Bitmap.createScaledBitmap(myBitmap, width, height, false);
            File f =
                new File(
                    Environment.getExternalStorageDirectory()
                        + File.separator
                        + "resize_"
                        + UUID.randomUUID().toString()
                        + ".jpeg");

            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            f.setReadable(true, false);
            f.setWritable(true, false);

            resBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
              Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
              Uri contentUri = Uri.fromFile(f);
              mediaScanIntent.setData(contentUri);
              getBaseContext().sendBroadcast(mediaScanIntent);
            } else {
              sendBroadcast(
                  new Intent(
                      Intent.ACTION_MEDIA_MOUNTED,
                      Uri.parse("file://" + Environment.getExternalStorageDirectory())));
            }
          } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
          }
          dialog1.dismiss();
        });

    btnCancel.setOnClickListener(v -> dialog1.dismiss());
  }

  private void deleteImage() {
    // luu lai vi tri cua hinh
    int tmp = viewPager.getCurrentItem();
    if (!loai) {
      // xử lý I

      // Xóa collectedimgs và add đối tượng cần xóa
      MainActivity.collectedimgs.clear();
      MainActivity.collectedimgs.add(GallaryImageFragment.mangHinh.get(tmp));

      // Tìm và xóa ảnh
      for (int j = 0; j < MainActivity.collectedimgs.size(); j++) {
        File file = new File(MainActivity.collectedimgs.get(j).getPath());
        file.delete();
        if (file.exists()) {
          try {
            file.getCanonicalFile().delete();
            if (file.exists()) {
              getApplicationContext().deleteFile(file.getName());
            }
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        getApplicationContext()
            .sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
      }

      // ***Cập nhập mảng chứa album***//
      refreshfAlbum(MainActivity.collectedimgs);
      // *** Xóa album rộng => Xóa Mảng rỗng trong MainActivity.mang ***//
      for (int i = 0; i < MainActivity.mang.size(); i++) {
        if (MainActivity.mang.get(i).size() == 0) {
          MainActivity.mang.remove(i);
          MainActivity.MangTen.remove(i);
        }
      }
      for (int i1 = 0; i1 < MainActivity.collectedimgs.size(); i1++) {
        for (int j = 0; j < GallaryImageFragment.mangHinh.size(); j++) {
          if (GallaryImageFragment.mangHinh
              .get(j)
              .getPath()
              .equals(MainActivity.collectedimgs.get(i1).getPath())) {
            GallaryImageFragment.mangHinh.remove(j);
          }
        }
      }
      ghivaobonhotrongtenalbum();
      ghivaobonhotrong();
      // su ly sau khi xoa

      ImagePagerAdapter imagePagerAdapter =
          new ImagePagerAdapter(GallaryImageFragment.mangHinh, GallaryImageActivity.this);
      viewPager.setAdapter(imagePagerAdapter);

      // *** Tìm vị trị thích hợp để setCurrent cho ViewPager***//
      if (tmp == GallaryImageFragment.mangHinh.size()) viewPager.setCurrentItem(tmp - 1);
      else viewPager.setCurrentItem(tmp);

    } else {
      // xử lý II

      MainActivity.collectedimgs.clear();
      MainActivity.collectedimgs.add(
          MainActivity.mang
              .get(AlbumFragment.postionofFocusingAlbum)
              .get(viewPager.getCurrentItem()));

      // ***Cập nhật mảng chứa các album***//
      if (MainActivity.collectedimgs.size() != 0) {
        // ***Các ảnh cần xóa nằm trong collectedimgs***//
        //       *
        for (int i = 0; i < MainActivity.collectedimgs.size(); i++) {
          for (int j1 = 0;
              j1 < MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size();
              j1++) {
            if (MainActivity.mang
                .get(AlbumFragment.postionofFocusingAlbum)
                .get(j1)
                .getPath()
                .equals(MainActivity.collectedimgs.get(i).getPath())) {
              MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).remove(j1);
            }
          }
        }
      }

      // su ly sau khi xoa
      if (MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size() == 0) {
        // *** Xóa album rộng => Xóa Mảng rỗng trong MainActivity.mang ***//
        for (int i = 0; i < MainActivity.mang.size(); i++) {
          if (MainActivity.mang.get(i).size() == 0) {
            MainActivity.mang.remove(i);
            MainActivity.MangTen.remove(i);
          }
        }
        ghivaobonhotrongtenalbum();
        ghivaobonhotrong();
        co = true;
        finish();
      } else {
        // *** Xóa album rộng => Xóa Mảng rỗng trong MainActivity.mang ***//
        for (int i = 0; i < MainActivity.mang.size(); i++) {
          if (MainActivity.mang.get(i).size() == 0) {
            MainActivity.mang.remove(i);
            MainActivity.MangTen.remove(i);
          }
        }
        ghivaobonhotrongtenalbum();
        ghivaobonhotrong();
        ImagePagerAdapter imagePagerAdapter =
            new ImagePagerAdapter(
                MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum),
                GallaryImageActivity.this);
        viewPager.setAdapter(imagePagerAdapter);
        // *** Tìm vị trị thích hợp để setCurrent cho ViewPager***//
        if (tmp == MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size() - 1)
          viewPager.setCurrentItem(tmp - 1);
        else viewPager.setCurrentItem(tmp);
      }
    }
  }

  private void cropImage() {
    int tmp = viewPager.getCurrentItem();
    GallaryImage choosenImage =
        (loai)
            ? MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(tmp)
            : GallaryImageFragment.mangHinh.get(tmp);
    File imgFile = new File(choosenImage.getPath());
    BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    editBitmap(choosenImage.getPath());
  }

  @SuppressLint("SetWorldWritable")
  private void compressImage() {
    final Dialog dialog1 = new Dialog(GallaryImageActivity.this);
    dialog1.setTitle("Nhập phần trăm muốn nén: ");
    dialog1.setContentView(R.layout.compress_dialog);
    dialog1.show();
    final SeekBar seekBar = dialog1.findViewById(R.id.seekBar);
    final TextView textView = dialog1.findViewById(R.id.textView_per);
    final EditText editText = dialog1.findViewById(R.id.editText_per);
    editText.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {}

          @Override
          public void afterTextChanged(Editable s) {
            try {
              // Update Seekbar value after entering a number
              if (s.toString().equals("")) {
                seekBar.setProgress(0);
              } else seekBar.setProgress(Integer.parseInt(s.toString()));
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          }
        });
    seekBar.setOnSeekBarChangeListener(
        new SeekBar.OnSeekBarChangeListener() {
          @SuppressLint("SetTextI18n")
          @Override
          public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            textView.setText("" + progress + "%");
            editText.setText("" + seekBar.getProgress());
            editText.setSelection(editText.getText().length());
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {}

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
            seekBar.getProgress();
          }
        });
    Button btn_OK, btn_Cancel;
    btn_OK = dialog1.findViewById(R.id.btn_OK);
    btn_OK.setOnClickListener(
        v -> {
          if (!loai) {
            int tmp = viewPager.getCurrentItem();
            GallaryImage choosenImage = GallaryImageFragment.mangHinh.get(tmp);
            File imgFile = new File(choosenImage.getPath());

            try {
              int Quality;
              if (editText.getText().toString().equals("")) {
                Quality = 5;
              } else {
                Quality = Integer.parseInt(editText.getText().toString()) / 10;
              }

              Bitmap mySample = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
              Bitmap myBm =
                  Bitmap.createScaledBitmap(
                      mySample,
                      mySample.getWidth() / Quality,
                      mySample.getHeight() / Quality,
                      true);
              File f =
                  new File(
                      Environment.getExternalStorageDirectory()
                          + File.separator
                          + "compress_"
                          + UUID.randomUUID().toString()
                          + ".jpeg");
              f.createNewFile();
              FileOutputStream out = new FileOutputStream(f);
              f.setReadable(true, false);
              f.setWritable(true, false);
              myBm.compress(Bitmap.CompressFormat.JPEG, 100, out);
              out.flush();
              out.close();
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getBaseContext().sendBroadcast(mediaScanIntent);
              } else {
                sendBroadcast(
                    new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
              }
              dialog1.dismiss();
            } catch (IOException e) {
              e.printStackTrace();
            }
          } else {
            int tmp = viewPager.getCurrentItem();
            GallaryImage choosenImage =
                MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(tmp);
            File imgFile = new File(choosenImage.getPath());

            try {
              int Quality;
              if (editText.getText().toString().equals("")) {
                Quality = 5;
              } else {
                Quality = Integer.parseInt(editText.getText().toString()) / 10;
              }
              Bitmap mySample = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
              Bitmap myBm =
                  Bitmap.createScaledBitmap(
                      mySample,
                      mySample.getWidth() / Quality,
                      mySample.getHeight() / Quality,
                      true);
              File f =
                  new File(
                      Environment.getExternalStorageDirectory()
                          + File.separator
                          + "compress_"
                          + UUID.randomUUID().toString()
                          + ".jpeg");
              f.createNewFile();
              FileOutputStream out = new FileOutputStream(f);
              f.setReadable(true, false);
              f.setWritable(true, false);
              myBm.compress(Bitmap.CompressFormat.JPEG, 100, out);
              out.flush();
              out.close();
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getBaseContext().sendBroadcast(mediaScanIntent);
              } else {
                sendBroadcast(
                    new Intent(
                        Intent.ACTION_MEDIA_MOUNTED,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));
              }
              dialog1.dismiss();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
    btn_Cancel = dialog1.findViewById(R.id.btn_Cancel);
    btn_Cancel.setOnClickListener(v -> dialog1.dismiss());
  }
}
