package com.example.unibus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search_LostItems extends AppCompatActivity {

    private RecyclerView itemRecyclerView;
    private ArrayList<PostedLostItem> lostItemArrayList = new ArrayList<>();
    private PostLostItemAdapter postLostItemAdapter;
    private EditText searchEditText;
    private Button reportLostButton;
    private ImageView cannotFound;
    private ImageButton searchBackMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lost_items);

        initViews();
        setupButtons();
        setupBackButtons();

        // Assuming you have a database reference for "Posted_LostItems"
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posted_LostItems");

        // Add a ValueEventListener to listen for changes in the data
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                handleFirebaseData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                handleFirebaseError(databaseError);
            }
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLostItems(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    // Text is empty, clear the filtered list
                    clearFilteredList();
                }

            }
        });


    }

    private void initViews() {
        itemRecyclerView = findViewById(R.id.recyclerViewItem);
        searchEditText = findViewById(R.id.searchEditText);

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postLostItemAdapter = new PostLostItemAdapter(Search_LostItems.this, lostItemArrayList);
        itemRecyclerView.setAdapter(postLostItemAdapter);
        cannotFound = findViewById(R.id.cannotFound);
    }

    private void handleFirebaseData(DataSnapshot dataSnapshot) {
        lostItemArrayList.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            System.out.println(snapshot.getValue());
            PostedLostItem postedLostItem = snapshot.getValue(PostedLostItem.class);
            lostItemArrayList.add(postedLostItem);
        }
        postLostItemAdapter.notifyDataSetChanged();
    }

    private void handleFirebaseError(DatabaseError databaseError) {
        // Handle errors
    }

    private void filterLostItems(String searchText) {
        ArrayList<PostedLostItem> filteredList = new ArrayList<>();

        // Trim and convert the search text to lowercase
        String searchLowerCase = searchText.toLowerCase().trim();

        for (PostedLostItem item : lostItemArrayList) {
            // Trim and convert the item text to lowercase
            String itemLowerCase = item.getItem().toLowerCase().trim();

            // Add items to the filtered list if they contain the trimmed and lowercase search text
            if (itemLowerCase.contains(searchLowerCase)) {
                filteredList.add(item);
            }
        }

        // Update the adapter with the filtered list
        postLostItemAdapter.filterList(filteredList);
        postLostItemAdapter.notifyDataSetChanged();

        cannotFound.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupButtons() {
        reportLostButton = findViewById(R.id.reportLostButton);
        reportLostButton.setOnClickListener(v -> startActivities(new Intent[]{new Intent(Search_LostItems.this, Report_LostItems.class)}));
    }

    private void clearFilteredList() {
        ArrayList<PostedLostItem> emptyList = new ArrayList<>();
        postLostItemAdapter.filterList(emptyList);
        postLostItemAdapter.notifyDataSetChanged();
        cannotFound.setVisibility(View.VISIBLE);
    }

    private void setupBackButtons() {
        searchBackMain = findViewById(R.id.searchBackMain);
        searchBackMain.setOnClickListener(v -> startActivities(new Intent[]{new Intent(Search_LostItems.this, LostItems.class)}));
    }
}
