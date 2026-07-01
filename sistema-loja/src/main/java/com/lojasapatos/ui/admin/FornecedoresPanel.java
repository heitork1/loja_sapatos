package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FornecedoresPanel extends JPanel {

    private DefaultTableModel modeloSolic;
    private JTable tabelaSolic;
    private JLabel lblForn=new JLabel("—"), lblStatus=new JLabel("—"),
                   lblEntrega=new JLabel("—");
    private DefaultTableModel modeloItens;

    public FornecedoresPanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        add(criarTopo(),  BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        carregarDados();
    }

    private JPanel criarTopo() {
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));

        JLabel t=new JLabel("Solicitações ao Fornecedor");
        t.setFont(Cores.SUBTITULO); t.setForeground(Cores.TEXTO_ESCURO);
        p.add(t,BorderLayout.WEST);

        JPanel acoes=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        acoes.setOpaque(false);

        JTextField busca=new JTextField(16); busca.setFont(Cores.TEXTO);
        busca.setBorder(new LineBorder(Cores.CINZA_BORDA,1,true));
        busca.putClientProperty("JTextField.placeholderText","Buscar solicitação...");

        Botao btnNova   =new Botao("+ Nova Solicitação",Cores.BTN_VERDE);
        Botao btnEditar =new Botao("✎ Editar",          Cores.BTN_AZUL);
        Botao btnExcluir=new Botao("✖ Excluir",         Cores.BTN_VERMELHO);

        btnNova.addActionListener(e->novaSolicitacao());

        acoes.add(btnNova);acoes.add(btnEditar);acoes.add(btnExcluir);acoes.add(busca);
        p.add(acoes,BorderLayout.EAST);
        return p;
    }

    private JSplitPane criarCorpo() {
        String[] cols={"Nº Solicitação","Fornecedor","Data","Status","Total Itens"};
        modeloSolic=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}};
        tabelaSolic=DashboardPanel.estilizarTabela(new JTable(modeloSolic));
        tabelaSolic.getSelectionModel().addListSelectionListener(e->{
            if(!e.getValueIsAdjusting()) preencherDetalhe();});

        JPanel pSolic=new JPanel(new BorderLayout());
        pSolic.setBackground(Cores.BRANCO);
        pSolic.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(),BorderFactory.createEmptyBorder(8,8,8,8)));
        pSolic.add(new JScrollPane(tabelaSolic),BorderLayout.CENTER);

        JPanel pDetalhe=new JPanel(new BorderLayout(0,8));
        pDetalhe.setBackground(Cores.CINZA_FUNDO);

        JPanel info=new JPanel(new GridLayout(3,2,6,6));
        info.setBackground(Cores.BRANCO);
        info.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(),BorderFactory.createEmptyBorder(12,12,12,12)));

        String[] labels={"Fornecedor:","Status:","Previsão Entrega:"};
        JLabel[] vals={lblForn,lblStatus,lblEntrega};
        for(int i=0;i<labels.length;i++){
            JLabel l=new JLabel(labels[i]); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            info.add(l);
            vals[i].setFont(Cores.TEXTO); vals[i].setForeground(Cores.TEXTO_ESCURO);
            info.add(vals[i]);
        }
        pDetalhe.add(info,BorderLayout.NORTH);

        JPanel pItens=new JPanel(new BorderLayout());
        pItens.setBackground(Cores.BRANCO);
        pItens.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(),BorderFactory.createEmptyBorder(8,8,8,8)));
        JLabel lItens=new JLabel("Itens Solicitados"); lItens.setFont(Cores.LABEL);
        lItens.setBorder(BorderFactory.createEmptyBorder(4,4,8,4));
        pItens.add(lItens,BorderLayout.NORTH);

        String[] cItens={"Produto","Quantidade"};
        modeloItens=new DefaultTableModel(cItens,0){
            public boolean isCellEditable(int r,int c){return false;}};
        pItens.add(new JScrollPane(DashboardPanel.estilizarTabela(new JTable(modeloItens))),BorderLayout.CENTER);
        pDetalhe.add(pItens,BorderLayout.CENTER);

        JSplitPane sp=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,pSolic,pDetalhe);
        sp.setDividerLocation(680); sp.setBorder(null);
        return sp;
    }

    private void carregarDados() {
        modeloSolic.setRowCount(0);
        try(Connection con=Conexao.obterConexao()){
            ResultSet rs=con.createStatement().executeQuery(
                "SELECT id_solicitacao, razao_social, data_entrega, status, quantidade " +
                "FROM solicitacao ORDER BY id_solicitacao DESC");
            while(rs.next())
                modeloSolic.addRow(new Object[]{
                    String.format("%05d",rs.getInt(1)),rs.getString(2),
                    rs.getString(3),rs.getString(4),rs.getInt(5)});
        } catch(Exception e){
            modeloSolic.addRow(new Object[]{"00045","Calçados Brasil LTDA","27/05/2025","Pendente",12});
            modeloSolic.addRow(new Object[]{"00044","Calçados Brasil LTDA","20/05/2025","Aprovada",8});
            modeloSolic.addRow(new Object[]{"00043","Calçados Brasil LTDA","13/05/2025","Recebida",15});
        }
    }

    private void preencherDetalhe() {
        int row=tabelaSolic.getSelectedRow();
        if(row<0) return;
        lblForn.setText((String)modeloSolic.getValueAt(row,1));
        lblStatus.setText((String)modeloSolic.getValueAt(row,3));
        lblEntrega.setText(modeloSolic.getValueAt(row,2)+"");
        modeloItens.setRowCount(0);
        try(Connection con=Conexao.obterConexao()){
            String id=(String)modeloSolic.getValueAt(row,0);
            ResultSet rs=con.createStatement().executeQuery(
                "SELECT p.marca, s.quantidade FROM solicitacao s JOIN produto p ON p.codigo=s.codigo " +
                "WHERE s.id_solicitacao="+Integer.parseInt(id));
            while(rs.next()) modeloItens.addRow(new Object[]{rs.getString(1),rs.getInt(2)});
        } catch(Exception ignored){
            modeloItens.addRow(new Object[]{"Tênis Runner",30});
            modeloItens.addRow(new Object[]{"Sapato Social",20});
        }
    }

    private void novaSolicitacao(){
        JPanel f=new JPanel(new GridLayout(4,2,8,8));
        f.add(new JLabel("Fornecedor:")); JTextField forn=new JTextField(); f.add(forn);
        f.add(new JLabel("Produto (código):")); JTextField prod=new JTextField(); f.add(prod);
        f.add(new JLabel("Quantidade:")); JTextField qtd=new JTextField(); f.add(qtd);
        f.add(new JLabel("Previsão entrega:")); JTextField dt=new JTextField(); f.add(dt);
        int r=JOptionPane.showConfirmDialog(this,f,"Nova Solicitação",JOptionPane.OK_CANCEL_OPTION);
        if(r==JOptionPane.OK_OPTION){
            try(Connection con=Conexao.obterConexao()){
                PreparedStatement ps=con.prepareStatement(
                    "INSERT INTO solicitacao(status,quantidade,custo,data_entrega,razao_social,codigo,id_sede) " +
                    "VALUES('Pendente',?,0,?,?,?,1)");
                ps.setInt(1,Integer.parseInt(qtd.getText().trim()));
                ps.setString(2,dt.getText().trim());
                ps.setString(3,forn.getText().trim());
                ps.setInt(4,Integer.parseInt(prod.getText().trim()));
                ps.executeUpdate();
                carregarDados();
            } catch(Exception e){
                JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage());
            }
        }
    }
}
