package com.lojasapatos.ui.login;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.lojasapatos.ui.Botao;
import com.lojasapatos.ui.Cores;
import com.lojasapatos.ui.admin.AdminFrame;
import com.lojasapatos.ui.cliente.ClienteFrame;

public class LoginFrame extends JFrame {

    private JPanel leftPanel;
    private JLabel lblLeftTitle;
    private JLabel lblLeftSubtitle;
    
    private JPanel rightCardPanel;
    private CardLayout cardLayout;

    // Referências para os campos (não mais usadas para validação, mas mantidas)
    private JComboBox<String> comboPerfil;
    private JTextField txtUserAdmin;
    private JPasswordField txtPassAdmin;
    private JTextField txtCpfLogin;
    private JPasswordField txtPassLogin;
    private JTextField txtNomeCad;
    private JTextField txtCpfCad;
    private JTextField txtEmailCad;
    private JPasswordField txtPassCad;

    public LoginFrame() {
        setTitle("Login - Sistema Loja de Sapatos");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));
        setResizable(false);

        initLeftPanel();
        initRightPanel();

        add(leftPanel);
        add(rightCardPanel);
    }

    // ==========================================
    // PAINEL ESQUERDO (Branding / Cor Dinâmica)
    // ==========================================
    private void initLeftPanel() {
        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Cores.ADMIN_PRIMARY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblIcon = new JLabel("👟");
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 100));
        lblIcon.setForeground(Color.WHITE);
        leftPanel.add(lblIcon, gbc);

        lblLeftTitle = new JLabel("Loja de Sapatos");
        lblLeftTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLeftTitle.setForeground(Color.WHITE);
        lblLeftTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        leftPanel.add(lblLeftTitle, gbc);

        lblLeftSubtitle = new JLabel("Sistema Administrativo", SwingConstants.CENTER);
        lblLeftSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblLeftSubtitle.setForeground(Color.WHITE);
        lblLeftSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        leftPanel.add(lblLeftSubtitle, gbc);
    }

    // ==========================================
    // PAINEL DIREITO (CardLayout)
    // ==========================================
    private void initRightPanel() {
        cardLayout = new CardLayout();
        rightCardPanel = new JPanel(cardLayout);
        rightCardPanel.setBackground(Color.WHITE);

        rightCardPanel.add(createAdminForm(), "ADMIN");
        rightCardPanel.add(createClienteLoginForm(), "CLIENTE_LOGIN");
        rightCardPanel.add(createClienteCadastroForm(), "CLIENTE_CADASTRO");
    }

    // --- FORMULÁRIO: ADMIN ---
    private JPanel createAdminForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40);
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
        comboPerfil = new JComboBox<>(perfis);
        comboPerfil.setBackground(Color.WHITE);
        comboPerfil.setFont(Cores.TEXTO);
        gbc.gridy = 2; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(comboPerfil, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblUser = new JLabel("Usuário");
        lblUser.setFont(Cores.LABEL);
        gbc.gridy = 3; panel.add(lblUser, gbc);

        txtUserAdmin = createTextField();
        addPlaceholder(txtUserAdmin, "Digite seu usuário");
        gbc.gridy = 4; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(txtUserAdmin, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblPass = new JLabel("Senha");
        lblPass.setFont(Cores.LABEL);
        gbc.gridy = 5; panel.add(lblPass, gbc);

        txtPassAdmin = createPasswordField();
        addPlaceholder(txtPassAdmin, "Digite sua senha");
        gbc.gridy = 6; gbc.insets = new Insets(0, 40, 25, 40);
        panel.add(txtPassAdmin, gbc);

        Botao btnEntrar = new Botao("Entrar", Cores.BTN_AZUL);
        gbc.gridy = 7; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(btnEntrar, gbc);

        // Ação do botão Entrar (Admin) - sem validação
        btnEntrar.addActionListener(e -> abrirAdmin());

        JLabel linkEsqueceu = createLink("Esqueceu sua senha?", Cores.BTN_AZUL);
        gbc.gridy = 8;
        panel.add(linkEsqueceu, gbc);

        comboPerfil.addActionListener(e -> {
            if (comboPerfil.getSelectedIndex() == 1) {
                switchToCliente();
                comboPerfil.setSelectedIndex(0);
            }
        });

        return panel;
    }

    // --- FORMULÁRIO: CLIENTE (LOGIN) ---
    private JPanel createClienteLoginForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel panelTabs = createTabs(true);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 40, 30, 40);
        panel.add(panelTabs, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setFont(Cores.LABEL);
        gbc.gridy = 1; panel.add(lblCpf, gbc);

        txtCpfLogin = createTextField();
        addPlaceholder(txtCpfLogin, "Digite seu CPF");
        gbc.gridy = 2; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(txtCpfLogin, gbc);

        gbc.insets = new Insets(5, 40, 2, 40);
        JLabel lblPass = new JLabel("Senha");
        lblPass.setFont(Cores.LABEL);
        gbc.gridy = 3; panel.add(lblPass, gbc);

        txtPassLogin = createPasswordField();
        addPlaceholder(txtPassLogin, "Digite sua senha");
        gbc.gridy = 4; gbc.insets = new Insets(0, 40, 25, 40);
        panel.add(txtPassLogin, gbc);

        Botao btnEntrar = new Botao("Entrar", Cores.CLIENTE_PRIMARY);
        gbc.gridy = 5; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(btnEntrar, gbc);

        // Ação do botão Entrar (Cliente) - sem validação
        btnEntrar.addActionListener(e -> abrirCliente());

        JLabel linkEsqueceu = createLink("Esqueceu sua senha?", Color.GRAY);
        gbc.gridy = 6;
        panel.add(linkEsqueceu, gbc);

        JPanel panelRodape = new JPanel(new GridLayout(2, 1, 0, 5));
        panelRodape.setBackground(Color.WHITE);
        
        JLabel lblNaoTem = createLink("Não tem conta? Cadastre-se", Cores.CLIENTE_ACCENT);
        lblNaoTem.setFont(Cores.PEQUENO);
        lblNaoTem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { cardLayout.show(rightCardPanel, "CLIENTE_CADASTRO"); }
        });
        
        JLabel linkVoltar = createLink("← Voltar para Admin", Cores.BTN_AZUL);
        linkVoltar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchToAdmin(); }
        });
        
        panelRodape.add(lblNaoTem);
        panelRodape.add(linkVoltar);

        gbc.gridy = 7; gbc.insets = new Insets(20, 40, 0, 40);
        panel.add(panelRodape, gbc);

        return panel;
    }

    // --- FORMULÁRIO: CLIENTE (CADASTRO) ---
    private JPanel createClienteCadastroForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 40, 2, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JPanel panelTabs = createTabs(false);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 40, 15, 40);
        panel.add(panelTabs, gbc);

        gbc.insets = new Insets(2, 40, 2, 40);
        JLabel lblNome = new JLabel("Nome Completo");
        lblNome.setFont(Cores.LABEL);
        gbc.gridy = 1; panel.add(lblNome, gbc);

        txtNomeCad = createTextField();
        addPlaceholder(txtNomeCad, "Digite seu nome");
        gbc.gridy = 2; gbc.insets = new Insets(0, 40, 8, 40);
        panel.add(txtNomeCad, gbc);

        gbc.insets = new Insets(2, 40, 2, 40);
        JLabel lblCpf = new JLabel("CPF");
        lblCpf.setFont(Cores.LABEL);
        gbc.gridy = 3; panel.add(lblCpf, gbc);

        txtCpfCad = createTextField();
        addPlaceholder(txtCpfCad, "Digite seu CPF");
        gbc.gridy = 4; gbc.insets = new Insets(0, 40, 8, 40);
        panel.add(txtCpfCad, gbc);

        gbc.insets = new Insets(2, 40, 2, 40);
        JLabel lblEmail = new JLabel("E-mail");
        lblEmail.setFont(Cores.LABEL);
        gbc.gridy = 5; panel.add(lblEmail, gbc);

        txtEmailCad = createTextField();
        addPlaceholder(txtEmailCad, "Digite seu e-mail");
        gbc.gridy = 6; gbc.insets = new Insets(0, 40, 8, 40);
        panel.add(txtEmailCad, gbc);

        gbc.insets = new Insets(2, 40, 2, 40);
        JLabel lblPass = new JLabel("Senha");
        lblPass.setFont(Cores.LABEL);
        gbc.gridy = 7; panel.add(lblPass, gbc);

        txtPassCad = createPasswordField();
        addPlaceholder(txtPassCad, "Crie uma senha");
        gbc.gridy = 8; gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(txtPassCad, gbc);

        Botao btnCadastrar = new Botao("Cadastrar", Cores.CLIENTE_PRIMARY);
        gbc.gridy = 9; gbc.insets = new Insets(0, 40, 10, 40);
        panel.add(btnCadastrar, gbc);

        // Ação do botão Cadastrar - sem validação
        btnCadastrar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            abrirCliente();
        });

        JLabel linkVoltar = createLink("← Voltar para Admin", Cores.BTN_AZUL);
        linkVoltar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { switchToAdmin(); }
        });
        gbc.gridy = 10;
        panel.add(linkVoltar, gbc);

        return panel;
    }

    // ==========================================
    // MÉTODOS DE TRANSIÇÃO
    // ==========================================
    private void switchToCliente() {
        leftPanel.setBackground(Cores.CLIENTE_PRIMARY);
        lblLeftSubtitle.setText("<html><center>Bem-vindo!<br>Faça login ou cadastre-se<br>para continuar.</center></html>");
        cardLayout.show(rightCardPanel, "CLIENTE_LOGIN");
    }

    private void switchToAdmin() {
        leftPanel.setBackground(Cores.ADMIN_PRIMARY);
        lblLeftSubtitle.setText("Sistema Administrativo");
        cardLayout.show(rightCardPanel, "ADMIN");
    }

    // ==========================================
    // ABERTURA DAS TELAS PRINCIPAIS
    // ==========================================
    private void abrirAdmin() {
        new AdminFrame().setVisible(true);
        dispose(); // Fecha a tela de login
    }

    private void abrirCliente() {
        new ClienteFrame().setVisible(true);
        dispose();
    }

    // ==========================================
    // HELPERS DE ESTILO (sem validação)
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

    private void addPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);

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
                        ((JPasswordField) field).setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });
    }

    // ==========================================
    // CRIAÇÃO DAS ABAS (TABS)
    // ==========================================
    private JPanel createTabs(boolean isLoginActive) {
        JPanel panelTabs = new JPanel(new GridLayout(1, 2));
        panelTabs.setBackground(Color.WHITE);
        
        JLabel tabLogin = new JLabel("Login", SwingConstants.CENTER);
        JLabel tabCadastrar = new JLabel("Cadastrar", SwingConstants.CENTER);

        if (isLoginActive) {
            tabLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
            tabLogin.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.TEXTO_ESCURO));
            tabLogin.setForeground(Cores.TEXTO_ESCURO);
            
            tabCadastrar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tabCadastrar.setForeground(Color.GRAY);
            tabCadastrar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            tabCadastrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tabCadastrar.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { cardLayout.show(rightCardPanel, "CLIENTE_CADASTRO"); }
            });
        } else {
            tabLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tabLogin.setForeground(Color.GRAY);
            tabLogin.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            tabLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tabLogin.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { cardLayout.show(rightCardPanel, "CLIENTE_LOGIN"); }
            });
            
            tabCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
            tabCadastrar.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Cores.TEXTO_ESCURO));
            tabCadastrar.setForeground(Cores.TEXTO_ESCURO);
        }

        panelTabs.add(tabLogin);
        panelTabs.add(tabCadastrar);
        return panelTabs;
    }
}
