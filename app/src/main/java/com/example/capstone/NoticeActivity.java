package com.example.capstone;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class NoticeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        String[] list = {"소나기를 이용해주셔서 감사합니다.", "이용방법"};

        final String[] list_explain = {" 많은 이용 부탁드립니다.", ": 자세한 사항은 앱 사용방법을 참고해주세요."};

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        ListView listView =(ListView) findViewById(R.id.notice_ListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText()
                +list_explain[position], Toast.LENGTH_LONG).show();

            }
        });
    }
}
