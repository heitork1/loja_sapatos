package com.lojasapatos.model;

public class Venda {
    private Integer idVenda;
    private String formaPagamento;
    private Integer idFilial;
    private Integer idFuncionario;
    private Integer numParcelas;

    public Venda(Integer idVenda, String formaPagamento, Integer idFilial, Integer idFuncionario, Integer numParcelas) {
        this.idVenda = idVenda;
        this.formaPagamento = formaPagamento;
        this.idFilial = idFilial;
        this.idFuncionario = idFuncionario;
        this.numParcelas = numParcelas;
    }

    public Integer getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(Integer idVenda) {
        this.idVenda = idVenda;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Venda() {}
}