package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Solicitacao;

public class SolicitacaoDAO {
    public void salvar(Solicitacao s) {
        String sql = "INSERT INTO solicitacao (status, custo, data_entrega, id_sede, razao_social_fornecedor, codigo_produto) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getStatus());
            stmt.setDouble(2, s.getCusto());
            stmt.setDate(3, Date.valueOf(s.getDataEntrega()));
            stmt.setInt(4, s.getIdSede());
            stmt.setString(5, s.getRazaoSocialFornecedor());
            stmt.setInt(6, s.getCodigoProduto());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
    public List<Solicitacao> listar() {
        List<Solicitacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM solicitacao";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Solicitacao(rs.getInt("id_solicitacao"), rs.getString("status"), rs.getDouble("custo"), rs.getDate("data_entrega").toLocalDate(), rs.getInt("id_sede"), rs.getString("razao_social_fornecedor"), rs.getInt("codigo_produto"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Solicitacao s) {
        String sql = "UPDATE solicitacao SET status=?, custo=?, data_entrega=?, id_sede=?, razao_social_fornecedor=?, codigo_produto=? WHERE id_solicitacao=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, s.getStatus()); stmt.setDouble(2, s.getCusto()); stmt.setDate(3, Date.valueOf(s.getDataEntrega())); stmt.setInt(4, s.getIdSede()); stmt.setString(5, s.getRazaoSocialFornecedor()); stmt.setInt(6, s.getCodigoProduto()); stmt.setInt(7, s.getIdSolicitacao()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int idSolicitacao) {
        String sql = "DELETE FROM solicitacao WHERE id_solicitacao=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idSolicitacao); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}