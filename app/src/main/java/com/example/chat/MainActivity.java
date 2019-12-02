package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setTitle("Home");


        Button loginBtn = (Button) findViewById(R.id.loginButton);
        Button signupBtn = (Button) findViewById(R.id.signupButton);
        Button findIDBtn = findViewById(R.id.findID);
        Button findPWBtn = findViewById(R.id.findPW);
        EditText autoLogin = (EditText) findViewById(R.id.passwordInput);

        autoLogin.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    loginBtn.performClick();
                    return true;
                }
                return false;
            }
        });

        findIDBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindID.class);
                startActivity(intent);
            }
        });

        findPWBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FindPW.class);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {

                EditText idet = (EditText) findViewById(R.id.idInput);
                EditText pwet = (EditText) findViewById(R.id.passwordInput);

                try {
                    String result;
                    String id = idet.getText().toString();
                    String pw = pwet.getText().toString();

                    if (pw.length() == 0 || id.length() == 0) {
                        Toast.makeText(getApplicationContext(), "빈칸을 채우세요.", Toast.LENGTH_SHORT).show();

                        return;

                    }

                    LoginActivity task = new LoginActivity();
                    result = task.execute(id, pw).get();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                    if (result.contains("로그인이 성공되었습니다.")) {
                        Ccc.setMyName(result.split(":")[0]);
                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        startActivity(intent);
                    }

                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }

            }
        });
    }
}
