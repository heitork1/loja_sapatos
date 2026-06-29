package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.lojasapatos.model.Estoque;

public class EstoqueDAO {
    public void salvar(Estoque e) {
        String sql = "INSERT INTO estoque (quantidade_minima) VALUES (?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, e.getQuantidadeMinima());
            stmt.executeUpdate();
        } catch (SQLException ex) { System.err.println("Erro: " + ex.getMessage()); }
    }
}