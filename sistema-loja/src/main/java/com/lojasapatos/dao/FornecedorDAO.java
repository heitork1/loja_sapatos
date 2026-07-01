package com.lojasapatos.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.lojasapatos.model.Fornecedor;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;


public class FornecedorDAO {
    public void inserir(Fornecedor f) throws SQLException {
        String sql = "INSERT INTO fornecedor (razao_social, cnpj, contato) VALUES (?, ?, ?)";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getRazaoSocial());
            ps.setString(2, f.getCnpj());
            ps.setString(3, f.getContato());
            ps.executeUpdate();
        }
    }

    public void atualizar(Fornecedor f) throws SQLException {
        String sql = "UPDATE fornecedor SET cnpj = ?, contato = ? WHERE razao_social = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getCnpj());
            ps.setString(2, f.getContato());
            ps.setString(3, f.getRazaoSocial());
            ps.executeUpdate();
        }
    }

    public void excluir(String razaoSocial) throws SQLException {
        String sql = "DELETE FROM fornecedor WHERE razao_social = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, razaoSocial);
            ps.executeUpdate();
        }
    }

    public Fornecedor buscarPorId(String razaoSocial) throws SQLException {
        String sql = "SELECT * FROM fornecedor WHERE razao_social = ?";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, razaoSocial);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
            return null;
        }
    }

    public List<Fornecedor> listarTodos() throws SQLException {
        List<Fornecedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor ORDER BY razao_social";
        try (Connection con = Conexao.obterConexao();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Fornecedor mapear(ResultSet rs) throws SQLException {
        return new Fornecedor(rs.getString("razao_social"), rs.getString("cnpj"), rs.getString("contato"));
    }
}
