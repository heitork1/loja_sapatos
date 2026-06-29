package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.lojasapatos.model.Fornecedor;

public class FornecedorDAO {
    public void salvar(Fornecedor f) {
        String sql = "INSERT INTO fornecedor (razao_social, cnpj, contato) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getRazaoSocial());
            stmt.setString(2, f.getCnpj());
            stmt.setString(3, f.getContato());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}