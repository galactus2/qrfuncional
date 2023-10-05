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

//implemetar el response y el error
public class ConsultarQr extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {


    private EditText txtId,txtNombre,txtApellidoPaterno,txtApellidoMaterno,txtEmpresa,txtCategoria,txtDocumento;
    private Button btnConsultarQr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_qr);

        //Crear union de

        txtId=findViewById(R.id.txtId);
        txtNombre=findViewById(R.id.txtNombre);
        txtApellidoPaterno=findViewById(R.id.txtApellidoPaterno);
        txtApellidoMaterno=findViewById(R.id.txtApellidoMaterno);
        txtEmpresa=findViewById(R.id.txtEmpresa);
        txtCategoria=findViewById(R.id.txtCategoria);
        txtDocumento=findViewById(R.id.txtDocumento);
        btnConsultarQr=findViewById(R.id.btnConsultarQr);


        //albre el qr y almacena data
        btnConsultarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator= new IntentIntegrator(ConsultarQr.this);
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

    //Almacenar la informacion que obtenenos atravez de un hashmap
    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getBaseContext(), "Se ha consultado correctamente", Toast.LENGTH_LONG).show();
        JSONArray json = response.optJSONArray("usuario");
        JSONObject jsonObject = null;

        try {
            jsonObject = json.getJSONObject(0);
            txtId.setText(jsonObject.optString("idAsistente"));
            txtNombre.setText(jsonObject.optString("nombre"));
            txtApellidoPaterno.setText(jsonObject.optString("apPaterno"));
            txtApellidoMaterno.setText(jsonObject.optString("apMaterno"));
            txtEmpresa.setText(jsonObject.optString("empresa"));
            txtCategoria.setText(jsonObject.optString("categoria"));
            txtDocumento.setText(jsonObject.optString("nroDoc"));






        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Dispara una funcion para consutar la data
    public void leerWsArray(String id) {

        String campoid=id;

        String url="https://latinsuppliesarchivos.000webhostapp.com/consultarValorID.php?idAsistente="+campoid;

        RequestQueue request;
        JsonObjectRequest jsonObjectRequest;
        request =  Volley.newRequestQueue(getBaseContext());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }


    //Leer la informacion del qr  y mandar la informacion a la funcion disparadora
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "Lectora Cancelada", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,    result.getContents(), Toast.LENGTH_SHORT).show();
                //txtResultado.setText(result.getContents());
                String id= result.getContents();
                leerWsArray(id);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}


