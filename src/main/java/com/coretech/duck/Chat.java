package com.coretech.duck;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    CircleImageView userprofile;
    String recivername, reciverid,senderid,reciverstat,getmesage;
    TextView textc,getstat;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    public static String simage,rimage;
    EditText messtype;
    CardView sendb;
    User user;
    String senderRoom,reciverRoom,newmeaasge;
    RecyclerView messageAdapter;
    ArrayList<Messages> messagesArrayList;
    MessAdptr messAdptr;
    ImageView color;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        reciverid = getIntent().getStringExtra("uid");
        recivername = getIntent().getStringExtra("name");
        rimage=getIntent().getStringExtra("image");
        reciverstat=getIntent().getStringExtra("stat");

        userprofile = findViewById(R.id.userprofile);
        getstat = findViewById(R.id.getstat);
        textc = findViewById(R.id.textc);
        messtype=findViewById(R.id.messtype);
        sendb=findViewById(R.id.sendb);
        messageAdapter=findViewById(R.id.messageAdapter);
        color=findViewById(R.id.color);

        textc.setText(recivername);
        DatabaseReference statusrefer=database.getReference().child("user").child(auth.getUid()).child("stat");
        statusrefer.setValue("online");

        getstat.setText(reciverstat);
        if (reciverstat.equals("online")){
            color.setVisibility(View.VISIBLE);

        }




        messagesArrayList=new ArrayList<>();
        messAdptr=new MessAdptr(Chat.this,messagesArrayList);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageAdapter.setLayoutManager(linearLayoutManager);
        messageAdapter.setAdapter(messAdptr);


        Glide.with(this).load(rimage).into(userprofile);

        senderid=auth.getUid();


        senderRoom=(senderid+reciverid);
        reciverRoom=(reciverid+senderid);

        DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                simage = snapshot.child("image").getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();

                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);

                    messageAdapter.scrollToPosition(messagesArrayList.size() - 1);



                }
                messAdptr.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendb.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String message=messtype.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(Chat.this,"Enter some text.",Toast.LENGTH_SHORT).show();
                    return;

                }

                messtype.setText("");
                String time;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                LocalDateTime now = LocalDateTime.now();
                time=(dtf.format(now));
                Messages messages=new Messages(message,senderid,time);
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(reciverRoom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });

                    }
                });
            }
        });




    }

}









