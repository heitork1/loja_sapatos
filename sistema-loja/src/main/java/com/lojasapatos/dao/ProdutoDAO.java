package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Produto(rs.getInt("codigo"), rs.getString("marca"), rs.getString("categoria"), rs.getString("publico_alvo"), rs.getDouble("preco"), rs.getInt("id_modelo"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Produto p) {
        String sql = "UPDATE produto SET marca=?, categoria=?, publico_alvo=?, preco=?, id_modelo=? WHERE codigo=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, p.getMarca()); stmt.setString(2, p.getCategoria()); stmt.setString(3, p.getPublicoAlvo()); stmt.setDouble(4, p.getPreco()); stmt.setInt(5, p.getIdModelo()); stmt.setInt(6, p.getCodigo()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int codigo) {
        String sql = "DELETE FROM produto WHERE codigo=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, codigo); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
    
}