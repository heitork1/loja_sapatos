package com.lojasapatos.model;
import java.time.LocalDate;

public class Transferencia {
    private int idFilial;
    private int codigoProduto;
    private LocalDate dataTransferencia; // O atributo extra que obrigou a ter a classe!

    public Transferencia() {}

    public Transferencia(int idFilial, int codigoProduto, LocalDate dataTransferencia) {
        this.idFilial = idFilial;
        this.codigoProduto = codigoProduto;
        this.dataTransferencia = dataTransferencia;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(int codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public LocalDate getDataTransferencia() {
        return dataTransferencia;
    }

    public void setDataTransferencia(LocalDate dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }
    
}