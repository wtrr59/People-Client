package com.peopledemo1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText email_edit;
    private EditText id_edit;
    private EditText password_edit;
    private static final int PICK_FROM_ALBUM = 1;
    private File tempFile;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email_edit = findViewById(R.id.signup_email_edit);
        id_edit = findViewById(R.id.signup_id_edit);
        password_edit = findViewById(R.id.signup_password_edit);

        findViewById(R.id.signup_accept_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(id_edit.getText().toString(),password_edit.getText().toString(),email_edit.getText().toString());

                RetrofitHelper.getApiService().sendSign(user).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String res = response.body();
                        if(res == null)
                            Log.e("res","null");
                        if(res != null && res.equals("성공")) {
                            if(mBitmap == null)
                                mBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.defaultprofile)).getBitmap();

                            try {
                                File filesDir = getApplicationContext().getFilesDir();
                                File file = new File(filesDir, email_edit.getText().toString()+".png");

                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                                byte[] bitmapdata = bos.toByteArray();

                                FileOutputStream fos = new FileOutputStream(file);
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();


                                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                                MultipartBody.Part body = MultipartBody.Part.createFormData("uploadprofile", file.getName(), reqFile);
                                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "uploadprofile");

                                RetrofitHelper.getApiService().postProfileImage(body,name).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                                        t.printStackTrace();
                                    }
                                });


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                            intent.putExtra("email",email_edit.getText().toString());
                            startActivity(intent);
                        }else
                            Log.e("Signup Error","회원가입 실패");
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Signup Error",t.getMessage());
                    }
                });
            }
        });

        findViewById(R.id.signup_profile_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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

            ImageButton profile_btn = findViewById(R.id.signup_profile_btn);

            BitmapFactory.Options options = new BitmapFactory.Options();
            mBitmap = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
            mBitmap = resizeBitmap(mBitmap);
            profile_btn.setBackground(new BitmapDrawable(mBitmap));
        }
    }

    static public Bitmap resizeBitmap(Bitmap original) {

        int resizeWidth = 200;

        double aspectRatio = (double) original.getHeight() / (double) original.getWidth();
        int targetHeight = (int) (resizeWidth * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(original, resizeWidth, targetHeight, false);
        if (result != original) {
            original.recycle();
        }
        return result;
    }
}
