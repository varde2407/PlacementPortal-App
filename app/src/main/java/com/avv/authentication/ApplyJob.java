package com.avv.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ApplyJob extends AppCompatActivity implements View.OnClickListener {

    EditText nameEDT, applicationEDT;
    TextView show1, show2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;
    private DataSnapshot ds;
    String jobID;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        mAuth= FirebaseAuth.getInstance();
        nameEDT = (EditText) findViewById(R.id.editTextTextPersonName4);
        applicationEDT = (EditText) findViewById(R.id.editTextTextPersonName5);

        show1 = (TextView) findViewById(R.id.textView10);
        show2 = (TextView) findViewById(R.id.textView11);

        findViewById(R.id.button8).setOnClickListener(this);

        Intent i=getIntent();
        String position= (String) i.getSerializableExtra("Position");
        jobID= (String) i.getSerializableExtra("JobID");
        String companyID= (String) i.getSerializableExtra("CompanyID");
        show1.setText(position);
        show2.setText(companyID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            
            case R.id.button8:

                rootnode= FirebaseDatabase.getInstance();
                mref=rootnode.getReference("jobs");
                mref=mref.child(jobID);
                mref=mref.child("applicants");

                String name=nameEDT.getText().toString().trim();
                String application=applicationEDT.getText().toString().trim();
                ApplicantClass AC= new ApplicantClass(jobID, name, name, application);
                mref.child(name).setValue(AC).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Applied sucessfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "application failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
                finish();
                //Intent intent=new Intent(ApplyJob.this, Student_viewjobs.class);
                //startActivity(intent);
                break;
        }
        
    }
}