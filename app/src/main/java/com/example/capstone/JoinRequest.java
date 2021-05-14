package com.example.capstone;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JoinRequest extends StringRequest {

    //서버 URL 설정(php 파일 연동)
    final static private String URL = "";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public JoinRequest(String UserEmail, String UserPwd, String UserName, String Pro, String Ward,
                       Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("UserEmail", UserEmail);
        map.put("UserPwd", UserPwd);
        map.put("UserName", UserName);
        map.put("UserPro", Pro);
        map.put("UserWard", Ward);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
