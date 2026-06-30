package com.lojasapatos.model;

public class Compra {
    private Integer idVenda;
    private String cpf;

    public Compra() {}
    public Compra(Integer idVenda, String cpf) {
        this.idVenda = idVenda;
        this.cpf = cpf;
    }

    public Integer getIdVenda() { return idVenda; }
    public void setIdVenda(Integer idVenda) { this.idVenda = idVenda; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    @Override
    public String toString() {
        return "Compra{idVenda=" + idVenda + ", cpf='" + cpf + "'}";
    }
}
