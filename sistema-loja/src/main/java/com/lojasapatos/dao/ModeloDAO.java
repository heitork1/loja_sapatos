package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.lojasapatos.model.Modelo;

public class ModeloDAO {
    public void salvar(Modelo m) {
        String sql = "INSERT INTO modelo (cor, numero, categoria_modelo) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, m.getCor());
            stmt.setInt(2, m.getNumero());
            stmt.setString(3, m.getCategoriaModelo());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}