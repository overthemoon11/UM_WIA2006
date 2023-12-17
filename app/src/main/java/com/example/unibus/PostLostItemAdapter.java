package com.example.unibus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostLostItemAdapter extends RecyclerView.Adapter<PostLostItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PostedLostItem> postedItemList;
    private ArrayList<PostedLostItem> filteredList;

    public PostLostItemAdapter(Context context, ArrayList<PostedLostItem> postedItemList) {
        this.context = context;
        this.postedItemList = postedItemList;
        this.filteredList = new ArrayList<>(postedItemList); // Initialize filtered list with all items
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lost_item_lists, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostedLostItem item = filteredList.get(position);

        holder.locationShow.setText("Location: " + item.getLocation());
        holder.dateShow.setText("Date: " + item.getDate());
        holder.timeShow.setText("Time: " + item.getTime());
        holder.itemShow.setText("Lost items: " + item.getItem());
        holder.pointShow.setText("Collect Point: " + item.getPoint());
        holder.extraShow.setText(item.getExtra());

        PostLostItemImageAdapter imageAdapter = new PostLostItemImageAdapter(context, item.getImageUrls());
        holder.lostItemImageList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        int horizontalSpaceWidth = 30; // Replace with your desired horizontal spacing in pixels
        holder.lostItemImageList.addItemDecoration(new HorizontalSpaceItemDecoration(horizontalSpaceWidth));
        holder.lostItemImageList.setAdapter(imageAdapter);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void filterList(ArrayList<PostedLostItem> filteredList) {
        this.filteredList = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView locationShow;
        TextView dateShow;
        TextView timeShow;
        TextView itemShow;
        TextView pointShow;
        TextView extraShow;
        RecyclerView lostItemImageList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationShow = itemView.findViewById(R.id.locationShow);
            dateShow = itemView.findViewById(R.id.dateShow);
            timeShow = itemView.findViewById(R.id.timeShow);
            itemShow = itemView.findViewById(R.id.itemShow);
            pointShow = itemView.findViewById(R.id.pointShow);
            extraShow = itemView.findViewById(R.id.extraShow);
            lostItemImageList = itemView.findViewById(R.id.lostItemImageList);
        }
    }

}
