package com.projeto.biblianvi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Ezequiel on 08/07/2015.
 */
public class Biblia {

    private SharedPreferences prefs = null;
    private Context context = null;
    private String termoBusca;
    private int id;
    private int idBook;
    private int totalDeVersosLidos;
    private int totalDeVersiculos;
    private String titleCapitulo;
    private String testamentName;
    private String bookVersion;
    private String booksName = " ";
    private String versesChapter = " ";
    private String versesNum = " ";
    private String text = " ";
    private int lido;
    private String idVerse;

    public Biblia() {


    }

    public String getTitleCapitulo() {
        return titleCapitulo;
    }

    public void setTitleCapitulo(String titleCapitulo) {
        this.titleCapitulo = titleCapitulo;
    }

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public String getBookVersion() {
        return bookVersion;
    }

    public void setBookVersion(String bookVersion) {
        this.bookVersion = bookVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalDeVersosLidos() {
        return totalDeVersosLidos;
    }

    public void setTotalDeVersosLidos(int totalDeVersosLidos) {
        this.totalDeVersosLidos = totalDeVersosLidos;
    }

    public int getTotalDeVersiculos() {
        return totalDeVersiculos;
    }

    public void setTotalDeVersiculos(int totalDeVersiculos) {
        this.totalDeVersiculos = totalDeVersiculos;
    }

    public void setVerseNum(String i) {

        versesNum = i;
    }

    public int getLido() {

        return lido;

    }

    public void setLido(int lido) {

        this.lido = lido;
    }

    public String getTestamentName() {
        return testamentName;
    }

    public void setTestamentName(String n) {

        testamentName = n;
    }

    public String getIdVerse() {

        return idVerse;
    }

    public void setIdVerse(String i) {

        idVerse = i;

    }

    public String getBooksName() {

        if (booksName != null)
            return booksName;
        else
            return "0";
    }

    public void setBooksName(String b) {

        booksName = b;

    }

    public String getChapter() {
        if (versesChapter != null)
            return versesChapter;
        else
            return "0";
    }

    public void setChapter(String c) {

        versesChapter = c;
    }

    public String getVersesNum() {

        if (versesNum != null)
            return versesNum;
        else
            return "0";

    }

    @Override
    public String toString() {

        String txt = "<font color='XXX'><b>" + versesNum + "</b></font>" + " " + text;

        if (getTitleCapitulo() != null)
            txt = "<font color='blue'>" + getTitleCapitulo() + "</font><br>" +
                    txt;

        if (getLido() == 1)
            return txt.replace("XXX", "green");
        else
            return txt;


    }

    public String getText() {

        if (text != null)
            return text;
        else
            return "0";
    }

    public void setText(String t) {

        text = t.replace(";", "");

    }

    public String toPesquisarString() {

        String texto = getText();

        //for more one term
        String[] temp = termoBusca.split(" ");
        if (temp.length > 1) {

            for (int i = 0; i < temp.length; i++) {

                texto = texto.replace(temp[i], "<font color=\"red\">" + temp[i] + "</font>");
                Log.e("termo: ", temp[i]);
                Log.e("text", texto);
            }

        } else {
            texto = getText().replace(termoBusca, "<font color=\"red\">" + termoBusca + "</font>");
        }

        Log.e("text: ", texto);
        return "<p>" + booksName + " " + versesChapter + ":" + versesNum + "</p>" +
                "<p>" + texto + "</p>";
    }

    public void setContext(Context context) {
        this.context = context;

        if (context != null) {

            prefs = context.getSharedPreferences("termo_busca", Activity.MODE_PRIVATE);
            termoBusca = prefs.getString("busca", "a");
        }

    }
}
