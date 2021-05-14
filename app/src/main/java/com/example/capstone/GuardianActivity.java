package com.example.capstone;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class GuardianActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_guardian);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
