package com.example.voztick.voztick;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageButton mic ;
    int result;
    Socket socket;
    final int recordCode=0;
    private TextView command;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        command= (TextView) findViewById(R.id.commandText);
        mic=(ImageButton) findViewById(R.id.micBtn);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something");
                try {
                    startActivityForResult(i, recordCode);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(MainActivity.this,"Seu dispositivo não suporta a conversão de voz em texto",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case recordCode:
                if(resultCode==RESULT_OK && data!= null){
                    ArrayList<String> texto = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    command.setText(texto.get(0));
                    /*URL url;
                    HttpURLConnection urlConnection = null;
                    try {
                        url = new URL("http://192.168.4.1/led/on");
                        urlConnection = (HttpURLConnection) url
                                .openConnection();

                        InputStream in = urlConnection.getInputStream();

                        InputStreamReader isw = new InputStreamReader(in);

                        int dat = isw.read();
                        while (dat != -1) {
                            char current = (char) dat;
                            dat = isw.read();
                            System.out.print(current);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                    }*/
                    if(texto.get(0).equals("ligar 1")||texto.get(0).equals("ligar um"))
                        new GetMethodDemo().execute("http://192.168.4.1/led/red/on");
                   else if(texto.get(0).equals("desligar 1")||texto.get(0).equals("desligar um"))
                        new GetMethodDemo().execute("http://192.168.4.1/led/red/off");
                   else if(texto.get(0).equals("ligar 2")||texto.get(0).equals("ligar dois"))
                        new GetMethodDemo().execute("http://192.168.4.1/led/yellow/on");
                   else if(texto.get(0).equals("desligar 2")||texto.get(0).equals("desligar dois"))
                        new GetMethodDemo().execute("http://192.168.4.1/led/yellow/off");

                }
                break;

        }
    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }
}