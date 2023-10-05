package com.example.consumowebserviceqrfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConsultarDni extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener   {

    private EditText txtId2,txtNombre2,txtApellidoPaterno2,txtApellidoMaterno2,txtEmpresa2,txtCategoria2,txtDocumento2;
    private Button btnConsultarDni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_dni);

        txtId2=findViewById(R.id.txtId2);
        txtNombre2=findViewById(R.id.txtNombre2);
        txtApellidoPaterno2=findViewById(R.id.txtApellidoPaterno2);
        txtApellidoMaterno2=findViewById(R.id.txtApellidoMaterno2);
        txtEmpresa2=findViewById(R.id.txtEmpresa2);
        txtCategoria2=findViewById(R.id.txtCategoria2);
        txtDocumento2=findViewById(R.id.txtDocumento2);

        btnConsultarDni=findViewById(R.id.btnConsultarDni);


        btnConsultarDni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator= new IntentIntegrator(ConsultarDni.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - QrEventoGrupo10");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getBaseContext(), "Consulta incorrecta", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getBaseContext(), "Se ha consultado correctamente", Toast.LENGTH_LONG).show();
        JSONArray json = response.optJSONArray("usuario");
        JSONObject jsonObject = null;

        try {
            jsonObject = json.getJSONObject(0);
            txtId2.setText(jsonObject.optString("idAsistente"));
            txtNombre2.setText(jsonObject.optString("nombre"));
            txtApellidoPaterno2.setText(jsonObject.optString("apPaterno"));
            txtApellidoMaterno2.setText(jsonObject.optString("apMaterno"));
            txtEmpresa2.setText(jsonObject.optString("empresa"));
            txtCategoria2.setText(jsonObject.optString("categoria"));
            txtDocumento2.setText(jsonObject.optString("nroDoc"));






        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void leerWsDni(String dni){
        String campoDni=dni;

        String url="https://latinsuppliesarchivos.000webhostapp.com/consultarValorDNI.php?nroDoc="+campoDni;

        RequestQueue request;
        JsonObjectRequest jsonObjectRequest;
        request =  Volley.newRequestQueue(getBaseContext());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Lectora Cancelada", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,    result.getContents(), Toast.LENGTH_SHORT).show();
                String dni= result.getContents();
                leerWsDni(dni);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}