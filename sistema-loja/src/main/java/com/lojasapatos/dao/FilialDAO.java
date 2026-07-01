package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Filial;

public class FilialDAO {
    public void inserir(Filial filial) throws SQLException {
        String sql = "INSERT INTO filial (nome_filial, id_sede, id_estoque) VALUES (?, ?, ?) RETURNING id_filial";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, filial.getNomeFilial());
            ps.setInt(2, filial.getIdSede());
            ps.setInt(3, filial.getIdEstoque());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) filial.setIdFilial(rs.getInt("id_filial"));
        }
    }

    public void atualizar(Filial filial) throws SQLException {
        String sql = "UPDATE filial SET nome_filial = ?, id_sede = ?, id_estoque = ? WHERE id_filial = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, filial.getNomeFilial());
            ps.setInt(2, filial.getIdSede());
            ps.setInt(3, filial.getIdEstoque());
            ps.setInt(4, filial.getIdFilial());
            ps.executeUpdate();
        }
    }

    public void excluir(Integer idFilial) throws SQLException {
        String sql = "DELETE FROM filial WHERE id_filial = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFilial);
            ps.executeUpdate();
        }
    }

    public Filial buscarPorId(Integer idFilial) throws SQLException {
        String sql = "SELECT * FROM filial WHERE id_filial = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFilial);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Filial> listarTodos() throws SQLException {
        List<Filial> lista = new ArrayList<>();
        String sql = "SELECT * FROM filial ORDER BY id_filial";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Filial mapear(ResultSet rs) throws SQLException {
        return new Filial(
            rs.getInt("id_filial"),
            rs.getString("nome_filial"),
            rs.getInt("id_sede"),
            rs.getInt("id_estoque")
        );
    }
    
}
