package com.example.mitienda;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

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
            referencia_email.setText(id_user);

        }
    }
}
