package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Modelo> listar() {
        List<Modelo> lista = new ArrayList<>();
        String sql = "SELECT * FROM modelo";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Modelo(rs.getInt("codigo"), rs.getString("cor"), rs.getInt("numero"), rs.getString("categoria_modelo"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Modelo m) {
        String sql = "UPDATE modelo SET cor=?, numero=?, categoria_modelo=? WHERE codigo=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, m.getCor()); stmt.setInt(2, m.getNumero()); stmt.setString(3, m.getCategoriaModelo()); stmt.setInt(4, m.getCodigo()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int codigo) {
        String sql = "DELETE FROM modelo WHERE codigo=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, codigo); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
    
}