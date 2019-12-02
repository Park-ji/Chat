package com.example.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {


    Button registerBtn, overlap_id, overlap_email;
    EditText idet, pwet, cpwet, nameet, emailet;
    ImageView check;
    boolean isFlag_id = false;
    boolean isFlag_email = false;
    boolean isFlag_pw = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        setTitle("회원가입");

        registerBtn = (Button) findViewById(R.id.register_btn);
        idet = (EditText) findViewById(R.id.register_id);
        pwet = (EditText) findViewById(R.id.register_pw);
        cpwet = (EditText) findViewById(R.id.check_pw);
        nameet = (EditText) findViewById(R.id.register_name);
        emailet = findViewById(R.id.register_email);
        overlap_id = findViewById(R.id.overlapID_btn);
        overlap_email = findViewById(R.id.overlapEMail_btn);
        check = findViewById(R.id.check);

        cpwet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (pwet.getText().toString().equals(cpwet.getText().toString())) {
                    check.setImageResource(R.drawable.verified);
                    isFlag_pw = true;
                } else {
                    check.setImageResource(R.drawable.cancel);
                    isFlag_pw = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    String result;
                    String id = idet.getText().toString();
                    String pw = pwet.getText().toString();
                    String name = nameet.getText().toString();
                    String email = emailet.getText().toString();

                    if (email.length() == 0 || name.length() == 0 || pw.length() == 0 || id.length() == 0) {
                        Toast.makeText(getApplicationContext(), "빈칸을 채우세요.", Toast.LENGTH_SHORT).show();

                        return;

                    }

                    SignUpActivity task = new SignUpActivity();

                    if (!isFlag_pw) {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!isFlag_id) {
                        Toast.makeText(getApplicationContext(), "아이디 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!isFlag_email) {
                        Toast.makeText(getApplicationContext(), "이메일 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    result = task.execute(id, pw, name, email).get();
                    if (result.equals("회원 가입 성공!")) {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    } else {

                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });

        overlap_id.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                try {
                    String result;
                    String id = idet.getText().toString();

                    if (id.length() == 0) {
                        Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();

                        return;

                    }

                    Overlap overlap = new Overlap(1);
                    result = overlap.execute(id).get();
                    if (result.equals("사용할 수 있는 ID입니다.")) isFlag_id = true;
                    else isFlag_id = false;
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });




        overlap_email.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                try {
                    String result;
                    String email = emailet.getText().toString();

                    if (email.length() == 0) {
                        Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();

                        return;

                    }

                    Overlap overlap = new Overlap(2);
                    result = overlap.execute(email).get();
                    if (result.equals("사용할 수 있는 Email입니다.")) isFlag_email = true;
                    else isFlag_email = false;
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();


                    //      "회원 가입 성공!" "실패" "이미 존재하는 아이디 입니다."

                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }
        });
        EditText autoRI = findViewById(R.id.register_id);
        EditText autoRE = findViewById(R.id.register_email);

        autoRI.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    overlap_id.performClick();

                    return true;
                }
                return false;
            }
        });

        autoRE.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    overlap_email.performClick();

                    return true;
                }
                return false;
            }
        });

    }
}
