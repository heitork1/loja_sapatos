package com.lojasapatos.model;

public class Estoque {
    private Integer idEstoque;
    private Integer quantidadeMinima;

    public Estoque(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
    public Estoque(Integer idEstoque, Integer quantidadeMinima) {
        this.idEstoque = idEstoque;
        this.quantidadeMinima = quantidadeMinima;
    }

    public Integer getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(Integer idEstoque) {
        this.idEstoque = idEstoque;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }

    @Override
    public String toString() {
        return "Estoque{idEstoque=" + idEstoque + ", quantidadeMinima=" + quantidadeMinima + "}";
    }
    
    public Estoque() {}
}
