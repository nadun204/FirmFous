package com.s22010342.firmfous;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.s22010342.firmfous.databinding.ActivityReminderModuleBinding;

public class ReminderModuleActivity extends AppCompatActivity {



    private AppBarConfiguration mAppBarConfiguration;
    private ActivityReminderModuleBinding binding;
    private String orgCode,getName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityReminderModuleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavigationView navigationView = binding.navView; //Get the NavigationView from your binding
        View headerView = navigationView.getHeaderView(0);//Get the header view from the NavigationView
        TextView nameTextView = headerView.findViewById(R.id.setTextName);
        TextView orgTextView = headerView.findViewById(R.id.textOrgCode);

        //Receive the organization code from the LoginActivity intent
        Bundle extras = getIntent().getExtras();
        if (extras  != null){
            //Use the same key ("orgCode") that LoginActivity used to send the data
            orgCode = extras.getString("organization_code");
            getName= extras.getString("name");
            orgTextView.setText("Organization Code : " + orgCode);
            if (getName != null){
                // check name is not null
                nameTextView.setText("Name : " + getName);
            }else{
                nameTextView.setText("name is empty");
            }


        }
        if (orgCode == null || orgCode.isEmpty()) {
            Toast.makeText(this, "Error: Could not get organization info.", Toast.LENGTH_LONG).show();
            // Send user back to login if data is missing
            // Intent backToLogin = new Intent(this, LoginActivity.class);
            // startActivity(backToLogin);
            // finish();
        }

        setSupportActionBar(binding.appBarReminderModule.toolbar);
        binding.appBarReminderModule.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();*/
                Intent intent = new Intent(ReminderModuleActivity.this,AddReminderActivity.class);
                intent.putExtra("organization_code", orgCode);
                startActivity(intent);
            }
        });

       /* FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderModuleActivity.this,AddReminderActivity.class);
                startActivity(intent);
            }
        });*/


        DrawerLayout drawer = binding.drawerLayout;
        //NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_reminder_module);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // adds items to the action bar.
        getMenuInflater().inflate(R.menu.reminder_module, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_reminder_module);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}