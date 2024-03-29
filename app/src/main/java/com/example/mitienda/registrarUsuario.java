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
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class registrarUsuario extends AppCompatActivity {
    private Context root;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        root=this;

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(registrarUsuario.this,login.class);
                startActivity(login);
            }
        });
    }


    public void registrarUsuario(View view) {
        Snackbar.make(view, "Registrando", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        setData();
    }

    public void setData(){
        EditText username = findViewById(R.id.et_nombre);
        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);
        EditText celular = findViewById(R.id.et_celular);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_name",username.getText().toString());
        params.add("email",email.getText().toString());
        params.add("password",password.getText().toString());
        params.add("celular",celular.getText().toString());

        client.post(utils.REGISTER_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                /** Show the loading dialog */
                progress = ProgressDialog.show(root, "Registrando Usuario",
                        "Un momento porfavor", true);
                //Toast.makeText(login.this,"Cargandoooo",Toast.LENGTH_LONG).show();
            }
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.dismiss();
                if(response.has("roles")){
                    Toast.makeText(registrarUsuario.this,"Registro exitoso",Toast.LENGTH_LONG).show();
                    Intent login = new Intent(registrarUsuario.this, login.class);
                    startActivity(login);
                }
            }
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progress.dismiss();
                Toast.makeText(registrarUsuario.this,"Error verifique sus datos",Toast.LENGTH_LONG).show();
            }

        });
    }
}
