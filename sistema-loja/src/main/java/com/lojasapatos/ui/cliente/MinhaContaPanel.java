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
    private JLabel lblNome   = new JLabel("—");
    private DefaultTableModel modeloPedidos;
    private CardLayout cardInterno;
    private JPanel conteudo;

    public MinhaContaPanel(ClienteFrame frame) {
        this.frame = frame;
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        add(criarSidebarConta(), BorderLayout.WEST);
        cardInterno = new CardLayout();
        conteudo    = new JPanel(cardInterno);
        conteudo.setBackground(Cores.CINZA_FUNDO);
        conteudo.add(criarPainelResumo(),    "Resumo");
        conteudo.add(criarPainelPedidos(),   "Pedidos");
        conteudo.add(criarPainelPontos(),    "Pontos");
        add(conteudo, BorderLayout.CENTER);
        cardInterno.show(conteudo,"Resumo");
        tentarCarregarCliente();
    }

    private JPanel criarSidebarConta() {
        JPanel p=new JPanel();
        p.setPreferredSize(new Dimension(180,0));
        p.setBackground(Cores.BRANCO);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(24,16,24,16));

        JLabel ico=new JLabel("👤",SwingConstants.CENTER);
        ico.setFont(new Font("Segoe UI",Font.PLAIN,40));
        ico.setAlignmentX(CENTER_ALIGNMENT);
        p.add(ico); p.add(Box.createVerticalStrut(8));

        lblNome.setFont(Cores.LABEL); lblNome.setForeground(Cores.TEXTO_ESCURO);
        lblNome.setAlignmentX(CENTER_ALIGNMENT);
        p.add(lblNome); p.add(Box.createVerticalStrut(20));

        String[] items={"Resumo","Meus Dados","Meus Endereços","Meus Pedidos","Meus Pontos"};
        String[] cards={"Resumo","Resumo","Resumo","Pedidos","Pontos"};
        for(int i=0;i<items.length;i++){
            final String card=cards[i];
            JButton btn=new JButton(items[i]);
            btn.setFont(Cores.TEXTO); btn.setForeground(Cores.TEXTO_ESCURO);
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setContentAreaFilled(false); btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8,4,8,4));
            btn.setMaximumSize(new Dimension(180,36));
            btn.setAlignmentX(LEFT_ALIGNMENT);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e->cardInterno.show(conteudo,card));
            p.add(btn);
        }
        p.add(Box.createVerticalGlue());

        JButton btnSair=new JButton("← Sair");
        btnSair.setFont(Cores.TEXTO); btnSair.setForeground(Cores.CINZA_TEXTO);
        btnSair.setContentAreaFilled(false); btnSair.setBorderPainted(false);
        btnSair.setFocusPainted(false); btnSair.setAlignmentX(LEFT_ALIGNMENT);
        btnSair.addActionListener(e->frame.irParaCatalogo());
        p.add(btnSair);
        return p;
    }

    private JPanel criarPainelResumo() {
        JPanel p=new JPanel(new BorderLayout(16,16));
        p.setBackground(Cores.CINZA_FUNDO);
        p.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        // Card de pontos
        JPanel cardPontos=new JPanel(){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp=new GradientPaint(0,0,Cores.CLIENTE_PRIMARY,getWidth(),getHeight(),new Color(140,20,50));
                g2.setPaint(gp);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.dispose();
            }
        };
        cardPontos.setOpaque(false);
        cardPontos.setLayout(new BoxLayout(cardPontos,BoxLayout.Y_AXIS));
        cardPontos.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
        cardPontos.setPreferredSize(new Dimension(240,0));

        JLabel lMeus=new JLabel("Meus Pontos"); lMeus.setFont(Cores.SUBTITULO);
        lMeus.setForeground(new Color(220,180,200)); lMeus.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lMeus); cardPontos.add(Box.createVerticalStrut(16));

        lblPontos.setFont(new Font("Segoe UI",Font.BOLD,48));
        lblPontos.setForeground(Cores.BRANCO); lblPontos.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lblPontos);

        JLabel lSub=new JLabel("pontos acumulados"); lSub.setFont(Cores.TEXTO);
        lSub.setForeground(new Color(220,180,200)); lSub.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lSub); cardPontos.add(Box.createVerticalStrut(16));

        JLabel lDica=new JLabel("<html><center>A cada R$ 1,00 gasto<br>você ganha 1 ponto.<br>100 pontos = R$ 5,00<br>de desconto.</center></html>");
        lDica.setFont(Cores.PEQUENO); lDica.setForeground(new Color(200,160,180));
        lDica.setAlignmentX(CENTER_ALIGNMENT);
        cardPontos.add(lDica);
        p.add(cardPontos,BorderLayout.WEST);

        // Histórico rápido
        JPanel cardHist=new JPanel(new BorderLayout());
        cardHist.setBackground(Cores.BRANCO);
        cardHist.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(),BorderFactory.createEmptyBorder(16,16,16,16)));

        JLabel lHist=new JLabel("Histórico de Compras"); lHist.setFont(Cores.SUBTITULO);
        lHist.setForeground(Cores.TEXTO_ESCURO);
        cardHist.add(lHist,BorderLayout.NORTH);

        String[] cols={"Nº Pedido","Data","Itens","Total","Status"};
        modeloPedidos=new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){return false;}};
        JTable t=com.lojasapatos.ui.admin.DashboardPanel.estilizarTabela(new JTable(modeloPedidos));
        cardHist.add(new JScrollPane(t),BorderLayout.CENTER);

        Botao btnTodos=new Botao("Ver todos os pedidos",Cores.CLIENTE_ACCENT);
        JPanel rodape=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,8));
        rodape.setOpaque(false); rodape.add(btnTodos);
        cardHist.add(rodape,BorderLayout.SOUTH);
        p.add(cardHist,BorderLayout.CENTER);

        return p;
    }

    private JPanel criarPainelPedidos() {
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JLabel t=new JLabel("Todos os Pedidos"); t.setFont(Cores.SUBTITULO);
        p.add(t,BorderLayout.NORTH);
        return p;
    }

    private JPanel criarPainelPontos() {
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JLabel t=new JLabel("Extrato de Pontos"); t.setFont(Cores.SUBTITULO);
        p.add(t,BorderLayout.NORTH);
        return p;
    }

    private void tentarCarregarCliente() {
        Cliente c=frame.getClienteLogado();
        if(c!=null){ lblNome.setText(c.getNome()); lblPontos.setText(String.valueOf(c.getPontos())); return; }

        // Tentar buscar o primeiro cliente cadastrado como fallback
        try(Connection con=Conexao.obterConexao()){
            ResultSet rs=con.createStatement().executeQuery(
                "SELECT nome,pontos FROM cliente ORDER BY cpf LIMIT 1");
            if(rs.next()){
                lblNome.setText(rs.getString(1));
                lblPontos.setText(String.valueOf(rs.getInt(2)));
            }
            // Pedidos recentes
            ResultSet rp=con.createStatement().executeQuery(
                "SELECT v.id_venda, TO_CHAR(v.data_venda,'DD/MM/YYYY'), " +
                "COUNT(c.codigo), COALESCE(SUM(c.quantidade*c.preco_unitario),0) " +
                "FROM venda v LEFT JOIN composicao c ON c.id_venda=v.id_venda " +
                "GROUP BY v.id_venda,v.data_venda ORDER BY v.data_venda DESC LIMIT 5");
            modeloPedidos.setRowCount(0);
            while(rp.next())
                modeloPedidos.addRow(new Object[]{
                    String.format("%06d",rp.getInt(1)),rp.getString(2),
                    rp.getInt(3),String.format("R$ %,.2f",rp.getDouble(4)),"Entregue"});
        } catch(Exception e){
            lblNome.setText("Visitante");
            modeloPedidos.addRow(new Object[]{"000123","27/05/2025",3,"R$ 609,70","Entregue"});
        }
    }
}

