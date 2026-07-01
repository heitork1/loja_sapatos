package com.lojasapatos.dao;

import com.lojasapatos.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (cpf, nome, data_nascimento, telefone, email, rua, numero, cep, pontos) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCpf());
            ps.setString(2, c.getNome());
            ps.setDate(3, c.getDataNascimento() != null ? Date.valueOf(c.getDataNascimento()) : null);
            ps.setString(4, c.getTelefone());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getRua());
            ps.setString(7, c.getNumero());
            ps.setString(8, c.getCep());
            ps.setInt(9, c.getPontos() != null ? c.getPontos() : 0);
            ps.executeUpdate();
        }
    }

    public void atualizar(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, data_nascimento = ?, telefone = ?, email = ?, " +
                     "rua = ?, numero = ?, cep = ?, pontos = ? WHERE cpf = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setDate(2, c.getDataNascimento() != null ? Date.valueOf(c.getDataNascimento()) : null);
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getRua());
            ps.setString(6, c.getNumero());
            ps.setString(7, c.getCep());
            ps.setInt(8, c.getPontos());
            ps.setString(9, c.getCpf());
            ps.executeUpdate();
        }
    }

    public void excluir(String cpf) throws SQLException {
        String sql = "DELETE FROM cliente WHERE cpf = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ps.executeUpdate();
        }
    }

    public Cliente buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nome";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void adicionarPontos(String cpf, int pontosGanhos) throws SQLException {
        String sql = "UPDATE cliente SET pontos = pontos + ? WHERE cpf = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pontosGanhos);
            ps.setString(2, cpf);
            ps.executeUpdate();
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Date nascimento = rs.getDate("data_nascimento");
        return new Cliente(
            rs.getString("cpf"),
            rs.getString("nome"),
            nascimento != null ? nascimento.toLocalDate() : null,
            rs.getString("telefone"),
            rs.getString("email"),
            rs.getString("rua"),
            rs.getString("numero"),
            rs.getString("cep"),
            rs.getInt("pontos")
        );
    }
}
