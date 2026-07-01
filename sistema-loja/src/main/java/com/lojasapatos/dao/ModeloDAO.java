package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Modelo;

public class ModeloDAO {
    public void inserir(Modelo m) throws SQLException {
        String sql = "INSERT INTO modelo (cor, numero, categoria_modelo) VALUES (?, ?, ?) RETURNING codigo";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getCor());
            ps.setInt(2, m.getNumero());
            ps.setString(3, m.getCategoriaModelo());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) m.setCodigo(rs.getInt("codigo"));
        }
    }

    public void atualizar(Modelo m) throws SQLException {
        String sql = "UPDATE modelo SET cor = ?, numero = ?, categoria_modelo = ? WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getCor());
            ps.setInt(2, m.getNumero());
            ps.setString(3, m.getCategoriaModelo());
            ps.setInt(4, m.getCodigo());
            ps.executeUpdate();
        }
    }

    public void excluir(Integer codigo) throws SQLException {
        String sql = "DELETE FROM modelo WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        }
    }

    public Modelo buscarPorId(Integer codigo) throws SQLException {
        String sql = "SELECT * FROM modelo WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Modelo> listarTodos() throws SQLException {
        List<Modelo> lista = new ArrayList<>();
        String sql = "SELECT * FROM modelo ORDER BY codigo";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Modelo mapear(ResultSet rs) throws SQLException {
        return new Modelo(rs.getInt("codigo"), rs.getString("cor"), rs.getInt("numero"),
                           rs.getString("categoria_modelo"));
    }
    
}
