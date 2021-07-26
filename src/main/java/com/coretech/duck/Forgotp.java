package com.coretech.duck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Forgotp extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    EditText Foremail;
    Button submit;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotp);

        auth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);


        Foremail=findViewById(R.id.Foremail);
        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpass();



            }
        });



    }
    public void resetpass(){
        progressDialog.show();
        String Femail=Foremail.getText().toString().trim();
        if (Femail.isEmpty()){
            Foremail.setError("Enter Email Address");
            Foremail.requestFocus();
            progressDialog.dismiss();
            return;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Femail).matches()){
            Foremail.setError("Enter valid Email Address");
            Foremail.requestFocus();
            progressDialog.dismiss();
            return;
        }

        auth.sendPasswordResetEmail(Femail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Dialog dialog=new Dialog(Forgotp.this,R.style.form);
                    dialog.setContentView(R.layout.forgotmail);
                    Button ok;
                    ok=dialog.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();

                        }

                    });
                    dialog.show();


                }
            }
        });
    }
}