package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Estoque;

public class EstoqueDAO {
    public void salvar(Estoque e) {
        String sql = "INSERT INTO estoque (quantidade_minima) VALUES (?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getQuantidadeMinima());
            stmt.executeUpdate();
        } catch (SQLException ex) { System.err.println("Erro: " + ex.getMessage()); }
    }

    public List<Estoque> listar() {
        List<Estoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM estoque";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Estoque(rs.getInt("id_estoque"), rs.getInt("quantidade_minima"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Estoque e) {
        String sql = "UPDATE estoque SET quantidade_minima=? WHERE id_estoque=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, e.getQuantidadeMinima()); stmt.setInt(2, e.getIdEstoque()); stmt.executeUpdate();
        } catch (SQLException ex) { System.err.println("Erro: " + ex.getMessage()); }
    }

    public void deletar(int idEstoque) {
        String sql = "DELETE FROM estoque WHERE id_estoque=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idEstoque); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}