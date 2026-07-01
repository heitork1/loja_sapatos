package com.lojasapatos.model;

public class Filial {
    private Integer idFilial;
    private Integer idEstoque; // FK
    private Integer idSede;    // FK
    private String nomeFilial;

    public Filial() {}
    public Filial(String nomeFilial, Integer idSede, Integer idEstoque) {
        this.nomeFilial = nomeFilial;
        this.idSede = idSede;
        this.idEstoque = idEstoque;
    }
    public Filial(Integer idFilial, String nomeFilial, Integer idSede, Integer idEstoque) {
        this.idFilial = idFilial;
        this.nomeFilial = nomeFilial;
        this.idSede = idSede;
        this.idEstoque = idEstoque;
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
    public String getNomeFilial() { return nomeFilial; }
    public void setNomeFilial(String nomeFilial) { this.nomeFilial = nomeFilial; }
    
    @Override
    public String toString() {
        return "Filial{idFilial=" + idFilial + ", nomeFilial='" + nomeFilial +
               "', idSede=" + idSede + ", idEstoque=" + idEstoque + "}";
    }
}
