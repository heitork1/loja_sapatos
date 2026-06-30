package com.lojasapatos.dao;

import com.lojasapatos.model.Oferta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfertaDAO {

    public void inserir(Oferta o) throws SQLException {
        String sql = "INSERT INTO oferta (id_promocao, codigo, desconto) VALUES (?, ?, ?)";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, o.getIdPromocao());
            ps.setInt(2, o.getCodigo());
            ps.setBigDecimal(3, o.getDesconto());
            ps.executeUpdate();
        }
    }

    //produtos contemplados por uma promoção específica
    public List<Oferta> listarPorPromocao(Integer idPromocao) throws SQLException {
        List<Oferta> lista = new ArrayList<>();
        String sql = "SELECT * FROM oferta WHERE id_promocao = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPromocao);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Oferta mapear(ResultSet rs) throws SQLException {
        return new Oferta(rs.getInt("id_promocao"), rs.getInt("codigo"), rs.getBigDecimal("desconto"));
    }
}
