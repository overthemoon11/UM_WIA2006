package com.example.Lostitem;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int spaceWidth;

    public HorizontalSpaceItemDecoration(int spaceWidth) {
        this.spaceWidth = spaceWidth;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        // Apply spacing to the right of each item (except the last one)
        if (parent.getChildAdapterPosition(view) < parent.getAdapter().getItemCount() - 1) {
            outRect.right = spaceWidth;
        }
    }
}
