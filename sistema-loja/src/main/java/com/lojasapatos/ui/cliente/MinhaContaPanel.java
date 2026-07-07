package com.lojasapatos.ui.cliente;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.model.Cliente;
import com.lojasapatos.ui.*;
import com.lojasapatos.ui.admin.DashboardPanel;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class MinhaContaPanel extends JPanel {

    private final ClienteFrame frame;
    private JLabel lblPontos = new JLabel("0");
    private JLabel lblNome   = new JLabel("Visitante");
    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloTodosPedidos;
    private DefaultTableModel modeloPontosHist;
    private CardLayout cardInterno;
    private JPanel conteudo;

    public MinhaContaPanel(ClienteFrame frame) {
        this.frame = frame;
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        add(criarSidebar(), BorderLayout.WEST);

        cardInterno = new CardLayout();
        conteudo = new JPanel(cardInterno);
        conteudo.setBackground(Cores.CINZA_FUNDO);
        conteudo.add(criarPainelResumo(), "Resumo");
        conteudo.add(criarPainelPedidos(),"Pedidos");
        conteudo.add(criarPainelPontos(), "Pontos");
        add(conteudo, BorderLayout.CENTER);
        cardInterno.show(conteudo, "Resumo");
        carregarDados();
    }

    //uma sidebar
    private JPanel criarSidebar() {
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(180, 0));
        p.setBackground(Cores.BRANCO);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        JLabel ico = new JLabel("👤", SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        ico.setAlignmentX(CENTER_ALIGNMENT);
        p.add(ico); p.add(Box.createVerticalStrut(8));

        lblNome.setFont(Cores.LABEL); lblNome.setForeground(Cores.TEXTO_ESCURO);
        lblNome.setAlignmentX(CENTER_ALIGNMENT);
        p.add(lblNome); p.add(Box.createVerticalStrut(20));

        String[][] itens = {{"Resumo","Resumo"},{"Meus Pedidos","Pedidos"},{"Meus Pontos","Pontos"}};
        for (String[] item : itens) {
            final String card = item[1];
            JButton btn = new JButton(item[0]);
            btn.setFont(Cores.TEXTO); btn.setForeground(Cores.TEXTO_ESCURO);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
            btn.setMaximumSize(new Dimension(180, 36)); btn.setAlignmentX(LEFT_ALIGNMENT);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> { cardInterno.show(conteudo, card); recarregarAba(card); });
            p.add(btn);
        }

        p.add(Box.createVerticalGlue());
        JButton btnSair = new JButton("← Voltar à Loja");
        btnSair.setFont(Cores.TEXTO); btnSair.setForeground(Cores.CINZA_TEXTO);
        btnSair.setContentAreaFilled(false); btnSair.setBorderPainted(false);
        btnSair.setFocusPainted(false); btnSair.setAlignmentX(LEFT_ALIGNMENT);
        btnSair.addActionListener(e -> frame.irParaCatalogo());
        p.add(btnSair);
        return p;
    }

    //resumo
    private JPanel criarPainelResumo() {
        JPanel p = new JPanel(new BorderLayout(16, 16));
        p.setBackground(Cores.CINZA_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Card de pontos com gradiente vinho
        JPanel cardPontos = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,Cores.CLIENTE_PRIMARY,getWidth(),getHeight(),new Color(140,20,50)));
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.dispose();
            }
        };
        cardPontos.setOpaque(false);
        cardPontos.setLayout(new BoxLayout(cardPontos, BoxLayout.Y_AXIS));
        cardPontos.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        cardPontos.setPreferredSize(new Dimension(240, 0));

        JLabel lMeus = new JLabel("Meus Pontos"); lMeus.setFont(Cores.SUBTITULO);
        lMeus.setForeground(new Color(220,180,200)); lMeus.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lMeus); cardPontos.add(Box.createVerticalStrut(12));

        lblPontos.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblPontos.setForeground(Cores.BRANCO); lblPontos.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lblPontos);

        JLabel lSub = new JLabel("pontos acumulados"); lSub.setFont(Cores.TEXTO);
        lSub.setForeground(new Color(220,180,200)); lSub.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lSub); cardPontos.add(Box.createVerticalStrut(16));

        JLabel lDica = new JLabel("<html><center>A cada R$ 1,00 gasto<br>você ganha 1 ponto.<br>100 pontos = R$ 5,00<br>de desconto.</center></html>");
        lDica.setFont(Cores.PEQUENO); lDica.setForeground(new Color(200,160,180));
        lDica.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lDica);
        p.add(cardPontos, BorderLayout.WEST);

        //historico de compras
        JPanel cardHist = new JPanel(new BorderLayout());
        cardHist.setBackground(Cores.BRANCO);
        cardHist.setBorder(BorderFactory.createCompoundBorder(
                new SombraBorda(), BorderFactory.createEmptyBorder(16,16,16,16)));
        JLabel lH = new JLabel("Últimas Compras"); lH.setFont(Cores.SUBTITULO); lH.setForeground(Cores.TEXTO_ESCURO);
        cardHist.add(lH, BorderLayout.NORTH);

        String[] cols = {"Nº Pedido","Data","Itens","Total","Status"};
        modeloPedidos = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        cardHist.add(new JScrollPane(DashboardPanel.estilizarTabela(new JTable(modeloPedidos))), BorderLayout.CENTER);
        p.add(cardHist, BorderLayout.CENTER);
        return p;
    }

    //pedidos
    private JPanel criarPainelPedidos() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Cores.CINZA_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JLabel t = new JLabel("Todos os Pedidos");
        t.setFont(Cores.SUBTITULO); t.setForeground(Cores.TEXTO_ESCURO);

        JPanel topo = new JPanel(new BorderLayout()); topo.setBackground(Cores.CINZA_FUNDO);
        topo.add(t, BorderLayout.WEST);
        p.add(topo, BorderLayout.NORTH);

        String[] cols = {"Nº Pedido","Data","Produto","Qtd","Total","Forma Pagamento","Status"};
        modeloTodosPedidos = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Cores.BRANCO);
        card.setBorder(new SombraBorda());
        card.add(new JScrollPane(DashboardPanel.estilizarTabela(new JTable(modeloTodosPedidos))), BorderLayout.CENTER);
        p.add(card, BorderLayout.CENTER);
        return p;
    }

    //pontos
    private JPanel criarPainelPontos() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Cores.CINZA_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JLabel t = new JLabel("Extrato de Pontos");
        t.setFont(Cores.SUBTITULO); t.setForeground(Cores.TEXTO_ESCURO);
        JPanel topo = new JPanel(new BorderLayout()); topo.setBackground(Cores.CINZA_FUNDO);
        topo.add(t, BorderLayout.WEST);
        p.add(topo, BorderLayout.NORTH);

        //saldo de pontos
        JPanel saldo = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        saldo.setOpaque(false);
        JLabel lSaldo = new JLabel("Saldo atual: ");
        lSaldo.setFont(Cores.LABEL); lSaldo.setForeground(Cores.CINZA_TEXTO);
        JLabel lVal = new JLabel("0 pontos  ≈  R$ 0,00 em descontos");
        lVal.setFont(new Font("Segoe UI", Font.BOLD, 14)); lVal.setForeground(Cores.CLIENTE_ACCENT);
        saldo.add(lSaldo); saldo.add(lVal);

        //tabela historico
        String[] cols = {"Data","Descrição","Pontos","Tipo"};
        modeloPontosHist = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Cores.BRANCO);
        card.setBorder(new SombraBorda());
        card.add(saldo, BorderLayout.NORTH);
        card.add(new JScrollPane(DashboardPanel.estilizarTabela(new JTable(modeloPontosHist))), BorderLayout.CENTER);

        lVal.putClientProperty("saldo_label", lVal);
        card.putClientProperty("saldo_ref", lVal);
        p.add(card, BorderLayout.CENTER);

        p.putClientProperty("saldo_label_ref", lVal);
        return p;
    }

    private void carregarDados() {
        Cliente c = frame.getClienteLogado();
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs;
            //nome e pontos
            if (c != null) {
                rs = con.createStatement().executeQuery(
                        "SELECT nome, pontos FROM cliente WHERE cpf = '" + c.getCpf() + "'");
            } else {
                rs = con.createStatement().executeQuery(
                        "SELECT nome, pontos FROM cliente ORDER BY cpf LIMIT 1");
            }
            if (rs.next()) {
                lblNome.setText(rs.getString(1));
                lblPontos.setText(String.valueOf(rs.getInt(2)));
            }

            //ultimas 5 vendas
            carregarResumoVendas(con);

        } catch (Exception e) {
            lblNome.setText("Visitante"); lblPontos.setText("0");
            modeloPedidos.addRow(new Object[]{"000001","—","—","—","—"});
        }
    }

    private void carregarResumoVendas(Connection con) throws SQLException {
        modeloPedidos.setRowCount(0);
        ResultSet rp = con.createStatement().executeQuery(
                "SELECT v.id_venda, TO_CHAR(v.data_venda,'DD/MM/YYYY'), " +
                "COUNT(c.codigo), COALESCE(SUM(c.quantidade*c.preco_unitario),0) " +
                "FROM venda v LEFT JOIN composicao c ON c.id_venda=v.id_venda " +
                "GROUP BY v.id_venda, v.data_venda ORDER BY v.data_venda DESC LIMIT 5");
        while (rp.next())
            modeloPedidos.addRow(new Object[]{
                    String.format("%06d", rp.getInt(1)), rp.getString(2),
                    rp.getInt(3), String.format("R$ %,.2f", rp.getDouble(4)), "Entregue"});
    }

    private void recarregarAba(String card) {
        try (Connection con = Conexao.obterConexao()) {
            if ("Pedidos".equals(card)) {
                modeloTodosPedidos.setRowCount(0);
                ResultSet rs = con.createStatement().executeQuery(
                        "SELECT v.id_venda, TO_CHAR(v.data_venda,'DD/MM/YYYY HH24:MI'), " +
                        "p.marca||' '||m.cor, c.quantidade, " +
                        "c.quantidade*c.preco_unitario, v.forma_pagamento " +
                        "FROM venda v " +
                        "JOIN composicao c ON c.id_venda=v.id_venda " +
                        "JOIN produto p ON p.codigo=c.codigo " +
                        "JOIN modelo m ON m.codigo=p.id_modelo " +
                        "ORDER BY v.data_venda DESC LIMIT 50");
                while (rs.next())
                    modeloTodosPedidos.addRow(new Object[]{
                            String.format("%06d", rs.getInt(1)), rs.getString(2), rs.getString(3),
                            rs.getInt(4), String.format("R$ %,.2f", rs.getDouble(5)),
                            rs.getString(6), "Entregue"});

            } else if ("Pontos".equals(card)) {
                modeloPontosHist.setRowCount(0);
                //historico, cada venda gera pontos
                ResultSet rs = con.createStatement().executeQuery(
                        "SELECT TO_CHAR(v.data_venda,'DD/MM/YYYY'), " +
                        "'Compra #'||v.id_venda, " +
                        "FLOOR(COALESCE(SUM(c.quantidade*c.preco_unitario),0)) " +
                        "FROM venda v LEFT JOIN composicao c ON c.id_venda=v.id_venda " +
                        "GROUP BY v.id_venda, v.data_venda ORDER BY v.data_venda DESC LIMIT 20");
                int total = 0;
                while (rs.next()) {
                    int pts = rs.getInt(3); total += pts;
                    modeloPontosHist.addRow(new Object[]{rs.getString(1), rs.getString(2), "+" + pts, "Crédito"});
                }
                lblPontos.setText(String.valueOf(total));
            }
        } catch (Exception e) {
            if ("Pedidos".equals(card))
                modeloTodosPedidos.addRow(new Object[]{"—","—","—","—","—","—","—"});
        }
    }
}
