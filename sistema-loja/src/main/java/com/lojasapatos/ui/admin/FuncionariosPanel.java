package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FuncionariosPanel extends JPanel {

    private DefaultTableModel modelo;
    private JTable tabela;
    private JLabel lblNome=new JLabel("—"), lblCargo=new JLabel("—"),
                   lblCPF=new JLabel("—"),  lblData=new JLabel("—"),
                   lblTel=new JLabel("—"),  lblStatus=new JLabel("Ativo");

    public FuncionariosPanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0,0));
        add(criarTopo(),  BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        carregarDados();
    }

    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));

        JLabel titulo=new JLabel("Gestão de Pessoal — Funcionários");
        titulo.setFont(Cores.SUBTITULO); titulo.setForeground(Cores.TEXTO_ESCURO);
        p.add(titulo,BorderLayout.WEST);

        JPanel acoes=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        acoes.setOpaque(false);

        JTextField txtBusca=new JTextField(18);
        txtBusca.setFont(Cores.TEXTO);
        txtBusca.setBorder(new LineBorder(Cores.CINZA_BORDA,1,true));
        txtBusca.putClientProperty("JTextField.placeholderText","Buscar funcionário...");

        Botao btnNovo   =new Botao("+ Novo Funcionário", Cores.BTN_VERDE);
        Botao btnEditar =new Botao("✎ Editar",           Cores.BTN_AZUL);
        Botao btnExcluir=new Botao("✖ Excluir",          Cores.BTN_VERMELHO);

        btnNovo.addActionListener(e->JOptionPane.showMessageDialog(this,"Formulário em desenvolvimento."));

        acoes.add(btnNovo); acoes.add(btnEditar); acoes.add(btnExcluir); acoes.add(txtBusca);
        p.add(acoes,BorderLayout.EAST);
        return p;
    }

    private JSplitPane criarCorpo() {
        // Tabela
        String[] cols={"Código","Nome","Cargo","Unidade","Status"};
        modelo=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}};
        tabela=DashboardPanel.estilizarTabela(new JTable(modelo));
        tabela.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting()) preencherDetalhe();});

        JPanel pTabela=new JPanel(new BorderLayout());
        pTabela.setBackground(Cores.BRANCO);
        pTabela.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(),BorderFactory.createEmptyBorder(8,8,8,8)));
        pTabela.add(new JScrollPane(tabela),BorderLayout.CENTER);

        // Detalhe
        JPanel pDetalhe=new JPanel();
        pDetalhe.setBackground(Cores.BRANCO);
        pDetalhe.setLayout(new BoxLayout(pDetalhe,BoxLayout.Y_AXIS));
        pDetalhe.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(),BorderFactory.createEmptyBorder(16,16,16,16)));

        JTabbedPane tabs=new JTabbedPane();
        tabs.setFont(Cores.TEXTO);
        tabs.addTab("Dados Pessoais", criarAbaDados());
        tabs.addTab("Endereço",       criarAbaEndereco());
        pDetalhe.add(tabs);

        JSplitPane sp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pTabela,pDetalhe);
        sp.setDividerLocation(700); sp.setBorder(null);
        return sp;
    }

    private JPanel criarAbaDados() {
        JPanel p=new JPanel(new GridBagLayout());
        p.setBackground(Cores.BRANCO);
        GridBagConstraints g=new GridBagConstraints();
        g.fill=GridBagConstraints.HORIZONTAL; g.insets=new Insets(6,4,6,4);

        String[][] rows={{"Nome:",""}, {"Cargo:",""}, {"CPF:",""}, {"Data de Admissão:",""}, {"Telefone:",""}, {"Status",""}};
        JLabel[] vals={lblNome, lblCargo, lblCPF, lblData, lblTel, lblStatus};

        for(int i=0;i<rows.length;i++){
            g.gridx=0; g.gridy=i; g.weightx=0;
            JLabel l=new JLabel(rows[i][0]); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            p.add(l,g);
            g.gridx=1; g.weightx=1;
            vals[i].setFont(Cores.TEXTO); vals[i].setForeground(Cores.TEXTO_ESCURO);
            p.add(vals[i],g);
        }
        return p;
    }

    private JPanel criarAbaEndereco() {
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.add(new JLabel("  Endereço do funcionário selecionado",SwingConstants.CENTER),BorderLayout.CENTER);
        return p;
    }

    private void carregarDados() {
        modelo.setRowCount(0);
        try(Connection con=Conexao.obterConexao()){
            ResultSet rs=con.createStatement().executeQuery(
                "SELECT fn.codigo, fn.nome, fn.funcao, f.nome_filial, 'Ativo' " +
                "FROM funcionario fn JOIN filial f ON f.id_filial=fn.id_filial ORDER BY fn.codigo");
            while(rs.next())
                modelo.addRow(new Object[]{
                    String.format("%03d",rs.getInt(1)),rs.getString(2),
                    rs.getString(3),rs.getString(4),rs.getString(5)});
        } catch(Exception e){
            modelo.addRow(new Object[]{"001","João Silva","Gerente","Filial Centro","Ativo"});
            modelo.addRow(new Object[]{"002","Maria Souza","Vendedora","Filial Centro","Ativo"});
            modelo.addRow(new Object[]{"003","Pedro Santos","Estoquista","Filial Shopping","Ativo"});
        }
    }

    private void preencherDetalhe() {
        int row=tabela.getSelectedRow();
        if(row<0) return;
        lblNome.setText((String)modelo.getValueAt(row,1));
        lblCargo.setText((String)modelo.getValueAt(row,2));
        lblStatus.setText((String)modelo.getValueAt(row,4));
        try(Connection con=Conexao.obterConexao()){
            int cod=Integer.parseInt(((String)modelo.getValueAt(row,0)));
            ResultSet rs=con.createStatement().executeQuery(
                "SELECT cpf,data_admissao,telefone FROM funcionario WHERE codigo="+cod);
            if(rs.next()){
                lblCPF.setText(rs.getString(1));
                lblData.setText(rs.getString(2));
                lblTel.setText(rs.getString(3)!=null?rs.getString(3):"—");
            }
        } catch(Exception ignored){}
    }
}
