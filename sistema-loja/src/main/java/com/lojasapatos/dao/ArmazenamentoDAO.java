package com.lojasapatos.dao;
import java.sql.*;
import com.lojasapatos.model.Armazenamento;

public class ArmazenamentoDAO {
    public void salvar(Armazenamento a) {
        String sql = "INSERT INTO armazenamento (id_estoque, codigo_produto, quantidade) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, a.getIdEstoque());
            stmt.setInt(2, a.getCodigoProduto());
            stmt.setInt(3, a.getQuantidade());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}