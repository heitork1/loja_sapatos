package com.lojasapatos.model;

public class Promocao {
    private Integer idPromocao;
    private String categorias;
    private String nome;
    private String periodoVigencia;

    public Promocao(Integer idPromocao, String categorias, String nome, String periodoVigencia) {
        this.idPromocao = idPromocao;
        this.categorias = categorias;
        this.nome = nome;
        this.periodoVigencia = periodoVigencia;
    }

    public Integer getIdPromocao() {
        return idPromocao;
    }

    public void setIdPromocao(Integer idPromocao) {
        this.idPromocao = idPromocao;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPeriodoVigencia() {
        return periodoVigencia;
    }

    public void setPeriodoVigencia(String periodoVigencia) {
        this.periodoVigencia = periodoVigencia;
    }

    public Promocao() {}
}