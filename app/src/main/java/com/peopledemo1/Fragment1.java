package com.peopledemo1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment1 extends Fragment{

    private String user_email;

    ViewGroup viewGroup;
    RecyclerView mRecyclerView = null;
    FeedAdapter feedAdapter= null;
    User user;

    public Fragment1(String user_email) { this.user_email = user_email; }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1,null);

        RetrofitHelper.getApiService().receiveUser(user_email).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                if(user != null){
                    RetrofitHelper.getApiService().getFeedAll().enqueue(new Callback<List<Feed>>() {
                        @Override
                        public void onResponse(Call<List<Feed>> call, Response<List<Feed>> response) {
                            List<Feed> feedList = response.body();
                            if(feedList != null && feedList.size() != 0){
                                for(int i = 0; i < user.getPrediction().size(); i++){
                                    /*if(user.getPrediction().get(i).getProbability() <= 0)
                                        continue;*/
                                    for(int j = 0; j < feedList.size(); j++){
                                        if(user.getPrediction().get(i).getName().equals(feedList.get(j).getUserid())){
                                            feedAdapter.addItem(feedList.get(j));
                                            feedList.remove(j);
                                            j--;
                                        }
                                    }
                                }
                            }
                            if(getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        feedAdapter.notifyDataSetChanged();
                                        mRecyclerView.invalidate();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Feed>> call, Throwable t) {
                            Log.e("Main feed all error",t.getMessage());
                        }
                    });
                }else
                    Log.e("Main feed error","res null");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Main feed error",t.getMessage());
            }
        });


        mRecyclerView = viewGroup.findViewById(R.id.feedview);

        feedAdapter = new FeedAdapter(getActivity().getApplicationContext());
        mRecyclerView.setAdapter(feedAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        feedAdapter.notifyDataSetChanged();

        feedAdapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),closeupActivity.class);
                intent.putExtra("feedid",feedAdapter.getmData().get(position).getFeedid());
                startActivity(intent);
            }
        });

        return viewGroup;
    }
}

