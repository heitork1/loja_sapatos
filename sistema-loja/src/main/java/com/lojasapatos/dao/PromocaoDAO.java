package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.lojasapatos.model.Promocao;

public class PromocaoDAO {
    public void salvar(Promocao p) {
        String sql = "INSERT INTO promocao (categorias, nome, periodo_vigencia) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getCategorias());
            stmt.setString(2, p.getNome());
            stmt.setString(3, p.getPeriodoVigencia());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}