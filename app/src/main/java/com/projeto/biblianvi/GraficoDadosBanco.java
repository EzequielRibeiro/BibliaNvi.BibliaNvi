package com.projeto.biblianvi;

/**
 * Created by Ezequiel on 04/09/2015.
 */
public class GraficoDadosBanco {

    private int id;
    private String nomeLivro;
    private int totalVersoslidos;
    private int totalDeVersos;


    public GraficoDadosBanco() {
    }

    public int getTotalDeVersos() {
        return totalDeVersos;
    }

    public void setTotalDeVersos(int totalDeVersos) {
        this.totalDeVersos = totalDeVersos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeLivro() {
        return nomeLivro;
    }

    public void setNomeLivro(String nomeLivro) {
        this.nomeLivro = nomeLivro;
    }

    public int getTotalVersoslidos() {
        return totalVersoslidos;
    }

    public void setTotalVersoslidos(int totalVersoslidos) {
        this.totalVersoslidos = totalVersoslidos;
    }
}
