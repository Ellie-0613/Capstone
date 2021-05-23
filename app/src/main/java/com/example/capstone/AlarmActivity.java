package com.example.capstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AlarmActivity extends AppCompatActivity {
    private Button btn_alarm, btn_logout;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_alarm );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //아이디 중복 체크
        btn_alarm = findViewById(R.id.alarm);
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sf = getSharedPreferences("sFile", MODE_PRIVATE);
                String token = sf.getString("accessToken", "");
                String url = "http://172.30.1.38:3000/log/alarm";
                RequestQueue queue = Volley.newRequestQueue(AlarmActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("test", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
                                dialog = builder.setMessage("보호자에게 알림을 보냈습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AlarmActivity.this);
                                dialog = builder.setMessage("오류가 발생했습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            Log.d("test", "error!");
                            Log.d("test", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test", "error2");
                        Log.d("test", error.getMessage());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        final HashMap<String, String> headers = new HashMap<>();
                        headers.put("token", token);
                        return headers;
                    }
                };
                queue.add(stringRequest);
            }
        });
        btn_logout = findViewById(R.id.logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("sFile",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("accessToken", "");
                editor.putString("userType", "");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), ContentActivity.class);
                startActivity(intent);
            }
        });
    }
}
