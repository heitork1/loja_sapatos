package com.lojasapatos.model;
import java.time.LocalDateTime;

public class Venda {
    private Integer idVenda;
    private String formaPagamento;
    private Integer idFilial;
    private Integer idFuncionario;
    private Integer numParcelas;
    private LocalDateTime dataVenda;

    public Venda() {}
    public Venda(LocalDateTime dataVenda, String formaPagamento, Integer numParcelas,
                 Integer idFilial, Integer idFuncionario) {
        this.dataVenda = dataVenda;
        this.formaPagamento = formaPagamento;
        this.numParcelas = numParcelas;
        this.idFilial = idFilial;
        this.idFuncionario = idFuncionario;
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
    
    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
    
    @Override
    public String toString() {
        return "Venda{idVenda=" + idVenda + ", dataVenda=" + dataVenda + ", formaPagamento='" +
               formaPagamento + "', numParcelas=" + numParcelas + ", idFilial=" + idFilial +
               ", idFuncionario=" + idFuncionario + "}";
    }
    public Integer getNumParcelas() {
        return numParcelas;
    }
    public void setNumParcelas(Integer numParcelas) {
        this.numParcelas = numParcelas;
    }

}
