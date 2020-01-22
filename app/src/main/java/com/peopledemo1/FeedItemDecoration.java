package com.peopledemo1;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class FeedItemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public FeedItemDecoration(int divHeight){
        this.space = divHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}
