package com.lojasapatos;

import java.sql.Connection;

import com.lojasapatos.dao.Conexao;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        Connection c = Conexao.getConexao();

        if (c != null) {
            System.out.println("O sistema está online e conectado ao Postgres!");
        } else {
            System.out.println("O sistema está offline. Verifique o banco de dados.");
        }
        
        System.out.println("Hello World!");
    }
}
