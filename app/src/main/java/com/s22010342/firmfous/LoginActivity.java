package com.s22010342.firmfous;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    TextView redirectToSignUp ;
    EditText loginName,loginPassword;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_main);

        redirectToSignUp = findViewById(R.id.redirectToSignUp);
        loginName = findViewById(R.id.login_name);
        loginPassword = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.loginActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //login button

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check validate password and user name not true
                if (validateUsername() && validatePassword()) {
                   //check the user in the database
                    checkUser();
                } else {
                    //  one is invalid the setError messages in the
                    Toast.makeText(LoginActivity.this, "Please check your inputs", Toast.LENGTH_SHORT).show();
                }
            }
        });

            //redirect to the signup
        redirectToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToSignup = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(goToSignup);
            }
        });

    }

public Boolean validateUsername(){
        //validate username && check username is empty
        String val = loginName.getText().toString();
        if (val.isEmpty()){
            loginName.setError("User Name Can't be Blank");
            return false;
        }else {
            loginName.setError(null);
            return true;
         }
    }

    public Boolean validatePassword(){
        //validate username && check username is empty
        String val = loginPassword.getText().toString();
        if (val.isEmpty()){
            loginPassword.setError("Password Can't be Blank");
            return false;
        }else {
            loginPassword.setError(null);
            return true;
        }
    }
    public void checkUser() {
        String userUsername = loginName.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loginName.setError(null);

                    //iterate through the snapshot results
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        // Retrieve the password from the found user record
                        String passwordFromDB = userSnapshot.child("password").getValue(String.class);

                        //password comparison
                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                            // Passwords match, proceed with login
                            loginPassword.setError(null);

                            String nameFromDB = userSnapshot.child("name").getValue(String.class);
                            String orgCodeFromDB = userSnapshot.child("organization_code").getValue(String.class);

                            // login sucessfully, loaf=d the next activity
                            Intent intent = new Intent(LoginActivity.this, ReminderModuleActivity.class);

                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("username", userUsername);
                            intent.putExtra("organization_code", orgCodeFromDB);


                            startActivity(intent);
                            finish(); // Finish LoginActivity  user can't go back
                            return; // Exit the listener
                        }
                    }

                    // Tif cant finding a matching password
                    loginPassword.setError("Invalid credentials");
                    loginPassword.requestFocus();

                } else {
                    // Username does not exist in the database
                    loginName.setError("User does not exist");
                    loginName.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle  database errors
                Toast.makeText(LoginActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();            }
        });

    }
}