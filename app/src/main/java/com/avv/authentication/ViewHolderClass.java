package com.avv.authentication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderClass extends RecyclerView.ViewHolder {

    public TextView position, companyID;

    public ViewHolderClass(@NonNull View itemView) {
        super(itemView);
        position = itemView.findViewById(R.id.textView8);
        companyID = itemView.findViewById(R.id.textView9);
    }
}
