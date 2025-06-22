package com.s22010342.firmfous;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    //declare varible
    EditText full_name,organization_code,signup_name,signup_password;
    TextView redirectTologing;
    Button signup_btn;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        full_name = findViewById(R.id.full_name);
        organization_code = findViewById(R.id.organization_code);
        signup_name = findViewById(R.id.signup_name);
        signup_password = findViewById(R.id.signup_password);
        redirectTologing = findViewById(R.id.redirectTologing);
        signup_btn = findViewById(R.id.signup_btn);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signupActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //set onclik lister signup button

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name = full_name.getText().toString();
                String orgCode = organization_code.getText().toString().toUpperCase();
                String username = signup_name.getText().toString();
                String password = signup_password.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(orgCode) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return; // Stop the function if any field is empty
                }

                //call database helper class
                DataBaseHelperClass dataBaseHelperClass = new DataBaseHelperClass(name,orgCode,username,password);
                /*reference.child(name).setValue(dataBaseHelperClass);

                //set toas msg signup is sucessfull
                Toast.makeText(SignUpActivity.this, "Signup Sucecessfully",Toast.LENGTH_SHORT).show();

                // go to the liging acticity when sigup uis successfull

                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);*/
                reference.child(username).setValue(dataBaseHelperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // SHOW SUCCESS AND NAVIGATE ONLY AFTER SUCCESSFULLY
                            Toast.makeText(SignUpActivity.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish(); // finish this activity  user can't go back to it
                        } else {
                            // The write failed. Show an error message.
                            Toast.makeText(SignUpActivity.this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //redirect to the loging
        redirectTologing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GotoLogin = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(GotoLogin);
            }
        });
    }
}