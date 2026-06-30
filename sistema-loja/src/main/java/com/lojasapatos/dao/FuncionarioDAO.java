package com.lojasapatos.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.lojasapatos.model.Funcionario;

public class FuncionarioDAO {
    public void salvar(Funcionario f) {
        String sql = "INSERT INTO funcionarios (cpf, funcao, nome, data_admissao, id_filial) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getCpf());
            stmt.setString(2, f.getFuncao());
            stmt.setString(3, f.getNome());
            stmt.setDate(4, Date.valueOf(f.getDataAdmissao()));
            stmt.setInt(5, f.getIdFilial());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public List<Funcionario> listar() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionarios";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Funcionario(rs.getInt("codigo"), rs.getString("cpf"), rs.getString("funcao"), rs.getString("nome"), rs.getDate("data_admissao").toLocalDate(), rs.getInt("id_filial"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Funcionario f) {
        String sql = "UPDATE funcionarios SET cpf=?, funcao=?, nome=?, data_admissao=?, id_filial=? WHERE codigo=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, f.getCpf()); stmt.setString(2, f.getFuncao()); stmt.setString(3, f.getNome()); stmt.setDate(4, Date.valueOf(f.getDataAdmissao())); stmt.setInt(5, f.getIdFilial()); stmt.setInt(6, f.getCodigo()); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(int codigo) {
        String sql = "DELETE FROM funcionarios WHERE codigo=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setInt(1, codigo); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}