package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Filial> listar() {
        List<Filial> lista = new ArrayList<>();
        String sql = "SELECT * FROM filial";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Filial(rs.getInt("id_filial"), rs.getInt("id_estoque"), rs.getInt("id_sede"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Filial f) {
        String sql = "UPDATE filial SET id_estoque=?, id_sede=? WHERE id_filial=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, f.getIdEstoque()); stmt.setInt(2, f.getIdSede()); stmt.setInt(3, f.getIdFilial()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int idFilial) {
        String sql = "DELETE FROM filial WHERE id_filial=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idFilial); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
    
}