package com.lojasapatos.model;

import java.time.LocalDate; // Importante para datas!

public class Cliente {
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;
    private String rua;
    private String numero;
    private String cep;
    private Integer pontos;

    public Cliente() {}

    public Cliente(String cpf, String nome, LocalDate dataNascimento, String telefone,
                    String email, String rua, String numero, String cep, Integer pontos) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.rua = rua;
        this.numero = numero;
        this.cep = cep;
        this.pontos = pontos;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public Integer getPontos() { return pontos; }
    public void setPontos(Integer pontos) { this.pontos = pontos; }

    @Override
    public String toString() {
        return "Cliente{cpf='" + cpf + "', nome='" + nome + "', pontos=" + pontos + "}";
    }
