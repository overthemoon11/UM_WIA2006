package com.example.Lostitem;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unibus.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context context;
    private List<Uri> imageList;
    private OnItemLongClickListener onItemLongClickListener;

    public ImageAdapter(Context context, List<Uri> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.onItemLongClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUrl = imageList.get(position);
        Picasso.get().load(imageUrl).fit().into(holder.imageView);

        holder.imageView.setOnLongClickListener(view -> {
            if (onItemLongClickListener != null) {
                onItemLongClickListener.onItemLongClick(holder.getAdapterPosition());
            }
            return true;
        });
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItem);
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }


}


