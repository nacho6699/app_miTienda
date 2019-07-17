package com.example.mitienda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mitienda.listDataProducto.CustomAdapter;
import com.example.mitienda.listDataProducto.ItemList;
import com.example.mitienda.listDataProductoUser.CustomAdapterProductUser;
import com.example.mitienda.listDataProductoUser.ItemListProductUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class productsUser extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView LIST;
    private ArrayList<ItemListProductUser> LISTINFO;
    private CustomAdapterProductUser ADAPTER;
    private Context root;

    private  String miToken;
    private SharedPreferences preferencias;
    private ProgressBar loading;
    private LinearLayout vistaPrevia;
    private TextView producto_borrar;
    private ImageView img_vista_previaProducto;

    private String id_pro;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_user);

        root = this;
        //recuperando datos usuario y token del usuario logeado;
        preferencias = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        miToken = preferencias.getString("token", "");

        LISTINFO = new ArrayList<ItemListProductUser>();
        loadComponents();
        loadProductsUser();
    }
    private void loadComponents() {

        //para la vista previa de borrado
        vistaPrevia = findViewById(R.id.layout_vista_previa);
        vistaPrevia.setVisibility(View.GONE);
        producto_borrar = findViewById(R.id.tv_productoAborrar);
        img_vista_previaProducto = findViewById(R.id.img_vista_previa_producto);

        LIST = (ListView) this.findViewById(R.id.list_productUser);

        ADAPTER = new CustomAdapterProductUser(this, LISTINFO);
        LIST.setAdapter(ADAPTER);
        //registrando el evento para el click en el item
        LIST.setOnItemClickListener(this);
    }
    //contenido de la view list -------------------Cargando los productos-----------------------------
    private void loadProductsUser() {
        loading = findViewById(R.id.progressBar3);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("Authorization", miToken);

        client.get(utils.LIST_PRODUCTOS_USER, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                loading.setVisibility(View.VISIBLE);
            }

            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                loading.setVisibility(View.GONE);
                LISTINFO.clear();
                for (int i = 0; i <= response.length(); i++) {
                    JSONObject itemJson = null;
                    try {
                        itemJson = response.getJSONObject(i);
                        String imgPro = utils.HOST + itemJson.getString("img");
                        String title = itemJson.getString("descripcion");
                        String precio = "Bs. "+itemJson.getString("precio");
                        String cantidad = itemJson.getString("cantidad");
                        String id_pro = itemJson.getString("_id");
                        //String id_user = itemJson.getString("id_user");

                        ItemListProductUser item = new ItemListProductUser(imgPro, title, precio, cantidad, id_pro);
                        LISTINFO.add(item);
                        ADAPTER.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ADAPTER.notifyDataSetChanged();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                loading.setVisibility(View.GONE);
                Toast.makeText(productsUser.this, "Error de servidor", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        id_pro = LISTINFO.get(position).getId_pro();
        String sendDescriocion = LISTINFO.get(position).getTitle();
        String sendImg = LISTINFO.get(position).getImgPro();

        Glide.with(root).load(sendImg).placeholder(R.drawable.img_default).transition(withCrossFade()).into(img_vista_previaProducto);
        producto_borrar.setText(sendDescriocion);
        vistaPrevia.setVisibility(View.VISIBLE);
        //Toast.makeText(this,">>>"+id_pro,Toast.LENGTH_LONG).show();
    }


   //evento borrar producto a partir del id obtenido

    public void borrarProducto(View view) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.addHeader("Authorization", miToken);
        params.add("idPro",id_pro);
        client.delete(utils.DELETE_PRODUCTOS_USER,params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                progress = ProgressDialog.show(root, "Borrando",
                        "Un momento porfavor", true);
            }

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.dismiss();
                Intent products_user = new Intent(productsUser.this,productsUser.class);
                startActivity(products_user);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progress.dismiss();
                loading.setVisibility(View.GONE);
                Toast.makeText(productsUser.this, "Error de servidor", Toast.LENGTH_LONG).show();
            }
        });
    }
}
