package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Sede;

public class SedeDAO {
    public void salvar(Sede s) {
        String sql = "INSERT INTO sede (nome_sede) VALUES (?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, s.getNomeSede());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public List<Sede> listar() {
        List<Sede> lista = new ArrayList<>();
        String sql = "SELECT * FROM sede";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Sede(rs.getInt("id_sede"), rs.getString("nome_sede"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Sede s) {
        String sql = "UPDATE sede SET nome_sede=? WHERE id_sede=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, s.getNomeSede()); stmt.setInt(2, s.getIdSede()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int idSede) {
        String sql = "DELETE FROM sede WHERE id_sede=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idSede); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}