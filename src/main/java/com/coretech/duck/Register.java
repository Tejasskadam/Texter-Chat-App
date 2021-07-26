package com.coretech.duck;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

@SuppressWarnings("ALL")
public class Register extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    EditText emailr,namer,phoner,passr;
    Button signup;
    ProgressDialog progressDialog;
    ImageView userpro;
    Uri image;
    String imageURL,stat;
    TextView signitext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        emailr=findViewById(R.id.emailr);
        namer=findViewById(R.id.namer);
        phoner=findViewById(R.id.phoner);
        passr=findViewById(R.id.passr);
        signup=findViewById(R.id.signup);
        userpro=findViewById(R.id.userpro);
        signitext=findViewById(R.id.signitext);







        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email=emailr.getText().toString().trim();
                String name=namer.getText().toString().trim();
                String phone=phoner.getText().toString().trim();
                String password=passr.getText().toString().trim();
                stat =("offline");

                if(email.isEmpty()){
                    emailr.setError("Email is required");
                    emailr.requestFocus();
                    return;

                }
                if(password.isEmpty()){
                    passr.setError("Password is required");
                    passr.requestFocus();
                    return;

                }
                if(name.isEmpty()){
                    namer.setError("Name is required");
                    namer.requestFocus();
                    return;

                }
                if(phone.isEmpty()){
                    phoner.setError("Phone number is required");
                    phoner.requestFocus();
                    return;

                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailr.setError("Email is not valid");
                    emailr.requestFocus();
                    return;
                }
                if (password.length()<6){
                    passr.setError("Minimum 6 chracter  required");
                    passr.requestFocus();
                    return;
                }


                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
                            StorageReference storageReference=storage.getReference().child("upload").child(auth.getUid());
                            if (image!=null){
                                storageReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()){
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    imageURL=uri.toString();
                                                    User user= new User(name,email,phone,password,imageURL,auth.getUid(),stat);
                                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                progressDialog.dismiss();
                                                                Toast.makeText(Register.this,"User Created Successfully.", Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(Register.this,Login.class));
                                                                finish();
                                                            }
                                                            else {
                                                                Toast.makeText(Register.this,"Something went wrong.", Toast.LENGTH_LONG).show();
                                                                progressDialog.dismiss();



                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(Register.this,"Did not get image uri.", Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });
                            }
                            else {
                                imageURL="https://firebasestorage.googleapis.com/v0/b/duck-chatapp.appspot.com/o/Users2_37173.png?alt=media&token=6f0522a0-6f08-440c-aa7f-44bd00cfd57b";
                                User user= new User(name,email,phone,password,imageURL,auth.getUid(),stat);
                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(Register.this,"User Created Successfully.", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(Register.this,Login.class));
                                        }
                                        else {
                                            Toast.makeText(Register.this,"Something went wrong.", Toast.LENGTH_LONG).show();


                                        }
                                    }
                                });

                            }



                        }
                        else{
                            Toast.makeText(Register.this,"Something went wrong.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();



                        }

                    }
                });
            }
        });
        signitext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));

            }
        });


        userpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select profile picture"),10);

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if (data!=null){
                image=data.getData();
                Glide.with(this).load(image).into(userpro);
            }
        }
    }
}