package com.example.unibus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Post_LostItems extends AppCompatActivity {

    private EditText editLocation, editDate, editTime, editItem, editPoint, editExtra;
    private Button btnSubmit;
    private ImageButton btnSelectImage;
    private RecyclerView recyclerView;
    private ImageAdapter imageAdapter;
    private List<Uri> selectedImageList;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lost_items);

        btnSelectImage = findViewById(R.id.selectImageButton);
        recyclerView = findViewById(R.id.imageRecyclerView);
        selectedImageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(this, selectedImageList);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        int horizontalSpaceWidth = 30; // Replace with your desired horizontal spacing in pixels
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(horizontalSpaceWidth));
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(layoutManager);

        btnSelectImage.setOnClickListener(view -> openImagePicker());
        imageAdapter.setOnItemLongClickListener(position -> removeSelectedImage(position));

        editLocation = findViewById(R.id.postEditLocation);
        editDate = findViewById(R.id.postEditDate);
        editTime = findViewById(R.id.postEditTime);
        editItem = findViewById(R.id.postEditItem);
        editPoint = findViewById(R.id.postEditPoint);
        editExtra = findViewById(R.id.postEditExtra);
        btnSubmit = findViewById(R.id.submitPostButton);

        btnSubmit.setOnClickListener(v -> submitPost());

        setupBackButton();
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            selectedImageList.add(selectedImageUri);
            Collections.reverse(selectedImageList);
            imageAdapter.notifyDataSetChanged();
        }
    }

    private void setupBackButton() {
        ImageButton postBackMain = findViewById(R.id.postBackMain);
        postBackMain.setOnClickListener(v -> startActivity(new Intent(Post_LostItems.this, LostItems.class)));
    }

    private void submitPost() {
        String location = editLocation.getText().toString();
        String date = editDate.getText().toString();
        String time = editTime.getText().toString();
        String item = editItem.getText().toString();
        String point = editPoint.getText().toString();
        String extra = editExtra.getText().toString();

        if (selectedImageList.size() >= 1 && !location.isEmpty() && !date.isEmpty() && !time.isEmpty()
                && !item.isEmpty() && !point.isEmpty() && !extra.isEmpty()) {
            uploadImagesToStorage(selectedImageList, location, date, time, item, point, extra);
        } else {
            Toast.makeText(this, "Please fill in all the fields and select at least one images.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImagesToStorage(List<Uri> imageUriList, String location, String date, String time, String item, String point, String extra) {
        List<String> imageUrlList = new ArrayList<>();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        for (int i = 0; i < imageUriList.size(); i++) {
            StorageReference imageRef = storageRef.child("lost_item_images/" + System.currentTimeMillis() + "_" + (i + 1) + ".jpg");
            Uri imageUri = imageUriList.get(i);

            final int finalI = i;
            imageRef.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrlList.add(uri.toString());

                        if (finalI == imageUriList.size() - 1) {
                            saveFoundItemToFirebase(imageUrlList, location, date, time, item, point, extra);
                        }
                    });
                } else {
                    Toast.makeText(Post_LostItems.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveFoundItemToFirebase(List<String> imageUrlList, String location, String date, String time, String item, String point, String extra) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posted_LostItems");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot snapshot) {
                String key = String.valueOf(snapshot.getChildrenCount());

                PostedLostItem addLostItem = new PostedLostItem(imageUrlList, location, date, time, item, point, extra);

                databaseReference.child(key).setValue(addLostItem).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Post_LostItems.this, "Successfully added Lost Item.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Post_LostItems.this, LostItems.class));
                        finish();
                    } else {
                        Toast.makeText(Post_LostItems.this, "Failed to add Lost Item.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });
    }

    private void removeSelectedImage(int position) {
        selectedImageList.remove(position);
        imageAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Photo removed", Toast.LENGTH_SHORT).show();
    }
}
