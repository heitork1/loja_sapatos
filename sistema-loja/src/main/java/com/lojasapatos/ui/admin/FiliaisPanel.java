package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.FilialDAO;
import com.lojasapatos.dao.SedeDAO;
import com.lojasapatos.dao.EstoqueDAO;
import com.lojasapatos.model.Filial;
import com.lojasapatos.model.Sede;
import com.lojasapatos.model.Estoque;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FiliaisPanel extends JPanel {

    private DefaultTableModel modeloFiliais;
    private JTable tabelaFiliais;
    private JLabel lblNome    = new JLabel("—");
    private JLabel lblSede    = new JLabel("—");
    private JLabel lblEstoque = new JLabel("—");
    
    private FilialDAO filialDAO;
    private SedeDAO sedeDAO;
    private EstoqueDAO estoqueDAO;

    public FiliaisPanel() {
        filialDAO = new FilialDAO();
        sedeDAO = new SedeDAO();
        estoqueDAO = new EstoqueDAO();
        
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
        JLabel t = new JLabel("Gerenciamento de Filiais");
        t.setFont(Cores.SUBTITULO); t.setForeground(Cores.TEXTO_ESCURO);
        p.add(t, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);

        Botao btnNova    = new Botao("+ Nova Filial", Cores.BTN_VERDE);
        Botao btnEditar  = new Botao("Editar",        Cores.BTN_AZUL);
        Botao btnExcluir = new Botao("Excluir",       Cores.BTN_VERMELHO);

        btnNova.addActionListener(e    -> abrirDialog(null));
        btnEditar.addActionListener(e  -> editarSelecionada());
        btnExcluir.addActionListener(e -> excluirSelecionada());

        acoes.add(btnNova); acoes.add(btnEditar); acoes.add(btnExcluir);
        p.add(acoes, BorderLayout.EAST);
        return p;
    }

    private JSplitPane criarCorpo() {
        String[] cols = {"ID", "Nome da Filial", "ID Sede", "ID Estoque"};
        modeloFiliais = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaFiliais = DashboardPanel.estilizarTabela(new JTable(modeloFiliais));
        tabelaFiliais.getSelectionModel().addListSelectionListener(
                e -> { if (!e.getValueIsAdjusting()) preencherDetalhe(); });

        JPanel pFiliais = new JPanel(new BorderLayout());
        pFiliais.setBackground(Cores.BRANCO);
        pFiliais.setBorder(new SombraBorda());
        pFiliais.add(new JScrollPane(tabelaFiliais), BorderLayout.CENTER);

        JPanel pDetalhe = new JPanel(new BorderLayout(0, 10));
        pDetalhe.setBackground(Cores.CINZA_FUNDO);

        JPanel info = new JPanel(new GridLayout(3, 2, 6, 8));
        info.setBackground(Cores.BRANCO);
        info.setBorder(BorderFactory.createCompoundBorder(
                new SombraBorda(), BorderFactory.createEmptyBorder(14,14,14,14)));
        
        String[] rots = {"Nome da Filial:", "ID da Sede:", "ID do Estoque:"};
        JLabel[] vals = {lblNome, lblSede, lblEstoque};
        for (int i = 0; i < rots.length; i++) {
            JLabel l = new JLabel(rots[i]); 
            l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            info.add(l);
            vals[i].setFont(Cores.TEXTO); vals[i].setForeground(Cores.TEXTO_ESCURO);
            info.add(vals[i]);
        }
        pDetalhe.add(info, BorderLayout.NORTH);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pFiliais, pDetalhe);
        sp.setDividerLocation(680); sp.setBorder(null);
        return sp;
    }

    private void carregarDados() {
        modeloFiliais.setRowCount(0);
        try {
            List<Filial> lista = filialDAO.listarTodos();
            for (Filial f : lista) {
                modeloFiliais.addRow(new Object[]{
                    String.format("%04d", f.getIdFilial()),
                    f.getNomeFilial(),
                    f.getIdSede(),
                    f.getIdEstoque()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar filiais: " + e.getMessage());
        }
    }

    private void preencherDetalhe() {
        int row = tabelaFiliais.getSelectedRow(); 
        if (row < 0) return;
        lblNome.setText((String) modeloFiliais.getValueAt(row, 1));
        lblSede.setText(String.valueOf(modeloFiliais.getValueAt(row, 2)));
        lblEstoque.setText(String.valueOf(modeloFiliais.getValueAt(row, 3)));
    }

    // Modal para gerenciar a Filial (Inserir/Editar)
    private void abrirDialog(Integer idExistente) {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                idExistente == null ? "Nova Filial" : "Editar Filial", true);
        dlg.setSize(450, 280);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(0, 8));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Cores.BRANCO);
        form.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(5,4,5,4);

        JTextField txtNome = campo(""); 
        
        // Criando JComboBoxes
        JComboBox<String> cbSede = new JComboBox<>();
        JComboBox<String> cbEstoque = new JComboBox<>();
        cbSede.setFont(Cores.TEXTO); cbEstoque.setFont(Cores.TEXTO);
        
        // Carrega dados iniciais pros JComboBox
        carregarComboSedes(cbSede);
        carregarComboEstoques(cbEstoque);

        // Painel para JComboBox de Sede + Botão de Nova Sede
        JPanel panelSede = new JPanel(new BorderLayout(5, 0));
        panelSede.setOpaque(false);
        panelSede.add(cbSede, BorderLayout.CENTER);
        JButton btnNovaSede = new JButton("+");
        btnNovaSede.addActionListener(e -> atalhoNovaSede(dlg, cbSede));
        panelSede.add(btnNovaSede, BorderLayout.EAST);

        // Painel para JComboBox de Estoque + Botão de Novo Estoque
        JPanel panelEstoque = new JPanel(new BorderLayout(5, 0));
        panelEstoque.setOpaque(false);
        panelEstoque.add(cbEstoque, BorderLayout.CENTER);
        JButton btnNovoEstoque = new JButton("+");
        btnNovoEstoque.addActionListener(e -> atalhoNovoEstoque(dlg, cbEstoque));
        panelEstoque.add(btnNovoEstoque, BorderLayout.EAST);

        // Se for edição, seta os valores nos componentes
        if (idExistente != null) {
            try {
                Filial f = filialDAO.buscarPorId(idExistente);
                if (f != null) {
                    txtNome.setText(f.getNomeFilial());
                    selecionarItemComboID(cbSede, f.getIdSede());
                    selecionarItemComboID(cbEstoque, f.getIdEstoque());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dlg, "Erro ao buscar filial: " + e.getMessage());
            }
        }

        Object[][] linhas = {
            {"Nome da Filial:", txtNome}, 
            {"Sede:", panelSede}, 
            {"Estoque:", panelEstoque}
        };
        
        for (int i = 0; i < linhas.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0;
            JLabel l = new JLabel((String)linhas[i][0]); 
            l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            form.add(l, g);
            g.gridx = 1; g.weightx = 1;
            form.add((Component)linhas[i][1], g);
        }

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,0));
        botoes.setBackground(Cores.BRANCO);
        botoes.setBorder(BorderFactory.createEmptyBorder(0,16,16,16));
        Botao btnSalvar   = new Botao("Salvar",   Cores.BTN_VERDE);
        Botao btnCancelar = new Botao("Cancelar", Cores.BTN_CINZA);
        btnCancelar.addActionListener(e -> dlg.dispose());

        btnSalvar.addActionListener(e -> {
            try {
                String nome = txtNome.getText().trim();
                if (nome.isEmpty()) { JOptionPane.showMessageDialog(dlg,"O nome não pode estar vazio."); return; }
                if (cbSede.getItemCount() == 0 || cbEstoque.getItemCount() == 0) {
                    JOptionPane.showMessageDialog(dlg,"Cadastre pelo menos uma Sede e um Estoque usando o botão '+'."); return;
                }
                
                // Extrai o ID da String formatada Ex: "1 - Minha Sede" -> 1
                String itemSede = (String) cbSede.getSelectedItem();
                String itemEstoque = (String) cbEstoque.getSelectedItem();
                int idSede = Integer.parseInt(itemSede.split(" - ")[0]);
                int idEstoque = Integer.parseInt(itemEstoque.split(" - ")[0]);

                Filial filial = new Filial(nome, idSede, idEstoque);

                if (idExistente == null) filialDAO.inserir(filial);
                else { filial.setIdFilial(idExistente); filialDAO.atualizar(filial); }
                
                dlg.dispose(); 
                carregarDados();
                JOptionPane.showMessageDialog(this, "Filial salva com sucesso!");
                        
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erro ao salvar no banco: " + ex.getMessage());
            }
        });

        botoes.add(btnSalvar); botoes.add(btnCancelar);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(botoes, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // --- ATALHOS PARA CADASTROS RÁPIDOS ---
    
    private void atalhoNovaSede(JDialog parent, JComboBox<String> combo) {
        String nome = JOptionPane.showInputDialog(parent, "Digite o nome da Nova Sede:");
        if (nome != null && !nome.trim().isEmpty()) {
            try {
                Sede s = new Sede(nome.trim());
                sedeDAO.inserir(s);
                carregarComboSedes(combo);
                selecionarItemComboID(combo, s.getIdSede());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Erro ao criar Sede: " + e.getMessage());
            }
        }
    }

    private void atalhoNovoEstoque(JDialog parent, JComboBox<String> combo) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Quantidade Mínima para novo Estoque:"), BorderLayout.NORTH);
        JTextField txtQtd = new JTextField("0");
        p.add(txtQtd, BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(parent, p, "Novo Estoque", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int qtd = Integer.parseInt(txtQtd.getText().trim());
                Estoque e = new Estoque();
                e.setQuantidadeMinima(qtd);
                estoqueDAO.inserir(e);
                carregarComboEstoques(combo);
                selecionarItemComboID(combo, e.getIdEstoque());
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(parent, "A quantidade deve ser um número inteiro!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(parent, "Erro ao criar Estoque: " + e.getMessage());
            }
        }
    }

    // --- MÉTODOS AUXILIARES ---

    private void carregarComboSedes(JComboBox<String> cb) {
        cb.removeAllItems();
        try {
            for (Sede s : sedeDAO.listarTodos()) {
                cb.addItem(s.getIdSede() + " - " + s.getNomeSede());
            }
        } catch (Exception ignored) {}
    }

    private void carregarComboEstoques(JComboBox<String> cb) {
        cb.removeAllItems();
        try {
            for (Estoque e : estoqueDAO.listarTodos()) {
                cb.addItem(e.getIdEstoque() + " - (Qtd Mínima: " + e.getQuantidadeMinima() + ")");
            }
        } catch (Exception ignored) {}
    }

    // Encontra a String correta na caixa de seleção através do ID e seleciona ela
    private void selecionarItemComboID(JComboBox<String> cb, int idProcurado) {
        String idStr = String.valueOf(idProcurado);
        for (int i = 0; i < cb.getItemCount(); i++) {
            if (cb.getItemAt(i).startsWith(idStr + " - ")) {
                cb.setSelectedIndex(i);
                break;
            }
        }
    }

    private void editarSelecionada() {
        int row = tabelaFiliais.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Selecione uma filial para editar."); return; }
        abrirDialog(Integer.parseInt(((String)modeloFiliais.getValueAt(row,0)).trim()));
    }

    private void excluirSelecionada() {
        int row = tabelaFiliais.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Selecione uma filial para excluir."); return; }
        
        int id = Integer.parseInt(((String)modeloFiliais.getValueAt(row,0)).trim());
        String nome = (String) modeloFiliais.getValueAt(row, 1);
        
        int conf = JOptionPane.showConfirmDialog(this,"Excluir a filial " + nome + "?","Confirmar Exclusão",JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                filialDAO.excluir(id);
                carregarDados();
                lblNome.setText("—"); lblSede.setText("—"); lblEstoque.setText("—");
            } catch (Exception e) { JOptionPane.showMessageDialog(this,"Erro ao excluir: " + e.getMessage()); }
        }
    }

    private JTextField campo(String texto) {
        JTextField t = new JTextField(texto); t.setFont(Cores.TEXTO);
        t.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Cores.CINZA_BORDA,1,true),
                BorderFactory.createEmptyBorder(4,6,4,6)));
        return t;
    }
}