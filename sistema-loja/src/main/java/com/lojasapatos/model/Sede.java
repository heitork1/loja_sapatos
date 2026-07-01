package com.lojasapatos.model;

public class Sede {
    private Integer idSede;
    private String nomeSede;
    
    public Sede() {}
    public Sede(String nomeSede) { this.nomeSede = nomeSede; }
    public Sede(Integer idSede, String nomeSede) {
        this.idSede = idSede;
        this.nomeSede = nomeSede;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public String getNomeSede() {
        return nomeSede;
    }

    public void setNomeSede(String nomeSede) {
        this.nomeSede = nomeSede;
    }
    
    @Override
    public String toString() {
        return "Sede{idSede=" + idSede + ", nomeSede='" + nomeSede + "'}";
    }
    
}
