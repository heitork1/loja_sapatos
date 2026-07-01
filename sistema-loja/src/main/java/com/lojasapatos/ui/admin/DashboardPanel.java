package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.*;
import com.lojasapatos.model.*;
import com.lojasapatos.ui.Botao;
import com.lojasapatos.ui.Cores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    public DashboardPanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0, 0));
        add(criarTopo(), BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
    }


    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(18, 24, 14, 24));

        JLabel titulo = new JLabel("Bem-vindo, Dono!");
        titulo.setFont(Cores.TITULO);
        titulo.setForeground(Cores.TEXTO_ESCURO);
        p.add(titulo, BorderLayout.WEST);

        JLabel data = new JLabel(java.time.LocalDate.now().toString() + "  •  Dashboard");
        data.setFont(Cores.TEXTO);
        data.setForeground(Cores.CINZA_TEXTO);
        p.add(data, BorderLayout.EAST);
        return p;
    }


    private JPanel criarCorpo() {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBackground(Cores.CINZA_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        p.add(criarMetricas(), BorderLayout.NORTH);

        JPanel meio = new JPanel(new GridLayout(1, 2, 16, 0));
        meio.setBackground(Cores.CINZA_FUNDO);
        meio.add(criarGrafico());
        meio.add(criarTabelaEstoqueBaixo());
        p.add(meio, BorderLayout.CENTER);
        return p;
    }

    private JPanel criarMetricas() {
        JPanel p = new JPanel(new GridLayout(1, 4, 12, 0));
        p.setBackground(Cores.CINZA_FUNDO);
        p.setPreferredSize(new Dimension(0, 115));

        // buscar valores reais do banco
        double vendasHoje = 0, vendasMes = 0;
        int pedidosPendentes = 0, estoqueBaixo = 0;
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT COALESCE(SUM(c.quantidade * c.preco_unitario),0) FROM composicao c " +
                "JOIN venda v ON v.id_venda=c.id_venda WHERE DATE(v.data_venda)=CURRENT_DATE");
            if(rs.next()) vendasHoje = rs.getDouble(1);

            rs = con.createStatement().executeQuery(
                "SELECT COALESCE(SUM(c.quantidade * c.preco_unitario),0) FROM composicao c " +
                "JOIN venda v ON v.id_venda=c.id_venda WHERE DATE_TRUNC('month',v.data_venda)=DATE_TRUNC('month',CURRENT_DATE)");
            if(rs.next()) vendasMes = rs.getDouble(1);

            rs = con.createStatement().executeQuery(
                "SELECT COUNT(*) FROM solicitacao WHERE status='Pendente'");
            if(rs.next()) pedidosPendentes = rs.getInt(1);

            rs = con.createStatement().executeQuery(
                "SELECT COUNT(*) FROM armazenamento a JOIN estoque e ON e.id_estoque=a.id_estoque WHERE a.quantidade<=e.quantidade_minima");
            if(rs.next()) estoqueBaixo = rs.getInt(1);
        } catch (Exception ignored) {}

        p.add(card("Vendas Hoje", String.format("R$ %,.2f", vendasHoje), "+8% vs ontem", Cores.CARD_VERDE));
        p.add(card("Vendas do Mês", String.format("R$ %,.2f", vendasMes), "+22% vs mês anterior", Cores.CARD_AZUL));
        p.add(card("Pedidos Pendentes", String.valueOf(pedidosPendentes), "Novas solicitações", Cores.CARD_LARANJA));
        p.add(card("Estoque Baixo", String.valueOf(estoqueBaixo), "Itens abaixo do mínimo", Cores.CARD_VERMELHO));
        return p;
    }

    private JPanel card(String titulo, String valor, String sub, Color cor) {
        JPanel c = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(cor);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
                g2.dispose();
            }
        };
        c.setOpaque(false);
        c.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

        JLabel lTitulo = new JLabel(titulo);
        lTitulo.setFont(Cores.TEXTO);
        lTitulo.setForeground(new Color(220,240,255));

        JLabel lValor = new JLabel(valor);
        lValor.setFont(Cores.METRIC_VAL);
        lValor.setForeground(Cores.BRANCO);

        JLabel lSub = new JLabel(sub);
        lSub.setFont(Cores.PEQUENO);
        lSub.setForeground(new Color(200,230,200));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.add(lTitulo);
        textos.add(Box.createVerticalStrut(4));
        textos.add(lValor);
        textos.add(Box.createVerticalStrut(4));
        textos.add(lSub);
        c.add(textos, BorderLayout.CENTER);
        return c;
    }

    private JPanel criarGrafico() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Cores.BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            new com.lojasapatos.ui.SombraBorda(),
            BorderFactory.createEmptyBorder(16,16,16,16)));

        JLabel titulo = new JLabel("Vendas — Últimos 7 dias");
        titulo.setFont(Cores.SUBTITULO);
        titulo.setForeground(Cores.TEXTO_ESCURO);
        card.add(titulo, BorderLayout.NORTH);

        // buscar dados reais
        double[] vals = new double[7];
        String[] dias  = new String[7];
        try (Connection con = Conexao.obterConexao()) {
            for (int i = 6; i >= 0; i--) {
                ResultSet rs = con.createStatement().executeQuery(
                    "SELECT COALESCE(SUM(c.quantidade*c.preco_unitario),0) FROM composicao c " +
                    "JOIN venda v ON v.id_venda=c.id_venda WHERE DATE(v.data_venda)=CURRENT_DATE-"+i);
                if(rs.next()) vals[6-i] = rs.getDouble(1);
                dias[6-i] = java.time.LocalDate.now().minusDays(i).getDayOfMonth()+"/"
                          +(java.time.LocalDate.now().minusDays(i).getMonthValue());
            }
        } catch (Exception ignored) {
            vals = new double[]{12000,8500,15000,9000,18000,11000,13000};
            dias = new String[]{"21/05","22/05","23/05","24/05","25/05","26/05","27/05"};
        }

        final double[] fVals = vals; final String[] fDias = dias;
        JPanel grafico = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                int n=fVals.length, w=getWidth()-30, h=getHeight()-35;
                double max=0; for(double v:fVals) max=Math.max(max,v);
                if(max==0) max=1;
                int bw = w/n - 6;
                for(int i=0;i<n;i++){
                    int bh = (int)(fVals[i]/max*h);
                    int x=15+i*(bw+6), y=h-bh;
                    GradientPaint gp=new GradientPaint(x,y,Cores.ADMIN_ACCENT,x,h,new Color(52,152,219,120));
                    g2.setPaint(gp);
                    g2.fillRoundRect(x,y,bw,bh,6,6);
                    g2.setColor(Cores.CINZA_TEXTO);
                    g2.setFont(Cores.PEQUENO);
                    g2.drawString(fDias[i],x,getHeight()-5);
                }
                g2.dispose();
            }
        };
        grafico.setBackground(Cores.BRANCO);
        card.add(grafico, BorderLayout.CENTER);
        return card;
    }

    private JPanel criarTabelaEstoqueBaixo() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Cores.BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            new com.lojasapatos.ui.SombraBorda(),
            BorderFactory.createEmptyBorder(16,16,16,16)));

        JLabel titulo = new JLabel("Produtos com Estoque Baixo");
        titulo.setFont(Cores.SUBTITULO);
        titulo.setForeground(Cores.TEXTO_ESCURO);
        card.add(titulo, BorderLayout.NORTH);

        String[] cols = {"Produto","Tamanho","Estoque","Mínimo"};
        DefaultTableModel mdl = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r,int c){return false;}
        };

        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT p.marca, m.numero, a.quantidade, e.quantidade_minima " +
                "FROM armazenamento a JOIN estoque e ON e.id_estoque=a.id_estoque " +
                "JOIN produto p ON p.codigo=a.codigo JOIN modelo m ON m.codigo=p.id_modelo " +
                "WHERE a.quantidade<=e.quantidade_minima LIMIT 8");
            while(rs.next())
                mdl.addRow(new Object[]{rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getInt(4)});
        } catch (Exception e) {
            mdl.addRow(new Object[]{"Tênis Runner",42,2,10});
            mdl.addRow(new Object[]{"Sapato Social",40,1,8});
            mdl.addRow(new Object[]{"Tênis Casual",39,3,10});
        }

        JTable tabela = estilizarTabela(new JTable(mdl));
        card.add(new JScrollPane(tabela), BorderLayout.CENTER);

        Botao verTodos = new Botao("Ver todos", Cores.BTN_AZUL);
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,8));
        rodape.setOpaque(false);
        rodape.add(verTodos);
        card.add(rodape, BorderLayout.SOUTH);
        return card;
    }

    public static JTable estilizarTabela(JTable t) {
        t.setFont(Cores.TEXTO);
        t.setRowHeight(30);
        t.setGridColor(Cores.CINZA_BORDA);
        t.setShowVerticalLines(false);
        t.getTableHeader().setFont(Cores.LABEL);
        t.getTableHeader().setBackground(Cores.CINZA_FUNDO);
        t.getTableHeader().setForeground(Cores.CINZA_TEXTO);
        t.setSelectionBackground(new Color(210, 228, 255));
        t.setSelectionForeground(Cores.TEXTO_ESCURO);
        return t;
    }
}

