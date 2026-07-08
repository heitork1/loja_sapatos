package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.SedeDAO;
import com.lojasapatos.model.Sede;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SedesPanel extends JPanel {

    private DefaultTableModel modeloSedes;
    private JTable tabelaSedes;
    private JLabel lblID = new JLabel("—");
    private JLabel lblNome = new JLabel("—");
    private SedeDAO sedeDAO;

    public SedesPanel() {
        sedeDAO = new SedeDAO();
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        add(criarTopo(),  BorderLayout.NORTH);
        add(criarCorpo(), BorderLayout.CENTER);
        carregarDados();
    }

    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));
        JLabel t = new JLabel("Gerenciamento de Sedes");
        t.setFont(Cores.SUBTITULO); t.setForeground(Cores.TEXTO_ESCURO);
        p.add(t, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);

        Botao btnNova    = new Botao("+ Nova Sede", Cores.BTN_VERDE);
        Botao btnEditar  = new Botao("Editar",      Cores.BTN_AZUL);
        Botao btnExcluir = new Botao("Excluir",     Cores.BTN_VERMELHO);

        btnNova.addActionListener(e    -> abrirDialog(null));
        btnEditar.addActionListener(e  -> editarSelecionada());
        btnExcluir.addActionListener(e -> excluirSelecionada());

        acoes.add(btnNova); acoes.add(btnEditar); acoes.add(btnExcluir);
        p.add(acoes, BorderLayout.EAST);
        return p;
    }

    private JSplitPane criarCorpo() {
        String[] cols = {"ID", "Nome da Sede"};
        modeloSedes = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaSedes = DashboardPanel.estilizarTabela(new JTable(modeloSedes));
        tabelaSedes.getSelectionModel().addListSelectionListener(
                e -> { if (!e.getValueIsAdjusting()) preencherDetalhe(); });

        JPanel pSedes = new JPanel(new BorderLayout());
        pSedes.setBackground(Cores.BRANCO);
        pSedes.setBorder(new SombraBorda());
        pSedes.add(new JScrollPane(tabelaSedes), BorderLayout.CENTER);

        JPanel pDetalhe = new JPanel(new BorderLayout(0, 10));
        pDetalhe.setBackground(Cores.CINZA_FUNDO);

        JPanel info = new JPanel(new GridLayout(2, 2, 6, 8));
        info.setBackground(Cores.BRANCO);
        info.setBorder(BorderFactory.createCompoundBorder(
                new SombraBorda(), BorderFactory.createEmptyBorder(14,14,14,14)));
        
        info.add(criarLabel("ID da Sede:")); info.add(lblID);
        info.add(criarLabel("Nome da Sede:")); info.add(lblNome);
        
        pDetalhe.add(info, BorderLayout.NORTH);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pSedes, pDetalhe);
        sp.setDividerLocation(680); sp.setBorder(null);
        return sp;
    }
    
    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
        return l;
    }

    private void carregarDados() {
        modeloSedes.setRowCount(0);
        try {
            List<Sede> lista = sedeDAO.listarTodos();
            for (Sede s : lista) {
                modeloSedes.addRow(new Object[]{ String.format("%04d", s.getIdSede()), s.getNomeSede() });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar sedes: " + e.getMessage());
        }
    }

    private void preencherDetalhe() {
        int row = tabelaSedes.getSelectedRow(); 
        if (row < 0) return;
        lblID.setText((String) modeloSedes.getValueAt(row, 0));
        lblNome.setText((String) modeloSedes.getValueAt(row, 1));
        lblID.setFont(Cores.TEXTO); lblID.setForeground(Cores.TEXTO_ESCURO);
        lblNome.setFont(Cores.TEXTO); lblNome.setForeground(Cores.TEXTO_ESCURO);
    }

    private void abrirDialog(Integer idExistente) {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                idExistente == null ? "Nova Sede" : "Editar Sede", true);
        dlg.setSize(400, 180);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(0, 8));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Cores.BRANCO);
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(5,4,5,4);

        JTextField txtNome = new JTextField(""); 
        txtNome.setFont(Cores.TEXTO);
        txtNome.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Cores.CINZA_BORDA,1,true), BorderFactory.createEmptyBorder(4,6,4,6)));

        if (idExistente != null) {
            try {
                Sede s = sedeDAO.buscarPorId(idExistente);
                if (s != null) txtNome.setText(s.getNomeSede());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
            }
        }

        g.gridx=0; g.gridy=0; g.weightx=0;
        form.add(criarLabel("Nome da Sede:"), g);
        g.gridx=1; g.weightx=1;
        form.add(txtNome, g);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        botoes.setBackground(Cores.BRANCO);
        Botao btnSalvar = new Botao("Salvar", Cores.BTN_VERDE);
        Botao btnCancelar = new Botao("Cancelar", Cores.BTN_CINZA);
        btnCancelar.addActionListener(e -> dlg.dispose());

        btnSalvar.addActionListener(e -> {
            try {
                if (txtNome.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dlg,"Nome não pode estar vazio."); return;
                }
                Sede s = new Sede(txtNome.getText().trim());
                if (idExistente == null) sedeDAO.inserir(s);
                else { s.setIdSede(idExistente); sedeDAO.atualizar(s); }
                
                dlg.dispose(); carregarDados();
                JOptionPane.showMessageDialog(this, "Sede salva com sucesso!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage());
            }
        });

        botoes.add(btnSalvar); botoes.add(btnCancelar);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(botoes, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void editarSelecionada() {
        int row = tabelaSedes.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Selecione uma sede."); return; }
        abrirDialog(Integer.parseInt(((String)modeloSedes.getValueAt(row,0)).trim()));
    }

    private void excluirSelecionada() {
        int row = tabelaSedes.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Selecione uma sede."); return; }
        int id = Integer.parseInt(((String)modeloSedes.getValueAt(row,0)).trim());
        int conf = JOptionPane.showConfirmDialog(this,"Excluir Sede #"+id+"?","Confirmar",JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try { sedeDAO.excluir(id); carregarDados(); lblID.setText("—"); lblNome.setText("—");
            } catch (Exception e) { JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
        }
    }
}