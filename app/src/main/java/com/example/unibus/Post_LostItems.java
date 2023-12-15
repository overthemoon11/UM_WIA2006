package com.example.unibus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Post_LostItems extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private EditText editLocation, editDate, editTime, editItem, editPoint, editExtra;
    private Button btnSubmit;
    private ImageButton postBackMain;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton btnSelectImage;
    private ImageView item_imageView;
    private String imageUrl;

    private Uri selectedImageUri;

    private List<Uri> selectedImageUris = new ArrayList<>();
    private LinearLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lost_items);

        btnSelectImage = findViewById(R.id.selectImageButton);
        item_imageView = findViewById(R.id.lostItemImage);
        parentLayout = findViewById(R.id.linearImage);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });




        editLocation = findViewById(R.id.postEditLocation);
        editDate = findViewById(R.id.postEditDate);
        editTime = findViewById(R.id.postEditTime);
        editItem = findViewById(R.id.postEditItem);
        editPoint = findViewById(R.id.postEditPoint);
        editExtra = findViewById(R.id.postEditExtra);
        btnSubmit = findViewById(R.id.submitPostButton);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String location = editLocation.getText().toString();
                String date = editDate.getText().toString();
                String time = editTime.getText().toString();
                String item = editItem.getText().toString();
                String point = editPoint.getText().toString();
                String extra = editExtra.getText().toString();



                if (selectedImageUri!=null&&!location.isEmpty() && !date.isEmpty() && !time.isEmpty() && !item.isEmpty() && !point.isEmpty() && !extra.isEmpty()) {
                    uploadImageToStorage(selectedImageUri);

                    // Save data to Firebase
//                    saveLostItemToFirebase(imageUrl,location, date, time, item, point, extra);
                } else {
                    // Handle empty input
                }
            }
        });
        setupBackButtons();
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            item_imageView.setImageURI(selectedImageUri);

            updateViewPositions();
        }
    }



    private void setupBackButtons() {
        postBackMain = findViewById(R.id.postBackMain);
        postBackMain.setOnClickListener(v -> startActivities(new Intent[]{new Intent(Post_LostItems.this, LostItems.class)}));
    }

    private void saveFoundItemToFirebase(String imageUrl, String location, String date, String time, String item, String point, String extra) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posted_LostItems");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String key = String.valueOf(snapshot.getChildrenCount());

                // Create a PostedLostItem object with image URL
                PostedLostItem addLostItem = new PostedLostItem(imageUrl,location, date, time, item, point, extra);

                // Save to Firebase
                databaseReference.child(key).setValue(addLostItem).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(Post_LostItems.this, "Successfully added Lost Item.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Post_LostItems.this, Search_LostItems.class));
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



    private void uploadImageToStorage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to the image location in Firebase Storage
        StorageReference imageRef = storageRef.child("lost_item_images/" + System.currentTimeMillis() + ".jpg");

        // Upload the image
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Save the image URL along with other data to Firebase Database
                        imageUrl= uri.toString();
                        saveFoundItemToFirebase(imageUrl, editLocation.getText().toString(), editDate.getText().toString(), editTime.getText().toString(), editItem.getText().toString(), editPoint.getText().toString(), editExtra.getText().toString());
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle failed upload z
                    System.out.println(e);
                    Toast.makeText(Post_LostItems.this, "Image upload failed.", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateViewPositions() {
        int imageViewIndex = parentLayout.indexOfChild(item_imageView);
        int buttonIndex = parentLayout.indexOfChild(btnSelectImage);

        // Remove the views from the parent layout
        parentLayout.removeView(item_imageView);
        parentLayout.removeView(btnSelectImage);

        // Add the views back to the parent layout with swapped positions
        parentLayout.addView(item_imageView, buttonIndex);
        parentLayout.addView(btnSelectImage, imageViewIndex);

        // Set layout parameters for ImageView to make it square
        int squareSize = (int) getResources().getDimension(R.dimen.square_size);
        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(
                squareSize,
                squareSize
        );

        // Set the margins for the item_imageView
        imageViewParams.setMargins((int) getResources().getDimension(R.dimen.image_margin_left), (int) getResources().getDimension(R.dimen.image_margin_top), 0, 0);
        item_imageView.setLayoutParams(imageViewParams);

        // Set layout parameters for ImageButton
        LinearLayout.LayoutParams imageButtonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        btnSelectImage.setLayoutParams(imageButtonParams);
    }









}
