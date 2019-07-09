package com.example.mitienda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class registrarProducto extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progress;
    protected Context miRoot;
    TextView descripcion;
    TextView precio;
    TextView cantidad;
    ImageButton foto;
    Button registrarProducto;

    //para la camara
    private int cod_gallery=10;
    private int cod_camara=20;
    ImageView vistaPrevia;

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
        loadComponents();

    }

    private void loadComponents() {
        ImageButton irGaleria = findViewById(R.id.btn_irGaleria);
        ImageButton tomarFoto = findViewById(R.id.btn_tomarFoto);
        vistaPrevia = findViewById(R.id.vista_previa_foto);

        irGaleria.setOnClickListener(this);
        tomarFoto.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_irGaleria:{
                loadGallery();
                break;
            }
            case R.id.btn_tomarFoto:{
                loadCamera();
                break;
            }
        }
    }

    private void loadGallery() {
        Intent files = new Intent(Intent.ACTION_PICK);
        files.setType("image/");
        this.startActivityForResult(files,cod_gallery);
    }
    private void loadCamera() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (cod_gallery == requestCode){
            //verificando si se cancela la eleccion de la imagen
            if (data != null){
                Uri img = data.getData();
                vistaPrevia.setImageURI(img);
            }

        }
    }

    //registrando producto
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
