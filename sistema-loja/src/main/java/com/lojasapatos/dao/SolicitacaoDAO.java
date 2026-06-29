package com.lojasapatos.dao;
import java.sql.*;
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
}