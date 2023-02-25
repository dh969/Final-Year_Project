package com.example.babble;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.babble.Models.Users;
import com.example.babble.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
       binding.saveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (!binding.etStatus.getText().toString().equals("") && !binding.txtUsername.getText().toString().equals("")) {
                   String status = binding.etStatus.getText().toString();
                   String username = binding.txtUsername.getText().toString();
                   HashMap<String, Object> obj = new HashMap<>();
                   obj.put("userName", username);
                   obj.put("status", status);
                   database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(obj);
                   Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
               }

           else{
               Toast.makeText(SettingsActivity.this, "Please Put username and status", Toast.LENGTH_SHORT).show();
           }}
       });
        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                Picasso.get()
                        .load(users.getProfilePic()).placeholder(R.drawable.avatar).into(binding.profileImage);
                binding.etStatus.setText(users.getStatus());
                binding.txtUsername.setText(users.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 25);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 25 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            binding.profileImage.setImageURI(selectedImageUri);
            final StorageReference reference = storage.getReference().child("profile_pic").child(auth.getUid());
            reference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            database.getReference().child("Users").child(auth.getUid()).child("profilePic").setValue(imageUrl);
                            Toast.makeText(SettingsActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }}

