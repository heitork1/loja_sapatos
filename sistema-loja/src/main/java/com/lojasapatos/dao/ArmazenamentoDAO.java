package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Armazenamento;

public class ArmazenamentoDAO {
    public void salvar(Armazenamento a) {
        String sql = "INSERT INTO armazenamento (id_estoque, codigo_produto, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getIdEstoque());
            stmt.setInt(2, a.getCodigoProduto());
            stmt.setInt(3, a.getQuantidade());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public List<Armazenamento> listar() {
        List<Armazenamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM armazenamento";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Armazenamento(rs.getInt("id_estoque"), rs.getInt("codigo_produto"), rs.getInt("quantidade"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Armazenamento a) {
        String sql = "UPDATE armazenamento SET quantidade=? WHERE id_estoque=? AND codigo_produto=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, a.getQuantidade()); stmt.setInt(2, a.getIdEstoque()); stmt.setInt(3, a.getCodigoProduto()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int idEstoque, int codigoProduto) {
        String sql = "DELETE FROM armazenamento WHERE id_estoque=? AND codigo_produto=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idEstoque); stmt.setInt(2, codigoProduto); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}