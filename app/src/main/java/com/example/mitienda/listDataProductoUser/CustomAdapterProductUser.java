package com.example.mitienda.listDataProductoUser;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mitienda.R;
import com.example.mitienda.listDataProducto.ItemList;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CustomAdapterProductUser extends BaseAdapter {

    private Context CONTEXT;
    private ArrayList<ItemListProductUser> LIST;
    public CustomAdapterProductUser(Context context, ArrayList<ItemListProductUser> list){
        this.CONTEXT=context;
        this.LIST=list;
    }
    @Override
    public int getCount() {
        return this.LIST.size();
    }

    @Override
    public Object getItem(int position) {
        return this.LIST.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflate = (LayoutInflater) this.CONTEXT.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflate.inflate(R.layout.item_layout_producto_user, null);
        }
        //recuperando de item_layout
        TextView title = (TextView)convertView.findViewById(R.id.tv_title_producto);
        TextView precio = (TextView)convertView.findViewById(R.id.tv_precio_pruducto);
        TextView cantidad = (TextView)convertView.findViewById(R.id.tv_cantidad_producto);
        //rellenado los textos
        title.setText(this.LIST.get(position).getTitle());
        precio.setText(this.LIST.get(position).getPrecio());
        cantidad.setText(this.LIST.get(position).getCantidad());
        //poniendo la imagen pero falta el hilo
        ImageView img = (ImageView)convertView.findViewById(R.id.img_productoUser);
        Glide.with(CONTEXT).load(LIST.get(position).getImgPro()).placeholder(R.drawable.img_default).transition(withCrossFade()).into(img);

        return convertView;
    }
}

