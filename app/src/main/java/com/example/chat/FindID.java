package com.example.chat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class FindID extends AppCompatActivity {

    Button btn;
    EditText emailet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        emailet = findViewById(R.id.input_email);
        btn = findViewById(R.id.btn);


        EditText autoFI = findViewById(R.id.input_email);

        autoFI.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btn.performClick();
                    return true;
                }
                return false;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                try {
                    String result;
                    String email = emailet.getText().toString();

                    AlertDialog.Builder builder = new AlertDialog.Builder(FindID.this);

                    if (email.length() == 0) {
                        //Toast.makeText(getApplicationContext(),"빈칸을 채우세요.",Toast.LENGTH_SHORT).show();
                        builder.setTitle("알 림").setMessage(" E-mail을 입력하세요. ").setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog alertDialog = builder.create();

                        alertDialog.show();

                        return;

                    }

                    FindIDActivity task = new FindIDActivity();
                    result = task.execute(email).get();
                    if (result.equals("ID찾기 실패")) {
                        builder.setTitle("알 림").setMessage("아이디 찾기 실패했습니다. \nE-mail을 확인하세요. ").setPositiveButton("닫기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        AlertDialog alertDialog = builder.create();

                        alertDialog.show();
                        return;
                    }

                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                    builder.setTitle("I D").setMessage(result).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();


                } catch (Exception e) {
                    Log.i("DBtest", ".....ERROR.....!");
                }
            }

        });


    }
}
