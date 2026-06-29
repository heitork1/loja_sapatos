package com.lojasapatos.dao;
import java.sql.*;
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
}