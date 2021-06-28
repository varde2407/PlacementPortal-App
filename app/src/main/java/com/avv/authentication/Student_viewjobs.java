package com.avv.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Student_viewjobs extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref , mref2;
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String uid;
    private float cpi_stu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("varde", "this is the error");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_viewjobs);

        recyclerView = findViewById(R.id.rec_view);
        recyclerView.setHasFixedSize(true);

        mAuth= FirebaseAuth.getInstance();
        rootnode = FirebaseDatabase.getInstance();
        mref = rootnode.getReference("jobs");
        mref2 = rootnode.getReference("users");
        mUser = mAuth.getCurrentUser();
        uid= mUser.getUid();

//      Double [] stu = new Double[10];
//      Double [] comp = new Double[10];
//
//
//        mref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
//                    JobHelperClass info =snapshot.getValue()
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        })
       //  Double stucpi;
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                Log.d("aditya","reached ondatachange");
//                // Get Post object and use the values to update the UI
//                UserHelperClass userdata = dataSnapshot.getValue(UserHelperClass.class);
//                // ...
//                cpi_stu = userdata.getCpi();
//                System.out.println(cpi_stu);
//              //  Boolean v =userdata.getStudent();
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//         mref2 = mref2.child(uid);
//        mref2.addValueEventListener(postListener);




        options = new FirebaseRecyclerOptions.Builder<JobHelperClass>().setQuery(mref.orderByChild("cpi_cutoff").startAt(cpi_stu), JobHelperClass.class).build();

        adapter= new FirebaseRecyclerAdapter<JobHelperClass, ViewHolderClass>(options) {
            @NonNull
            @Override
            public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,  false);
                return new ViewHolderClass(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderClass holder, int k, @NonNull JobHelperClass model) {

                holder.companyID.setText(model.getCompanyID());
                holder.position.setText(model.getPosition());
                //holder.StudentviewJobImage.setImageBitmap(model.getStudentviewJobImage());
              //  holder.Job_Location.setText(model.getJob_Location());
                holder.position.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(Student_viewjobs.this, ApplyJob.class);
                        intent.putExtra("JobID", model.getJobID());
                        intent.putExtra("Position", model.getPosition());
                        intent.putExtra("CompanyID", model.getCompanyID());
                       intent.putExtra("job_Location",model.getCpi_cutoff());
                      intent.putExtra("job_Location",model.getJob_Location());
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }



}