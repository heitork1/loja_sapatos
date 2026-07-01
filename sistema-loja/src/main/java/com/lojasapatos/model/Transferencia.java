package com.lojasapatos.model;
import java.time.LocalDate;

public class Transferencia {
    private int idFilial;
    private int codigo;
    private LocalDate dataTransferencia; // O atributo extra que obrigou a ter a classe!

    public Transferencia() {}
    public Transferencia(int idFilial, int codigo, LocalDate dataTransferencia) {
        this.idFilial = idFilial;
        this.codigo = codigo;
        this.dataTransferencia = dataTransferencia;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public LocalDate getDataTransferencia() {
        return dataTransferencia;
    }

    public void setDataTransferencia(LocalDate dataTransferencia) {
        this.dataTransferencia = dataTransferencia;
    }
    
    @Override
    public String toString() {
        return "Transferencia{idFilial=" + idFilial + ", codigo=" + codigo +
               ", dataTransferencia=" + dataTransferencia + "}";
    }
    
}
