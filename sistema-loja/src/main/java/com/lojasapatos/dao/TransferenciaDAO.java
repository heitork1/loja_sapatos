package com.lojasapatos.dao;
import java.sql.*;
import com.lojasapatos.model.Transferencia;

public class TransferenciaDAO {
    public void salvar(Transferencia t) {
        String sql = "INSERT INTO transferencia (id_filial, codigo_produto, data_transferencia) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getIdFilial());
            stmt.setInt(2, t.getCodigoProduto());
            stmt.setDate(3, Date.valueOf(t.getDataTransferencia()));
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}