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

public class CompanySignup extends AppCompatActivity {



    private static final int CHOOSE_IMAGE = 101;
    private FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;

    Uri uricompanyimage;
    String logouri;
    EditText emailET, passwordET, name, desc;
    ProgressBar progressBar;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companysignup);

        mAuth=FirebaseAuth.getInstance();
        emailET = findViewById(R.id.mailtv);
        passwordET = findViewById(R.id.passtv);
        name = findViewById(R.id.nametv);
        desc = findViewById(R.id.desctv);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.imageView4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageChooser();

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
        if(logouri.isEmpty()){
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
                    Log.d("the image uri is ",logouri);
                    UserHelperClass UHC= new UserHelperClass(email, password,name.getText().toString(),"", false, (float)0.00, desc.getText().toString(),logouri,"",0);
                    mref.child(user.getUid()).setValue(UHC);
                    startActivity(new Intent(getApplicationContext(), CompanyProfileActivity.class));
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
            uricompanyimage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uricompanyimage);
                ImageView img = findViewById(R.id.imageView4);
                img.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadImageToFirebaseStorage() {
        StorageReference companyRef = FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (uricompanyimage != null) {
            progressBar.setVisibility(View.VISIBLE);
            companyRef.putFile(uricompanyimage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("we did it", " onsuccess of firebase upload");
                            progressBar.setVisibility(View.GONE);
                            companyRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    logouri = uri.toString();
                                    Log.d("final ",logouri);
                                    ImageView img = findViewById(R.id.imageView4);
                                    Glide.with(getApplicationContext())
                                            .load(uricompanyimage)
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


