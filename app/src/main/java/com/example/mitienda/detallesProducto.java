package com.example.mitienda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class detallesProducto extends AppCompatActivity {

    private ImageView visorImg;
    private TextView descripcion;
    private TextView precio;
    private TextView cantidad;
    private TextView id_pro;
    private TextView referencia_email;
    private TextView referencia_celular;

    private Context miContexto;

    private  String miToken;
    private SharedPreferences preferencias;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);
        //recuperando el token para la consulta de datos de usuario
        preferencias = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        miToken = preferencias.getString("token", "");

        miContexto = this;
        loadComponentsDetalles();
    }

    private void loadComponentsDetalles() {
        visorImg = findViewById(R.id.imageProducto);
        descripcion = findViewById(R.id.tv_descripcion);
        precio = findViewById(R.id.tv_precio);
        cantidad = findViewById(R.id.tv_cantidad);
        referencia_email = findViewById(R.id.tv_email);
        referencia_celular = findViewById(R.id.tv_celular);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String img = b.getString("img");
        String R_descripcion =b.getString("descripcion");
        String R_precio = b.getString("precio");
        String R_cantidad = b.getString("cantidad");
        String R_id_pro = b.getString("id_pro");
        String id_user = b.getString("id_user");
        if (b != null){
            Glide.with(miContexto).load(img).placeholder(R.drawable.img_default).transition(withCrossFade()).into(visorImg);
            descripcion.setText(R_descripcion);
            precio.setText(R_precio);
            cantidad.setText(R_cantidad);

        }

        //consultando los datos de los usurios para la referencia
        loading = findViewById(R.id.progressBar2);

        RequestParams params = new RequestParams();
        params.put("id",id_user);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", miToken);

        client.get(utils.LIST_USERS, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                loading.setVisibility(View.VISIBLE);
            }
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                loading.setVisibility(View.GONE);
                JSONObject itemJson = null;
                try {
                    itemJson = response.getJSONObject(0);
                    String email =itemJson.getString("email");
                    String celular = itemJson.getString("celular");
                    //cargando los datos recuperados
                    referencia_email.setText(email);
                    referencia_celular.setText(celular);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                loading.setVisibility(View.GONE);
                Toast.makeText(detallesProducto.this,"Error de servidor",Toast.LENGTH_LONG).show();
            }

        });

    }
}
