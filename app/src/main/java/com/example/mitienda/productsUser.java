package com.example.mitienda;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class productsUser extends AppCompatActivity {

    private ListView LIST;
    private ArrayList<ItemListProductUser> LISTINFO;
    private CustomAdapterProductUser ADAPTER;
    private Context root;

    private  String miToken;
    private SharedPreferences preferencias;
    private ProgressBar loading;

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

        LIST = (ListView) this.findViewById(R.id.list_productUser);

        ADAPTER = new CustomAdapterProductUser(this, LISTINFO);
        LIST.setAdapter(ADAPTER);
        //registrando el evento para el click en el item
        //LIST.setOnItemClickListener(this);
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
                        String precio = itemJson.getString("precio");
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
}
