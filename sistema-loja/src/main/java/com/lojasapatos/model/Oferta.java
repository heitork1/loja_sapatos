package com.lojasapatos.model;

import java.math.BigDecimal;

public class Oferta {
    private Integer idPromocao;
    private Integer codigo;
    private BigDecimal desconto;

    public Oferta() {}
    public Oferta(Integer idPromocao, Integer codigo, BigDecimal desconto) {
        this.idPromocao = idPromocao;
        this.codigo = codigo;
        this.desconto = desconto;
    }

    public Integer getIdPromocao() { return idPromocao; }
    public void setIdPromocao(Integer idPromocao) { this.idPromocao = idPromocao; }
    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    @Override
    public String toString() {
        return "Oferta{idPromocao=" + idPromocao + ", codigo=" + codigo + ", desconto=" + desconto + "}";
    }
}
