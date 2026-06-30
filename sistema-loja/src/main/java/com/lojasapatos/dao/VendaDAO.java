package com.lojasapatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Venda;

public class VendaDAO {
    public List<Venda> listar() {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.cpf FROM venda v LEFT JOIN compra c ON v.id_venda = c.id_venda";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Venda(rs.getInt("id_venda"), rs.getString("forma_pagamento"), rs.getInt("id_filial"), rs.getInt("id_funcionarios"), rs.getInt("num_parcelas"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Venda v) {
        String sql = "UPDATE venda SET forma_pagamento=?, id_filial=?, id_funcionarios=? WHERE id_venda=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, v.getFormaPagamento()); stmt.setInt(2, v.getIdFilial()); stmt.setInt(3, v.getIdFuncionario()); stmt.setInt(4, v.getIdVenda()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int idVenda) {
        String sqlCompra = "DELETE FROM compra WHERE id_venda=?";
        String sqlVenda = "DELETE FROM venda WHERE id_venda=?";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt1 = conn.prepareStatement(sqlCompra); PreparedStatement stmt2 = conn.prepareStatement(sqlVenda)) {
            stmt1.setInt(1, idVenda); stmt1.executeUpdate(); // Deleta a dependência primeiro
            stmt2.setInt(1, idVenda); stmt2.executeUpdate(); // Depois deleta a venda
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}
