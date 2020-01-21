package com.peopledemo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class closeupActivity extends AppCompatActivity {

    private String feedid;
    private Feed feedinfo;

    private CircleImageView profile;
    private TextView userid;
    private ImageView userimage;
    private TextView outer;
    private TextView top;
    private TextView pants;
    private TextView shoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closeup);

        feedid = getIntent().getStringExtra("feedid");

        profile = findViewById(R.id.closeup_profile);
        userid = findViewById(R.id.closeup_userid);
        userimage = findViewById(R.id.closeup_image);
        outer = findViewById(R.id.closeup_outer);
        top = findViewById(R.id.closeup_top);
        pants = findViewById(R.id.closeup_pants);
        shoes = findViewById(R.id.closeup_shoes);

        RetrofitHelper.getApiService().getFeedByfeedid(feedid).enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                feedinfo = response.body();
                if(feedinfo != null){
                    profile.setImageBitmap(getBitmapFromString(feedinfo.getProfile()));
                    userid.setText(feedinfo.getUserid());
                    userimage.setImageBitmap(getBitmapFromString(feedinfo.getImage()));
                    outer.setText(feedinfo.getClothes().getOuter());
                    top.setText(feedinfo.getClothes().getTop());
                    pants.setText(feedinfo.getClothes().getPants());
                    shoes.setText(feedinfo.getClothes().getShoes());
                }else{
                    Log.e("closeup error","res null");
                }
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e("closeup error",t.getMessage());
            }
        });
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
