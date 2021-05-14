package com.example.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

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
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                join_email.setEnabled(false); //아이디값 고정
                                validate = true; //검증 완료
                                btn_check.setBackgroundColor(getResources().getColor(R.color.Gray));
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                dialog = builder.setMessage("이미 존재하는 아이디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(UserEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                queue.add(validateRequest);
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
                final String Ward = join_ward.getText().toString();

                //아이디 중복체크 했는지 확인
                if (!validate) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    dialog = builder.setMessage("중복된 아이디가 있는지 확인하세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                //한 칸이라도 입력 안했을 경우
                if (UserEmail.equals("") || UserPwd.equals("") || UserName.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    dialog = builder.setMessage("모두 입력해주세요.").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
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
                };

                //서버로 Volley를 이용해서 요청
                JoinRequest joinRequest = new JoinRequest( UserEmail, UserPwd, UserName, Pro, Ward, responseListener);
                RequestQueue queue = Volley.newRequestQueue( JoinActivity.this );
                queue.add( joinRequest );
            }
        });
    }
}
