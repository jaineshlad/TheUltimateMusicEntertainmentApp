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

public class AdminLogin extends AppCompatActivity {
    TextView txtRegister,txtAdminLogin;
    EditText edtUserName,edtPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        InitializeObjectAndValues();


        //navigate to admin login page
        txtAdminLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AdminLogin.this, MainActivity.class);
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
    public void btnLoginClicked(View v)
    {
        if(isValidInput()){
            if(edtUserName.getText().toString().equals("admin") && edtPassword.getText().toString().equals("admin"))
            {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isAdmin",true);
                editor.apply();
               // Toast.makeText(MainActivity.this,"LOG IN SUCCESS",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminLogin.this, DisplaySongList.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(AdminLogin.this,"LOG IN FAILED",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Boolean isValidInput(){

        if(edtUserName.getText().length() <= 0){
            Toast.makeText(AdminLogin.this,"Enter Email Id",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if (edtPassword.getText().length() <= 0){
            Toast.makeText(AdminLogin.this,"Enter Password",Toast.LENGTH_SHORT).show();
            return  false;
        }
        return true;
    }



}
