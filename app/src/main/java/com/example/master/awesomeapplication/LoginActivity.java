package com.example.master.awesomeapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare above and Initialize on the constructor of On Create
    private Button btnLogIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null){
            //start profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

        //Initialization of variables
        btnLogIn =  (Button) this.findViewById(R.id.btn_login);
        editTextEmail = (EditText) this.findViewById(R.id.tb_name);
        editTextPassword = (EditText) this.findViewById(R.id.tb_password);
        progressDialog  = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        btnLogIn.setOnClickListener(this);
    }

    public void btnRegisterClick(View view){
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {

        if (v==btnLogIn){
            userLoginIn();
        }
        else {
            Toast toast=Toast.makeText(getApplicationContext(), "Please Register first", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 0);
            finish();
            startActivity(new Intent(this,RegisterActivity.class));
            toast.show();
        }
    }

    public void userLoginIn(){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        // check the email and password is empty.
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //check for password strenght here!
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        //if the email and password are not empty
        //dispaly a progress dialog
        progressDialog.setMessage("login user...");
        progressDialog.show();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            //start profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();

                        }
                    }
                });
    }

}
