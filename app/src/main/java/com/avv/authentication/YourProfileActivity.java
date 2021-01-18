package com.avv.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class YourProfileActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("aditya","reached oncreate of Yourprofile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth=FirebaseAuth.getInstance();

        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button3:
                mAuth.signOut();
                finish();
                Intent intent = new Intent(YourProfileActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button7:
                Log.d("aditya","reached button onclick");
                Intent intent1 = new Intent(YourProfileActivity.this, StudentProfile.class);
                intent1.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(intent1);
                break;
        }
    }
}