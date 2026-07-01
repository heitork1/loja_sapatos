package com.lojasapatos.dao;

import com.lojasapatos.model.Compra;

import java.sql.*;

public class CompraDAO {

    public void inserir(Compra c) throws SQLException {
        String sql = "INSERT INTO compra (id_venda, cpf) VALUES (?, ?)";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdVenda());
            ps.setString(2, c.getCpf());
            ps.executeUpdate();
        }
    }

    public Compra buscarPorVenda(Integer idVenda) throws SQLException {
        String sql = "SELECT * FROM compra WHERE id_venda = ?";
        try (Connection con = Conexao.getConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenda);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Compra(rs.getInt("id_venda"), rs.getString("cpf"));
            return null; // venda sem cliente cadastrado cardinalidade 0,1
        }
    }
}
