package com.example.mitienda.listDataProducto;

public class ItemList {
    private String imgPro;
    private String title;
    private String precio;
    private String id_pro;
    public ItemList(String imgPro, String title, String precio, String id_pro){
        this.imgPro=imgPro;
        this.title=title;
        this.precio=precio;
        this.id_pro=id_pro;
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

    public void setPrecio(String year) {
        this.precio = year;
    }

    public String getId_pro() {
        return this.id_pro;
    }

    public void setId_pro(String type) {
        this.id_pro = type;
    }


}
