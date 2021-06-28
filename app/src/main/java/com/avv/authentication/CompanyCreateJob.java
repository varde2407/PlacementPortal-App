package com.avv.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CompanyCreateJob extends AppCompatActivity implements View.OnClickListener{

    EditText EDTposition, EDTdescription, EDTNumber,companyCreateJobLocation,companyCreateCpiCutoffEditTxt;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;
    Boolean is_male,is_female;
    RadioButton companyCreateMaleRadioBtn,companyCreateFemaleRadioBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        is_male=false;
        is_female=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_create_job);
        mAuth= FirebaseAuth.getInstance();

        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        EDTposition= (EditText) findViewById(R.id.editTextTextPersonName2);
        EDTdescription= (EditText) findViewById(R.id.editTextTextPersonName3);
        EDTNumber= (EditText) findViewById(R.id.editTextNumber2);
        companyCreateJobLocation = findViewById(R.id.companyCreateJobLocation);
        companyCreateCpiCutoffEditTxt =findViewById(R.id.companyCreateCpiCutoffEditTxt);
        companyCreateMaleRadioBtn = findViewById(R.id.companyCreateMaleRadioBtn);
        companyCreateFemaleRadioBtn = findViewById(R.id.companyCreateFemaleRadioBtn);


        findViewById(R.id.button18).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jj=new Intent(getApplicationContext(), CompanyProfileActivity.class);
                startActivity(jj);
            }
        });
        companyCreateMaleRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_male=true;
                System.out.println("1");
            }
        });
        companyCreateFemaleRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_female=true;
                System.out.println("2");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button6:
                rootnode= FirebaseDatabase.getInstance();
                mref=rootnode.getReference("jobs");
                CreateJob();
                break;

            case R.id.button9:
                Intent ii=new Intent(CompanyCreateJob.this, Company_viewjobs.class);
                startActivity(ii);
                break;
        }
    }

    private void CreateJob() {

        String position= EDTposition.getText().toString().trim();
        String description= EDTdescription.getText().toString().trim();
        String  job_Location=companyCreateJobLocation.getText().toString().trim();
        int number= Integer.parseInt(EDTNumber.getText().toString().trim() );

      double Cpi_cutoff=Double.parseDouble(companyCreateCpiCutoffEditTxt.getText().toString().trim());
        FirebaseUser user= mAuth.getCurrentUser();
        JobHelperClass JHC=new JobHelperClass(user.getUid() + System.currentTimeMillis(),user.getUid(), position, description, number, false, "xyz",job_Location,Cpi_cutoff,is_male,is_female);
        mref.child(JHC.getJobID()).setValue(JHC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "job profile added", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Addition failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return;
    }
}