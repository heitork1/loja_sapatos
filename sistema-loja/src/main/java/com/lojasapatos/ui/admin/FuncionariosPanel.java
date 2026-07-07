package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.dao.FuncionarioDAO;
import com.lojasapatos.model.Funcionario;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class FuncionariosPanel extends JPanel {

    private DefaultTableModel modelo;
    private JTable tabela;
    private JLabel lblNome   = new JLabel("—"), lblCargo  = new JLabel("—"),
                   lblCPF    = new JLabel("—"), lblData   = new JLabel("—"),
                   lblStatus = new JLabel("Ativo");
    private JTextField txtBusca;

    public FuncionariosPanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0, 0));
        add(criarTopo(),  BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        carregarDados();
    }

    //topo
    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JLabel titulo = new JLabel("Gestão de Pessoal — Funcionários");
        titulo.setFont(Cores.SUBTITULO); titulo.setForeground(Cores.TEXTO_ESCURO);
        p.add(titulo, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);

        txtBusca = new JTextField(18);
        txtBusca.setFont(Cores.TEXTO);
        txtBusca.setBorder(new LineBorder(Cores.CINZA_BORDA, 1, true));
        txtBusca.putClientProperty("JTextField.placeholderText", "Buscar funcionário...");
        txtBusca.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrar(); }
            public void removeUpdate(DocumentEvent e) { filtrar(); }
            public void changedUpdate(DocumentEvent e) { filtrar(); }
        });

        Botao btnNovo    = new Botao("+ Novo",   Cores.BTN_VERDE);
        Botao btnEditar  = new Botao("✎ Editar", Cores.BTN_AZUL);
        Botao btnExcluir = new Botao("✖ Excluir",Cores.BTN_VERMELHO);

        btnNovo.addActionListener(e    -> abrirDialog(null));
        btnEditar.addActionListener(e  -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());

        acoes.add(btnNovo); acoes.add(btnEditar); acoes.add(btnExcluir); acoes.add(txtBusca);
        p.add(acoes, BorderLayout.EAST);
        return p;
    }

    //corpo split
    private JSplitPane criarCorpo() {
        String[] cols = {"Código", "Nome", "Cargo", "Unidade", "Status"};
        modelo = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = DashboardPanel.estilizarTabela(new JTable(modelo));
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) preencherDetalhe();
        });

        JPanel pTabela = new JPanel(new BorderLayout());
        pTabela.setBackground(Cores.BRANCO);
        pTabela.setBorder(new SombraBorda());
        pTabela.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel pDetalhe = new JPanel();
        pDetalhe.setBackground(Cores.BRANCO);
        pDetalhe.setLayout(new BoxLayout(pDetalhe, BoxLayout.Y_AXIS));
        pDetalhe.setBorder(BorderFactory.createCompoundBorder(
                new SombraBorda(), BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        JTabbedPane tabs = new JTabbedPane(); tabs.setFont(Cores.TEXTO);
        tabs.addTab("Dados Pessoais", criarAbaDados());
        pDetalhe.add(tabs);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pTabela, pDetalhe);
        sp.setDividerLocation(680); sp.setBorder(null);
        return sp;
    }

    private JPanel criarAbaDados() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Cores.BRANCO);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(6, 4, 6, 4);

        String[] rots = {"Nome:", "Cargo:", "CPF:", "Data de Admissão:", "Status"};
        JLabel[] vals = {lblNome, lblCargo, lblCPF, lblData, lblStatus};
        for (int i = 0; i < rots.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0;
            JLabel l = new JLabel(rots[i]); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            p.add(l, g);
            g.gridx = 1; g.weightx = 1;
            vals[i].setFont(Cores.TEXTO); vals[i].setForeground(Cores.TEXTO_ESCURO);
            p.add(vals[i], g);
        }
        return p;
    }

    //dados
    private void carregarDados() { filtrar(); }

    private void filtrar() {
        String q = txtBusca != null ? txtBusca.getText().trim().toLowerCase() : "";
        modelo.setRowCount(0);
        try (Connection con = Conexao.obterConexao()) {
            String sql = "SELECT fn.codigo, fn.nome, fn.funcao, f.nome_filial " +
                         "FROM funcionario fn JOIN filial f ON f.id_filial = fn.id_filial " +
                         (q.isEmpty() ? "" : "WHERE LOWER(fn.nome) LIKE ? OR LOWER(fn.funcao) LIKE ? ") +
                         "ORDER BY fn.codigo";
            PreparedStatement ps = con.prepareStatement(sql);
            if (!q.isEmpty()) { ps.setString(1, "%" + q + "%"); ps.setString(2, "%" + q + "%"); }
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                modelo.addRow(new Object[]{
                        String.format("%03d", rs.getInt(1)), rs.getString(2),
                        rs.getString(3), rs.getString(4), "Ativo"});
        } catch (Exception e) {
            modelo.addRow(new Object[]{"001", "João Silva", "Gerente", "Filial Centro", "Ativo"});
        }
    }

    private void preencherDetalhe() {
        int row = tabela.getSelectedRow(); if (row < 0) return;
        lblNome.setText((String) modelo.getValueAt(row, 1));
        lblCargo.setText((String) modelo.getValueAt(row, 2));
        lblStatus.setText((String) modelo.getValueAt(row, 4));
        try (Connection con = Conexao.obterConexao()) {
            int cod = Integer.parseInt(((String) modelo.getValueAt(row, 0)));
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT cpf, data_admissao FROM funcionario WHERE codigo = " + cod);
            if (rs.next()) {
                lblCPF.setText(rs.getString(1));
                lblData.setText(rs.getString(2) != null ? rs.getString(2) : "—");
            }
        } catch (Exception ignored) {}
    }

    private void editarSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um funcionário."); return; }
        int cod = Integer.parseInt((String) modelo.getValueAt(row, 0));
        try {
            Funcionario f = new FuncionarioDAO().buscarPorId(cod);
            abrirDialog(f);
        } catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
    }

    private void excluirSelecionado() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um funcionário."); return; }
        String nome = (String) modelo.getValueAt(row, 1);
        int cod = Integer.parseInt((String) modelo.getValueAt(row, 0));
        int conf = JOptionPane.showConfirmDialog(this,
                "Excluir funcionário '" + nome + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try { new FuncionarioDAO().excluir(cod); carregarDados(); }
            catch (Exception e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
        }
    }

    //edicao e dialog
    private void abrirDialog(Funcionario existente) {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                existente == null ? "Novo Funcionário" : "Editar Funcionário", true);
        dlg.setSize(420, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Cores.BRANCO);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(5, 4, 5, 4); g.weightx = 1;

        JTextField fNome  = campo(); JTextField fCPF   = campo();
        JTextField fFuncao= campo(); JTextField fData  = campo();
        JComboBox<String> cbFilial = new JComboBox<>();

        //carregar as filiais
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                    "SELECT id_filial, nome_filial FROM filial ORDER BY id_filial");
            while (rs.next()) cbFilial.addItem(rs.getInt(1) + " - " + rs.getString(2));
        } catch (Exception ignored) { cbFilial.addItem("1 - Filial"); }

        String[][] linhas = {{"Nome:", ""}, {"CPF:", ""}, {"Função:", ""}, {"Data Admissão (AAAA-MM-DD):", ""}};
        JTextField[] campos = {fNome, fCPF, fFuncao, fData};

        for (int i = 0; i < linhas.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0;
            JLabel l = new JLabel(linhas[i][0]); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            form.add(l, g);
            g.gridx = 1; g.weightx = 1; form.add(campos[i], g);
        }
        g.gridx = 0; g.gridy = 4; g.weightx = 0;
        JLabel lFil = new JLabel("Filial:"); lFil.setFont(Cores.LABEL); lFil.setForeground(Cores.CINZA_TEXTO);
        form.add(lFil, g);
        g.gridx = 1; g.weightx = 1; form.add(cbFilial, g);

        if (existente != null) {
            fNome.setText(existente.getNome());
            fCPF.setText(existente.getCpf());
            fFuncao.setText(existente.getFuncao());
            fData.setText(existente.getDataAdmissao().toString());
            for (int i = 0; i < cbFilial.getItemCount(); i++) {
                if (cbFilial.getItemAt(i).startsWith(existente.getIdFilial() + " -")) {
                    cbFilial.setSelectedIndex(i); break;
                }
            }
        }

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoes.setBackground(Cores.BRANCO);
        Botao btnSalvar   = new Botao("💾 Salvar",   Cores.BTN_VERDE);
        Botao btnCancelar = new Botao("✖ Cancelar", Cores.BTN_CINZA);
        btnCancelar.addActionListener(e -> dlg.dispose());
        btnSalvar.addActionListener(e -> {
            try {
                String nome  = fNome.getText().trim();
                String cpf   = fCPF.getText().trim();
                String funcao= fFuncao.getText().trim();
                String data  = fData.getText().trim();
                if (nome.isEmpty() || cpf.isEmpty() || funcao.isEmpty() || data.isEmpty()) {
                    JOptionPane.showMessageDialog(dlg, "Preencha todos os campos!"); return;
                }
                int idFilial = Integer.parseInt(((String) cbFilial.getSelectedItem()).split(" - ")[0]);
                FuncionarioDAO dao = new FuncionarioDAO();
                if (existente == null) {
                    dao.inserir(new Funcionario(cpf, funcao, nome, LocalDate.parse(data), idFilial));
                } else {
                    existente.setNome(nome); existente.setCpf(cpf);
                    existente.setFuncao(funcao); existente.setDataAdmissao(LocalDate.parse(data));
                    existente.setIdFilial(idFilial);
                    dao.atualizar(existente);
                }
                dlg.dispose(); carregarDados();
                JOptionPane.showMessageDialog(this, "Funcionário salvo com sucesso!");
            } catch (Exception ex) { JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage()); }
        });
        botoes.add(btnSalvar); botoes.add(btnCancelar);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(botoes, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private JTextField campo() {
        JTextField t = new JTextField(); t.setFont(Cores.TEXTO);
        t.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Cores.CINZA_BORDA, 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return t;
    }
}
