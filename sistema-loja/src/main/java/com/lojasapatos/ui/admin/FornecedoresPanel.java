package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class FornecedoresPanel extends JPanel {

    private DefaultTableModel modeloSolic;
    private JTable tabelaSolic;
    private JLabel lblForn   = new JLabel("—");
    private JLabel lblStatus = new JLabel("—");
    private JLabel lblEntrega= new JLabel("—");
    private JLabel lblCusto  = new JLabel("—");
    private DefaultTableModel modeloItens;

    public FornecedoresPanel() {
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
        JLabel t = new JLabel("Solicitacoes ao Fornecedor");
        t.setFont(Cores.SUBTITULO); t.setForeground(Cores.TEXTO_ESCURO);
        p.add(t, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);
        JTextField busca = new JTextField(16); busca.setFont(Cores.TEXTO);
        busca.setBorder(new LineBorder(Cores.CINZA_BORDA,1,true));
        busca.putClientProperty("JTextField.placeholderText","Buscar solicitacao...");

        Botao btnNova    = new Botao("+ Nova Solicitacao", Cores.BTN_VERDE);
        Botao btnEditar  = new Botao("Editar",             Cores.BTN_AZUL);
        Botao btnExcluir = new Botao("Excluir",            Cores.BTN_VERMELHO);

        btnNova.addActionListener(e    -> abrirDialog(null));
        btnEditar.addActionListener(e  -> editarSelecionado());
        btnExcluir.addActionListener(e -> excluirSelecionado());

        acoes.add(btnNova); acoes.add(btnEditar); acoes.add(btnExcluir); acoes.add(busca);
        p.add(acoes, BorderLayout.EAST);
        return p;
    }

    private JSplitPane criarCorpo() {
        String[] cols = {"Nr Solic.","Fornecedor","Data Entrega","Status","Qtd"};
        modeloSolic = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaSolic = DashboardPanel.estilizarTabela(new JTable(modeloSolic));
        tabelaSolic.getSelectionModel().addListSelectionListener(
                e -> { if (!e.getValueIsAdjusting()) preencherDetalhe(); });

        JPanel pSolic = new JPanel(new BorderLayout());
        pSolic.setBackground(Cores.BRANCO);
        pSolic.setBorder(new SombraBorda());
        pSolic.add(new JScrollPane(tabelaSolic), BorderLayout.CENTER);

        JPanel pDetalhe = new JPanel(new BorderLayout(0, 10));
        pDetalhe.setBackground(Cores.CINZA_FUNDO);

        //info card
        JPanel info = new JPanel(new GridLayout(4, 2, 6, 8));
        info.setBackground(Cores.BRANCO);
        info.setBorder(BorderFactory.createCompoundBorder(
                new SombraBorda(), BorderFactory.createEmptyBorder(14,14,14,14)));
        String[] rots = {"Fornecedor:","Status:","Previsao Entrega:","Custo:"};
        JLabel[] vals = {lblForn, lblStatus, lblEntrega, lblCusto};
        for (int i = 0; i < rots.length; i++) {
            JLabel l = new JLabel(rots[i]); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            info.add(l);
            vals[i].setFont(Cores.TEXTO); vals[i].setForeground(Cores.TEXTO_ESCURO);
            info.add(vals[i]);
        }
        pDetalhe.add(info, BorderLayout.NORTH);

        //itens card
        JPanel pItens = new JPanel(new BorderLayout());
        pItens.setBackground(Cores.BRANCO);
        pItens.setBorder(BorderFactory.createCompoundBorder(
                new SombraBorda(), BorderFactory.createEmptyBorder(10,10,10,10)));
        JLabel lItens = new JLabel("Itens Solicitados"); lItens.setFont(Cores.LABEL);
        lItens.setBorder(BorderFactory.createEmptyBorder(0,0,8,0));
        pItens.add(lItens, BorderLayout.NORTH);
        String[] cIt = {"Produto","Quantidade"};
        modeloItens = new DefaultTableModel(cIt, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        pItens.add(new JScrollPane(DashboardPanel.estilizarTabela(new JTable(modeloItens))), BorderLayout.CENTER);
        pDetalhe.add(pItens, BorderLayout.CENTER);

        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pSolic, pDetalhe);
        sp.setDividerLocation(680); sp.setBorder(null);
        return sp;
    }

    private void carregarDados() {
        modeloSolic.setRowCount(0);
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT id_solicitacao, razao_social, data_entrega, status, quantidade " +
                "FROM solicitacao ORDER BY id_solicitacao DESC");
            while (rs.next())
                modeloSolic.addRow(new Object[]{
                    String.format("%05d", rs.getInt(1)), rs.getString(2),
                    rs.getString(3) != null ? rs.getString(3) : "—",
                    rs.getString(4), rs.getInt(5)});
        } catch (Exception e) {
            modeloSolic.addRow(new Object[]{"00001","Calcados Brasil LTDA","05/06/2026","Pendente",30});
        }
    }

    private void preencherDetalhe() {
        int row = tabelaSolic.getSelectedRow(); if (row < 0) return;
        lblForn.setText((String)modeloSolic.getValueAt(row,1));
        lblStatus.setText((String)modeloSolic.getValueAt(row,3));
        lblEntrega.setText((String)modeloSolic.getValueAt(row,2));
        modeloItens.setRowCount(0);
        try (Connection con = Conexao.obterConexao()) {
            String id = (String)modeloSolic.getValueAt(row,0);
            int sid = Integer.parseInt(id.trim());
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT p.marca||' '||m.cor, s.quantidade, s.custo " +
                "FROM solicitacao s JOIN produto p ON p.codigo=s.codigo " +
                "JOIN modelo m ON m.codigo=p.id_modelo WHERE s.id_solicitacao="+sid);
            while (rs.next()) {
                modeloItens.addRow(new Object[]{rs.getString(1), rs.getInt(2)});
                lblCusto.setText(String.format("R$ %,.2f", rs.getDouble(3)));
            }
        } catch (Exception ignored) {
            modeloItens.addRow(new Object[]{"Produto Exemplo",10});
        }
    }

    //dialog nova e editar
    private void abrirDialog(Integer idExistente) {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                idExistente == null ? "Nova Solicitacao" : "Editar Solicitacao", true);
        dlg.setSize(500, 380);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(0, 8));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Cores.BRANCO);
        form.setBorder(BorderFactory.createEmptyBorder(20,20,10,20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5,4,5,4);

        JComboBox<String> cbForn    = new JComboBox<>();
        JComboBox<String> cbProduto = new JComboBox<>();
        JComboBox<String> cbStatus  = new JComboBox<>(
            new String[]{"Pendente","Entregue Parcialmente","Entregue","Cancelado"});
        JTextField txtQtd    = campo(""); 
        JTextField txtCusto  = campo("0.00");
        JTextField txtData   = campo(java.time.LocalDate.now().plusDays(10).toString());

        cbForn.setFont(Cores.TEXTO); cbProduto.setFont(Cores.TEXTO); cbStatus.setFont(Cores.TEXTO);

        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT razao_social FROM fornecedor ORDER BY razao_social");
            while (rs.next()) cbForn.addItem(rs.getString(1));
            rs = con.createStatement().executeQuery(
                "SELECT p.codigo, p.marca||' '||m.cor||' (Nr '||m.numero||')' FROM produto p JOIN modelo m ON m.codigo=p.id_modelo ORDER BY p.marca");
            while (rs.next()) cbProduto.addItem(rs.getInt(1)+" | "+rs.getString(2));
        } catch (Exception e) {
            cbForn.addItem("Fornecedor Exemplo");
            cbProduto.addItem("1 | Produto Exemplo");
        }

        Object[][] linhas = {
            {"Fornecedor:",cbForn}, {"Produto:",cbProduto}, {"Quantidade:",txtQtd},
            {"Custo unitario (R$):",txtCusto}, {"Previsao entrega (AAAA-MM-DD):",txtData},
            {"Status:",cbStatus}
        };
        for (int i=0; i<linhas.length; i++) {
            g.gridx=0; g.gridy=i; g.weightx=0;
            JLabel l = new JLabel((String)linhas[i][0]); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            form.add(l,g);
            g.gridx=1; g.weightx=1;
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
                String fornNome  = (String)cbForn.getSelectedItem();
                String prodItem  = (String)cbProduto.getSelectedItem();
                int codigoProd   = Integer.parseInt(prodItem.split(" \\| ")[0].trim());
                int qtd          = Integer.parseInt(txtQtd.getText().trim());
                double custo     = Double.parseDouble(txtCusto.getText().trim());
                String dataEnt   = txtData.getText().trim();
                String status    = (String)cbStatus.getSelectedItem();

                if (qtd <= 0) { JOptionPane.showMessageDialog(dlg,"Quantidade deve ser maior que zero."); return; }

                try (Connection con = Conexao.obterConexao()) {
                    if (idExistente == null) {
                        PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO solicitacao(status,quantidade,custo,data_entrega,razao_social,codigo,id_sede) " +
                            "VALUES(?,?,?,?,?,?,1)");
                        ps.setString(1, status);
                        ps.setInt(2, qtd);
                        ps.setDouble(3, custo * qtd);
                        ps.setString(4, dataEnt);
                        ps.setString(5, fornNome);
                        ps.setInt(6, codigoProd);
                        ps.executeUpdate();
                    } else {
                        PreparedStatement ps = con.prepareStatement(
                            "UPDATE solicitacao SET status=?,quantidade=?,custo=?,data_entrega=?," +
                            "razao_social=?,codigo=? WHERE id_solicitacao=?");
                        ps.setString(1, status);
                        ps.setInt(2, qtd);
                        ps.setDouble(3, custo * qtd);
                        ps.setString(4, dataEnt);
                        ps.setString(5, fornNome);
                        ps.setInt(6, codigoProd);
                        ps.setInt(7, idExistente);
                        ps.executeUpdate();
                    }
                }
                dlg.dispose(); carregarDados();
                JOptionPane.showMessageDialog(this, "Solicitacao salva com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erro: " + ex.getMessage());
            }
        });

        botoes.add(btnSalvar); botoes.add(btnCancelar);
        dlg.add(form, BorderLayout.CENTER);
        dlg.add(botoes, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void editarSelecionado() {
        int row = tabelaSolic.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Selecione uma solicitacao."); return; }
        int id = Integer.parseInt(((String)modeloSolic.getValueAt(row,0)).trim());
        abrirDialog(id);
    }

    private void excluirSelecionado() {
        int row = tabelaSolic.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this,"Selecione uma solicitacao."); return; }
        int id = Integer.parseInt(((String)modeloSolic.getValueAt(row,0)).trim());
        int conf = JOptionPane.showConfirmDialog(this,"Excluir solicitacao #"+id+"?","Confirmar",JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try (Connection con = Conexao.obterConexao()) {
                con.createStatement().executeUpdate("DELETE FROM solicitacao WHERE id_solicitacao="+id);
                carregarDados();
            } catch (Exception e) { JOptionPane.showMessageDialog(this,"Erro: "+e.getMessage()); }
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
