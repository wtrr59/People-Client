package com.peopledemo1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPhotoActivity extends AppCompatActivity {

    private final static int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_FROM_ALBUM = 4;
    private String user_email;
    private User userinfo;
    private Bitmap mBitmap;
    private Feed newfeed = new Feed();
    private File tempFile;

    EditText outer;
    EditText top;
    EditText pants;
    EditText shoe;
    ImageButton imageButton;

    private String imageFilePath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addphoto);

        user_email = getIntent().getStringExtra("email");
        RetrofitHelper.getApiService().receiveUser(user_email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response != null){
                    userinfo = response.body();
                    newfeed.setProfile(userinfo.getProfile());
                    newfeed.setUserid(userinfo.getUserid());
                    newfeed.setFeedid(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("addfeed error",t.getMessage());
            }
        });
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        Button addpostbutton = (Button) findViewById(R.id.addpostbutton);
        outer =  findViewById(R.id.outer);
        top =  findViewById(R.id.top);
        pants =  findViewById(R.id.pants);
        shoe =  findViewById(R.id.shoe);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendTakePhotoIntent();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        addpostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newfeed.getImage() != null) {
                    newfeed.setClothes(new Clothes(outer.getText().toString(), top.getText().toString(), pants.getText().toString(), shoe.getText().toString()));
                    RetrofitHelper.getApiService().postFeed(newfeed).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String result = response.body();
                            if(result != null){
                                finish();
                            }else{
                                Log.e("feedmake error","result null");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("feedmake error",t.getMessage());
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"이미지를 업로드하세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);
            ExifInterface exif = null;

            try {
                exif = new ExifInterface(imageFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }


            mBitmap = rotate(bitmap, exifDegree);
            mBitmap = resizeBitmap(mBitmap);
            newfeed.setImage(getStringFromBitmap(mBitmap));
            imageButton.setImageBitmap(mBitmap);
        }

        if (requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Cursor cursor = null;

            try {
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }


            ExifInterface exif = null;
            try {
                exif = new ExifInterface(tempFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

            int exifOrientation;
            int exifDegree;

            if (exif != null) {
                exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                exifDegree = exifOrientationToDegrees(exifOrientation);
            } else {
                exifDegree = 0;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            mBitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            mBitmap = resizeBitmap(mBitmap);
            mBitmap = rotate(mBitmap, exifDegree);
            imageButton.setImageBitmap(mBitmap);
            newfeed.setImage(getStringFromBitmap(mBitmap));
        }
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,      /* prefix */
                ".jpg",         /* suffix */
                storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    static public Bitmap resizeBitmap(Bitmap original) {

        int resizeWidth = 1300;
        int resizeHeight = 2000;

        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, resizeHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }
}
