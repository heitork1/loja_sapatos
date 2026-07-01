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
        public void inserir(Armazenamento a) throws SQLException {
        String sql = "INSERT INTO armazenamento (id_estoque, codigo, quantidade) VALUES (?, ?, ?)";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getIdEstoque());
            ps.setInt(2, a.getCodigo());
            ps.setInt(3, a.getQuantidade());
            ps.executeUpdate();
        }
    }

    public void atualizarQuantidade(Integer idEstoque, Integer codigo, Integer novaQuantidade) throws SQLException {
        String sql = "UPDATE armazenamento SET quantidade = ? WHERE id_estoque = ? AND codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, novaQuantidade);
            ps.setInt(2, idEstoque);
            ps.setInt(3, codigo);
            ps.executeUpdate();
        }
    }

    /** Requisito: alerta de reposição -- itens abaixo da quantidade mínima do estoque. */
    public List<String> listarAbaixoDoMinimo(Integer idEstoque) throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT p.marca, a.quantidade, e.quantidade_minima " +
                     "FROM armazenamento a " +
                     "JOIN estoque e ON e.id_estoque = a.id_estoque " +
                     "JOIN produto p ON p.codigo = a.codigo " +
                     "WHERE a.id_estoque = ? AND a.quantidade <= e.quantidade_minima";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstoque);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(String.format("%s | atual: %d | mínimo: %d",
                        rs.getString("marca"), rs.getInt("quantidade"), rs.getInt("quantidade_minima")));
            }
        }
        return lista;
    }

    public List<Armazenamento> listarPorEstoque(Integer idEstoque) throws SQLException {
        List<Armazenamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM armazenamento WHERE id_estoque = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstoque);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Armazenamento mapear(ResultSet rs) throws SQLException {
        return new Armazenamento(rs.getInt("id_estoque"), rs.getInt("codigo"), rs.getInt("quantidade"));
    }
}
