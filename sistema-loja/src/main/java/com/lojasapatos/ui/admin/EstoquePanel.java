package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.*;
import com.lojasapatos.model.*;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.List;

public class EstoquePanel extends JPanel {

    private DefaultTableModel modeloProdutos;
    private JTable tabelaProdutos;
    private JTextField txtBusca;
    // Campos de detalhe
    private JTextField fNome = new JTextField(), fCategoria = new JTextField(),
                       fMarca= new JTextField(), fPreco = new JTextField();
    private JLabel lblEstoque = new JLabel("—");
    private JCheckBox chkAtivo = new JCheckBox("Ativo");

    public EstoquePanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0,0));
        add(criarTopo(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                criarTabela(), criarDetalhe());
        split.setDividerLocation(680);
        split.setBorder(null);
        split.setBackground(Cores.CINZA_FUNDO);
        add(split, BorderLayout.CENTER);

        carregarDados();
    }
/
    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));

        JLabel titulo = new JLabel("Gestão de Estoque — Produtos");
        titulo.setFont(Cores.SUBTITULO);
        titulo.setForeground(Cores.TEXTO_ESCURO);
        p.add(titulo, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);
        txtBusca = new JTextField(18);
        txtBusca.setFont(Cores.TEXTO);
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Cores.CINZA_BORDA,1,true),
                BorderFactory.createEmptyBorder(4,8,4,8)));
        txtBusca.putClientProperty("JTextField.placeholderText","Buscar produto...");
        txtBusca.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){filtrar();}
            public void removeUpdate(DocumentEvent e){filtrar();}
            public void changedUpdate(DocumentEvent e){filtrar();}
        });

        Botao btnNovo  = new Botao("+ Novo Produto", Cores.BTN_VERDE);
        Botao btnEditar= new Botao("✎ Editar",       Cores.BTN_AZUL);
        Botao btnExcluir=new Botao("✖ Excluir",      Cores.BTN_VERMELHO);

        btnNovo.addActionListener(e -> novoProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());

        acoes.add(btnNovo); acoes.add(btnEditar); acoes.add(btnExcluir); acoes.add(txtBusca);
        p.add(acoes, BorderLayout.EAST);
        return p;
    }

    private JPanel criarTabela() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(), BorderFactory.createEmptyBorder(8,8,8,8)));

        String[] cols = {"Código","Produto","Categoria","Preço","Estoque"};
        modeloProdutos = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}};
        tabelaProdutos = DashboardPanel.estilizarTabela(new JTable(modeloProdutos));
        tabelaProdutos.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting()) preencherDetalhe();
        });
        p.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarDetalhe() {
        JPanel p = new JPanel();
        p.setBackground(Cores.BRANCO);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(), BorderFactory.createEmptyBorder(16,16,16,16)));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Cores.TEXTO);
        tabs.addTab("Detalhes do Produto", criarAbaDetalhes());
        tabs.addTab("Estoque por Filial",   criarAbaEstoque());
        p.add(tabs);
        return p;
    }

    private JPanel criarAbaDetalhes() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Cores.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.insets = new Insets(4,4,4,4);

        String[][] campos = {{"Nome:",""},{"Descrição:",""},{"Categoria:",""},
                             {"Marca:",""},{"Preço:",""}};
        JTextField[] fields = {fNome, new JTextField(), fCategoria, fMarca, fPreco};

        for(int i=0;i<campos.length;i++){
            gbc.gridx=0; gbc.gridy=i; gbc.weightx=0;
            JLabel l=new JLabel(campos[i][0]); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            p.add(l,gbc);
            gbc.gridx=1; gbc.weightx=1;
            fields[i].setFont(Cores.TEXTO);
            p.add(fields[i],gbc);
        }
        gbc.gridx=0; gbc.gridy=campos.length; gbc.weightx=0;
        JLabel lEst=new JLabel("Estoque Mínimo:"); lEst.setFont(Cores.LABEL); lEst.setForeground(Cores.CINZA_TEXTO);
        p.add(lEst,gbc);
        gbc.gridx=1; gbc.weightx=1;
        p.add(lblEstoque,gbc);

        gbc.gridx=0; gbc.gridy=campos.length+1; gbc.gridwidth=2;
        chkAtivo.setFont(Cores.TEXTO); chkAtivo.setOpaque(false);
        p.add(chkAtivo,gbc);
        return p;
    }

    private JPanel criarAbaEstoque() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        String[] cols = {"Filial","Qtd Disponível","Qtd Mínima"};
        DefaultTableModel m = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}};
        p.add(new JScrollPane(DashboardPanel.estilizarTabela(new JTable(m))), BorderLayout.CENTER);
        return p;
    }
  
    void carregarDados() {
        modeloProdutos.setRowCount(0);
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT p.codigo, p.marca||' '||m.cor, p.categoria, p.preco, " +
                "COALESCE((SELECT SUM(a.quantidade) FROM armazenamento a WHERE a.codigo=p.codigo),0) " +
                "FROM produto p JOIN modelo m ON m.codigo=p.id_modelo ORDER BY p.codigo");
            while(rs.next())
                modeloProdutos.addRow(new Object[]{
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    "R$ "+rs.getBigDecimal(4), rs.getInt(5)});
        } catch(Exception e) {
            modeloProdutos.addRow(new Object[]{1,"Tênis Runner Preto","Tênis","R$ 189,90",25});
            modeloProdutos.addRow(new Object[]{2,"Sapato Social Couro","Social","R$ 259,90",12});
        }
    }

    private void filtrar() {
        String q = txtBusca.getText().toLowerCase();
        modeloProdutos.setRowCount(0);
        try (Connection con = Conexao.obterConexao()) {
            PreparedStatement ps = con.prepareStatement(
                "SELECT p.codigo, p.marca||' '||m.cor, p.categoria, p.preco, " +
                "COALESCE((SELECT SUM(a.quantidade) FROM armazenamento a WHERE a.codigo=p.codigo),0) " +
                "FROM produto p JOIN modelo m ON m.codigo=p.id_modelo " +
                "WHERE LOWER(p.marca) LIKE ? OR LOWER(p.categoria) LIKE ? ORDER BY p.codigo");
            ps.setString(1,"%"+q+"%"); ps.setString(2,"%"+q+"%");
            ResultSet rs = ps.executeQuery();
            while(rs.next())
                modeloProdutos.addRow(new Object[]{
                    rs.getInt(1),rs.getString(2),rs.getString(3),"R$ "+rs.getBigDecimal(4),rs.getInt(5)});
        } catch(Exception ignored){}
    }

    private void preencherDetalhe() {
        int row = tabelaProdutos.getSelectedRow();
        if(row<0) return;
        fNome.setText((String)modeloProdutos.getValueAt(row,1));
        fCategoria.setText((String)modeloProdutos.getValueAt(row,2));
        fPreco.setText(modeloProdutos.getValueAt(row,3).toString());
        chkAtivo.setSelected(true);
    }

    private void novoProduto(){
        JOptionPane.showMessageDialog(this,"Formulário de novo produto em desenvolvimento.","Novo Produto",JOptionPane.INFORMATION_MESSAGE);
    }
    private void editarProduto(){
        if(tabelaProdutos.getSelectedRow()<0){JOptionPane.showMessageDialog(this,"Selecione um produto."); return;}
        JOptionPane.showMessageDialog(this,"Edição de produto em desenvolvimento.","Editar",JOptionPane.INFORMATION_MESSAGE);
    }
    private void excluirProduto(){
        int row=tabelaProdutos.getSelectedRow();
        if(row<0){JOptionPane.showMessageDialog(this,"Selecione um produto."); return;}
        int conf = JOptionPane.showConfirmDialog(this,"Excluir produto selecionado?","Confirmar",JOptionPane.YES_NO_OPTION);
        if(conf==JOptionPane.YES_OPTION){
            try(Connection con=Conexao.obterConexao()){
                int cod=(int)modeloProdutos.getValueAt(row,0);
                con.createStatement().executeUpdate("DELETE FROM produto WHERE codigo="+cod);
                carregarDados();
            } catch(Exception e){JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage());}
        }
    }
}
