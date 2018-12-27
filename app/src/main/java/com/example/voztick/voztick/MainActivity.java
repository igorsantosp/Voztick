package com.example.voztick.voztick;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
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
    Button bluetoothBtn;
    Socket socket;
    final int recordCode=0;
    private TextView command;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        command= (TextView) findViewById(R.id.commandText);
        mic=(ImageButton) findViewById(R.id.micBtn);
        final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        bluetoothBtn= (Button) findViewById(R.id.bluetoothBtn);
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS , 1);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE,true);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Fale");
        i.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    //command.setText(texto.get(0));
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
                    if(texto.get(0).contains("ula")){
                        new GetMethodDemo().execute("http://192.168.4.1/p");
                        command.setText(texto.get(0)+" - Comando 1");}
                   else if(texto.get(0).contains("aix")){
                        new GetMethodDemo().execute("http://192.168.4.1/a");
                        command.setText(texto.get(0)+" - Comando 2");
                    }
                   else if(texto.get(0).contains("esquerd")){
                        new GetMethodDemo().execute("http://192.168.4.1/e");
                        command.setText(texto.get(0)+" - Comando 3");
                    }
                   else if(texto.get(0).contains("direit")){
                        new GetMethodDemo().execute("http://192.168.4.1/d");
                        command.setText(texto.get(0)+" - Comando 4");
                    }

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