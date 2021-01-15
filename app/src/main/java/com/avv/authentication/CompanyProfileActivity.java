package com.avv.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class CompanyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);

        mAuth= FirebaseAuth.getInstance();

        findViewById(R.id.button4).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button4:
                mAuth.signOut();
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            case R.id.button5:
                Intent ii = new Intent(this, CompanyCreateJob.class);
                startActivity(ii);
        }
    }
}