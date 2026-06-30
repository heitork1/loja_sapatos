package com.lojasapatos.model;

public class Modelo {
    private Integer codigo;
    private String cor;
    private Integer numero; //do calçado
    private String categoriaModelo;

    public Modelo(Integer codigo, String cor, Integer numero, String categoriaModelo) {
        this.codigo = codigo;
        this.cor = cor;
        this.numero = numero;
        this.categoriaModelo = categoriaModelo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCategoriaModelo() {
        return categoriaModelo;
    }

    public void setCategoriaModelo(String categoriaModelo) {
        this.categoriaModelo = categoriaModelo;
    }

    public Modelo() {}
}