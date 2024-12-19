package com.example.buckleup.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buckleup.Adapters.UsersAdapter;
import com.example.buckleup.Models.Users;
import com.example.buckleup.R;
import com.example.buckleup.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private UsersAdapter adapter;
    private ArrayList<Users> list = new ArrayList<>();
    private FirebaseDatabase database;
    private FragmentChatBinding binding;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize the RecyclerView and Adapter
        chatRecyclerView = binding.chatRecyclerview;
        adapter = new UsersAdapter(list, getContext());
        chatRecyclerView.setAdapter(adapter);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance();

        // Fetch data from Firebase and update the RecyclerView
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        user.setUserId(dataSnapshot.getKey());
                        if(!Users.getUserId().equals(FirebaseAuth.getInstance().getUid())){
                            list.add(user);
                        }

                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter about data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });

        return view;
    }
}
