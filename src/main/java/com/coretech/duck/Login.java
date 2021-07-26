package com.coretech.duck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class  Login extends AppCompatActivity {
    FirebaseAuth auth;
    TextView SignupText,forgot;
    EditText emaill,passl;
    Button signil;
    ProgressDialog progressDialog;
    CheckBox reme;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();

        SignupText=findViewById(R.id.SignupText);
        emaill=findViewById(R.id.emaill);
        passl=findViewById(R.id.passl);
        signil=findViewById(R.id.signil);
        forgot=findViewById(R.id.forgot);
        reme=findViewById(R.id.remember);

        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");
        if (checkbox.equals("true")){
            startActivity(new Intent(Login.this,Home.class));



        }else if (checkbox.equals("false")){

        }







        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,Forgotp.class));

            }
        });


        SignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Login.this,Register.class));
                finish();
            }
        });
        reme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();


                }
                else if (!compoundButton.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                }
            }
        });

        signil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                final String email=emaill.getText().toString().trim();
                String password=passl.getText().toString();


                if(email.isEmpty()){
                    progressDialog.dismiss();

                    emaill.setError("Email is required");
                    emaill.requestFocus();
                    return;

                }
                if(password.isEmpty()){
                    progressDialog.dismiss();

                    passl.setError("Password is required");
                    passl.requestFocus();
                    return;

                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    progressDialog.dismiss();

                    emaill.setError("Email is not valid");
                    emaill.requestFocus();
                    return;
                }
                if (password.length()<6){
                    progressDialog.dismiss();

                    passl.setError("Minimum 6 chracter required");
                    passl.requestFocus();
                    return;
                }


                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()){
                                progressDialog.dismiss();

                                startActivity(new Intent(Login.this,Home.class));


                            }
                            else{
                                progressDialog.dismiss();
                                user.sendEmailVerification();
                                Dialog dialog=new Dialog(Login.this,R.style.emailver);
                                dialog.setContentView(R.layout.emailverify);
                                Button ok;
                                ok=dialog.findViewById(R.id.ok);
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                    }
                                });
                                dialog.show();



                            }
                        }
                        else{
                            Toast.makeText(Login.this,"Somthing went wrong", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();



                        }
                    }
                });
            }
        });


        //No internet dialog.
        if (!isOnline()){
            Dialog dialog=new Dialog(Login.this,R.style.noternet);
            dialog.setContentView(R.layout.nointernet);
            Button retry;
            retry=dialog.findViewById(R.id.retry);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();

        }

    }
    public boolean isOnline(){
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo==null || !networkInfo.isAvailable() || !networkInfo.isConnected()){
            return false;

        }
        return true;
    }
}