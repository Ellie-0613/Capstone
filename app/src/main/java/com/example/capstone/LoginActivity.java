package com.example.capstone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity{

    private EditText login_email, login_password;
    private LinearLayout LinearLayoutLogin, LinearLayoutJoin, LinearLayoutLoginAsWard;

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
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.d("test", "get fcm token failed");
                                    return;
                                }
                                String FcmToken = task.getResult();
                                String UserEmail = login_email.getText().toString();
                                String UserPwd = login_password.getText().toString();

                                String url = "http://172.30.1.38:3000/user/login";
                                Log.d("test", FcmToken);
                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put("email", UserEmail);
                                    jsonBody.put("password", UserPwd);
                                    jsonBody.put("fcm_token", FcmToken);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                final String mRequestBody = jsonBody.toString();
                                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject( response );
                                            boolean success = jsonObject.getBoolean( "success" );
                                            if(success) {//로그인 성공시
                                                String UserName = jsonObject.getString( "nickname" );
                                                String AccessToken = jsonObject.getString( "token" );
                                                SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

                                                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("accessToken", AccessToken);
                                                editor.putString("userType", "guardian");
                                                editor.commit();

                                                Toast.makeText( getApplicationContext(), String.format("%s님 환영합니다.", UserName), Toast.LENGTH_SHORT ).show();
                                                Intent intent = new Intent( LoginActivity.this, MainActivity.class );
                                                startActivity(intent);
                                            } else {//로그인 실패시
                                                Toast.makeText( getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                    }
                                }) {
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8";
                                    }
                                    @Override
                                    public byte[] getBody() throws AuthFailureError {
                                        try {
                                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                                        } catch (UnsupportedEncodingException uee) {
                                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                                            return null;
                                        }
                                    }
                                };
                                //LoginRequest loginRequest = new LoginRequest( UserEmail, UserPwd, responseListener );
                                //RequestQueue queue = Volley.newRequestQueue( LoginActivity.this );
                                queue.add( stringRequest );
                            }
                        });
            }
        });
        LinearLayoutLoginAsWard = findViewById(R.id.LoginAsWard);
        LinearLayoutLoginAsWard.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserEmail = login_email.getText().toString();
                String UserPwd = login_password.getText().toString();

                String url = "http://172.30.1.38:3000/usercli/login";
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", UserEmail);
                    jsonBody.put("password", UserPwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String mRequestBody = jsonBody.toString();
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );
                            Log.d("test", response);
                            if(success) {//로그인 성공
                                String UserName = jsonObject.getString( "nickname" );
                                String AccessToken = jsonObject.getString( "token" );
                                SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);

                                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Log.d("test", AccessToken);
                                editor.putString("accessToken", AccessToken);
                                editor.putString("userType", "ward");
                                editor.commit();

                                SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                                String token = sf.getString("accessToken", "");
                                Log.d("test", "accessToken "+token);

                                Toast.makeText( getApplicationContext(), String.format("%s님 환영합니다.", UserName), Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( LoginActivity.this, AlarmActivity.class );
                                startActivity(intent);

                            } else {//로그인 실패시
                                Toast.makeText( getApplicationContext(), "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT ).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }
                };
                queue.add( stringRequest );
            }
        });
    }
}
