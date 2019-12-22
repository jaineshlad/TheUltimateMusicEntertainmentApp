package com.example.theultimatemusicentertainmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theultimatemusicentertainmentapp.DBHelper.DatabaseAccess;
import com.example.theultimatemusicentertainmentapp.Model.User;

public class MainActivity extends AppCompatActivity {

    //declaring all the global variables
    TextView txtRegister,txtAdminLogin;
    EditText edtUserName,edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting current user from shared preference
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String CurrentUserID = preferences.getString("CurrentUserID", "");
        final String CurrentUserName = preferences.getString("CurrentUserName", "");

        //if user already logged in the show song list or else show login screen
        if(!CurrentUserID.isEmpty() && !CurrentUserName.isEmpty()){
            Intent intent = new Intent(this, DisplaySongList.class);
            startActivity(intent);
            finish();
            return;
        }

        //if user not logged in then user will be show login scree

        //initializing all object when app start
        InitializeObjectAndValues();


        //register  textview click event handle to navigate in register screen
        //navigate to registration page
        txtRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //intent to navigate to register scree
                Intent intent = new Intent(MainActivity.this, Register_layout.class);
                startActivity(intent);
            }
        });

        //navigate to admin login page when click on admin login textview
        txtAdminLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //Inte
                Intent intent = new Intent(MainActivity.this, AdminLogin.class);
                startActivity(intent);
            }
        });

    }

    private void InitializeObjectAndValues(){
        txtRegister = findViewById(R.id.txtRegister);
        txtAdminLogin = findViewById(R.id.txtAdminLogin);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    //BUTTON CLICK METHODS for login button
    public void btnLoginClicked(View v)
    {
        //Validation method to check all input
        if(isValidInput()){

            //access database to check credential
            DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
            da.open();
            User currentuser = da.getUserByEmail(edtUserName.getText().toString());
            if(currentuser.getEmail() != null )
            {
                //it will check entered username and password with database records
                if(currentuser.getEmail().toLowerCase().equals(edtUserName.getText().toString()) &&
                    currentuser.getPassword().equals(edtPassword.getText().toString()))
                {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("CurrentUserID",currentuser.getUserId());
                    editor.putString("CurrentUserName",currentuser.getEmail());
                    editor.putBoolean("isAdmin",false);
                    editor.apply();
                    Toast.makeText(MainActivity.this,"LOG IN SUCCESS",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, DisplaySongList.class);
                    startActivity(intent);
                }
            }
            else
            {
                Toast.makeText(MainActivity.this,"LOG IN FAILED",Toast.LENGTH_SHORT).show();
            }
            da.close();
        }
    }


    //validation method to check all input.
    private Boolean isValidInput(){

        if(edtUserName.getText().length() <= 0){
            Toast.makeText(MainActivity.this,"Enter Email Id",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if (edtPassword.getText().length() <= 0){
            Toast.makeText(MainActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }
}
