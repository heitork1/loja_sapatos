package com.lojasapatos.ui.cliente;

import com.lojasapatos.dao.*;
import com.lojasapatos.model.*;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class CarrinhoPanel extends JPanel {

    private final ClienteFrame frame;
    private DefaultTableModel modelo;
    private JLabel lblSubtotal, lblDesconto, lblTotal;
    private JTextField txtCupom;
    private List<int[]> carrinhoRef;
    private Map<Integer,Object[]> produtosRef;

    public CarrinhoPanel(ClienteFrame frame) {
        this.frame = frame;
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0,0));
        add(criarTopo(),    BorderLayout.NORTH);
        add(criarCorpo(),   BorderLayout.CENTER);
    }

    private JPanel criarTopo() {
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));

        JButton btnVoltar=new JButton("← Continuar Comprando");
        btnVoltar.setFont(Cores.TEXTO); btnVoltar.setForeground(Cores.CLIENTE_ACCENT);
        btnVoltar.setContentAreaFilled(false); btnVoltar.setBorderPainted(false);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVoltar.addActionListener(e->frame.irParaCatalogo());

        JLabel titulo=new JLabel("Carrinho de Compras");
        titulo.setFont(Cores.SUBTITULO); titulo.setForeground(Cores.TEXTO_ESCURO);

        p.add(btnVoltar,BorderLayout.WEST);
        p.add(titulo,   BorderLayout.CENTER);
        return p;
    }

    private JPanel criarCorpo() {
        JPanel p=new JPanel(new BorderLayout(16,0));
        p.setBackground(Cores.CINZA_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(16,24,16,24));
        p.add(criarTabela(),  BorderLayout.CENTER);
        p.add(criarResumo(),  BorderLayout.EAST);
        return p;
    }


    private JPanel criarTabela() {
        JPanel card=new JPanel(new BorderLayout());
        card.setBackground(Cores.BRANCO);
        card.setBorder(new SombraBorda());

        String[] cols={"Produto","Tamanho","Quantidade","Preço Unit.","Subtotal",""};
        modelo=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}
            public Class<?> getColumnClass(int c){return c==5?JButton.class:Object.class;}
        };
        JTable t=com.lojasapatos.ui.admin.DashboardPanel.estilizarTabela(new JTable(modelo));
        t.getColumnModel().getColumn(5).setMaxWidth(40);

        // Botão de remoção por linha
        t.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e){
                int row=t.rowAtPoint(e.getPoint()), col=t.columnAtPoint(e.getPoint());
                if(col==5&&row>=0){removerItem(row);}
            }
        });

        card.add(new JScrollPane(t), BorderLayout.CENTER);
        return card;
    }


    private JPanel criarResumo() {
        JPanel card=new JPanel();
        card.setBackground(Cores.BRANCO);
        card.setPreferredSize(new Dimension(260,0));
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(),BorderFactory.createEmptyBorder(20,20,20,20)));

        JLabel titulo=new JLabel("Resumo");
        titulo.setFont(Cores.SUBTITULO); titulo.setForeground(Cores.TEXTO_ESCURO);
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        card.add(titulo); card.add(Box.createVerticalStrut(16));

        lblSubtotal=resumoLinha("Subtotal:",  "R$ 0,00"); card.add(lblSubtotal.getParent());
        card.add(Box.createVerticalStrut(6));
        lblDesconto=resumoLinha("Desconto:",  "R$ 0,00"); card.add(lblDesconto.getParent());
        card.add(Box.createVerticalStrut(6));

        // Cupom
        JLabel lCupom=new JLabel("Cupom de desconto"); lCupom.setFont(Cores.LABEL);
        lCupom.setForeground(Cores.CINZA_TEXTO); lCupom.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lCupom); card.add(Box.createVerticalStrut(4));

        JPanel pCupom=new JPanel(new BorderLayout(4,0));
        pCupom.setOpaque(false); pCupom.setAlignmentX(LEFT_ALIGNMENT);
        pCupom.setMaximumSize(new Dimension(220,32));
        txtCupom=new JTextField(); txtCupom.setFont(Cores.TEXTO);
        txtCupom.setBorder(new LineBorder(Cores.CINZA_BORDA,1,true));
        Botao btnAplicar=new Botao("Aplicar",Cores.BTN_CINZA);
        btnAplicar.setPreferredSize(new Dimension(70,28));
        btnAplicar.addActionListener(e->aplicarCupom());
        pCupom.add(txtCupom,BorderLayout.CENTER); pCupom.add(btnAplicar,BorderLayout.EAST);
        card.add(pCupom); card.add(Box.createVerticalStrut(12));

        JSeparator sep=new JSeparator(); sep.setAlignmentX(LEFT_ALIGNMENT);
        sep.setMaximumSize(new Dimension(220,1)); card.add(sep);
        card.add(Box.createVerticalStrut(10));

        lblTotal=resumoLinha("Total:", "R$ 0,00"); card.add(lblTotal.getParent());
        lblTotal.setFont(new Font("Segoe UI",Font.BOLD,16));
        lblTotal.setForeground(Cores.CLIENTE_ACCENT);

        card.add(Box.createVerticalStrut(20));
        Botao btnFinalizar=new Botao("  Finalizar Compra  ",Cores.CLIENTE_VERDE);
        btnFinalizar.setAlignmentX(LEFT_ALIGNMENT);
        btnFinalizar.setMaximumSize(new Dimension(220,42));
        btnFinalizar.addActionListener(e->finalizarCompra());
        card.add(btnFinalizar);
        return card;
    }

    private JLabel resumoLinha(String rotulo, String valor) {
        JPanel linha=new JPanel(new BorderLayout());
        linha.setOpaque(false); linha.setAlignmentX(LEFT_ALIGNMENT);
        linha.setMaximumSize(new Dimension(220,24));
        JLabel lR=new JLabel(rotulo); lR.setFont(Cores.TEXTO); lR.setForeground(Cores.CINZA_TEXTO);
        JLabel lV=new JLabel(valor);  lV.setFont(Cores.LABEL); lV.setForeground(Cores.TEXTO_ESCURO);
        linha.add(lR,BorderLayout.WEST); linha.add(lV,BorderLayout.EAST);
        // Retorna o JLabel do valor para poder atualizar depois
        // (o parent é o 'linha' que adicionamos ao card)
        lV.putClientProperty("parent",linha);
        return lV;
    }

    public void recarregar(List<int[]> carrinho, Map<Integer,Object[]> produtos) {
        this.carrinhoRef=carrinho; this.produtosRef=produtos;
        modelo.setRowCount(0);
        BigDecimal subtotal=BigDecimal.ZERO;
        for(int[] item:carrinho){
            Object[] info=produtos.get(item[0]);
            if(info==null) continue;
            String nome=(String)info[0];
            BigDecimal preco=(BigDecimal)info[1];
            BigDecimal sub=preco.multiply(new BigDecimal(item[1]));
            subtotal=subtotal.add(sub);
            modelo.addRow(new Object[]{
                nome, "—", item[1]+" — +", "R$ "+preco, "R$ "+sub, "🗑"});
        }
        lblSubtotal.setText("R$ "+subtotal);
        lblDesconto.setText("R$ 0,00");
        lblTotal.setText("R$ "+subtotal);
    }

    private void removerItem(int row){
        if(carrinhoRef!=null&&row<carrinhoRef.size()){
            carrinhoRef.remove(row);
            recarregar(carrinhoRef,produtosRef);
        }
    }

    private void aplicarCupom(){
        String cupom=txtCupom.getText().trim();
        if("SAPATO10".equalsIgnoreCase(cupom))
            JOptionPane.showMessageDialog(this,"Cupom aplicado! 10% de desconto.");
        else
            JOptionPane.showMessageDialog(this,"Cupom inválido.","Cupom",JOptionPane.WARNING_MESSAGE);
    }

    private void finalizarCompra(){
        if(carrinhoRef==null||carrinhoRef.isEmpty()){
            JOptionPane.showMessageDialog(this,"Seu carrinho está vazio!"); return;
        }
        String[] formas={"Dinheiro","PIX","Cartão de Débito","Cartão de Crédito"};
        String forma=(String)JOptionPane.showInputDialog(this,"Forma de pagamento:","Finalizar Compra",
            JOptionPane.QUESTION_MESSAGE,null,formas,formas[1]);
        if(forma==null) return;

        try(Connection con=Conexao.obterConexao()){
            con.setAutoCommit(false);
            // Inserir venda
            PreparedStatement ps=con.prepareStatement(
                "INSERT INTO venda(data_venda,forma_pagamento,id_filial,id_funcionario) " +
                "VALUES(?,?,1,1) RETURNING id_venda");
            ps.setTimestamp(1,Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(2,forma);
            ResultSet rs=ps.executeQuery();
            int idVenda=rs.next()?rs.getInt(1):0;
            // Inserir itens
            for(int[] item:carrinhoRef){
                Object[] info=produtosRef.get(item[0]);
                BigDecimal preco=info!=null?(BigDecimal)info[1]:BigDecimal.ONE;
                PreparedStatement pi=con.prepareStatement(
                    "INSERT INTO composicao(id_venda,codigo,quantidade,preco_unitario) VALUES(?,?,?,?)");
                pi.setInt(1,idVenda); pi.setInt(2,item[0]);
                pi.setInt(3,item[1]); pi.setBigDecimal(4,preco);
                pi.executeUpdate();
            }
            con.commit();
            carrinhoRef.clear();
            modelo.setRowCount(0);
            lblSubtotal.setText("R$ 0,00"); lblTotal.setText("R$ 0,00");
            JOptionPane.showMessageDialog(this,
                "✅ Compra realizada com sucesso!\nNúmero do pedido: "+String.format("%06d",idVenda),
                "Pedido Confirmado",JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,"Erro ao finalizar compra: "+e.getMessage());
        }
    }
}

