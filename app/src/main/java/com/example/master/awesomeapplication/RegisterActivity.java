package com.example.master.awesomeapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //
    //taking the gui elements
    //
    private EditText pass;
    private EditText name;;
    private EditText age;
    private EditText gender;
    private EditText email;
    private Button button;
    private ProgressDialog progressDialog;
    //firebase instance to create user
    //
    FirebaseAuth auth;
    //ArrayList<String> input = new ArrayList<String>();

    //
    //auxiliary data structure to hold input
    //
    Map<String, String> input = new HashMap<>();



    ArrayList<User> users = new ArrayList<User>();
    //
    //methods
    //
    //onCreate - override, sets the button onClick listener and displays message if the action fails
    //checkInput - takes the input dictionary (HashMap) and checks values
    //addUser - creates user and starts map activity
    //tryParseIntFail - helper for parsing a String to int
    //getInput - takes input from the gui elements and stores them in a HashMap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        pass = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        gender = (EditText) findViewById(R.id.gender);
        email = (EditText) findViewById(R.id.email);
        button = (Button) findViewById(R.id.btn_register);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getInput()) clickingRegister();
                else{
                    Toast.makeText(RegisterActivity.this, "Input could not be retrieved", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void clickingRegister(){

        String emailCheck = email.getText().toString().trim();
        String passwordCheck = pass.getText().toString().trim();
        String ageCheck = age.getText().toString().trim();
        String genderCheck = gender.getText().toString().trim();
        final String nameCheck = name.getText().toString().trim();
        final Intent i = new Intent(this, MapsActivity.class);

        if(TextUtils.isEmpty(nameCheck)){
            //check for name strength here!
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(emailCheck)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(passwordCheck)){
            //check for password strength here!
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(genderCheck)){
            //check for password strength here!
            Toast.makeText(this, "Please enter gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(ageCheck)){
            //check for password strength here!
            Toast.makeText(this, "Please enter age", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        auth.createUserWithEmailAndPassword(emailCheck,passwordCheck)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Callback for when the registration is successful
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            finish();
                            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                            Toast.makeText(RegisterActivity.this, auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

                            startActivity(i);
                            //Toast.makeText(Registration.this, "You have been successfully registered", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }
                    }
                });

    }


    public boolean getInput(){

        try{
            String p = pass.getText().toString();
            String n = name.getText().toString();
            String a = age.getText().toString();
            String g = gender.getText().toString();
            String e = email.getText().toString();

            input.put("p", p);
            input.put("n", n);
            input.put("a", a);
            input.put("g", g);
            input.put("e", e);
        }

        catch(Exception r){
            return false;
        }

        return true;

    }

    /** Not using this method the above is better but might want to do some checks later */



    public void checkInput(){

        String p = "", e = "", g = "", a = "", n = "";
        boolean correctEmail = false;

        for(Map.Entry<String, String> element : input.entrySet()){
            String value = element.getValue();
            String key = element.getKey();

            //
            //initialising User's characteristics (age, name, email, password, gender) and perform some checking
            //

            //the password should be at least 6 characters long
            if(key.equals("p")) {
                p = value;
                if(p.length() <= 6) {
                    Toast.makeText(RegisterActivity.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    input.clear();
                    return;
                }
            }

            //the email should contain the @ sign
            if(key.equals("e")) {
                e = value;
                for(char c : e.toCharArray())
                    if(c == '@') correctEmail = true;
            }

            //the gender must have certain values
            if(key.equals("g")){
                g = value;
                if(!(g.equals("M") || g.equals("F")
                        || g.equals("MALE") || g.equals("FEMALE")
                        || g.equals("male") || g.equals("female")
                        || g.equals("m") || g.equals("f")))  {
                    Toast.makeText(RegisterActivity.this, "Gender input is not proper", Toast.LENGTH_SHORT).show();
                    input.clear();
                    return;
                }
            }

            //check if the age is a string and if it is a positive number
            if(key.equals("a")){
                int temporaryAge = 0;
                a = value;
                if(tryParseIntFail(a)) {
                    Toast.makeText(RegisterActivity.this, "Age input should be a positive number", Toast.LENGTH_SHORT).show();
                    input.clear();
                    return;
                }
                else{
                    temporaryAge = Integer.parseInt(a);
                    if(temporaryAge < 0) {
                        Toast.makeText(RegisterActivity.this, "Age input should be a positive number", Toast.LENGTH_SHORT).show();
                        input.clear();
                        return;
                    }

                }

            }

            //make name mandatory
            if(key.equals("n")){
                n = value;
                if(TextUtils.isEmpty(n)) {
                    Toast.makeText(RegisterActivity.this, "No name input", Toast.LENGTH_SHORT).show();
                    input.clear();
                    return;
                }
            }


            if(correctEmail) {
                addUser(a,e,g,n,p);
            }

            else{
                Toast.makeText(RegisterActivity.this, "Email is not correct", Toast.LENGTH_SHORT).show();
                input.clear();
            }

        }

    }

    public void addUser(String a, String e, String g, String n, String p){

        //create new user and add it to the list
        User newUser = new User();
        newUser.setAge(a);
        newUser.setEmail(e);
        newUser.setGender(g);
        newUser.setName(n);
        newUser.setPassword(p);

        users.add(newUser);

        //if a user with the exact email and password already exists, the action fails
        for(User u : users)
            if(u.getEmail().equals(newUser.getEmail()) && u.getPassword().equals(newUser.getPassword())){
                Toast.makeText(RegisterActivity.this, "Combination of password and email already exists", Toast.LENGTH_SHORT).show();
                return;
            }

        //create a new user in the firebase
        auth.createUserWithEmailAndPassword(e, p);

        //and start the new activity and send all the users to MapsActivity
        //to be moved to ProfileActivity later
        Intent i = new Intent(this, MapsActivity.class);

        int counter = 0;

        for(User u : users){
            i.putExtra(Integer.toString(counter), u);
            counter++;

        }

        //information to pass to the maps activity so that it knows the current user from the list
        i.putExtra("currentEmail", e);
        i.putExtra("currentPassword", p);

        startActivity(i);
    }

    boolean tryParseIntFail(String value) {
        try {
            Integer.parseInt(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }


}
