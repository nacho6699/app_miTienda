package com.example.mitienda;

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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
    }

    public void login(View view) {

        EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email",email.getText().toString());
        params.add("password",password.getText().toString());
       // Toast.makeText(login.this,email.getText().toString(),Toast.LENGTH_LONG).show();
        client.post(utils.LOGIN_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                /** Show the loading dialog */
                Toast.makeText(login.this,"Cargandoooo",Toast.LENGTH_LONG).show();
            }
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               if(response.has("token")){
                   try {
                       utils.token = response.getString("token");
                       Toast.makeText(login.this,"Bienvenidoooo :)",Toast.LENGTH_LONG).show();
                       Intent home = new Intent(login.this, MainActivity.class);
                       startActivity(home);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(login.this,"Error verifique sus datos",Toast.LENGTH_LONG).show();
            }

        });
    }

    public void irAregistro(View view) {
        Intent login = new Intent(login.this, registrarUsuario.class);
        startActivity(login);
    }
}
