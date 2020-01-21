package com.peopledemo1;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{
    private ArrayList<FeedRecyclerItem> mData = null;
    private OnItemClickListener mListener = null;
    Context context;

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    FeedAdapter(ArrayList<FeedRecyclerItem> list){
        mData = list ;
    }

    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.feed_item, parent, false);
        FeedAdapter.ViewHolder vh = new FeedAdapter.ViewHolder(view) ;

        return vh;

    }

    @Override
    public void onBindViewHolder(FeedAdapter.ViewHolder holder, int position) {

        FeedRecyclerItem item = mData.get(position) ;

        holder.profile.setImageDrawable(item.getProfileimg());
        holder.userID.setText(item.getUserID()) ;
        holder.userPost.setImageDrawable(item.getPeoplePost());
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile ;
        TextView userID ;
        ImageView userPost ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            profile = itemView.findViewById(R.id.profile) ;
            userID = itemView.findViewById(R.id.identity) ;
            userPost = itemView.findViewById(R.id.feedimage) ;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.background_rounding);
                userPost.setBackground(drawable);
                userPost.setClipToOutline(true);
            }



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                        if(mListener != null)
                            mListener.OnItemClick(v,pos);
                }
            });

        }
    }

}
