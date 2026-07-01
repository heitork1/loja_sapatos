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

    public void atualizar(Estoque estoque) throws SQLException {
        String sql = "UPDATE estoque SET quantidade_minima = ? WHERE id_estoque = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estoque.getQuantidadeMinima());
            ps.setInt(2, estoque.getIdEstoque());
            ps.executeUpdate();
        }
    }

    public void inserir(Estoque estoque) throws SQLException {
        String sql = "INSERT INTO estoque (quantidade_minima) VALUES (?) RETURNING id_estoque";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, estoque.getQuantidadeMinima());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) estoque.setIdEstoque(rs.getInt("id_estoque"));
        }
    }

    public void deletar(int idEstoque) {
        String sql = "DELETE FROM estoque WHERE id_estoque=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idEstoque); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
        public void excluir(Integer idEstoque) throws SQLException {
        String sql = "DELETE FROM estoque WHERE id_estoque = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstoque);
            ps.executeUpdate();
        }
    }

    public Estoque buscarPorId(Integer idEstoque) throws SQLException {
        String sql = "SELECT * FROM estoque WHERE id_estoque = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstoque);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Estoque> listarTodos() throws SQLException {
        List<Estoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM estoque ORDER BY id_estoque";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Estoque mapear(ResultSet rs) throws SQLException {
        return new Estoque(rs.getInt("id_estoque"), rs.getInt("quantidade_minima"));
    }
    
}
