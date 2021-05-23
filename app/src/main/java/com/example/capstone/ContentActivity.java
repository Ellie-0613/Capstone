package com.example.capstone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContentActivity extends AppCompatActivity{
    private Button Registration, Method;
    private TextView Using;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Button Registration = (Button) findViewById(R.id.registration);
        Button Method = (Button) findViewById(R.id.methed);
        Using = findViewById(R.id.using) ;

        Registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        Method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MethodActivity.class);
                startActivity(intent);
            }
        });

        Using.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //토큰 가져오기
                SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                String token = sf.getString("accessToken", "");
                if (token.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    String userType = sf.getString("userType", "");
                    if (userType.equals("ward")) {
                        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                        startActivity(intent);
                    }
                    else if (userType.equals("guardian")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

}
