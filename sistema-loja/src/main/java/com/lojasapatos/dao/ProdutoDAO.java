package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Produto;

public class ProdutoDAO {
    public void inserir(Produto p) throws SQLException {
        String sql = "INSERT INTO produto (marca, categoria, publico_alvo, preco, id_modelo) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING codigo";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getMarca());
            ps.setString(2, p.getCategoria());
            ps.setString(3, p.getPublicoAlvo());
            ps.setBigDecimal(4, p.getPreco());
            ps.setInt(5, p.getIdModelo());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) p.setCodigo(rs.getInt("codigo"));
        }
    }

    public void atualizar(Produto p) throws SQLException {
        String sql = "UPDATE produto SET marca = ?, categoria = ?, publico_alvo = ?, preco = ?, id_modelo = ? " +
                     "WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getMarca());
            ps.setString(2, p.getCategoria());
            ps.setString(3, p.getPublicoAlvo());
            ps.setBigDecimal(4, p.getPreco());
            ps.setInt(5, p.getIdModelo());
            ps.setInt(6, p.getCodigo());
            ps.executeUpdate();
        }
    }

    public void excluir(Integer codigo) throws SQLException {
        String sql = "DELETE FROM produto WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        }
    }

    public Produto buscarPorId(Integer codigo) throws SQLException {
        String sql = "SELECT * FROM produto WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto ORDER BY codigo";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<String> listarDisponiveisPorFilial(Integer idFilial) throws SQLException {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT p.marca, m.cor, m.numero, a.quantidade " +
                     "FROM armazenamento a " +
                     "JOIN estoque e ON a.id_estoque = e.id_estoque " +
                     "JOIN filial f ON f.id_estoque = e.id_estoque " +
                     "JOIN produto p ON p.codigo = a.codigo " +
                     "JOIN modelo m ON m.codigo = p.id_modelo " +
                     "WHERE f.id_filial = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFilial);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(String.format("%s | Cor: %s | Número: %d | Qtd: %d",
                        rs.getString("marca"), rs.getString("cor"),
                        rs.getInt("numero"), rs.getInt("quantidade")));
            }
        }
        return lista;
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        return new Produto(
            rs.getInt("codigo"), rs.getString("marca"), rs.getString("categoria"),
            rs.getString("publico_alvo"), rs.getBigDecimal("preco"), rs.getInt("id_modelo")
        );
    }
    
}
