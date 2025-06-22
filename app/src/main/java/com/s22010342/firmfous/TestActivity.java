package com.s22010342.firmfous;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TestActivity extends AppCompatActivity {

    // Declare the TextViews you will use
    TextView displayName, displayOrgCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);

        // Find the TextViews from your layout by their ID
        displayName = findViewById(R.id.display_name);
        displayOrgCode = findViewById(R.id.display_org_code);

        //  Get the Bundle of extras from the Intent that started this activity
        Bundle extras = getIntent().getExtras();

        // check extras not null
        if (extras != null) {
            String nameFromLogin = extras.getString("name");
            String orgCodeFromLogin = extras.getString("organization_code");

            // Set the text of  TextViews with the retrieved data
            displayName.setText(nameFromLogin);
            displayOrgCode.setText("Organization: " + orgCodeFromLogin);
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}