package com.example.weighttrackingapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // For SMS permissions
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    // Private variables on login screen
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button loginButton;
    private Button newAccountButton;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign the views using IDs
        editTextUsername = (EditText) findViewById(R.id.userName);
        editTextPassword = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);
        newAccountButton = (Button) findViewById(R.id.accountButton);
        DB = new DBHelper(this);

        editTextUsername.addTextChangedListener(textWatcher);
        editTextPassword.addTextChangedListener(textWatcher);

        // Code to create a new account and check the database for existing account
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editTextUsername.getText().toString().trim();
                String pass = editTextPassword.getText().toString().trim();

                if(!user.isEmpty() && !pass.isEmpty()){
                    Boolean checkUser = DB.checkUsername(user);
                    if(checkUser==false){
                        Boolean insert = DB.insertData(user, pass);
                        if(insert==true){
                            Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), GridScreenActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "User already exists, sign in instead", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // code for when the user presses login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the username and password
                String user = editTextUsername.getText().toString().trim();
                String pass = editTextPassword.getText().toString().trim();

                // make sure the username and password are not empty, check if they exist together in the users table
                if(!user.isEmpty() && !pass.isEmpty()){
                    Boolean checkUserPass = DB.checkUsernamePassword(user, pass);
                    if(checkUserPass==true){
                        // toasts are used as confirmation that login was successful or unsuccessful
                        Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();
                        // open the grid screen activity
                        Intent intent = new Intent(getApplicationContext(), GridScreenActivity.class);
                        startActivity(intent);
                        // if username and/or password are not correct
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // check if SMS permission is not granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            // if permission is not granted then check if the user has denied permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){

            } else {
                // a pop up will appear asking for required permission
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }

    } // onCreate

    // after getting the result of permission request, the result will be passed through this method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        // will check the request code
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // check whether the length of grantResults is greater than 0 and is equal to PERMISSION_GRANTED
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You have allowed the app to send SMS notifications.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "You have denied permission to send SMS notifications.", Toast.LENGTH_SHORT).show();
                }
            }
        }// switch
    }// method

    // Text watcher method (can be re-used for multiple text change listeners)
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        // Checks if text has been entered, .trim() removes spaces so only text is accepted
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String usernameInput = editTextUsername.getText().toString().trim();
            String passwordInput = editTextPassword.getText().toString().trim();

            // if text is present, button is enabled
            loginButton.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}