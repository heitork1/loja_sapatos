package com.lojasapatos.dao;
import java.sql.*;
import com.lojasapatos.model.Produto;

public class ProdutoDAO {
    public void salvar(Produto p) {
        String sql = "INSERT INTO produto (marca, categoria, publico_alvo, preco, id_modelo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getMarca());
            stmt.setString(2, p.getCategoria());
            stmt.setString(3, p.getPublicoAlvo());
            stmt.setDouble(4, p.getPreco());
            stmt.setInt(5, p.getIdModelo());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}