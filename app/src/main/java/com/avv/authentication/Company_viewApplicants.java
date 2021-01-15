package com.avv.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

public class Company_viewApplicants extends AppCompatActivity {

    String jobID;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref, mref2, mref3;
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth mauth;
    private Boolean filled;
    private String selected_name;
    private TextView Curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_view_applicants);

        Intent i=getIntent();
        jobID= (String) i.getSerializableExtra("JobID");

        recyclerView = findViewById(R.id.rec_view2);
        recyclerView.setHasFixedSize(true);


        rootnode = FirebaseDatabase.getInstance();
        mref = rootnode.getReference("jobs");

        mref2=mref.child(jobID);

        mref=mref.child(jobID).child("applicants");

        Curr=findViewById(R.id.textView14);

        filled=false;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                JobHelperClass jobdata = dataSnapshot.getValue(JobHelperClass.class);
                // ...
                filled =jobdata.getStatus();
                if (filled==true) {selected_name=jobdata.getSelected_name(); Curr.setText("The applicant selected is "+selected_name); }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mref2.addValueEventListener(postListener);


        options = new FirebaseRecyclerOptions.Builder<ApplicantClass>().setQuery(mref, ApplicantClass.class).build();

        adapter= new FirebaseRecyclerAdapter<ApplicantClass, ViewHolderClass2>(options) {
            @NonNull
            @Override
            public ViewHolderClass2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row2, parent,  false);
                return new ViewHolderClass2(view);


            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderClass2 holder, int k, @NonNull ApplicantClass model) {

                holder.name.setText(model.getName());
                holder.desc.setText(model.getApplication());
                holder.select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (filled==true) {  Toast.makeText(getApplicationContext(), "You cannot select another applicant", Toast.LENGTH_SHORT).show(); }


                        else {
                            Toast.makeText(getApplicationContext(), "The applicant has been selected for this job", Toast.LENGTH_SHORT).show();
                            mref3 = mref.getParent();
                            //mref is the jobID
                            mref3.child("status").setValue(true);
                            mref3.child("selected_name").setValue(model.getName());
                        }
                    }
                });
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}