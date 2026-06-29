package com.lojasapatos.dao;
import java.sql.*;
import com.lojasapatos.model.Cliente;

public class ClienteDAO {
    public void salvar(Cliente c) {
        String sql = "INSERT INTO cliente (cpf, telefone, email, pontos, nome, data_nascimento, numero, rua) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getCpf());
            stmt.setString(2, c.getTelefone());
            stmt.setString(3, c.getEmail());
            stmt.setInt(4, c.getPontos());
            stmt.setString(5, c.getNome());
            stmt.setDate(6, Date.valueOf(c.getDataNascimento())); // Conversão da data aqui!
            stmt.setString(7, c.getNumero());
            stmt.setString(8, c.getRua());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}