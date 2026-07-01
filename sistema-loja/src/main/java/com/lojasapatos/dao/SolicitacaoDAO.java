package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Solicitacao;

public class SolicitacaoDAO {
    public void inserir(Solicitacao s) throws SQLException {
        String sql = "INSERT INTO solicitacao (status, quantidade, custo, data_entrega, razao_social, codigo, id_sede) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_solicitacao";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getStatus());
            ps.setInt(2, s.getQuantidade());
            ps.setBigDecimal(3, s.getCusto());
            ps.setDate(4, s.getDataEntrega() != null ? Date.valueOf(s.getDataEntrega()) : null);
            ps.setString(5, s.getRazaoSocial());
            ps.setInt(6, s.getCodigo());
            ps.setInt(7, s.getIdSede());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) s.setIdSolicitacao(rs.getInt("id_solicitacao"));
        }
    }

    public void atualizarStatus(Integer idSolicitacao, String novoStatus) throws SQLException {
        String sql = "UPDATE solicitacao SET status = ? WHERE id_solicitacao = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, novoStatus);
            ps.setInt(2, idSolicitacao);
            ps.executeUpdate();
        }
    }

    public void excluir(Integer idSolicitacao) throws SQLException {
        String sql = "DELETE FROM solicitacao WHERE id_solicitacao = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitacao);
            ps.executeUpdate();
        }
    }

    public Solicitacao buscarPorId(Integer idSolicitacao) throws SQLException {
        String sql = "SELECT * FROM solicitacao WHERE id_solicitacao = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idSolicitacao);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Solicitacao> listarTodos() throws SQLException {
        List<Solicitacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitacao ORDER BY id_solicitacao";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Solicitacao mapear(ResultSet rs) throws SQLException {
        Date entrega = rs.getDate("data_entrega");
        Solicitacao s = new Solicitacao(
            rs.getString("status"), rs.getInt("quantidade"), rs.getBigDecimal("custo"),
            entrega != null ? entrega.toLocalDate() : null,
            rs.getString("razao_social"), rs.getInt("codigo"), rs.getInt("id_sede")
        );
        s.setIdSolicitacao(rs.getInt("id_solicitacao"));
        return s;
    }
}
