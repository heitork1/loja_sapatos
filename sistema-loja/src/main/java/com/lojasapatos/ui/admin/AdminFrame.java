package com.lojasapatos.ui.admin;

import com.lojasapatos.ui.Cores;
import com.lojasapatos.ui.login.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelConteudo;
    private JButton btnAtivo;

    public AdminFrame() {
        setTitle("Sistema Loja de Sapatos - Administração");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(criarSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(Cores.CINZA_FUNDO);
        painelConteudo.add(new DashboardPanel(),   "Dashboard");
        painelConteudo.add(new EstoquePanel(),     "Estoque");
        painelConteudo.add(new VendasPanel(),      "Vendas");
        painelConteudo.add(new FuncionariosPanel(),"Pessoal");
        painelConteudo.add(new FornecedoresPanel(),"Fornecedores");
        painelConteudo.add(new SedesPanel(),       "Sede");
        painelConteudo.add(new FiliaisPanel(),     "Filiais"); 
        add(painelConteudo, BorderLayout.CENTER);

        cardLayout.show(painelConteudo, "Dashboard");
    }

    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0,0,Cores.ADMIN_PRIMARY,0,getHeight(),Cores.ADMIN_SIDEBAR);
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Logo
        JPanel logo = new JPanel(new BorderLayout());
        logo.setOpaque(false);
        logo.setBorder(BorderFactory.createEmptyBorder(20,15,20,15));
        JLabel lblLogo = new JLabel("👟  Loja de Sapatos");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        lblLogo.setForeground(Cores.BRANCO);
        logo.add(lblLogo, BorderLayout.CENTER);
        sidebar.add(logo);

        // Separador
        sidebar.add(separador());

        // Itens de menu
        String[] itens   = {"Dashboard","Estoque","Vendas","Pessoal","Fornecedores","Sede","Filiais"};
        String[] icones  = {"📊", "📦","💰","👤","🏭","🏛","🏪"};
        String[] cards   = {"Dashboard","Estoque","Vendas","Pessoal","Fornecedores","Sede","Filiais"};
        
        btnAtivo = null;
        for (int i = 0; i < itens.length; i++) {
            final String card = cards[i];
            JButton btn = criarBtnMenu(icones[i] + "  " + itens[i]);
            btn.addActionListener(e -> {
                cardLayout.show(painelConteudo, card);
                setAtivo(btn);
            });
            sidebar.add(btn);
            if (i == 0) { setAtivo(btn); }
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(separador());

        // Sair
        JButton btnSair = criarBtnMenu("⬅  Sair");
        btnSair.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        });
        sidebar.add(btnSair);
        sidebar.add(Box.createVerticalStrut(16));

        return sidebar;
    }

    private JButton criarBtnMenu(String texto) {
        JButton btn = new JButton(texto) {
            private boolean hover = false;
            { addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){hover=true;repaint();}
                public void mouseExited(MouseEvent e){hover=false;repaint();}
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                if(hover||this==btnAtivo)
                    g2.setColor(Cores.ADMIN_SIDEBAR_HL);
                else
                    g2.setColor(new Color(0,0,0,0));
                g2.fillRect(0,0,getWidth(),getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 14));
        btn.setForeground(new Color(190,210,240));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(12,20,12,10));
        btn.setMaximumSize(new Dimension(200,48));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setAtivo(JButton btn) {
        btnAtivo = btn;
        btn.setForeground(Cores.BRANCO);
        btn.getParent().repaint();
    }

    private JSeparator separador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(50, 70, 120));
        sep.setMaximumSize(new Dimension(200,1));
        return sep;
    }
}

