package com.projeto.biblianvi;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Ezequiel on 08/07/2015.
 */
public class Biblia {

    private SharedPreferences prefs = null;
    private Context context = null;
    private String[] termoBusca;
    private int id = 0;
    private int idBook = 0;
    private int totalDeVersosLidos;
    private int totalDeVersiculos;
    private String titleCapitulo = "";
    private String testamentName = "";
    private String bookVersion = "";
    private String booksName = "";
    private String versesChapter = "";
    private String versesNum = "";
    private String text = "";
    private int lido = 0;
    private String idVerse = "";

    public Biblia() {
    }


    public String[] getTermoBusca() {
        return termoBusca;
    }

    public void setTermoBusca(String termoBusca) {

        this.termoBusca = termoBusca.split("%");

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
        return booksName;
    }

    public void setBooksName(String b) {
        booksName = b;
    }

    public String getChapter() {
        return versesChapter;
    }

    public void setChapter(String c) {

        versesChapter = c;
    }

    public String getVersesNum() {
        return versesNum;

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
        return text;
    }

    public void setText(String t) {
        text = t;
    }

    public String toPesquisarString() {

        for (String t : getTermoBusca()) {

            if (t.length() > 1) {
                t = t.toLowerCase();
                setText(getText().replace(t, "<font color=\"red\">" + t + "</font>"));
                t = t.substring(0, 1).toUpperCase() + t.substring(1);
                setText(getText().replace(t, "<font color=\"red\">" + t + "</font>"));
                // setText(StringUtils.replaceIgnoreCase(getText(), t, "<font color=\"red\">" + t + "</font>"));
                // setText(getText().replace(t, "<font color=\"red\">" + t + "</font>"));
            }

        }
        return "<b>" + booksName + " " + versesChapter + ":" + versesNum + "</b><br>" + getText();
    }

}
