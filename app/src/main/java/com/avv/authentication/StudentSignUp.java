package com.avv.authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class StudentSignUp extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private static final int CHOOSE_RESUME = 438;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;

    int gender = 0;
    Uri uriProfileImage, uriResume;
    String profileImageUrl,resumeURL;
    EditText emailET, passwordET, name, branch, cpi, desc;
    ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsignup);
        mAuth=FirebaseAuth.getInstance();
        emailET = findViewById(R.id.mailtv);
        passwordET = findViewById(R.id.passtv);
        name = findViewById(R.id.nametv);
        branch = findViewById(R.id.branchtv);
        cpi = findViewById(R.id.cpitv);
        desc = findViewById(R.id.desctv);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageChooser();

            }
        });
        findViewById(R.id.button15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent.createChooser(intent,"Select CV File(pdf)"), CHOOSE_RESUME);

            }
        });

        findViewById(R.id.button16).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootnode= FirebaseDatabase.getInstance();
                mref=rootnode.getReference("users");
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                RegisterUser(email, password);
            }
        });
    }
    private void RegisterUser(String email, String password) {
        RadioGroup radioGroup = findViewById(R.id.radiogroupgender);
        if(radioGroup == null){
            Toast.makeText(getApplicationContext(),"Select gender.",Toast.LENGTH_SHORT).show();
            return;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(R.id.radioButton2 == checkedId){
                    gender = 1;
                }else{
                    gender = 2;
                }
            }
        });

        if(email.isEmpty()){
            emailET.setError("Email is required!");
            emailET.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailET.setError("Please enter a valid email");
            emailET.requestFocus();
            return;
        }

        if(password.isEmpty()){
            passwordET.setError("Password is required!");
            passwordET.requestFocus();
            return;
        }

        if(name.getText().toString().isEmpty()){
            name.setError("Name is required!");
            name.requestFocus();
            return;
        }
        if(branch.getText().toString().isEmpty()){
            branch.setError("Branch is required!");
            branch.requestFocus();
            return;
        }
        if(cpi.getText().toString().isEmpty()){
            cpi.setError("CPI is required!");
            cpi.requestFocus();
            return;
        }
        if(desc.getText().toString().isEmpty()){
            desc.setError("Description is required!");
            desc.requestFocus();
            return;
        }

        if(password.length()<6) {
            passwordET.setError("Password must consists of atleast 6 characters!");
            passwordET.requestFocus();
            return;
        }
        if(profileImageUrl.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please upload an image",Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if(task.isSuccessful()){

                    FirebaseUser user= mAuth.getCurrentUser();

                    //create entry in database
                    Log.d("the image uri is ",profileImageUrl);
                    UserHelperClass UHC= new UserHelperClass(email, password,name.getText().toString(),branch.getText().toString(), true, Float.parseFloat(cpi.getText().toString()), desc.getText().toString(),profileImageUrl,resumeURL,gender);
                    mref.child(user.getUid()).setValue(UHC);
                    startActivity(new Intent(getApplicationContext(), StudentProfile.class));
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                ImageView img = findViewById(R.id.imageView4);
                img.setImageBitmap(bitmap);
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
            progressBar.setVisibility(View.VISIBLE);
            resumeRef.putFile(uriResume)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("we did it", " onsuccess of resume upload");
                            progressBar.setVisibility(View.GONE);
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
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImage != null) {
            progressBar.setVisibility(View.VISIBLE);
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("we did it", " onsuccess of firebase upload");
                            progressBar.setVisibility(View.GONE);
                            profileImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
                                    Log.d("final ",profileImageUrl);
                                    ImageView img = findViewById(R.id.imageView4);
                                    Glide.with(getApplicationContext())
                                            .load(profileImageUrl)
                                            .into(img);
                                    // just do your task //like hashmaps to put in
                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
