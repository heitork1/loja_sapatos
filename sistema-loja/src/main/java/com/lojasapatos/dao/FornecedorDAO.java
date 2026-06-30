package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.lojasapatos.model.Fornecedor;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;


public class FornecedorDAO {
    public void salvar(Fornecedor f) {
        String sql = "INSERT INTO fornecedor (razao_social, cnpj, contato) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getRazaoSocial());
            stmt.setString(2, f.getCnpj());
            stmt.setString(3, f.getContato());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public List<Fornecedor> listar() {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) { lista.add(new Fornecedor(rs.getString("razao_social"), rs.getString("cnpj"), rs.getString("contato"))); }
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
        return lista;
    }

    public void atualizar(Fornecedor f) {
        String sql = "UPDATE fornecedor SET cnpj=?, contato=? WHERE razao_social=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, f.getCnpj()); stmt.setString(2, f.getContato()); stmt.setString(3, f.getRazaoSocial());
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }

    public void deletar(String razaoSocial) {
        String sql = "DELETE FROM fornecedor WHERE razao_social=?";
        try (Connection c = Conexao.getConexao(); PreparedStatement stmt = c.prepareStatement(sql)) {
            stmt.setString(1, razaoSocial); stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Erro: " + e.getMessage()); }
    }
}