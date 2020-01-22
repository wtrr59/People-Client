package com.peopledemo1;

import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class Fragment2 extends Fragment{
    ViewGroup viewGroup;

    private static final int REQUEST_INVALIDATE = 3;
    private RecyclerView gallery_recycler;
    private CircleImageView userprofile;
    private TextView userid;
    private String user_email;
    private GalleryAdapter mAdapter;
    private FloatingActionButton feedaddbtn;

    public Fragment2(String user_email) {
        this.user_email = user_email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment2,container,false);

        gallery_recycler = viewGroup.findViewById(R.id.gallery_recyclerview);
        userprofile = viewGroup.findViewById(R.id.gallery_user_profile);
        userid = viewGroup.findViewById(R.id.gallery_user_name);
        feedaddbtn = viewGroup.findViewById(R.id.gallery_plus_feed);

        mAdapter = new GalleryAdapter();
        gallery_recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),closeupActivity.class);
                intent.putExtra("feedid",mAdapter.getFeedlist().get(position).getFeed_id());
                startActivity(intent);
            }
        });
        gallery_recycler.setLayoutManager(new GridLayoutManager(getActivity(),3));
        gallery_recycler.addItemDecoration(new GalleryItemDecoration(5));


        RetrofitHelper.getApiService().receiveUser(user_email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response != null) {
                    User user = response.body();
                    userprofile.setImageBitmap(getBitmapFromString(user.getProfile()));
                    userid.setText(user.getUserid());
                    RetrofitHelper.getApiService().getFeedByuserid(user.getUserid()).enqueue(new Callback<List<Feed>>() {
                        @Override
                        public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                            ArrayList<Feed> feedList = (ArrayList<Feed>) response.body();
                            if(feedList != null && feedList.size() != 0) {
                                for (int i = 0; i < feedList.size(); i++)
                                    mAdapter.additem(new GalleryItem(feedList.get(i).getImage(), feedList.get(i).getFeedid()));
                                if(getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();
                                            gallery_recycler.invalidate();
                                        }
                                    });
                                }
                            }else{
                                Log.e("Gallery Feed error","res null");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Feed>> call, Throwable t) {
                            Log.e("Gallery Feed error",t.getMessage());
                        }
                    });
                }else{
                    Log.e("Gallery error","res null");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Gallery error",t.getMessage());
            }
        });

        feedaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddPhotoActivity.class);
                intent.putExtra("email",user_email);
                getActivity().startActivityForResult(intent,REQUEST_INVALIDATE);
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

    public RecyclerView getGallery_recycler() {
        return gallery_recycler;
    }

    public GalleryAdapter getmAdapter() {
        return mAdapter;
    }
}