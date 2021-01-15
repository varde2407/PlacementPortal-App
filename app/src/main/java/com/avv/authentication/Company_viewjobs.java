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

public class Company_viewjobs extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;
    private FirebaseRecyclerOptions options;
    private FirebaseRecyclerAdapter adapter;
    private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("varde", "this is the error");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_viewjobs);

        recyclerView = findViewById(R.id.rec_view1);
        recyclerView.setHasFixedSize(true);

        mauth=FirebaseAuth.getInstance();
        FirebaseUser usser=mauth.getCurrentUser();
        rootnode = FirebaseDatabase.getInstance();
        mref = rootnode.getReference("jobs");

        options = new FirebaseRecyclerOptions.Builder<JobHelperClass>().setQuery(mref.orderByChild("companyID").equalTo(usser.getUid()), JobHelperClass.class).build();

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
                holder.position.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(Company_viewjobs.this, Company_viewApplicants.class);
                        intent.putExtra("JobID", model.getJobID());
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