package com.example.mitienda;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import com.google.android.gms.common.api.GoogleApiClient.Builder;


public class login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    //para google login
   private GoogleApiClient client;
   private  int GOOGLE_CODE=12345;
   //para le loadding
   ProgressDialog progress;
   private Context miRoot;

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

        miRoot = this;
        //para el google login
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();

        loadLoginGoogle();

    }
    //mi login
    public void login(View view) {

        final EditText email = findViewById(R.id.et_email);
        EditText password = findViewById(R.id.et_password);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email",email.getText().toString());
        params.add("password",password.getText().toString());

        client.post(utils.LOGIN_SERVICE, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                /** Show the loading dialog */
                progress = ProgressDialog.show(miRoot, "Accediendo",
                        "Cargando", true);
                //Toast.makeText(login.this,"Cargandoooo",Toast.LENGTH_LONG).show();
            }
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.dismiss();
               if(response.has("token")){
                   try {
                       utils.token = response.getString("token");

                       Toast.makeText(login.this,"Bienvenidoooo :)",Toast.LENGTH_LONG).show();
                       Intent home = new Intent(login.this, MainActivity.class);
                      // home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       //PendingIntent pendingIntent = PendingIntent.getActivity(root, 0, home, PendingIntent.FLAG_UPDATE_CURRENT);
                       //enviando el correo al main activiti
                       Bundle miBundle = new Bundle();
                       miBundle.putString("usuario", email.getText().toString());
                       home.putExtras(miBundle);

                       startActivity(home);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }

            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progress.dismiss();
                Toast.makeText(login.this,"Error verifique sus datos",Toast.LENGTH_LONG).show();
            }

        });
    }

    public void irAregistro(View view) {
        Intent login = new Intent(login.this, registrarUsuario.class);
        startActivity(login);
    }
    //login por google
    public void loadLoginGoogle(){
        SignInButton login_btn = (SignInButton)this.findViewById(R.id.btn_loginGoogle);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
                startActivityForResult(intent, GOOGLE_CODE);
                //client.connect();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()){
                Toast.makeText(this, "OK", Toast.LENGTH_LONG).show();
               // result.getSignInAccount().
            }else{
                Toast.makeText(this, "Error vuelva a intentarlo"+result, Toast.LENGTH_LONG).show();

            }
        }


    }

    //para google login metodo si algo sale mal
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
