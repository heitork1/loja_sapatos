package com.lojasapatos.model;

public class Armazenamento {
    private Integer idEstoque;
    private Integer codigoProduto;
    private Integer quantidade; // Quantos sapatos tem no estoque
    private Integer codigo;

    public Armazenamento(Integer idEstoque, Integer codigoProduto, Integer quantidade) {
        this.idEstoque = idEstoque;
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
    }

    public Armazenamento() {}

    public Integer getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(Integer idEstoque) {
        this.idEstoque = idEstoque;
    }

    public Integer getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Integer codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    @Override
    public String toString() {
        return "Armazenamento{idEstoque=" + idEstoque + ", codigo=" + codigo + ", quantidade=" + quantidade + "}";
    }
}
