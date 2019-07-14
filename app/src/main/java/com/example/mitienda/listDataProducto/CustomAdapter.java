package com.example.mitienda.listDataProducto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.mitienda.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CustomAdapter extends BaseAdapter {

    private Context CONTEXT;
    private ArrayList<ItemList> LIST;
    public CustomAdapter(Context context, ArrayList<ItemList> list){
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
            convertView = inflate.inflate(R.layout.item_layout_producto, null);
        }
        //recuperando de item_layout
        TextView title = (TextView)convertView.findViewById(R.id.tv_titulo);
        TextView precio = (TextView)convertView.findViewById(R.id.tv_precio);
       // TextView type = (TextView)convertView.findViewById(R.id.tv_tipo);
        //rellenado los textos
        title.setText(this.LIST.get(position).getTitle());
        precio.setText(this.LIST.get(position).getPrecio());
       // type.setText(this.LIST.get(position).getType());
        //poniendo la imagen pero falta el hilo
        ImageView img = (ImageView)convertView.findViewById(R.id.img_producto);
        Glide.with(CONTEXT).load(LIST.get(position).getImgPro()).placeholder(R.drawable.img_default).transition(withCrossFade()).into(img);
        /*try {
            URL url=new URL(this.LIST.get(position).getImgPro());
            InputStream stream =url.openConnection().getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(stream);
            img.setImageBitmap(imageBitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return convertView;
    }
}
