package com.lojasapatos.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Solicitacao {
    private Integer idSolicitacao;
    private String status;
    private Integer quantidade;
    private BigDecimal custo;
    private LocalDate dataEntrega;
    private String razaoSocial;
    private Integer codigo;
    private Integer idSede;

    public Solicitacao() {}
    public Solicitacao(String status, Integer quantidade, BigDecimal custo, LocalDate dataEntrega,
                        String razaoSocial, Integer codigo, Integer idSede) {
        this.status = status;
        this.quantidade = quantidade;
        this.custo = custo;
        this.dataEntrega = dataEntrega;
        this.razaoSocial = razaoSocial;
        this.codigo = codigo;
        this.idSede = idSede;
    }

    public Integer getIdSolicitacao() { return idSolicitacao; }
    public void setIdSolicitacao(Integer idSolicitacao) { this.idSolicitacao = idSolicitacao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public BigDecimal getCusto() { return custo; }
    public void setCusto(BigDecimal custo) { this.custo = custo; }
    public LocalDate getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
    public Integer getCodigo() { return codigo; }
    public void setCodigo(Integer codigo) { this.codigo = codigo; }
    public Integer getIdSede() { return idSede; }
    public void setIdSede(Integer idSede) { this.idSede = idSede; }

    @Override
    public String toString() {
        return "Solicitacao{idSolicitacao=" + idSolicitacao + ", status='" + status +
               "', quantidade=" + quantidade + ", custo=" + custo + ", dataEntrega=" + dataEntrega +
               ", razaoSocial='" + razaoSocial + "', codigo=" + codigo + ", idSede=" + idSede + "}";
    }
}
