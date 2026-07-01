package com.lojasapatos.model;

import java.time.LocalDate;

public class Promocao {
    private Integer idPromocao;
    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String categorias;

    public Promocao() {}
    public Promocao(String nome, LocalDate dataInicio, LocalDate dataFim, String categorias) {
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.categorias = categorias;
    }

    public Integer getIdPromocao() { return idPromocao; }
    public void setIdPromocao(Integer idPromocao) { this.idPromocao = idPromocao; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
    public String getCategorias() { return categorias; }
    public void setCategorias(String categorias) { this.categorias = categorias; }

    @Override
    public String toString() {
        return "Promocao{idPromocao=" + idPromocao + ", nome='" + nome + "', dataInicio=" + dataInicio +
               ", dataFim=" + dataFim + ", categorias='" + categorias + "'}";
    }
}
