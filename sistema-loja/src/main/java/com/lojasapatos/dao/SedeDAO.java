package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Sede;

public class SedeDAO {
    public void inserir(Sede sede) throws SQLException {
        String sql = "INSERT INTO sede (nome_sede) VALUES (?) RETURNING id_sede";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sede.getNomeSede());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) sede.setIdSede(rs.getInt("id_sede"));
        }
    }

    public void atualizar(Sede sede) throws SQLException {
        String sql = "UPDATE sede SET nome_sede = ? WHERE id_sede = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sede.getNomeSede());
            ps.setInt(2, sede.getIdSede());
            ps.executeUpdate();
        }
    }

    public void excluir(Integer idSede) throws SQLException {
        String sql = "DELETE FROM sede WHERE id_sede = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSede);
            ps.executeUpdate();
        }
    }

    public Sede buscarPorId(Integer idSede) throws SQLException {
        String sql = "SELECT * FROM sede WHERE id_sede = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSede);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Sede> listarTodos() throws SQLException {
        List<Sede> lista = new ArrayList<>();
        String sql = "SELECT * FROM sede ORDER BY id_sede";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Sede mapear(ResultSet rs) throws SQLException {
        return new Sede(rs.getInt("id_sede"), rs.getString("nome_sede"));
    }
}
