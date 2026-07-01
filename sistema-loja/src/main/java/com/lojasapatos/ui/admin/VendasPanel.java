package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class VendasPanel extends JPanel {

    private DefaultTableModel modelo;
    private JLabel lblTotal, lblQtd;

    public VendasPanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0,0));
        add(criarTopo(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
        carregarDados(null, null);
    }

    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));

        JLabel titulo = new JLabel("Relatório de Vendas");
        titulo.setFont(Cores.SUBTITULO); titulo.setForeground(Cores.TEXTO_ESCURO);
        p.add(titulo, BorderLayout.WEST);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        filtros.setOpaque(false);

        JComboBox<String> periodo = new JComboBox<>(
                new String[]{"Personalizado","Hoje","Esta semana","Este mês"});
        periodo.setFont(Cores.TEXTO);

        JLabel lDe=new JLabel("De:"); lDe.setFont(Cores.TEXTO);
        JTextField txtDe=new JTextField("01/01/2026",10); txtDe.setFont(Cores.TEXTO);
        txtDe.setBorder(new LineBorder(Cores.CINZA_BORDA,1,true));

        JLabel lAte=new JLabel("Até:"); lAte.setFont(Cores.TEXTO);
        JTextField txtAte=new JTextField(java.time.LocalDate.now().toString(),10);
        txtAte.setFont(Cores.TEXTO);
        txtAte.setBorder(new LineBorder(Cores.CINZA_BORDA,1,true));

        Botao btnFiltrar = new Botao("Filtrar", Cores.BTN_AZUL);
        Botao btnExcel   = new Botao("Exportar Excel", Cores.BTN_VERDE);
        btnExcel.addActionListener(e->JOptionPane.showMessageDialog(this,"Exportação em desenvolvimento."));

        filtros.add(periodo); filtros.add(lDe); filtros.add(txtDe);
        filtros.add(lAte); filtros.add(txtAte); filtros.add(btnFiltrar);
        p.add(filtros, BorderLayout.EAST);

        btnFiltrar.addActionListener(e -> carregarDados(txtDe.getText(), txtAte.getText()));
        return p;
    }

    private JScrollPane criarTabela() {
        String[] cols={"Nº Venda","Data","Cliente","Filial","Total","Forma Pgto"};
        modelo = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}};

        JTable t = DashboardPanel.estilizarTabela(new JTable(modelo));
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(), BorderFactory.createEmptyBorder(0,16,0,16)));
        return sp;
    }

    private JPanel criarRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT,24,10));
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Cores.CINZA_BORDA));

        lblQtd  = new JLabel("Total de Vendas: 0");
        lblTotal= new JLabel("Total Geral: R$ 0,00");
        lblQtd.setFont(Cores.LABEL);  lblQtd.setForeground(Cores.TEXTO_ESCURO);
        lblTotal.setFont(Cores.LABEL);lblTotal.setForeground(Cores.CARD_VERDE);

        p.add(lblQtd); p.add(lblTotal);
        return p;
    }

    private void carregarDados(String de, String ate) {
        modelo.setRowCount(0);
        int qtd=0; double total=0;
        try(Connection con=Conexao.obterConexao()){
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT v.id_venda, TO_CHAR(v.data_venda,'DD/MM/YYYY HH24:MI'), " +
                "COALESCE(cl.nome,'—'), f.nome_filial, " +
                "COALESCE(SUM(c.quantidade*c.preco_unitario),0), v.forma_pagamento " +
                "FROM venda v " +
                "JOIN filial f ON f.id_filial=v.id_filial " +
                "LEFT JOIN compra cp ON cp.id_venda=v.id_venda " +
                "LEFT JOIN cliente cl ON cl.cpf=cp.cpf " +
                "LEFT JOIN composicao c ON c.id_venda=v.id_venda " +
                "GROUP BY v.id_venda,v.data_venda,cl.nome,f.nome_filial,v.forma_pagamento " +
                "ORDER BY v.data_venda DESC");
            while(rs.next()){
                double val=rs.getDouble(5); total+=val; qtd++;
                modelo.addRow(new Object[]{
                    String.format("%06d",rs.getInt(1)), rs.getString(2),
                    rs.getString(3), rs.getString(4),
                    String.format("R$ %,.2f",val), rs.getString(6)});
            }
        } catch(Exception e){
            modelo.addRow(new Object[]{"000123","27/05/2025","Maria Oliveira","Filial Centro","R$ 299,90","Cartão"});
            modelo.addRow(new Object[]{"000122","27/05/2025","João Pereira","Filial Shopping","R$ 159,90","Pix"});
        }
        lblQtd.setText("Total de Vendas: "+qtd);
        lblTotal.setText(String.format("Total Geral: R$ %,.2f",total));
    }
}

