package com.example.capstone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout linearLayoutOption, board;
    private final String TAG = getClass().getSimpleName();
    private long backKeyPressedTime = 0;//
    private Toast toast;//

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat dtNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String formatDate = dtNow.format(date);

    TextView date_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate:start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        activityInit();
        Log.d(TAG, "onCreate:end");

        date_time = (TextView) findViewById(R.id.date_time); //시간설정
        date_time.setText(formatDate);

        board = findViewById(R.id.board) ;

        board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoardActivity.class);
                startActivity(intent);
            }
        });

    }

    private void activityInit() {
        Log.d(TAG, "activityInit: start");
        linearLayoutOption = findViewById(R.id.LinearLayoutOption);
        linearLayoutOption.setOnClickListener(this);

        Log.d(TAG, "activityInit: end");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: start");

        // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
        // super.onBackPressed();

        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
        Log.d(TAG, "onBackPressed: end");
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: start");
        Intent intent = new Intent(this, OptionActivity.class);
        startActivity(intent);

    }
}