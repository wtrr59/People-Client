package com.peopledemo1;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
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


public class Fragment1 extends Fragment{

    ViewGroup viewGroup;
    RecyclerView mRecyclerView = null;
    FeedAdapter feedAdapter= null;
    ArrayList<FeedRecyclerItem> mList = new ArrayList<FeedRecyclerItem>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mList.clear();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1,null);

        mRecyclerView = viewGroup.findViewById(R.id.feedview);

        feedAdapter = new FeedAdapter(mList);
        mRecyclerView.setAdapter(feedAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addItem(getActivity().getDrawable(R.drawable.circleprofile),"mikeTyson88",getActivity().getDrawable(R.drawable.postedimg));
        }

        feedAdapter.notifyDataSetChanged();

        feedAdapter.setOnItemClickListener(new FeedAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),closeupActivity.class);
                startActivity(intent);
            }
        });


        return viewGroup;
    }

    public void addItem(Drawable profile, String userid, Drawable posts){
        FeedRecyclerItem item = new FeedRecyclerItem();

        item.setPeoplePost(profile);
        item.setUserID(userid);
        item.setPeoplePost(posts);

        mList.add(item);
    }
}

