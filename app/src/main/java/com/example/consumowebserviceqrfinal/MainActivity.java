package com.example.consumowebserviceqrfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void CosultarQr(View v){
        Intent consultarqr = new Intent(this,ConsultarQr.class);
        startActivity(consultarqr);
    }

    public void ConsultarDni(View v){
        Intent consultardni = new Intent(this,ConsultarDni.class);
        startActivity(consultardni);
    }
}