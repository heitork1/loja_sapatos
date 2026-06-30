package com.lojasapatos.model;

public class Filial {
    private Integer idFilial;
    private Integer idEstoque; // FK
    private Integer idSede;    // FK

    public Filial(Integer idFilial, Integer idEstoque, Integer idSede) {
        this.idFilial = idFilial;
        this.idEstoque = idEstoque;
        this.idSede = idSede;
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public Integer getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(Integer idEstoque) {
        this.idEstoque = idEstoque;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }
}