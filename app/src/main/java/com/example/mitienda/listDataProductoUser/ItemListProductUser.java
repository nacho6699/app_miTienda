package com.example.mitienda.listDataProductoUser;

public class ItemListProductUser {
    private String imgPro;
    private String title;
    private String precio;
    private String id_pro;
    private String cantidad;
   // private String id_user;
    public ItemListProductUser(String imgPro, String title, String precio,String cantidad, String id_pro){
        this.imgPro=imgPro;
        this.title=title;
        this.precio=precio;
        this.id_pro=id_pro;
        this.cantidad = cantidad;
        //this.id_user = id_user;
    }

    public String getImgPro() {
        return this.imgPro;
    }

    public void setImgPro(String poster) {
        this.imgPro = poster;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrecio() {
        return this.precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getId_pro() {
        return this.id_pro;
    }

    public void setId_pro(String id_pro) {
        this.id_pro = id_pro;
    }

    public String getCantidad() {
        return this.cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }


}
