package com.example.petts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Glavn extends AppCompatActivity {
    Button knopka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maindisp);
    }

    public void pit(View v) {
        Intent intent = new Intent(this, .class);
        startActivity(intent);
    }
}
