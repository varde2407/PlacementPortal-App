package com.avv.authentication;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderClass2 extends RecyclerView.ViewHolder{

    public TextView name, desc;
    public Button select;

    public ViewHolderClass2(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textView8);
        desc = itemView.findViewById(R.id.textView9);
        select=itemView.findViewById(R.id.button10);
    }
}
