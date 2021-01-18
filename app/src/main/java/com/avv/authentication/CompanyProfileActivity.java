package com.avv.authentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

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

public class CompanyProfileActivity extends AppCompatActivity{

    private static final int CHOOSE_IMAGE = 101;
    FirebaseAuth mAuth;
    private FirebaseDatabase rootnode;
    private DatabaseReference mref;
    String companypicuri;
    ProgressBar progressbar2;
    ImageView img;
    EditText name, desc;
    Uri logouri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        img = findViewById(R.id.imageViewCo);
        name = findViewById(R.id.companyname);
        desc = findViewById(R.id.editTextTextMultiLine);
        rootnode = FirebaseDatabase.getInstance();
        progressbar2 = findViewById(R.id.progressbar);
        mref = rootnode.getReference("users");
        mAuth= FirebaseAuth.getInstance();
        loadCompanyInformation();


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImageChooser();

            }
        });
        findViewById(R.id.updcom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCompanyProfile();
            }
        });
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button17).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), CompanyCreateJob.class);
                startActivity(ii);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iii = new Intent(getApplicationContext(), Company_viewjobs.class);
                startActivity(iii);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            logouri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), logouri);
                img.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference companyRef = FirebaseStorage.getInstance().getReference("companypics/" + System.currentTimeMillis() + ".jpg");
        if (logouri != null) {
            progressbar2.setVisibility(View.VISIBLE);
            companyRef.putFile(logouri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("we did it", " onsuccess of firebase upload of logo");
                            progressbar2.setVisibility(View.GONE);
                            companyRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    companypicuri = uri.toString();
                                    logouri= uri;
                                    Log.d("final ",companypicuri);

                                    Glide.with(getApplicationContext())
                                            .load(uri)
                                            .into(img);

                                    // just do your task //like hashmaps to put in
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

    private void showImageChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Logo Image"), CHOOSE_IMAGE);


    }

    private void loadCompanyInformation() {
        FirebaseUser user = mAuth.getCurrentUser();
        String myuid = user.getUid();

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("ADI", "helookoko");
                // Get Post object and use the values to update the UI
                UserHelperClass userdata = dataSnapshot.getValue(UserHelperClass.class);


                String locuri = userdata.getImageuri();
                companypicuri = locuri;
                name.setText(userdata.getName());
                desc.setText(userdata.getDesc());


                if(locuri!=null) {
                    Glide.with(getApplicationContext())
                            .load(Uri.parse(locuri))
                            .into(img);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mref = mref.child(myuid);
        mref.addValueEventListener(postListener);
    }



    private void updateCompanyProfile() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Name required");
            name.requestFocus();
            return;
        }
        if(desc.getText().toString().isEmpty()){

            desc.setError("Description cannot be empty");
            desc.requestFocus();
        }
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mref.child("name").setValue(name.getText().toString());
            mref.child("desc").setValue(desc.getText().toString());
            mref.child("imageuri").setValue(companypicuri);
        }
    }
}