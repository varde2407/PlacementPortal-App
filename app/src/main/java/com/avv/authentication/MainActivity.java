package com.avv.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText EDTemail1, EDTpassword1;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;
    private DataSnapshot ds;
    private boolean vv ;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("adityaaaa ","reached oncreate mainactivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth= FirebaseAuth.getInstance();
        EDTemail1 = (EditText) findViewById(R.id.newemail);
        EDTpassword1 = (EditText) findViewById(R.id.newpassword);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.textView3).setOnClickListener(this);

        rootnode= FirebaseDatabase.getInstance();
        mref=rootnode.getReference("users");

        if(mAuth.getCurrentUser()!=null)
        {
           FirebaseUser user2=mAuth.getCurrentUser();
            mref=mref.child(user2.getUid());

            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    UserHelperClass userdata = dataSnapshot.getValue(UserHelperClass.class);
                    // ...
                     vv =userdata.getStudent();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            mref.addValueEventListener(postListener);
        }
        TextView textView =(TextView)findViewById(R.id.textView16);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://github.com/varde2407/Authentication'> Made by Atharva, Keshav and Aditya</a>";
        textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
        // TO FIX
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Log.d("aditya","reached ondata");
                    UserHelperClass userdata = dataSnapshot.getValue(UserHelperClass.class);
                    // ...
                    vv = userdata.getStudent();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };

            mref.addValueEventListener(postListener);

            if(vv){ startActivity(new Intent(this, YourProfileActivity.class)); }
            else { startActivity(new Intent(this,CompanyProfileActivity.class));}
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView3:
                finish();
                startActivity(new Intent(this, CommonSignup.class));
//                startActivity(new Intent(this, SignupPage.class));
                break;

            case R.id.button:
                LoginUser();
                break;

        }
    }

    private void LoginUser(){
        Log.d("aditya","reached loginuser");
        String email = EDTemail1.getText().toString().trim();
        String password = EDTpassword1.getText().toString().trim();

        if(email.isEmpty()){
            EDTemail1.setError("Email is required!");
            EDTemail1.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EDTemail1.setError("Please enter a valid email");
            EDTemail1.requestFocus();
            return;
        }

        if(password.isEmpty()){
            EDTpassword1.setError("Password is required!");
            EDTpassword1.requestFocus();
            return;
        }

        if(password.length()<6) {
            EDTpassword1.setError("Password must consists of atleast 6 characters!");
            EDTpassword1.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Log.d("aditya","reached succesful login");
                    FirebaseUser user= mAuth.getCurrentUser();

                    mref=mref.child(user.getUid());

                    ValueEventListener postListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.d("aditya","reached ondatachange");
                            // Get Post object and use the values to update the UI
                            UserHelperClass userdata = dataSnapshot.getValue(UserHelperClass.class);
                            // ...
                            Boolean v =userdata.getStudent();
                            if (v)
                            {

                                Log.d("aditya","reached a student");
                                Intent intent= new Intent(getApplicationContext(), StudentProfile.class);
                                startActivity(intent);
                            }
                            else if (!v)
                            {
                                Intent intent= new Intent(getApplicationContext(), CompanyProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };

                    mref.addValueEventListener(postListener);


                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

