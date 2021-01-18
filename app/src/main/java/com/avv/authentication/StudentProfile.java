package com.avv.authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class StudentProfile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private static final int CHOOSE_RESUME = 438;
    ImageView imageView;
    EditText cpi;
    EditText name;
    TextView genderET;
    EditText branch;
    Uri uriProfileImage,uriResume;
    String loadresumeURL;
    String profileImageUrl,resumeURL;
    EditText desc;
    ProgressBar progressbar2;
    FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;
    String locuri ="" ;
    String locname ="dumdum" ;
    Float loccpi=(float)0.00;
    String locdesc ="testing";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        Log.d("Hello reached profile"," oncreate of stu");

        mAuth = FirebaseAuth.getInstance();

        rootnode = FirebaseDatabase.getInstance();
        mref = rootnode.getReference("users");
        cpi = findViewById(R.id.editTextNumber);
        imageView = findViewById(R.id.imageView3);
        desc = findViewById(R.id.editTextTextMultiLine);
        branch = findViewById(R.id.editTextTextPersonName7);
        name=  findViewById(R.id.editTextTextPersonName6);
        progressbar2 = findViewById(R.id.progressbar);
        genderET = findViewById(R.id.textView4);

        loadUserInformation();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageChooser();

            }
        });

        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent.createChooser(intent,"Select CV File(pdf)"), CHOOSE_RESUME);

            }
        });
        findViewById(R.id.button13).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadresumeURL != null) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loadresumeURL));
                    startActivity(browserIntent);
                }
            }
        });
        findViewById(R.id.updcom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Student_viewjobs.class);
                intent1.addFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP));
                startActivity(intent1);

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        String myuid = user.getUid();
        Log.d("adiii",myuid);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("ADI", "helookoko");
                // Get Post object and use the values to update the UI
                UserHelperClass userdata = dataSnapshot.getValue(UserHelperClass.class);

                locuri = userdata.getImageuri();
               locname = userdata.getName();
               loccpi = userdata.getCpi();
               locdesc = userdata.getDesc();
                loadresumeURL = userdata.getResumeuri();

                int gender = userdata.getGender();

                    String gen = (gender == 1 ? "Male" : "Female");
                    genderET.setText(gen);

                resumeURL = loadresumeURL;
                name.setText(locname);
                cpi.setText(userdata.getCpi().toString());
                desc.setText(locdesc);
                branch.setText(userdata.getBranch());


                if(locuri!=null) {
                    Glide.with(getApplicationContext())
                            .load(Uri.parse(locuri))
                            .into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mref = mref.child(myuid);
        mref.addValueEventListener(postListener);

    }
    private void saveUserInformation() {


        if (name.getText().toString().isEmpty()) {
            name.setError("Name required");
            name.requestFocus();
            return;
        }
        if(cpi.getText().toString().isEmpty()){

            cpi.setError("CPI required");
            cpi.requestFocus();
        }
        String mycpi = cpi.getText().toString();
        if(Float.parseFloat(mycpi) >10 || Float.parseFloat(mycpi) <0 ){

            cpi.setError("CPI must be between 0 and 10");
            cpi.requestFocus();
        }
        if(desc.getText().toString().isEmpty()){

            desc.setError("Description cannot be empty");
            desc.requestFocus();
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String myuid = user.getUid();

            mref.child("name").setValue(name.getText().toString());
            mref.child("cpi").setValue(Float.parseFloat(cpi.getText().toString()));
            mref.child("desc").setValue(desc.getText().toString());
            mref.child("imageuri").setValue(locuri);
            mref.child("resumeuri").setValue(resumeURL);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(requestCode == CHOOSE_RESUME && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            uriResume = data.getData();
            uploadResumeToFirebaseStorage();
        }
    }
    private void uploadResumeToFirebaseStorage(){
        StorageReference resumeRef = FirebaseStorage.getInstance().getReference("resume/" + System.currentTimeMillis() + ".pdf");
        if(uriResume != null) {
            progressbar2.setVisibility(View.VISIBLE);
            resumeRef.putFile(uriResume)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("we did it", " onsuccess of resume upload");
                            progressbar2.setVisibility(View.GONE);
                            resumeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    resumeURL = uri.toString();
                                    Log.d("final ", resumeURL);
                                    Toast.makeText(getApplicationContext(), "Resume Loaded Successfully", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar2.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImage != null) {
            progressbar2.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("we did it", " onsuccess of firebase upload");
                            progressbar2.setVisibility(View.GONE);
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
                                    Log.d("final ",profileImageUrl);

                                    Glide.with(getApplicationContext())
                                            .load(profileImageUrl)
                                            .into(imageView);
                                    locuri = profileImageUrl;
                                    // just do your task //like hashmaps to put in
                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressbar2.setVisibility(View.GONE);
                            Toast.makeText(StudentProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }






    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);

    }





}
