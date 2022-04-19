package com.example.disasteye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.appbar.MaterialToolbar;

public class aboutus extends AppCompatActivity {
    private ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        /*back = (ImageButton) findViewById(R.id.backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked");
                goBackHome();
            }
        });*/
    }

   /* public void goBackHome(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }*/

    public void btnBack(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }


    //Disables back button.
    /*
    @Override
    public void onBackPressed(){
    }
    */
}
//    @Override
//    public void onBackPressed() {
//        final MaterialToolbar TopAppBar = super.findViewById(R.id.topAppBar);
//        TopAppBar.setVisibility(View.VISIBLE);
//        super.onBackPressed();
//    }
