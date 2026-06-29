package com.lojasapatos.model;

import java.time.LocalDate; // Importante para datas!

public class Cliente {
    private String cpf;
    private String telefone;
    private String email;
    private int pontos;

    
    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }
    private String nome;
    private LocalDate dataNascimento; // Em Java usamos LocalDate para datas
    private String numero;
    private String rua;

    public Cliente() {}

    public Cliente(String cpf, String telefone, String email, int pontos, String nome, LocalDate dataNascimento, String numero, String rua) {
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.pontos = pontos;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.numero = numero;
        this.rua = rua;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}