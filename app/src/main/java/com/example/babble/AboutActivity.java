package com.example.babble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.babble.Models.Users;
import com.example.babble.databinding.ActivityAboutBinding;
import com.example.babble.databinding.ActivitySettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding binding;
    FirebaseAuth mAuth;
    String chatPartnerId;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Get the user ID of the chat partner
        final String senderId = mAuth.getUid();
        String chatPartnerId = getIntent().getStringExtra("chatPartnerId");
        database.getReference().child("Users").child(chatPartnerId).addListenerForSingleValueEvent(new ValueEventListener() {
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
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, ChatDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


    }
}

