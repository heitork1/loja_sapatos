package com.lojasapatos.ui.login;

import com.lojasapatos.ui.Botao;
import com.lojasapatos.ui.Cores;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private JPanel leftPanel;
    private JLabel lblLeftTitle;
    private JLabel lblLeftSubtitle;
    
    private JPanel rightCardPanel;
    private CardLayout cardLayout;

    public LoginFrame() {
        setTitle("Login - Sistema Loja de Sapatos");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));
        setResizable(false);

        initLeftPanel();
        initRightPanel();

        add(leftPanel);
        add(rightCardPanel);
    }

    private void initLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setBackground(Cores.ADMIN_PRIMARY);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(80, 40, 40, 40));

        JLabel lblIcon = new JLabel("👟");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        lblIcon.setForeground(Color.WHITE);
        lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblLeftTitle = new JLabel("Loja de Sapatos");
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLeftTitle.setForeground(Color.WHITE);
        lblLeftTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLeftTitle.setBorder(new EmptyBorder(20, 0, 10, 0));

        lblLeftSubtitle = new JLabel("Sistema Administrativo");
        lblLeftSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblLeftSubtitle.setForeground(Color.WHITE);
        lblLeftSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(lblIcon);
        leftPanel.add(lblLeftTitle);
        leftPanel.add(lblLeftSubtitle);
    }

    private void initRightPanel() {
        cardLayout = new CardLayout();
        rightCardPanel = new JPanel(cardLayout);
        rightCardPanel.setBackground(Color.WHITE);

        rightCardPanel.add(createAdminForm(), "ADMIN");
        rightCardPanel.add(createClienteForm(), "CLIENTE");
    }

    private JPanel createAdminForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40); // Margens laterais
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel lblTitle = new JLabel("Acesse sua conta");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Cores.TEXTO_ESCURO);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 40, 20, 40);
        panel.add(lblTitle, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblPerfil = new JLabel("Perfil:");
        lblPerfil.setFont(Cores.LABEL);
        gbc.gridy = 1; panel.add(lblPerfil, gbc);

        String[] perfis = {"Dono (Administrador)", "Cliente"};
        JComboBox<String> comboPerfil = new JComboBox<>(perfis);
        comboPerfil.setBackground(Color.WHITE);
        comboPerfil.setFont(Cores.TEXTO);
        gbc.gridy = 2; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(comboPerfil, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblUser = new JLabel("Usuário");
        lblUser.setFont(Cores.LABEL);
        gbc.gridy = 3; panel.add(lblUser, gbc);

        JTextField txtUser = createTextField();
        addPlaceholder(txtUser, "Digite seu usuário");
        gbc.gridy = 4; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(txtUser, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblPass = new JLabel("Senha");
        lblPass.setFont(Cores.LABEL);
        gbc.gridy = 5; panel.add(lblPass, gbc);

        JPasswordField txtPass = createPasswordField();
        addPlaceholder(txtPass, "Digite sua senha");
        gbc.gridy = 6; gbc.insets = new Insets(0, 40, 25, 40);
        panel.add(txtPass, gbc);

        Botao btnEntrar = new Botao("Entrar", Cores.BTN_AZUL);
        gbc.gridy = 7; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(btnEntrar, gbc);

        JLabel linkEsqueceu = createLink("Esqueceu sua senha?", Cores.BTN_AZUL);
        gbc.gridy = 8;
        panel.add(linkEsqueceu, gbc);

        comboPerfil.addActionListener(e -> {
            if (comboPerfil.getSelectedIndex() == 1) {
                switchToCliente();
                comboPerfil.setSelectedIndex(0); // Reseta o combo para quando voltar
            }
        });

        return panel;
    }

    private JPanel createClienteForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel panelTabs = new JPanel(new GridLayout(1, 2));
        panelTabs.setBackground(Color.WHITE);
        
        JLabel tabLogin = new JLabel("Login", SwingConstants.CENTER);
        tabLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabLogin.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.TEXTO_ESCURO));
        
        JLabel tabCadastrar = new JLabel("Cadastrar", SwingConstants.CENTER);
        tabCadastrar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabCadastrar.setForeground(Color.GRAY);
        tabCadastrar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        panelTabs.add(tabLogin);
        panelTabs.add(tabCadastrar);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 40, 30, 40);
        panel.add(panelTabs, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setFont(Cores.LABEL);
        gbc.gridy = 1; panel.add(lblCpf, gbc);

        JTextField txtCpf = createTextField();
        addPlaceholder(txtCpf, "Digite seu CPF");
        gbc.gridy = 2; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(txtCpf, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblPass = new JLabel("Senha");
        lblPass.setFont(Cores.LABEL);
        gbc.gridy = 3; panel.add(lblPass, gbc);

        JPasswordField txtPass = createPasswordField();
        addPlaceholder(txtPass, "Digite sua senha");
        gbc.gridy = 4; gbc.insets = new Insets(0, 40, 25, 40);
        panel.add(txtPass, gbc);

        // Botão Entrar
        Botao btnEntrar = new Botao("Entrar", Cores.CLIENTE_PRIMARY);
        gbc.gridy = 5; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(btnEntrar, gbc);

        // Link Esqueci Senha
        JLabel linkEsqueceu = createLink("Esqueceu sua senha?", Color.GRAY);
        gbc.gridy = 6;
        panel.add(linkEsqueceu, gbc);

        // Link Cadastre-se e Voltar
        JPanel panelRodape = new JPanel(new GridLayout(2, 1, 0, 5));
        panelRodape.setBackground(Color.WHITE);
        
        JLabel lblNaoTem = new JLabel("Não tem conta? Cadastre-se", SwingConstants.CENTER);
        lblNaoTem.setFont(Cores.PEQUENO);
        
        JLabel linkVoltar = createLink("← Voltar para Admin", Cores.BTN_AZUL);
        
        panelRodape.add(lblNaoTem);
        panelRodape.add(linkVoltar);

        gbc.gridy = 7; gbc.insets = new Insets(20, 40, 0, 40);
        panel.add(panelRodape, gbc);

        // AÇÃO: Voltar para Admin
        linkVoltar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToAdmin();
            }
        });

        return panel;
    }

    // ==========================================
    // MÉTODOS DE TRANSIÇÃO (As Mágicas das Cores)
    // ==========================================
    private void switchToCliente() {
        leftPanel.setBackground(Cores.CLIENTE_PRIMARY);
        lblLeftSubtitle.setText("<html><center>Bem-vindo!<br>Faça login ou cadastre-se<br>para continuar.</center></html>");
        cardLayout.show(rightCardPanel, "CLIENTE");
    }

    private void switchToAdmin() {
        leftPanel.setBackground(Cores.ADMIN_PRIMARY);
        lblLeftSubtitle.setText("Sistema Administrativo");
        cardLayout.show(rightCardPanel, "ADMIN");
    }

    // ==========================================
    // ESTILIZAÇÃO E PLACEHOLDERS (Helpers)
    // ==========================================
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Cores.CINZA_BORDA),
                new EmptyBorder(5, 10, 5, 10)));
        return txt;
    }

    private JPasswordField createPasswordField() {
        JPasswordField txt = new JPasswordField();
        txt.setPreferredSize(new Dimension(200, 35));
        txt.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Cores.CINZA_BORDA),
                new EmptyBorder(5, 10, 5, 10)));
        return txt;
    }

    private JLabel createLink(String text, Color color) {
        JLabel link = new JLabel(text, SwingConstants.CENTER);
        link.setForeground(color);
        link.setFont(Cores.TEXTO);
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return link;
    }

    // Truque para colocar um texto cinza que some ao clicar (Placeholder)
    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        // Se for campo de senha, desativa os "bolinhas" inicialmente
        if (field instanceof JPasswordField) {
            ((JPasswordField) field).setEchoChar((char) 0);
        }

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Cores.TEXTO_ESCURO);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar('•'); // Volta bolinhas
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0); // Tira bolinhas
                    }
                }
            }
        });
    }
}