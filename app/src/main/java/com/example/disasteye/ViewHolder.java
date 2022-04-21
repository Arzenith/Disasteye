package com.example.disasteye;

import android.view.View;
import android.widget.TextView;

public class ViewHolder {
    TextView newsTitle;

    ViewHolder (View V){
        newsTitle = V.findViewById(R.id.newTitle);
    }
}
