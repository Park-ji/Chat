package com.example.chat;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

public class Ccc {



    Socket socket;
    AppCompatActivity ma;
    final TextView textView;
    static private String serverIP;
    private ConnectionEnum ce = ConnectionEnum.ServerIP;

    public static void setMyName(String myName) {
        Ccc.myName = myName;
    }

    private static String myName = "defaultName";

    public Ccc(AppCompatActivity chatActivity) {
        ma = chatActivity;
        textView = ma.findViewById(R.id.textView11);
        serverIP = ce.getIp();
    }

    void start() {
        if(socket != null && socket.isConnected()){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Runnable rr = () -> {

            try {

                socket = new Socket();
                socket.connect(new InetSocketAddress(serverIP, 9000));
                makeToast( "서버 - "+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+" 접속");

            } catch (IOException e) {
                e.printStackTrace();
            }
            recieve();
        };
        Thread t = new Thread(rr);
        t.start();
    }

    /**
     *
     */
    private void recieve() {
    boolean isend = false;
        while (!isend) {

            try {
                InputStream bufReader = (socket.getInputStream());

                byte[] bb = new byte[1024];
                int readByteSize = bufReader.read(bb);

                String message;
                message = new String(bb, 0, readByteSize, "UTF-8");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 사용하고자 하는 코드
                        textView.append("\n"+ message);
                        final int scrollAmount = textView.getLayout().getLineTop(textView.getLineCount()) - textView.getHeight();
                        // if there is no need to scroll, scrollAmount will be <=0
                        if (scrollAmount > 0)
                            textView.scrollTo(0, scrollAmount);
                        else
                            textView.scrollTo(0, 0);
                    }
                }, 0);


            } catch (Exception e) {
                e.printStackTrace();
                textView.setText(textView.getText() + "\n서버끊어짐");
                try {
                    socket.close();
                    isend= true;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    public void send(String msg) {
        Runnable rr = () -> {

            try {
    if(socket == null){
        makeToast("접속 먼저 하세요");
    }
                // 서버 접속


                // Server에 보낼 데이터

                OutputStream bufWriter = (socket.getOutputStream());

                bufWriter.write((myName+" : "+msg).getBytes("UTF-8"));

                bufWriter.flush();

            } catch (Exception e) {

                e.printStackTrace();

            }
        };
        Thread t = new Thread(rr);
        t.start();
    }

    public void sendPhoto(File img) {
        Runnable rr;
        rr = () -> {
            try {
                Socket pSocket = new Socket();

                pSocket.connect(new InetSocketAddress(serverIP,7777));
                if(pSocket == null){
                    makeToast("접속 먼저 하세요");
                }
                // 서버 접속

                // Server에 보낼 데이터

                File file = img;
                if (!file.exists()) {
                    System.out.println("File not Exist.");
                    System.exit(0);
                }

                BufferedOutputStream toServer = new BufferedOutputStream(pSocket.getOutputStream());
                DataOutputStream dos = new DataOutputStream(pSocket.getOutputStream());
                dos.writeUTF(new String("up".getBytes(), "UTF-8"));
                dos.writeUTF(new String(myName.getBytes(), "UTF-8"));
                dos.writeUTF(new String(file.getName().getBytes(), "UTF-8"));
                dos.writeUTF("" + file.length());

                OutputStream outputStream = pSocket.getOutputStream();

                FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fileInputStream);

                byte[] dataBuff = new byte[(int) file.length()];
                int length = fileInputStream.read(dataBuff);
                while (length != -1) {
                    outputStream.write(dataBuff, 0, length);
                    length = fileInputStream.read(dataBuff);
                }
                System.out.println("전송 성공");

                byte[] buf = new byte[4096]; //buf 생성합니다.
                int theByte = 0;
                while ((theByte = bis.read(buf)) != -1) // BufferedInputStream으로
                {
                    toServer.write(buf,0,theByte);
                }

                toServer.flush();
                toServer.close();
                bis.close();
                fileInputStream.close();
                pSocket.close();
            } catch (Exception e) {

                e.printStackTrace();

            }
        };
        Thread t = new Thread(rr);
        t.start();
    }

    public void recievePhoto(String fileName) {
        Runnable rr;
        rr = () -> {
            try {
                Socket pSocket = new Socket();

                pSocket.connect(new InetSocketAddress(serverIP,7777));
                if(pSocket == null){
                    makeToast("접속 먼저 하세요");
                }
                // 서버 접속

                // Server에 보낼 데이터
                final String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
                String folder = LOCAL_PATH + "/downloads";
                BufferedOutputStream toServer = new BufferedOutputStream(pSocket.getOutputStream());
                DataOutputStream dos = new DataOutputStream(pSocket.getOutputStream());
                dos.writeUTF(new String("down".getBytes(), "UTF-8"));
                dos.writeUTF(new String(fileName.getBytes(), "UTF-8"));


                BufferedInputStream up = new BufferedInputStream(pSocket.getInputStream());
                DataInputStream fromClient = new DataInputStream(up);
                String filename = fromClient.readUTF();
                int filesize = Integer.parseInt(fromClient.readUTF());

                System.out.println(filename + "\t을 받습니다.");

                // client단에서 전송되는 file 내용을 server단에 생성시킨 file에 write할수 있는 stream
                File newfile = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS+"/testDown");
                if (!newfile.exists()) {
                    newfile.mkdir();
                }
                System.out.println(newfile.getCanonicalPath() + "/" + filename);
                FileOutputStream toFile = new FileOutputStream(newfile.getCanonicalPath() + "/" + filename);
                BufferedOutputStream outFile = new BufferedOutputStream(toFile);
                System.out.println((filename + " " + filesize));
                byte[] bb = new byte[filesize];
                int ch = 0;
                while ((ch = up.read()) != -1) {
                    outFile.write(ch);
                }

                makeToast(filename + "을(를) 받았습니다");
                outFile.flush();
                outFile.close();
                pSocket.close();
            } catch (Exception e) {

                e.printStackTrace();

            }
        };
        Thread t = new Thread(rr);
        t.start();
    }

    private void makeToast(String tm) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ma,tm,Toast.LENGTH_SHORT).show();;
            }
        }, 0);
    }


    public static class requestImgList extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg = "사진 없음";

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://"+serverIP+":8080/ChatTest/ImageList.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoOutput(false);
                String str;
                String[] imgList;
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();

                    // jsp에서 보낸 값을 받는 부분
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    if(buffer.length()!=0){
                        receiveMsg= buffer.toString();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //jsp로부터 받은 리턴 값
            return receiveMsg;
        }


    }

//http://localhost:8080/Serrrverrr/ImageList.jsp


}

