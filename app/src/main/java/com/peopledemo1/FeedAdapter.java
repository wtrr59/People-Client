package com.peopledemo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>{
    private ArrayList<Feed> mData = new ArrayList<>();
    private OnItemClickListener mListener = null;

    private Context mContext;

    public FeedAdapter(Context mContext) {
        this.mContext = mContext;
    }

    Context context;


    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
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

        Feed item = mData.get(position) ;

        holder.profile.setImageBitmap(getBitmapFromString(item.getProfile()));
        holder.userID.setText(item.getUserid()) ;
        holder.userPost.setImageBitmap(resizeBitmap(getBitmapFromString(item.getImage())));


    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile ;
        TextView userID ;
        ImageView userPost ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            profile = itemView.findViewById(R.id.profile) ;
            userID = itemView.findViewById(R.id.identity) ;
            userPost = itemView.findViewById(R.id.feedimage) ;

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

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public ArrayList<Feed> getmData() {
        return mData;
    }

    public void addItem(Feed feed) {
        mData.add(feed);
    }

    static public Bitmap resizeBitmap(Bitmap original) {

        int resizeWidth = 1600;
        int resizeHeight = 2400;

        Bitmap result = Bitmap.createBitmap(Bitmap.createScaledBitmap(original, resizeWidth, resizeHeight, false),0,0,1600,2000);
        if (result != original) {
            original.recycle();
        }
        return result;
    }
}
