package com.lojasapatos.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Armazenamento;

public class ArmazenamentoDAO {

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

    public void atualizar(Armazenamento a) throws SQLException {
        String sql = "UPDATE armazenamento SET quantidade = ? WHERE id_estoque = ? AND codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getQuantidade());
            ps.setInt(2, a.getIdEstoque());
            ps.setInt(3, a.getCodigo());
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

    public void deletar(int idEstoque, int codigo) throws SQLException {
        String sql = "DELETE FROM armazenamento WHERE id_estoque = ? AND codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstoque);
            ps.setInt(2, codigo);
            ps.executeUpdate();
        }
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

    public List<Armazenamento> listar() throws SQLException {
        List<Armazenamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM armazenamento";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

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
            while (rs.next())
                lista.add(String.format("%s | atual: %d | mínimo: %d",
                        rs.getString("marca"), rs.getInt("quantidade"), rs.getInt("quantidade_minima")));
        }
        return lista;
    }

    private Armazenamento mapear(ResultSet rs) throws SQLException {
        return new Armazenamento(rs.getInt("id_estoque"), rs.getInt("codigo"), rs.getInt("quantidade"));
    }
}
