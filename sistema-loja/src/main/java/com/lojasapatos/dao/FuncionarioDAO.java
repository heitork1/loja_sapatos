package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Funcionario;

public class FuncionarioDAO {
    public void inserir(Funcionario f) throws SQLException {
        String sql = "INSERT INTO funcionario (cpf, funcao, nome, data_admissao, id_filial) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING codigo";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getCpf());
            ps.setString(2, f.getFuncao());
            ps.setString(3, f.getNome());
            ps.setDate(4, Date.valueOf(f.getDataAdmissao()));
            ps.setInt(5, f.getIdFilial());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) f.setCodigo(rs.getInt("codigo"));
        }
    }

    public void atualizar(Funcionario f) throws SQLException {
        String sql = "UPDATE funcionario SET cpf = ?, funcao = ?, nome = ?, data_admissao = ?, id_filial = ? " +
                     "WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getCpf());
            ps.setString(2, f.getFuncao());
            ps.setString(3, f.getNome());
            ps.setDate(4, Date.valueOf(f.getDataAdmissao()));
            ps.setInt(5, f.getIdFilial());
            ps.setInt(6, f.getCodigo());
            ps.executeUpdate();
        }
    }

    public void excluir(Integer codigo) throws SQLException {
        String sql = "DELETE FROM funcionario WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
        }
    }

    public Funcionario buscarPorId(Integer codigo) throws SQLException {
        String sql = "SELECT * FROM funcionario WHERE codigo = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Funcionario> listarPorFilial(Integer idFilial) throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario WHERE id_filial = ? ORDER BY nome";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFilial);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario ORDER BY codigo";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Funcionario mapear(ResultSet rs) throws SQLException {
        return new Funcionario(
            rs.getInt("codigo"),
            rs.getString("cpf"),
            rs.getString("funcao"),
            rs.getString("nome"),
            rs.getDate("data_admissao").toLocalDate(),
            rs.getInt("id_filial")
        );
    }
}
