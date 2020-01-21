package com.peopledemo1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AddPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addphoto);

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        Button addpostbutton = (Button) findViewById(R.id.addpostbutton);
        final TextView outer = (TextView) findViewById(R.id.outer);
        final TextView top = (TextView) findViewById(R.id.top);
        final TextView pants = (TextView) findViewById(R.id.pants);
        final TextView shoe = (TextView) findViewById(R.id.shoe);


    }
}
