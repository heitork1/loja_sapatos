package com.lojasapatos.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL = "jdbc:postgresql://localhost:5432/loja_sapatos";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "admin";

    public static Connection getConexao(){
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e){
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
            return null;
        }
    }
}
