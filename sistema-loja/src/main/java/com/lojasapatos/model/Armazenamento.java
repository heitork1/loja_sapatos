package com.lojasapatos.model;

public class Armazenamento {
    private Integer idEstoque;
    private Integer quantidade; // Quantos sapatos tem no estoque
    private Integer codigo;

    public Armazenamento(Integer idEstoque, Integer codigo, Integer quantidade) {
        this.idEstoque = idEstoque;
        this.codigoProduto = codigo;
        this.quantidade = quantidade;
    }

    public Armazenamento() {}

    public Integer getIdEstoque()  { return idEstoque; }
    public void setIdEstoque(Integer v) { this.idEstoque = v; }

    public Integer getCodigo()     { return codigo; }
    public void setCodigo(Integer v) { this.codigo = v; }

    public Integer getCodigoProduto() { return codigo; }
    public void setCodigoProduto(Integer v) { this.codigo = v; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer v) { this.quantidade = v; }

    @Override
    public String toString() {
        return "Armazenamento{idEstoque=" + idEstoque + ", codigo=" + codigo
                + ", quantidade=" + quantidade + "}";
    }
}
