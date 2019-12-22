package com.example.theultimatemusicentertainmentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theultimatemusicentertainmentapp.DBHelper.DatabaseAccess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_layout extends AppCompatActivity {
    //DECLARATION
    TextView txtBackLogin;
    EditText edtFirstName,edtLastName,edtUserEmail,edtUserPassword,edtUserConfirmPassowrd;
    Button btnUserRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);

        InitializeObjectAndValues();

        //If user is already a member then back to login page
        txtBackLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Register_layout.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void InitializeObjectAndValues()
    {
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtUserEmail = findViewById(R.id.edtUserEmail);
        edtUserPassword = findViewById(R.id.edtUserPassword);
        edtUserConfirmPassowrd = findViewById(R.id.edtUserConfirmPassowrd);
        txtBackLogin = findViewById(R.id.txtBackLogin);
        btnUserRegister = findViewById(R.id.btnUserRegister);
    }


    //BUTTON CLICK METHODS

    public void btnUserRegisterClicked(View v){
        if(isValidInput()){
            Toast.makeText(Register_layout.this,"All Field Are Valid",Toast.LENGTH_SHORT).show();

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String userID = date.toString() + edtUserEmail.getText().toString();
            //Insert Data into Database table
            String strQuery = "Insert into user " +
                    "(userid,firstname,lastname,email,password) values " +
                    "('"+userID+"'," +
                    "'"+edtFirstName.getText().toString()+"'," +
                    "'"+edtLastName.getText().toString()+"'," +
                    "'"+edtUserEmail.getText().toString()+"'," +
                    "'"+edtUserPassword.getText().toString()+"')";



            DatabaseAccess da = DatabaseAccess.getInstance(getApplicationContext());
            da.open();
            int resultFlag = da.insertUserRecordToDb(strQuery);
            da.close();

            if(resultFlag == 0){
                Toast.makeText(Register_layout.this,"Value not inserted",Toast.LENGTH_SHORT).show();
            }
            else{
                //Navigate to login screen after registration is complete
                Intent intent = new Intent(Register_layout.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    //VALIDATION METHODS
    private  Boolean isValidInput(){

        //check all field are valid or not
        // if it is not valid then return false and display error..
        if(getEditTextCount(edtFirstName) <= 0){
            Toast.makeText(Register_layout.this,"Please enter username.",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(getEditTextCount(edtLastName ) <= 0){
            Toast.makeText(Register_layout.this,"Please enter Email.",Toast.LENGTH_SHORT).show();
            return  false;

        }
        else if(getEditTextCount(edtUserEmail ) <= 0){
            Toast.makeText(Register_layout.this,"Please enter Email.",Toast.LENGTH_SHORT).show();
            return  false;

        }
        else if(getEditTextCount(edtUserPassword) <= 0){
            Toast.makeText(Register_layout.this,"Please enter password.",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(getEditTextCount(edtUserConfirmPassowrd) <= 0)
        {
            Toast.makeText(Register_layout.this,"Please enter password to confirm.",Toast.LENGTH_SHORT).show();
            return  false;
        }

        else if(!isValidEmail(edtUserEmail.getText().toString())){
            Toast.makeText(Register_layout.this,"Please enter valid email",Toast.LENGTH_SHORT).show();
            return  false;
        }
        else if(!edtUserConfirmPassowrd.getText().toString().equals(edtUserPassword.getText().toString())){

            Toast.makeText(Register_layout.this,"Confirm Password is not same as password field.",Toast.LENGTH_SHORT).show();
            return  false;
        }
        return  true;
    }

    //HELPER METHODS
    //regex method to check valid email enter or not..
    private  boolean isValidEmail(String strEmail)
    {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strEmail);
        return matcher.matches();
    }

    private int getEditTextCount(EditText edtTextView){
        return  edtTextView.getText().length();
    }
}
