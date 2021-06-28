package com.avv.authentication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderClass extends RecyclerView.ViewHolder {

    public TextView position, companyID;
    public ImageView StudentviewJobImage;


    public ViewHolderClass(@NonNull View itemView) {
        super(itemView);
        position = itemView.findViewById(R.id.textView8);
        companyID = itemView.findViewById(R.id.textView9);
       // Job_Location= itemView.findViewById(R.id.CompanyLocationTxt);
        StudentviewJobImage = itemView.findViewById(R.id.StudentviewJobImage);
    }
}
