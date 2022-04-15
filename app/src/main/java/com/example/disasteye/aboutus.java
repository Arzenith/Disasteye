package com.example.disasteye;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

public class aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
    }

    //Disables back button.
    @Override
    public void onBackPressed(){
    }

}
//    @Override
//    public void onBackPressed() {
//        final MaterialToolbar TopAppBar = super.findViewById(R.id.topAppBar);
//        TopAppBar.setVisibility(View.VISIBLE);
//        super.onBackPressed();
//    }
