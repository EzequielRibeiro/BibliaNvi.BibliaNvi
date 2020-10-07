package com.projeto.biblianvi;

/**
 * Created by Ezequiel on 27/04/2016.
 */
public class Anotacao {


    private int id = 0;
    private String titulo = " ";
    private String texto = " ";
    private String data = " ";

    public String getId() {
        return Integer.toString(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
