package com.lojasapatos.model;

import java.time.LocalDate;

public class Funcionario {
    private Integer codigo; // SERIAL do banco vira Integer
    private String cpf;
    private String funcao;
    private String nome;
    private LocalDate dataAdmissao;
    private Integer idFilial;

    public Funcionario(Integer codigo, String cpf, String funcao, String nome, LocalDate dataAdmissao,
            Integer idFilial) {
        this.codigo = codigo;
        this.cpf = cpf;
        this.funcao = funcao;
        this.nome = nome;
        this.dataAdmissao = dataAdmissao;
        this.idFilial = idFilial;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    
    public Funcionario() {}
}