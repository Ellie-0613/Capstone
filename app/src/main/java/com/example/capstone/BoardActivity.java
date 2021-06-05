package com.example.capstone;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BoardActivity extends AppCompatActivity{
    private ImageButton write;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_board);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        adapter.add("안녕하세요");
        adapter.add("반갑습니다");
        adapter.add("환영합니다");
        adapter.add("오늘도 좋은 하루 보내세요");

        ListView board_ListView = (ListView)findViewById(R.id.board_ListView);
        board_ListView.setAdapter(adapter);

        write=(ImageButton)findViewById(R.id.write);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.add);
                String str = et.getText().toString();
                adapter.add(str);
                et.setText("");
            }
        });
    }
}
