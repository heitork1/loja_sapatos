package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Promocao;

public class PromocaoDAO {
    public void inserir(Promocao p) throws SQLException {
        String sql = "INSERT INTO promocao (nome, data_inicio, data_fim, categorias) VALUES (?, ?, ?, ?) " +
                     "RETURNING id_promocao";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setDate(2, Date.valueOf(p.getDataInicio()));
            ps.setDate(3, Date.valueOf(p.getDataFim()));
            ps.setString(4, p.getCategorias());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) p.setIdPromocao(rs.getInt("id_promocao"));
        }
    }

    public List<Promocao> listarAtivas() throws SQLException {
        List<Promocao> lista = new ArrayList<>();
        String sql = "SELECT * FROM promocao WHERE CURRENT_DATE BETWEEN data_inicio AND data_fim";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Promocao> listarTodas() throws SQLException {
        List<Promocao> lista = new ArrayList<>();
        String sql = "SELECT * FROM promocao ORDER BY data_inicio DESC";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Promocao mapear(ResultSet rs) throws SQLException {
        Promocao p = new Promocao(rs.getString("nome"), rs.getDate("data_inicio").toLocalDate(),
                                   rs.getDate("data_fim").toLocalDate(), rs.getString("categorias"));
        p.setIdPromocao(rs.getInt("id_promocao"));
        return p;
    }
}
