package com.peopledemo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment2 extends Fragment{
    ViewGroup viewGroup;

    private RecyclerView gallery_recycler;
    private ImageView userprofile;
    private TextView userid;
    private String user_email;
    private GalleryAdapter mAdapter;


    public Fragment2(String user_email) {
        this.user_email = user_email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment2,container,false);

        gallery_recycler = viewGroup.findViewById(R.id.gallery_recyclerview);
        userprofile = viewGroup.findViewById(R.id.gallery_user_profile);
        userid = viewGroup.findViewById(R.id.gallery_user_name);

        mAdapter = new GalleryAdapter();
        gallery_recycler.setLayoutManager(new GridLayoutManager(getActivity(),3));
        gallery_recycler.addItemDecoration(new GalleryItemDecoration(20));
        gallery_recycler.setAdapter(mAdapter);

        RetrofitHelper.getApiService().receiveUser(user_email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response != null) {
                    User user = response.body();
                    userprofile.setImageBitmap(getBitmapFromString(user.getProfile()));
                    userid.setText(user.getUserid());
                    if(user.getFeed().size() != 0){
                        for(int i = 0; i < user.getFeed().size(); i++)
                            mAdapter.additem(user.getFeed().get(i));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gallery_recycler.invalidateItemDecorations();
                            }
                        });
                    }
                }else{
                    Log.e("Gallery error","res null");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Gallery error",t.getMessage());
            }
        });


        return viewGroup;
    }


    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
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
}