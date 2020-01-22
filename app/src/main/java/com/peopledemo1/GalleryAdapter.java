package com.peopledemo1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private OnItemClickListener mListener = null;
    private ArrayList<GalleryItem> feedlist = new ArrayList<>();

    public interface OnItemClickListener {
        void OnItemClick(View v, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.gallery_item, parent, false);
        GalleryAdapter.ViewHolder vh = new GalleryAdapter.ViewHolder(view) ;
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        Bitmap bitmap = getBitmapFromString(feedlist.get(position).getFeed_img());
        holder.feeditem.setImageBitmap(Bitmap.createBitmap(bitmap,bitmap.getWidth()/4,bitmap.getHeight()/4,400,400));
    }

    @Override
    public int getItemCount() {
        return feedlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView feeditem;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            feeditem = itemView.findViewById(R.id.gallery_recycler_item);

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

    public void additem(GalleryItem feed){
        feedlist.add(feed);
    }

    public ArrayList<GalleryItem> getFeedlist() {
        return feedlist;
    }

    private Bitmap getBitmapFromString(String stringPicture) {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

}
