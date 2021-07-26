package com.coretech.duck;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView mainUserRecyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<User> userArrayList;
    ImageView more;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        more=findViewById(R.id.more);



        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Home.this, R.style.emailver);
                dialog.setContentView(R.layout.moreset);
                Button logout;
                TextView caed;
                logout= dialog.findViewById(R.id.logout);
                caed= dialog.findViewById(R.id.caed);


                caed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Home.this,Setting.class));
                        dialog.dismiss();

                    }
                });
                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putString("remember","false");
                        editor.apply();
                        DatabaseReference statusrefer=database.getReference().child("user").child(auth.getUid()).child("stat");
                        statusrefer.setValue("offline");
                        finish();



                    }
                });
                dialog.show();

            }


        });



     

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        userArrayList=new ArrayList<>();

        DatabaseReference statusrefer=database.getReference().child("user").child(auth.getUid()).child("stat");
        statusrefer.setValue("online");

        DatabaseReference reference=database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User user=dataSnapshot.getValue(User.class);
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.getUid()))
                    {

                        userArrayList.remove(user);


                    }else {
                        userArrayList.add(user);

                    }







                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mainUserRecyclerView=findViewById(R.id.mainUserRecyclerView);
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(Home.this,userArrayList);
        mainUserRecyclerView.setAdapter(adapter);


    }



    @Override
    protected void onPause() {
        super.onPause();
        DatabaseReference statusrefer=database.getReference().child("user").child(auth.getUid()).child("stat");
        statusrefer.setValue("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference statusrefer=database.getReference().child("user").child(auth.getUid()).child("stat");
        statusrefer.setValue("online");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference statusrefer=database.getReference().child("user").child(auth.getUid()).child("stat");
        statusrefer.setValue("offline");

    }
}