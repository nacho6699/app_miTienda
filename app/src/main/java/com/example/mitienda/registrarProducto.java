package com.example.mitienda;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.extras.Base64;

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
    //variable para guardar la ruta de mi img
    private String PATHIMAGE;
    private SharedPreferences preferencias;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //metodo para solicitar los permisos
        checkPermissionCameraAndStorage();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(registrarProducto.this,MainActivity.class);
                startActivity(home);
            }
        });

        miRoot = this;
        //recuperando el token del usuario para registrar el producto
        preferencias = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        token = preferencias.getString("token", "vacio");
        loadComponents();

    }
    //permisos camara y storage
    private void checkPermissionCameraAndStorage() {
        //si la version es menor a magmelo:) (antiguo)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            return;
        }
        if (this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED || this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED || this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            return;
        }else{
            //muestra la ventana para dar los permisos
            this.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
        //return;
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
    //para almacenar en memoria del movil
    private File createFile(){
        File file = new File(Environment.getExternalStorageDirectory(),"images/misimagenes");
        if (!file.exists()){
            file.mkdirs();
        }
        String nameImg="";
        if (file.exists()){
            nameImg = "IMG_"+System.currentTimeMillis()/1000+"_.jpg";
        }
        PATHIMAGE = file.getAbsolutePath() +File.separator+ nameImg;
        File newFile = new File(PATHIMAGE);
        return newFile;
    }
    private void loadCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //aumentando para el guardado de la img en el disposiivo
        File file = createFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //this.getApplicationContext().getPackageName();
            Uri url = FileProvider.getUriForFile(this,"com.example.mitienda.provider",file);
            camera.putExtra(MediaStore.EXTRA_OUTPUT,url);//guardando en memoria
        }else{
            //para versiones mas antiguas
            camera.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file));
        }

        this.startActivityForResult(camera,cod_camara);
    }
    //obteniendo el resultado de la carga de imagen desde galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //obteniendo foto desde galeria
        if (cod_gallery == requestCode){
            //verificando si se cancela la eleccion de la imagen
            if (data != null){
                Uri img = data.getData();
                //almacenado la ruta de la imagen
                PATHIMAGE = img.toString();
                //vistaPrevia.setImageURI(img);
                Glide.with(miRoot).load(img).into(vistaPrevia);
            }

        }
        //obteniendo resultados desde la camara
        if (cod_camara == requestCode){
            //if (data != null){
                //Bundle informacion = data.getExtras();
               // Bitmap img = (Bitmap) informacion.get("data");
               // vistaPrevia.setImageBitmap(img);

                //Bitmap img = BitmapFactory.decodeFile(PATHIMAGE);
                //vistaPrevia.setImageBitmap(img);

                //con glide
                Glide.with(miRoot).load(PATHIMAGE).into(vistaPrevia);
            //}
        }
    }

    //registrando producto
    public void registrarProducto(View view) throws FileNotFoundException {
        descripcion = findViewById(R.id.et_descripcion);
        precio = findViewById(R.id.et_precio);
        cantidad = findViewById(R.id.et_cantidad);
        foto = findViewById(R.id.btn_tomarFoto);

        //File file = new File(PATHIMAGE);

        RequestParams params = new RequestParams();
        params.put("descripcion",descripcion.getText().toString());
        params.put("precio",precio.getText().toString());
        params.put("cantidad",cantidad.getText().toString());
        params.put("register","");
        params.put("id_user","");
        //params.add("img", String.valueOf(file));
        //Bitmap img = BitmapFactory.decodeFile(PATHIMAGE);
        File file = new File(PATHIMAGE);
        params.put("img", file);

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", token);

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
                        //para limpiar la actividad anterior que es registro
                        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

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
