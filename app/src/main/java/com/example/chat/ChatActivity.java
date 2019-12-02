package com.example.chat;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class ChatActivity extends AppCompatActivity {
    Ccc ccc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatt);
        ccc = new Ccc(ChatActivity.this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
//참조 : http://naminsik.com/blog/3662


        final TextView textView = findViewById(R.id.textView11);
        final TextInputEditText extView = findViewById(R.id.textInputEditText23);
        Button btn = findViewById(R.id.sendButton);
        Button imgPick = findViewById(R.id.pickImage);

        textView.setMovementMethod(new ScrollingMovementMethod());

        extView.requestFocus();
        ccc.start();

        Button imgDD = findViewById(R.id.down);
        imgDD.setOnClickListener((View v) -> {
            String list;
            try {
                Ccc.requestImgList cl = new Ccc.requestImgList();
                list = cl.execute("").get();
                Intent intent = new Intent(getApplicationContext(), ImgDown.class);
                intent.putExtra("lintname", list);
                startActivity(intent);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        imgPick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 200);
            }
        });

        textView.post(() -> {
            int lineTop = textView.getLayout().getLineTop(textView.getLineCount());
            int scrollY = lineTop - textView.getHeight();
            if (scrollY > 0) {
                textView.scrollTo(0, scrollY);
            } else {
                textView.scrollTo(0, 0);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String aa = textView.getText().toString() + "\n" + extView.getText();
                // textView.setText(aa);


                ccc.send(extView.getText().toString());
                extView.setText("");
            }

        });


        extView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    btn.performClick();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 200 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri photoUri = data.getData();
            Cursor cursor = null;
            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                File tempFile = new File(cursor.getString(column_index));
                ccc.sendPhoto(tempFile);

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }

    }
}
