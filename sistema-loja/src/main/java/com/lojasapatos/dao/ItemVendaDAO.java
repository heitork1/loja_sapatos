package com.lojasapatos.dao;

import com.lojasapatos.model.ItemVenda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//itens de uma venda
public class ItemVendaDAO {

    public void inserir(ItemVenda item) throws SQLException {
        String sql = "INSERT INTO composicao (id_venda, codigo, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getIdVenda());
            ps.setInt(2, item.getCodigo());
            ps.setInt(3, item.getQuantidade());
            ps.setBigDecimal(4, item.getPrecoUnitario());
            ps.executeUpdate();
        }
    }

    public void excluir(Integer idVenda, Integer codigo) throws SQLException {
        String sql = "DELETE FROM composicao WHERE id_venda = ? AND codigo = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenda);
            ps.setInt(2, codigo);
            ps.executeUpdate();
        }
    }

    public List<ItemVenda> listarPorVenda(Integer idVenda) throws SQLException {
        List<ItemVenda> lista = new ArrayList<>();
        String sql = "SELECT * FROM composicao WHERE id_venda = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenda);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private ItemVenda mapear(ResultSet rs) throws SQLException {
        return new ItemVenda(rs.getInt("id_venda"), rs.getInt("codigo"),
                              rs.getInt("quantidade"), rs.getBigDecimal("preco_unitario"));
    }
}
