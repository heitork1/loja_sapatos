package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Promocao> listar() {
        List<Promocao> lista = new ArrayList<>();
        String sql = "SELECT * FROM promocao";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Promocao(rs.getInt("id_promocao"), rs.getString("categorias"), rs.getString("nome"), rs.getString("periodo_vigencia"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Promocao p) {
        String sql = "UPDATE promocao SET categorias=?, nome=?, periodo_vigencia=? WHERE id_promocao=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, p.getCategorias()); stmt.setString(2, p.getNome()); stmt.setString(3, p.getPeriodoVigencia()); stmt.setInt(4, p.getIdPromocao()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int idPromocao) {
        String sql = "DELETE FROM promocao WHERE id_promocao=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idPromocao); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}