package com.example.master.awesomeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button btnLogout;
    private Button btnGoToMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth= FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        FirebaseUser user=firebaseAuth.getCurrentUser();
        textViewUserEmail=(TextView)findViewById(R.id.textViewUserEmail);
        btnLogout=(Button)findViewById(R.id.buttonLogOut);
        btnGoToMap=(Button)findViewById(R.id.buttonGoToMap);
        textViewUserEmail.setText("Welcome  "+ user.getEmail());
        btnLogout.setOnClickListener(this);
        btnGoToMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==btnLogout) {
            firebaseAuth.signOut();
            //finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if (v==btnGoToMap){
            //finish();
            startActivity(new Intent(this,MapsActivity.class));

        }
    }

}
