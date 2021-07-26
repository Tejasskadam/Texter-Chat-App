package com.coretech.duck;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.coretech.duck.Chat.simage;


public class Setting extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView name;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Button logout,save,editpic,editname;
    ImageView back;
    Uri nimage;
    String unmg,uemail,finalimg,upass,uimg,uphone,ustat,getnamed;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        circleImageView=findViewById(R.id.pic);
        name=findViewById(R.id.names);
        logout=findViewById(R.id.logout);
        save=findViewById(R.id.save);
        editname=findViewById(R.id.editname);
        editpic=findViewById(R.id.editpic);
        back=findViewById(R.id.back);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");

        DatabaseReference statusrefer=database.getReference().child("user").child(auth.getUid()).child("stat");
        statusrefer.setValue("online");

        DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
        StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                uimg=snapshot.child("image").getValue().toString();
                unmg=snapshot.child("name").getValue().toString();
                uemail=snapshot.child("email").getValue().toString();
                uphone=snapshot.child("phone").getValue().toString();
                ustat=snapshot.child("stat").getValue().toString();


                name.setText(unmg);
                Picasso.get().load(uimg).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nimage!=null && getnamed!=null){
                    progressDialog.show();
                    storageReference.putFile(nimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        finalimg = uri.toString();
                                        User user = new User(getnamed, uemail, uphone, upass, finalimg, auth.getUid(),ustat);
                                        reference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if (task.isComplete()){
                                                    progressDialog.dismiss();
                                                    name.setText(unmg);
                                                    Glide.with(Setting.this).load(uimg).into(circleImageView);
                                                    Toast.makeText(Setting.this,"Updated Successfully", Toast.LENGTH_LONG).show();


                                                }
                                                else{
                                                    progressDialog.dismiss();

                                                    Toast.makeText(Setting.this,"failed", Toast.LENGTH_LONG).show();

                                                }

                                            }
                                        });
                                    }
                                });
                            }
                            else {
                                progressDialog.dismiss();

                                Toast.makeText(Setting.this,"failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
                else if (nimage==null && getnamed!=null){
                    progressDialog.show();
                    User user = new User(getnamed, uemail, uphone, upass, uimg, auth.getUid(),ustat);
                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull  Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                name.setText(unmg);
                                Toast.makeText(Setting.this,"Updated Successfully", Toast.LENGTH_LONG).show();

                            }
                            else {
                                progressDialog.dismiss();

                                Toast.makeText(Setting.this,"failed", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
                else if (nimage!=null && getnamed==null){
                    progressDialog.show();
                    storageReference.putFile(nimage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        finalimg = uri.toString();
                                        User user = new User(unmg, uemail, uphone, upass, finalimg, auth.getUid(), ustat);
                                        reference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if (task.isComplete()) {
                                                    progressDialog.dismiss();
                                                    Glide.with(Setting.this).load(uimg).into(circleImageView);
                                                    Toast.makeText(Setting.this, "Updated Successfully", Toast.LENGTH_LONG).show();


                                                } else {
                                                    progressDialog.dismiss();

                                                    Toast.makeText(Setting.this, "failed", Toast.LENGTH_LONG).show();

                                                }


                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
                else {

                    Toast.makeText(Setting.this," Already Updated", Toast.LENGTH_LONG).show();

                }




            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

        editpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select profile picture"),20);
            }
        });
        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(Setting.this,R.style.noternet);
                dialog.setContentView(R.layout.editnamed);
                Button done;
                EditText editnamed;
                done=dialog.findViewById(R.id.okn);
                editnamed=dialog.findViewById(R.id.editnamed);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getnamed=editnamed.getText().toString();
                        if (getnamed.isEmpty()){
                            editnamed.setError("Name cannot be empty");
                            editnamed.requestFocus();
                            return;
                        }
                        name.setText(getnamed);
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==20){
            if (data!=null){
                nimage=data.getData();
                circleImageView.setImageURI(nimage);
            }
        }
    }






}