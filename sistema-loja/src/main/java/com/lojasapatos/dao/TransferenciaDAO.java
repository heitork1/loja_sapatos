package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Transferencia;

public class TransferenciaDAO {
    public void salvar(Transferencia t) {
        String sql = "INSERT INTO transferencia (id_filial, codigo_produto, data_transferencia) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, t.getIdFilial());
            stmt.setInt(2, t.getCodigoProduto());
            stmt.setDate(3, Date.valueOf(t.getDataTransferencia()));
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public List<Transferencia> listar() {
        List<Transferencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM transferencia";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Transferencia(rs.getInt("id_filial"), rs.getInt("codigo_produto"), rs.getDate("data_transferencia").toLocalDate())); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Transferencia t) {
        String sql = "UPDATE transferencia SET data_transferencia=? WHERE id_filial=? AND codigo_produto=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(t.getDataTransferencia())); stmt.setInt(2, t.getIdFilial()); stmt.setInt(3, t.getCodigoProduto()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int idFilial, int codigoProduto) {
        String sql = "DELETE FROM transferencia WHERE id_filial=? AND codigo_produto=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, idFilial); stmt.setInt(2, codigoProduto); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}