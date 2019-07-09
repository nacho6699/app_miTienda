package com.example.mitienda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class registrarProducto extends AppCompatActivity {

    ProgressDialog progress;
    protected Context miRoot;
    TextView descripcion;
    TextView precio;
    TextView cantidad;
    ImageButton foto;
    Button registrarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        miRoot = this;

    }


    public void registrarProducto(View view) {
        descripcion = findViewById(R.id.et_descripcion);
        precio = findViewById(R.id.et_precio);
        cantidad = findViewById(R.id.et_cantidad);
        foto = findViewById(R.id.btn_tomarFoto);

        RequestParams params = new RequestParams();
        params.add("descripcion",descripcion.getText().toString());
        params.add("precio",precio.getText().toString());
        params.add("cantidad",cantidad.getText().toString());
        params.add("img", "la imagen");

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", utils.token);

        client.post(utils.REGISTER_PRODUCTOS_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                /** Show the loading dialog */
                progress = ProgressDialog.show(miRoot, "Registrando",
                        "Cargando", true);
                //Toast.makeText(login.this,"Cargandoooo",Toast.LENGTH_LONG).show();
            }
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.dismiss();
                    try {

                        Toast.makeText(registrarProducto.this,"Registro Exitoso : "+response.getString("descripcion") ,Toast.LENGTH_LONG).show();
                        Intent home = new Intent(registrarProducto.this, MainActivity.class);

                        startActivity(home);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progress.dismiss();
                Toast.makeText(registrarProducto.this,"Error registro de producto",Toast.LENGTH_LONG).show();
            }

        });
    }
}
