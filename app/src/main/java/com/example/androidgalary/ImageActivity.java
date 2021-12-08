package com.example.androidgalary;

import androidx.appcompat.app.AppCompatActivity;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

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
import id.zelory.compressor.Compressor;

public class ImageActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPagerFixer viewPager;
    Button btn;
    Button btn_Share;
    Button btn_Crop;
    Button btn_Resize;
    int position=9;
    String diachi;
    boolean loai;
    static public boolean co=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        //nhan thong tin tu activity truoc
        final Intent intent = getIntent();
        diachi=intent.getStringExtra("vitri");
        loai=intent.getBooleanExtra("loai",false);
        //

        Anhxa();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        viewPager.setCurrentItem(position);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if(i==R.id.compress)
                {
                    final Dialog dialog1 = new Dialog(ImageActivity.this);
                    dialog1.setTitle("Nhập phần trăm muốn nén: ");
                    dialog1.setContentView(R.layout.compress_dialog);
                    dialog1.show();
                    final SeekBar seekBar=(SeekBar) dialog1.findViewById(R.id.seekBar);
                    final TextView textView=(TextView) dialog1.findViewById(R.id.textView_per);
                    final EditText editText=(EditText) dialog1.findViewById(R.id.editText_per);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            try{
                                //Update Seekbar value after entering a number
                                if(s.toString().equals(""))
                                {
                                    seekBar.setProgress(0);
                                }
                                else seekBar.setProgress(Integer.parseInt(s.toString()));
                            } catch(Exception ex) {

                            }

                        }
                    });
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            textView.setText("" + progress + "%");
                            editText.setText(""+seekBar.getProgress());
                            editText.setSelection(editText.getText().length());
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            int value = seekBar.getProgress();

                        }
                    });
                    Button btn_OK,btn_Cancel;
                    btn_OK=(Button) dialog1.findViewById(R.id.btn_OK);
                    btn_OK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (loai == false) {
                                int tmp = viewPager.getCurrentItem();
                                Hinh choosenImage = AnhFragment.mangHinh.get(tmp);
                                File imgFile = new File(choosenImage.duongdan);

                                try {

                                    int Quality = 0;
                                    if (editText.getText().toString().equals("")) {
                                        Quality = 5;
                                    } else {
                                        Quality = Integer.parseInt(editText.getText().toString()) / 10;
                                    }
                                /*BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = Quality;
                                Bitmap myBm= BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);*/
                                    Bitmap mySample = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    Bitmap myBm = Bitmap.createScaledBitmap(mySample, mySample.getWidth() / Quality, mySample.getHeight() / Quality, true);

                                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "compress_" + UUID.randomUUID().toString() + ".jpeg");
                                    f.createNewFile();
                                    FileOutputStream out = new FileOutputStream(f);
                                    f.setReadable(true, false);
                                    f.setWritable(true, false);
                                    myBm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                    out.flush();
                                    out.close();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        Intent mediaScanIntent = new Intent(
                                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri contentUri = Uri.fromFile(f);
                                        mediaScanIntent.setData(contentUri);
                                        getBaseContext().sendBroadcast(mediaScanIntent);
                                    } else {
                                        sendBroadcast(new Intent(
                                                Intent.ACTION_MEDIA_MOUNTED,
                                                Uri.parse("file://"
                                                        + Environment.getExternalStorageDirectory())));
                                    }
                                    dialog1.dismiss();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                int tmp = viewPager.getCurrentItem();
                                Hinh choosenImage = MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(tmp);
                                File imgFile = new File(choosenImage.duongdan);

                                try {

                                    int Quality = 0;
                                    if (editText.getText().toString().equals("")) {
                                        Quality = 5;
                                    } else {
                                        Quality = Integer.parseInt(editText.getText().toString()) / 10;
                                    }
                                /*BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = Quality;
                                Bitmap myBm= BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);*/
                                    Bitmap mySample = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                    Bitmap myBm = Bitmap.createScaledBitmap(mySample, mySample.getWidth() / Quality, mySample.getHeight() / Quality, true);

                                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "compress_" + UUID.randomUUID().toString() + ".jpeg");
                                    f.createNewFile();
                                    FileOutputStream out = new FileOutputStream(f);
                                    f.setReadable(true, false);
                                    f.setWritable(true, false);
                                    myBm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                                    out.flush();
                                    out.close();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        Intent mediaScanIntent = new Intent(
                                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                        Uri contentUri = Uri.fromFile(f);
                                        mediaScanIntent.setData(contentUri);
                                        getBaseContext().sendBroadcast(mediaScanIntent);
                                    } else {
                                        sendBroadcast(new Intent(
                                                Intent.ACTION_MEDIA_MOUNTED,
                                                Uri.parse("file://"
                                                        + Environment.getExternalStorageDirectory())));
                                    }
                                    dialog1.dismiss();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    btn_Cancel=(Button) dialog1.findViewById(R.id.btn_Cancel);
                    btn_Cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog1.dismiss();
                        }
                    });


                }
                return false;
            }
        });
        //Sự kiện crop ảnh
        btn_Crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loai==false) {
                    int tmp = viewPager.getCurrentItem();
                    Log.d("tmp", tmp + "");
                    Log.d("tmp", AnhFragment.mangHinh.get(tmp).tenhinh + "");
                    Hinh choosenImage = AnhFragment.mangHinh.get(tmp);
                    File imgFile = new File(choosenImage.duongdan);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    editBitmap(myBitmap, choosenImage.duongdan);
                }
                else
                {
                    int tmp = viewPager.getCurrentItem();
                    Log.d("tmp", tmp + "");
                    Log.d("tmp", AnhFragment.mangHinh.get(tmp).tenhinh + "");
                    Hinh choosenImage = MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(tmp);
                    File imgFile = new File(choosenImage.duongdan);
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    editBitmap(myBitmap, choosenImage.duongdan);
                }
            }
        });
        //Xử lý sự kiện Share ảnh
        btn_Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp=viewPager.getCurrentItem();
                Hinh choosenImage=AnhFragment.mangHinh.get(tmp);
                try {
                    final File photoFile = new File(choosenImage.duongdan);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(photoFile));
                    startActivity(Intent.createChooser(intent,"Share via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //choosenImage.duongdan
            }
        });
        //Sự kiện resize ảnh
        btn_Resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp=viewPager.getCurrentItem();
                final Hinh choosenImage=AnhFragment.mangHinh.get(tmp);
                final Dialog dialog1;
                dialog1 = new Dialog(ImageActivity.this);
                dialog1.setTitle("Nhập kích thước: ");
                dialog1.setContentView(R.layout.resize_dialog);
                dialog1.show();
                final EditText Width,Height;
                Button btnOK,btnCancel;
                Width=dialog1.findViewById(R.id.editText_Width);
                Height=dialog1.findViewById(R.id.editText_Height);
                btnOK=dialog1.findViewById(R.id.btnOk);
                btnCancel=dialog1.findViewById(R.id.btnCancel);
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int width=Integer.parseInt(Width.getText().toString());
                        int height=Integer.parseInt(Height.getText().toString());
                        File imgFile=new File(choosenImage.duongdan);
                        /*ImageView temp = null;
                        Bitmap bitmap=null;
                        Glide.with(getBaseContext()).load(imgFile).override(width,height).into(temp);*/
                        Bitmap myBitmap=BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Bitmap resBitmap=Bitmap.createScaledBitmap(myBitmap,width,height,false);
                        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "resize_"+ UUID.randomUUID().toString() +".jpeg");
                        try {
                            f.createNewFile();
                            FileOutputStream out = new FileOutputStream(f);
                            f.setReadable(true,false);
                            f.setWritable(true,false);
                            /*Matrix matrix = new Matrix();

                            matrix.postRotate(270);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(resBitmap, 0, 0, resBitmap.getWidth(), resBitmap.getHeight(), matrix, true);*/
                            resBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Intent mediaScanIntent = new Intent(
                                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                Uri contentUri = Uri.fromFile(f);
                                mediaScanIntent.setData(contentUri);
                                getBaseContext().sendBroadcast(mediaScanIntent);
                            } else {
                                sendBroadcast(new Intent(
                                        Intent.ACTION_MEDIA_MOUNTED,
                                        Uri.parse("file://"
                                                + Environment.getExternalStorageDirectory())));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog1.dismiss();


                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
            }
        });

        //*************************************************
        //* Xử lí sự kiện xóa ảnh trong hai trường hợp    *
        //*     I:  ảnh của Anhfragment                   *
        //*     II: ảnh của Album                         *
        //*************************************************
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // luu lai vi tri cua hinh
                int tmp=viewPager.getCurrentItem();
                //Toast.makeText(ImageActivity.this, ""+viewPager.getCurrentItem(), //Toast.LENGTH_SHORT).show();
                if(loai==false)
                {
                    //xử lý I

                    //Xóa collectedimgs và add đối tượng cần xóa
                    MainActivity.collectedimgs.clear();
                    MainActivity.collectedimgs.add(AnhFragment.mangHinh.get(tmp));

                    // Tìm và xóa ảnh


                    for(int j=0;j<MainActivity.collectedimgs.size();j++)
                    {
                        File file =new File (MainActivity.collectedimgs.get(j).getDuongdan());
                        boolean flag=file.delete();
                        //Toast.makeText(ImageActivity.this,"Do dai cua mang"+MainActivity.collectedimgs.size(),//Toast.LENGTH_SHORT).show();
                    }

                    //***Cập nhập mảng chứa album***//
                    refreshfAlbum(MainActivity.collectedimgs);
                    //*** Xóa album rộng => Xóa Mảng rỗng trong MainActivity.mang ***//
                    for (int i = 0; i < MainActivity.mang.size(); i++) {
                        if (MainActivity.mang.get(i).size() == 0) {
                            MainActivity.mang.remove(i);
                            MainActivity.MangTen.remove(i);
                        }
                    }
                    for(int i1=0;i1<MainActivity.collectedimgs.size();i1++)
                    {
                        for(int j=0;j<AnhFragment.mangHinh.size();j++)
                        {
                            if(AnhFragment.mangHinh.get(j).getDuongdan().equals(MainActivity.collectedimgs.get(i1).duongdan))
                            {
                                AnhFragment.mangHinh.remove(j);
                            }
                        }
                    }
                    ghivaobonhotrongtenalbum();
                    ghivaobonhotrong();
                    //su ly sau khi xoa

                    ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(AnhFragment.mangHinh, ImageActivity.this, getSupportFragmentManager());
                    viewPager.setAdapter(imagePagerAdapter);

                    //*** Tìm vị trị thích hợp để setCurrent cho ViewPager***//
                    if(tmp==AnhFragment.mangHinh.size())
                        viewPager.setCurrentItem(tmp-1);
                    else
                        viewPager.setCurrentItem(tmp);

                }
                else
                {
                    //xử lý II

                    MainActivity.collectedimgs.clear();
                    MainActivity.collectedimgs.add(MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(viewPager.getCurrentItem()));

                    //***Cập nhật mảng chứa các album***//
                    if (MainActivity.collectedimgs.size() == 0) {
                        //Toast.makeText(getApplicationContext(), "Chua chon anh", //Toast.LENGTH_SHORT).show();
                    } else {
                        //***Các ảnh cần xóa nằm trong collectedimgs***//                                         *
                        for (int i = 0; i < MainActivity.collectedimgs.size(); i++) {
                            for (int j1 = 0; j1 < MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size(); j1++) {
                                if (MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(j1).getDuongdan().equals(MainActivity.collectedimgs.get(i).getDuongdan())) {
                                    MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).remove(j1);
                                }
                            }


                        }
                    }


                    //su ly sau khi xoa
                    if(MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size()==0)
                    {
                        //*** Xóa album rộng => Xóa Mảng rỗng trong MainActivity.mang ***//
                        for (int i = 0; i < MainActivity.mang.size(); i++) {
                            if (MainActivity.mang.get(i).size() == 0) {
                                MainActivity.mang.remove(i);
                                MainActivity.MangTen.remove(i);
                            }
                        }
                        ghivaobonhotrongtenalbum();
                        ghivaobonhotrong();
                        co=true;
                        finish();
                    }
                    else {
                        //*** Xóa album rộng => Xóa Mảng rỗng trong MainActivity.mang ***//
                        for (int i = 0; i < MainActivity.mang.size(); i++) {
                            if (MainActivity.mang.get(i).size() == 0) {
                                MainActivity.mang.remove(i);
                                MainActivity.MangTen.remove(i);
                            }
                        }
                        ghivaobonhotrongtenalbum();
                        ghivaobonhotrong();
                        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum), ImageActivity.this, getSupportFragmentManager());
                        viewPager.setAdapter(imagePagerAdapter);
                        //*** Tìm vị trị thích hợp để setCurrent cho ViewPager***//
                        if (tmp == MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size() - 1)
                            viewPager.setCurrentItem(tmp - 1);
                        else
                            viewPager.setCurrentItem(tmp);
                    }
                }
            }
        });

    }
    private void editBitmap (Bitmap bitmap,String filePath) {
        try {
            File file = new File(filePath);
            file.setReadable(true,false);
            file.setWritable(true,false);
            /*FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();*/
            CropImage.activity(Uri.fromFile(file))
                    .start(this);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bm = null;
                InputStream is = null;
                String url = resultUri.toString();
                BufferedInputStream bis = null;
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
//                    FileOutputStream out = new FileOutputStream(file);
//                    file.setReadable(true,false);
//                    file.setWritable(true,false);
//                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    out.flush();
//                    out.close();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        Intent mediaScanIntent = new Intent(
//                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                        Uri contentUri = Uri.fromFile(file);
//                        mediaScanIntent.setData(contentUri);
//                        getBaseContext().sendBroadcast(mediaScanIntent);
//                    } else {
//                        sendBroadcast(new Intent(
//                                Intent.ACTION_MEDIA_MOUNTED,
//                                Uri.parse("file://"
//                                        + Environment.getExternalStorageDirectory())));
//
//                    }
                    FileOutputStream out = new FileOutputStream(file);
                    file.setReadable(true, false);
                    file.setWritable(true, false);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void Anhxa() {

        toolbar=findViewById(R.id.toolbarofimgactivity);

        btn=findViewById(R.id.delete_btn);
        btn_Share=(Button) findViewById(R.id.share_btn);
        btn_Crop=(Button) findViewById(R.id.crop_btn);
        btn_Resize=(Button) findViewById(R.id.Resize_btn);

//        //Tạo nút back trên toolbar
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.inflateMenu(R.menu.menu_image);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        viewPager=(ViewPagerFixer) findViewById(R.id.viewpagerofimgactivity);


        if(loai==false) {
            //********************************************
            //*Xử lí trường hợp: Xuất ảnh của Anhfragment*
            //********************************************
            for(int i=0;i<AnhFragment.mangHinh.size();i++)
            {
                if(diachi.equals(AnhFragment.mangHinh.get(i).getDuongdan()))
                {
                    position=i;
                    break;
                }
            }
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(AnhFragment.mangHinh, this, getSupportFragmentManager());
            viewPager.setAdapter(imagePagerAdapter);
        }
        else{
            //****************************************
            //*Xử lí trường hợp: Xuất ảnh của 1 album*
            //****************************************
            for(int i=0;i<MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).size();i++)
            {
                if(diachi.equals(MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum).get(i).getDuongdan()))
                {
                    position=i;
                    break;
                }
            }
            ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(MainActivity.mang.get(AlbumFragment.postionofFocusingAlbum), this, getSupportFragmentManager());
            viewPager.setAdapter(imagePagerAdapter);
            Log.d("123456",""+AlbumFragment.postionofFocusingAlbum);

        }



    }
    //*** Hàm dùng để cập nhật mảng chứa các album sau khi xóa và ghi lại vào bộ nhớ***//
    public void refreshfAlbum(ArrayList<Hinh> collectedimgs) {


        //***Các ảnh cần xóa nằm trong collectedimgs***//                                         *


        for (int i = 0; i < collectedimgs.size(); i++) {
            for (int j = 0; j < MainActivity.mang.size(); j++)
                for (int j1 = 0; j1 < MainActivity.mang.get(j).size(); j1++) {
                    if (MainActivity.mang.get(j).get(j1).getDuongdan().equals(collectedimgs.get(i).getDuongdan())) {
                        MainActivity.mang.get(j).remove(j1);
                    }
                }
        }


    }
    //*** Hàm dùng để ghi lại danh sách album vào bộ nhớ***//
    public void ghivaobonhotrong()
    {

        try {
            //***Lấy đường dẫn bộ nhớ Cache***//
            File duongdan=getCacheDir();
            //***Đặt tên file chứa thông tin album***//
            File taptin=new File(duongdan,"imgofalbum.txt");
            //***Tạo luồng ghi***//
            FileOutputStream out=new FileOutputStream(taptin);
            //*** Tạo buffer để chứa dự liệu cần ghi vào File "imgofalbum.txt" ***//
            String buffer=new String();
            //*** Tiến hành ghi dữ liệu vào buffer***//

            //**********************************************************************************************************************
            //* Một album được ghi dưới dạng:                                                                                      *
            //*                                                                                                                    *
            //* "duongdan[1]"#"tenhinh[1]"#"date[1]"#"duongdan[2]"#"tenhinh[2]"#"date[2]"#"duongdan[3]"#"tenhinh[4]"#"date[3]"...% *
            //*                                                                                                                    *
            //* Kết thúc 1 album là dấu (  %  )                                                                                    *
            //**********************************************************************************************************************
            for(int i=0;i<MainActivity.mang.size();i++) {
                for (int j = 0; j < MainActivity.mang.get(i).size(); j++) {
                    if(j==MainActivity.mang.get(i).size()-1)
                    {
                        //Trường hợp ảnh cuối cùng của Album
                        //Không có dấu (  #  ) ở cuối
                        buffer = buffer + MainActivity.mang.get(i).get(j).getDuongdan().toString()
                                + "#"
                                + MainActivity.mang.get(i).get(j).getTenHinh().toString()
                                + "#"
                                + MainActivity.mang.get(i).get(j).getAddDate().toString()
                        ;
                    }
                    else {
                        //Trường hợp bình thường
                        //Có dấu (  #  ) ở cuối
                        buffer = buffer + MainActivity.mang.get(i).get(j).getDuongdan().toString()
                                + "#"
                                + MainActivity.mang.get(i).get(j).getTenHinh().toString()
                                + "#"
                                + MainActivity.mang.get(i).get(j).getAddDate().toString()
                                + "#";
                    }

                }
                buffer+="%";
            }
            //*** ghi vào bộ nhớ ***//
            out.write(buffer.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void ghivaobonhotrongtenalbum()
    {
        File duongdan=getCacheDir();
        File file= new File(duongdan,"nameofalbum.txt");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String buffer=new String();
            for(int i=0;i<MainActivity.MangTen.size();i++)
            {
                if(i!=MainActivity.MangTen.size()-1)
                    buffer+=MainActivity.MangTen.get(i)+"#";
                else
                    buffer+=MainActivity.MangTen.get(MainActivity.MangTen.size()-1);
            }

            fileOutputStream.write(buffer.getBytes());
            fileOutputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
}

