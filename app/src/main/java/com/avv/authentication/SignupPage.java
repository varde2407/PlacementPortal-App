package com.avv.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupPage extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;

    EditText EDTemail, EDTpassword;
    ProgressBar progressBar;
    CheckBox checkbox1, checkbox2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        mAuth=FirebaseAuth.getInstance();

        EDTemail = (EditText) findViewById(R.id.newemail);
        EDTpassword = (EditText) findViewById(R.id.newpassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        checkbox1= (CheckBox) findViewById(R.id.checkBox1);
        checkbox2= (CheckBox) findViewById(R.id.checkBox2);
        progressBar.setVisibility(View.INVISIBLE);

        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.gotologin).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button2 :
                String email= EDTemail.getText().toString().trim();
                String password= EDTpassword.getText().toString().trim();
                rootnode= FirebaseDatabase.getInstance();
                mref=rootnode.getReference("users");

                RegisterUser(email, password);
                break;

            case R.id.gotologin:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

    private void RegisterUser(String email, String password) {
        if(email.isEmpty()){
            EDTemail.setError("Email is required!");
            EDTemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EDTemail.setError("Please enter a valid email");
            EDTemail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            EDTpassword.setError("Password is required!");
            EDTpassword.requestFocus();
            return;
        }

        if(password.length()<6) {
            EDTpassword.setError("Password must consists of atleast 6 characters!");
            EDTpassword.requestFocus();
            return;
        }
        if (!checkbox1.isChecked() && !checkbox2.isChecked())
        {
            checkbox1.setError("Please check at least one checkbox");
            checkbox1.requestFocus();
            checkbox2.requestFocus();
            return;
        }
        if (checkbox1.isChecked() && checkbox2.isChecked())
        {
            checkbox1.setError("Please check only one checkbox");
            checkbox1.requestFocus();
            checkbox2.requestFocus();
            return;
        }
        Boolean v=checkbox1.isChecked();
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){

                    FirebaseUser user= mAuth.getCurrentUser();

                    //create entry in database
                    UserHelperClass UHC= new UserHelperClass(email, password, v);
                    mref.child(user.getUid()).setValue(UHC);

                    if (v)
                    {
                        startActivity(new Intent(SignupPage.this, YourProfileActivity.class));
                    }
                    else if(!v) {
                        startActivity(new Intent(SignupPage.this, CompanyCreateJob.class));
                    }
                    finish();
                }

                else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}