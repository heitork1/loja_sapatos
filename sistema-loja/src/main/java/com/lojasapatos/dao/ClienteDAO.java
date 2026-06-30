package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Cliente(rs.getString("cpf"), rs.getString("telefone"), rs.getString("email"), rs.getInt("pontos"), rs.getString("nome"), rs.getDate("data_nascimento").toLocalDate(), rs.getString("numero"), rs.getString("rua"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Cliente cli) {
        String sql = "UPDATE cliente SET telefone=?, email=?, pontos=?, nome=?, data_nascimento=?, numero=?, rua=? WHERE cpf=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, cli.getTelefone()); stmt.setString(2, cli.getEmail()); stmt.setInt(3, cli.getPontos()); stmt.setString(4, cli.getNome()); stmt.setDate(5, Date.valueOf(cli.getDataNascimento())); stmt.setString(6, cli.getNumero()); stmt.setString(7, cli.getRua()); stmt.setString(8, cli.getCpf()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(String cpf) {
        String sql = "DELETE FROM cliente WHERE cpf=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, cpf); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}