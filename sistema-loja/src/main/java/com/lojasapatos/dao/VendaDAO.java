package com.lojasapatos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Venda;

public class VendaDAO {
    public void inserir(Venda v) throws SQLException {
        String sql = "INSERT INTO venda (data_venda, forma_pagamento, num_parcelas, id_filial, id_funcionario) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id_venda";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(v.getDataVenda()));
            ps.setString(2, v.getFormaPagamento());
            if (v.getNumParcelas() != null) ps.setInt(3, v.getNumParcelas());
            else ps.setNull(3, Types.INTEGER);
            ps.setInt(4, v.getIdFilial());
            ps.setInt(5, v.getIdFuncionario());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) v.setIdVenda(rs.getInt("id_venda"));
        }
    }

    public Venda buscarPorId(Integer idVenda) throws SQLException {
        String sql = "SELECT * FROM venda WHERE id_venda = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenda);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Venda> listarPorFilial(Integer idFilial) throws SQLException {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT * FROM venda WHERE id_filial = ? ORDER BY data_venda DESC";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFilial);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Venda> listarTodos() throws SQLException {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT * FROM venda ORDER BY id_venda";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void realizarVenda(Venda venda, List<ItemVenda> itens, String cpfCliente, Integer idEstoqueFilial)
            throws SQLException {

        Connection con = null;
        try {
            con = Conexao.obterConexao();
            con.setAutoCommit(false);

            String sqlVenda = "INSERT INTO venda (data_venda, forma_pagamento, num_parcelas, id_filial, id_funcionario) " +
                               "VALUES (?, ?, ?, ?, ?) RETURNING id_venda";
            try (PreparedStatement ps = con.prepareStatement(sqlVenda)) {
                ps.setTimestamp(1, Timestamp.valueOf(venda.getDataVenda()));
                ps.setString(2, venda.getFormaPagamento());
                if (venda.getNumParcelas() != null) ps.setInt(3, venda.getNumParcelas());
                else ps.setNull(3, Types.INTEGER);
                ps.setInt(4, venda.getIdFilial());
                ps.setInt(5, venda.getIdFuncionario());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) venda.setIdVenda(rs.getInt("id_venda"));
            }

            String sqlItem = "INSERT INTO composicao (id_venda, codigo, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";
            String sqlAbateEstoque = "UPDATE armazenamento SET quantidade = quantidade - ? " +
                                      "WHERE id_estoque = ? AND codigo = ?";
            BigDecimalTotal total = new BigDecimalTotal();

            try (PreparedStatement psItem = con.prepareStatement(sqlItem);
                 PreparedStatement psEstoque = con.prepareStatement(sqlAbateEstoque)) {
                for (ItemVenda item : itens) {
                    psItem.setInt(1, venda.getIdVenda());
                    psItem.setInt(2, item.getCodigo());
                    psItem.setInt(3, item.getQuantidade());
                    psItem.setBigDecimal(4, item.getPrecoUnitario());
                    psItem.executeUpdate();

                    psEstoque.setInt(1, item.getQuantidade());
                    psEstoque.setInt(2, idEstoqueFilial);
                    psEstoque.setInt(3, item.getCodigo());
                    psEstoque.executeUpdate();

                    total.somar(item.getPrecoUnitario().multiply(new java.math.BigDecimal(item.getQuantidade())));
                }
            }

            if (cpfCliente != null && !cpfCliente.isBlank()) {
                try (PreparedStatement psCompra = con.prepareStatement(
                        "INSERT INTO compra (id_venda, cpf) VALUES (?, ?)")) {
                    psCompra.setInt(1, venda.getIdVenda());
                    psCompra.setString(2, cpfCliente);
                    psCompra.executeUpdate();
                }
                int pontosGanhos = total.getValor().intValue(); // 1 ponto por real gasto
                try (PreparedStatement psPontos = con.prepareStatement(
                        "UPDATE cliente SET pontos = pontos + ? WHERE cpf = ?")) {
                    psPontos.setInt(1, pontosGanhos);
                    psPontos.setString(2, cpfCliente);
                    psPontos.executeUpdate();
                }
            }

            con.commit();
        } catch (SQLException e) {
            if (con != null) con.rollback();
            throw e;
        } finally {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        }
    }

    private Venda mapear(ResultSet rs) throws SQLException {
        Venda v = new Venda(
            rs.getTimestamp("data_venda").toLocalDateTime(),
            rs.getString("forma_pagamento"),
            rs.getObject("num_parcelas") != null ? rs.getInt("num_parcelas") : null,
            rs.getInt("id_filial"),
            rs.getInt("id_funcionario")
        );
        v.setIdVenda(rs.getInt("id_venda"));
        return v;
    }
    
    private static class BigDecimalTotal {
        private java.math.BigDecimal valor = java.math.BigDecimal.ZERO;
        void somar(java.math.BigDecimal v) { valor = valor.add(v); }
        java.math.BigDecimal getValor() { return valor; }
    }
}
