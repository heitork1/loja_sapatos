package com.lojasapatos.dao;
import java.sql.*;
import com.lojasapatos.model.Filial;

public class FilialDAO {
    public void salvar(Filial f) {
        String sql = "INSERT INTO filial (id_estoque, id_sede) VALUES (?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, f.getIdEstoque());
            stmt.setInt(2, f.getIdSede());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}