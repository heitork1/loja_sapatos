package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.lojasapatos.model.Sede;

public class SedeDAO {
    public void salvar(Sede s) {
        String sql = "INSERT INTO sede (nome_sede) VALUES (?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getNomeSede());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}