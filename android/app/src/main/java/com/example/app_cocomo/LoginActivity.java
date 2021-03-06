package com.example.app_cocomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_cocomo.auth.fingerprint.FingerprintActivity;
import com.example.app_cocomo.rest.RestBuilder;
import com.example.app_cocomo.rest.define.DefinePath;
import com.example.app_cocomo.rest.define.LogTag;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    private EditText etUserId;
    private EditText etPasswd;

    private boolean formChkRes = false;
    private boolean sendReqRes = false;

    private RestBuilder restBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // REST API 통신
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DefinePath.DEFAULT) // localhost:1208/
                .addConverterFactory(GsonConverterFactory.create()) // JSON -> Java 객체 변환
                .build();

        restBuilder = retrofit.create(RestBuilder.class);


        // 회원가입 버튼
        Button btnJoin = (Button) findViewById(R.id.btn_join);
        btnJoin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent_join = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent_join);
            }
        });


        // 로그인 버튼
        // 두 번 터치해야 실행됨... 대체 왜?
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                etFormCheck();

                if (sendReqRes)
                {
                    Intent intentHome = new Intent(LoginActivity.this, HomeActivity.class);
                    intentHome.putExtra("userId", etUserId.getText().toString());

                    startActivity(intentHome);
                    finish();
                }

            }
        });

    }

    private void etFormCheck()
    {
        etUserId = (EditText)findViewById(R.id.editText_id);
        etPasswd = (EditText)findViewById(R.id.editText_pw);

        if (etUserId.getText().toString().length() < 4)
        {
            Toast.makeText(LoginActivity.this, "ID는 4글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            etUserId.requestFocus();
        }
        else if (etPasswd.getText().toString().length() < 5)
        {
            Toast.makeText(LoginActivity.this, "비밀번호는 5글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            etPasswd.requestFocus();
        }
        else
        {
            Log.d(LogTag.LoginTag, "etFormCheck()" + LogTag.SUCCESS);
            sendRequest();
        }
    }

    private void sendRequest()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", etUserId.getText().toString());
        map.put("passwd", etPasswd.getText().toString());
        Log.d(LogTag.LoginTag, "POST Body: " + map.toString());

        restBuilder.signInUser(map).enqueue(new Callback<Void>()
        {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response)
            {
                if (response.isSuccessful())
                {
                    Log.d(LogTag.LoginTag, "sendRequest()" + LogTag.SUCCESS);
                    sendReqRes = true;

                    return;
                } else {
                    Toast.makeText(LoginActivity.this, "ID와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t)
            {
                Log.d(LogTag.LoginTag, "REST API" + LogTag.FAILED + t.getMessage());
            }
        });
    }

}