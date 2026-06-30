package com.lojasapatos.model;

import java.math.BigDecimal;

public class ItemVenda {
    private Integer idVenda;
    private Integer codigo; // código do produto
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public ItemVenda() {}
    public ItemVenda(Integer idVenda, Integer codigo, Integer quantidade, BigDecimal precoUnitario) {
        this.idVenda = idVenda;
        this.codigo = codigo;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Integer getIdVenda() { return idVenda; }
    public void setIdVenda(Integer idVenda) { this.idVenda = idVenda; }
    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    @Override
    public String toString() {
        return "ItemVenda{idVenda=" + idVenda + ", codigo=" + codigo + ", quantidade=" + quantidade +
               ", precoUnitario=" + precoUnitario + "}";
    }
}
