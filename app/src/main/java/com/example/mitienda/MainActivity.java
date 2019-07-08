package com.example.mitienda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mitienda.listDataProducto.CustomAdapter;
import com.example.mitienda.listDataProducto.ItemList;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

   // private ListView LIST;
   private GridView LIST;
    private ArrayList<ItemList> LISTINFO;
    private CustomAdapter ADAPTER;
    private Context root;

    private TextView userEmail;
    private  String miToken;
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //verificar si ya inicio sesión

        //-----acceder al textview del nav
        View v = navigationView.getHeaderView(0);
        userEmail =(TextView) v.findViewById(R.id.tv_userEmail);

        Bundle intentExtras = this.getIntent().getExtras();
        //String datos = intentExtras.getString("usuario");

       //recuperando datos usuario y token del usuario logeado;
        preferencias = getSharedPreferences("shared_login_data",   Context.MODE_PRIVATE);
        String email = preferencias.getString("usuario", "vacio");
        miToken = preferencias.getString("token", "");

        //llamando inicioSession para verificar si existe el token y cerrar el acceso al mainActivity
        inicioSesion();
        userEmail.setText(email);

        //para la listwiew----adapter
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        root = this;
        LISTINFO = new ArrayList<ItemList>();
        String aux = null;
        loadInitRestData(aux);
        loadComponents();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //recuperando el valor del Edit view del nav bar
        MenuItem searchItem = menu.findItem(R.id.buscar_producto);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //ebvento para escuchar el cambio del texto
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                loadInitRestData(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //menu nav------------------------

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:{
                Intent home = new Intent(this, MainActivity.class);
                startActivity(home);
                break;
            }
            case R.id.nav_logout:{
                preferencias.edit().clear().commit();
                //preferencias.edit().remove("usuario");
                //preferencias.edit().commit();
                Intent login = new Intent(this,login.class);
                startActivity(login);
                break;
            }
            case R.id.nav_registro:{
                Intent register = new Intent(this,registrarUsuario.class);
                startActivity(register);
                break;
            }
            case R.id.nav_vender:{
                Intent registrarProducto = new Intent(this,registrarProducto.class);
                startActivity(registrarProducto);
                break;
            }
            case R.id.nav_citas:{

                break;
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //contenido de la view list -------------------Cargando los productos-----------------------------
    private void loadInitRestData(String key) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("buscar",key);
        //String url = "http://www.omdbapi.com/?s="+key+"&page=1&apikey=1e15062a";
        client.get(utils.LIST_PRODUCTOS_SERVICE, params, new JsonHttpResponseHandler(){
           /* @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray list= (JSONArray)response.get("Search");
                    LISTINFO.clear();
                    for (int i=0; i <= list.length();i++){
                        JSONObject itemJson = list.getJSONObject(i);
                        String imgPro = itemJson.getString("Poster");
                        String title = itemJson.getString("Title");
                        String precio = itemJson.getString("Year");
                        String id_pro = "b"+i;


                        ItemList item = new ItemList(imgPro, title, precio, id_pro);
                        LISTINFO.add(item);
                        ADAPTER.notifyDataSetChanged();
                    }
                    ADAPTER = new CustomAdapter(root,LISTINFO);
                    //LIST.setAdapter(ADAPTER);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }*/
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                LISTINFO.clear();
                for (int i=0; i <= response.length();i++){
                    JSONObject itemJson = null;
                    try {
                        itemJson = response.getJSONObject(i);
                        String imgPro = itemJson.getString("img");
                        String title = itemJson.getString("descripcion");
                        String precio = itemJson.getString("precio");
                        String id_pro = "b"+i;


                        ItemList item = new ItemList(imgPro, title, precio, id_pro);
                        LISTINFO.add(item);
                        ADAPTER.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ADAPTER.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadComponents() {
        //LIST = (ListView)this.findViewById(R.id.list_view_producto);
        LIST = (GridView) this.findViewById(R.id.list_view_producto);
        //para probar con un item
        //LISTINFO.add(new ItemList("https://images-na.ssl-images-amazon.com/images/M/MV5BMjA4MzAyNDE1MF5BMl5BanBnXkFtZTgwODQxMjU5MzE@._V1_SX300.jpg","Titanic","2019","Acción"));
        /*-------------escuchar el cambio de texto de un Edittext
        EditText search = (EditText)this.findViewById(R.id.et_buscar_producto);
        //evento al cambiar el texto
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                loadInitRestData(str);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
        //LISTINFO.add(new ItemList("https://images-na.ssl-images-amazon.com/images/M/MV5BMjA4MzAyNDE1MF5BMl5BanBnXkFtZTgwODQxMjU5MzE@._V1_SX300.jpg","Titanic","45bs","1"));
        ADAPTER = new CustomAdapter(this,LISTINFO);
        LIST.setAdapter(ADAPTER);
    }
    //verificar si ya tiene el token
    public  void inicioSesion(){

        if (miToken== ""){
            Intent login = new Intent(MainActivity.this, login.class);
            startActivity(login);
            return;
        }
    }


}
