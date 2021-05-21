package com.example.capstone;

import android.content.Intent;
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

public class JoinActivity extends AppCompatActivity {
    private EditText join_email, join_password, join_name, join_pwck, join_pro, join_ward;
    private Button btn_register, btn_check;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_join );

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //아이디값 찾아주기
        join_email = findViewById( R.id.t_email );
        join_password = findViewById( R.id.t_pass );
        join_name = findViewById( R.id.check_name);
        join_pwck = findViewById(R.id.t_pw_pass);
        join_pro = findViewById(R.id.t_pro);
        join_ward = findViewById(R.id.t_ward);


        //아이디 중복 체크
        btn_check = findViewById(R.id.duplicate);
        btn_check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String UserEmail = join_email.getText().toString();
                if (validate) {
                    return; //검증 완료
                }

                if (UserEmail.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    dialog = builder.setMessage("이메일을 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
                String url = "http://172.30.1.38:3000/email";
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("email", UserEmail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String mRequestBody = jsonBody.toString();
                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                dialog = builder.setMessage("인증메일이 발송되었습니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                join_email.setEnabled(false); //아이디값 고정
                                validate = true; //검증 완료
                                btn_check.setBackgroundColor(getResources().getColor(R.color.Gray));
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                dialog = builder.setMessage("오류가 발생했습니다.").setNegativeButton("확인", null).create();
                                dialog.show();
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
                queue.add(stringRequest);
            }
        });

        //회원가입 버튼 클릭 시 수행
        btn_register = findViewById( R.id.btn_register );
        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String UserEmail = join_email.getText().toString();
                final String UserPwd = join_password.getText().toString();
                final String UserName = join_name.getText().toString();
                final String PassCk = join_pwck.getText().toString();
                final String Pro = join_pro.getText().toString();
                final String Code = join_ward.getText().toString(); //인증코드

                if (UserEmail.equals("") || UserPwd.equals("") || UserName.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.d("test", "getToken failed");
                                    return;
                                }
                                // Get new FCM registration token
                                String FcmToken = task.getResult();
                                String url = "http://172.30.1.38:3000/user";
                                //보호자 이메일 란이 빈칸이 아니면 피보호자로 회원가입 되게
                                if (!Pro.equals("")) url = "http://172.30.1.38:3000/usercli";
                                JSONObject jsonBody = new JSONObject();
                                try {
                                    jsonBody.put("email", UserEmail);
                                    jsonBody.put("code", Code);
                                    jsonBody.put("nickname", UserName);
                                    jsonBody.put("password", UserPwd);
                                    jsonBody.put("fcm_token", FcmToken);
                                    if (!Pro.equals("")) jsonBody.put("nokemail", Pro);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                final String mRequestBody = jsonBody.toString();
                                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject = new JSONObject( response );
                                            boolean success = jsonObject.getBoolean( "success" );

                                            //회원가입 성공시
                                            if(UserPwd.equals(PassCk)) {
                                                if (success) {

                                                    Toast.makeText(getApplicationContext(), String.format("%s님 가입을 환영합니다.", UserName), Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                                    startActivity(intent);

                                                    //회원가입 실패시
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                                dialog = builder.setMessage("비밀번호가 동일하지 않습니다.").setNegativeButton("확인", null).create();
                                                dialog.show();
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
        });
    }
}
