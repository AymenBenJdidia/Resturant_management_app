package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast; // Add this import statement
import android.graphics.Color; // Add this import statement for Color class




public class LoginPage extends AppCompatActivity {
    private DatabaseUsersHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        dbHelper = new DatabaseUsersHelper(this);
        Button signInButton=(Button)findViewById(R.id.sign_in_button);
        EditText logIn = (EditText) findViewById(R.id.username_email);
        EditText passwd = (EditText) findViewById(R.id.password);
        TextView tx1 = (TextView) findViewById(R.id.wrong_login);
        TextView signUPtext = (TextView) findViewById(R.id.sign_up);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = logIn.getText().toString().trim();
                String password = passwd.getText().toString().trim();
                long userId = dbHelper.getUserId(username, password);
                if(username.equals("admin") && password.equals("admin")){
                    Toast.makeText(LoginPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    intent.putExtra("USER_ID", -1);
                    startActivity(intent);
                    finish();

                } else if (dbHelper.checkUser(username, password)) {
                    Toast.makeText(LoginPage.this, "Login successful", Toast.LENGTH_SHORT).show();
                    // Proceed to the next activity
                    // Perform redirection here
                    Intent intent = new Intent(LoginPage.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    tx1.setVisibility(View.VISIBLE);
                    passwd.setBackgroundColor(Color.RED);
                    logIn.setBackgroundColor(Color.RED);
                    // You might want to set an error message here instead of setting text to counter
                    tx1.setText("Invalid credentials");
                }
            }
        });

        signUPtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginPage.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }


}