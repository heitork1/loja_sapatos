package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Transferencia;

public class TransferenciaDAO {
    public void inserir(Transferencia t) throws SQLException {
        String sql = "INSERT INTO transferencia (id_filial, codigo, data_transferencia) VALUES (?, ?, ?)";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getIdFilial());
            ps.setInt(2, t.getCodigo());
            ps.setDate(3, Date.valueOf(t.getDataTransferencia()));
            ps.executeUpdate();
        }
    }

    public List<Transferencia> listarPorFilial(Integer idFilial) throws SQLException {
        List<Transferencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM transferencia WHERE id_filial = ? ORDER BY data_transferencia DESC";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFilial);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Transferencia mapear(ResultSet rs) throws SQLException {
        return new Transferencia(rs.getInt("id_filial"), rs.getInt("codigo"),
                                  rs.getDate("data_transferencia").toLocalDate());
    }
}
