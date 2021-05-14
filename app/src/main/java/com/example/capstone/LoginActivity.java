package com.example.capstone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity{

    private EditText login_email, login_password;
    private LinearLayout LinearLayoutLogin, LinearLayoutJoin;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        login_email = findViewById( R.id.login_id );
        login_password = findViewById( R.id.login_pw );

        LinearLayoutJoin=findViewById(R.id.LinearLayoutJoin);
        LinearLayoutJoin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( LoginActivity.this, JoinActivity.class );
                startActivity(intent);
            }
        });


        LinearLayoutLogin = findViewById(R.id.LinearLayoutLogin);
        LinearLayoutLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserEmail = login_email.getText().toString();
                String UserPwd = login_password.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );

                            if(success) {//로그인 성공시

                                String UserEmail = jsonObject.getString( "UserEmail" );
                                String UserPwd = jsonObject.getString( "UserPwd" );
                                String UserName = jsonObject.getString( "UserName" );

                                Toast.makeText( getApplicationContext(), String.format("%s님 환영합니다.", UserName), Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( LoginActivity.this, ContentActivity.class );

                                intent.putExtra( "UserEmail", UserEmail );
                                intent.putExtra( "UserPwd", UserPwd );
                                intent.putExtra( "UserName", UserName );

                                startActivity(intent);

                            } else {//로그인 실패시
                                Toast.makeText( getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest( UserEmail, UserPwd, responseListener );
                RequestQueue queue = Volley.newRequestQueue( LoginActivity.this );
                queue.add( loginRequest );

            }
        });
    }
}
