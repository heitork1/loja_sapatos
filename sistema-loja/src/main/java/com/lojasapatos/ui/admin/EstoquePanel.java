package com.lojasapatos.ui.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.lojasapatos.dao.ArmazenamentoDAO;
import com.lojasapatos.dao.Conexao;
import com.lojasapatos.dao.EstoqueDAO;
import com.lojasapatos.dao.ModeloDAO;
import com.lojasapatos.dao.ProdutoDAO;
import com.lojasapatos.model.Armazenamento;
import com.lojasapatos.model.Estoque;
import com.lojasapatos.model.Modelo;
import com.lojasapatos.model.Produto;
import com.lojasapatos.ui.Botao;
import com.lojasapatos.ui.Cores;
import com.lojasapatos.ui.SombraBorda;

public class EstoquePanel extends JPanel {

    private DefaultTableModel modeloProdutos;
    private JTable tabelaProdutos;
    private JTextField txtBusca;
    private JTextField fNome = new JTextField(), fCategoria = new JTextField(),
                       fMarca = new JTextField(), fPreco = new JTextField();
    private JLabel lblEstoque = new JLabel("—");
    private JCheckBox chkAtivo = new JCheckBox("Ativo");

    public EstoquePanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0,0));
        add(criarTopo(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                criarTabela(), criarDetalhe());
        split.setDividerLocation(680);
        split.setBorder(null);
        split.setBackground(Cores.CINZA_FUNDO);
        add(split, BorderLayout.CENTER);

        carregarDados();
    }

    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14,24,14,24));

        JLabel titulo = new JLabel("📦 Gestão de Estoque — Produtos");
        titulo.setFont(Cores.SUBTITULO);
        titulo.setForeground(Cores.TEXTO_ESCURO);
        p.add(titulo, BorderLayout.WEST);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acoes.setOpaque(false);

        txtBusca = new JTextField(18);
        txtBusca.setFont(Cores.TEXTO);
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Cores.CINZA_BORDA,1,true),
                BorderFactory.createEmptyBorder(4,8,4,8)));
        txtBusca.putClientProperty("JTextField.placeholderText", "🔍 Buscar produto...");
        txtBusca.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){ filtrar(); }
            public void removeUpdate(DocumentEvent e){ filtrar(); }
            public void changedUpdate(DocumentEvent e){ filtrar(); }
        });

        Botao btnNovo   = new Botao("➕ Novo Produto", Cores.BTN_VERDE);
        Botao btnEditar = new Botao("✏️ Editar",       Cores.BTN_AZUL);
        Botao btnExcluir= new Botao("🗑️ Excluir",      Cores.BTN_VERMELHO);
        Botao btnRefresh= new Botao("🔄 Atualizar",   Cores.BTN_CINZA);

        btnNovo.addActionListener(e -> novoProduto());
        btnEditar.addActionListener(e -> editarProduto());
        btnExcluir.addActionListener(e -> excluirProduto());
        btnRefresh.addActionListener(e -> carregarDados());

        acoes.add(btnNovo); acoes.add(btnEditar); acoes.add(btnExcluir);
        acoes.add(btnRefresh); acoes.add(txtBusca);
        p.add(acoes, BorderLayout.EAST);
        return p;
    }

    private JPanel criarTabela() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(), BorderFactory.createEmptyBorder(8,8,8,8)));

        String[] cols = {"Código","Produto","Categoria","Preço","Estoque"};
        modeloProdutos = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        tabelaProdutos = DashboardPanel.estilizarTabela(new JTable(modeloProdutos));
        tabelaProdutos.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()) preencherDetalhe();
        });
        p.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);
        return p;
    }

    private JPanel criarDetalhe() {
        JPanel p = new JPanel();
        p.setBackground(Cores.BRANCO);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createCompoundBorder(
            new SombraBorda(), BorderFactory.createEmptyBorder(16,16,16,16)));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Cores.TEXTO);
        tabs.addTab("📋 Detalhes do Produto", criarAbaDetalhes());
        tabs.addTab("🏢 Estoque por Filial",  criarAbaEstoque());
        p.add(tabs);
        return p;
    }

    private JPanel criarAbaDetalhes() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Cores.BRANCO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4,4,4,4);

        String[][] campos = {{"Nome:",""}, {"Categoria:",""}, {"Marca:",""}, {"Preço:",""}};
        JTextField[] fields = {fNome, fCategoria, fMarca, fPreco};

        for(int i=0; i<campos.length; i++){
            gbc.gridx=0; gbc.gridy=i; gbc.weightx=0;
            JLabel l = new JLabel(campos[i][0]);
            l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
            p.add(l, gbc);
            gbc.gridx=1; gbc.weightx=1;
            fields[i].setFont(Cores.TEXTO);
            fields[i].setEditable(false);
            p.add(fields[i], gbc);
        }

        gbc.gridx=0; gbc.gridy=campos.length; gbc.weightx=0;
        JLabel lEst = new JLabel("Estoque Mínimo:");
        lEst.setFont(Cores.LABEL); lEst.setForeground(Cores.CINZA_TEXTO);
        p.add(lEst, gbc);
        gbc.gridx=1; gbc.weightx=1;
        lblEstoque.setFont(Cores.TEXTO);
        p.add(lblEstoque, gbc);

        gbc.gridx=0; gbc.gridy=campos.length+1; gbc.gridwidth=2;
        chkAtivo.setFont(Cores.TEXTO);
        chkAtivo.setOpaque(false);
        chkAtivo.setEnabled(false);
        p.add(chkAtivo, gbc);

        return p;
    }

    private JPanel criarAbaEstoque() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        String[] cols = {"Filial","Qtd Disponível","Qtd Mínima"};
        DefaultTableModel m = new DefaultTableModel(cols,0){
            public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable t = DashboardPanel.estilizarTabela(new JTable(m));
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    void carregarDados() {
        modeloProdutos.setRowCount(0);
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT p.codigo, p.marca || ' ' || m.cor AS produto, " +
                "p.categoria, p.preco, " +
                "COALESCE((SELECT SUM(a.quantidade) FROM armazenamento a WHERE a.codigo = p.codigo), 0) AS total_estoque " +
                "FROM produto p " +
                "JOIN modelo m ON m.codigo = p.id_modelo " +
                "ORDER BY p.codigo");
            while (rs.next()) {
                modeloProdutos.addRow(new Object[]{
                    rs.getInt("codigo"),
                    rs.getString("produto"),
                    rs.getString("categoria"),
                    "R$ " + rs.getBigDecimal("preco"),
                    rs.getInt("total_estoque")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrar() {
        String q = txtBusca.getText().trim().toLowerCase();
        modeloProdutos.setRowCount(0);
        try (Connection con = Conexao.obterConexao()) {
            String sql = "SELECT p.codigo, p.marca || ' ' || m.cor AS produto, " +
                         "p.categoria, p.preco, " +
                         "COALESCE((SELECT SUM(a.quantidade) FROM armazenamento a WHERE a.codigo = p.codigo), 0) AS total_estoque " +
                         "FROM produto p " +
                         "JOIN modelo m ON m.codigo = p.id_modelo " +
                         "WHERE LOWER(p.marca) LIKE ? OR LOWER(p.categoria) LIKE ? " +
                         "ORDER BY p.codigo";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + q + "%");
            ps.setString(2, "%" + q + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modeloProdutos.addRow(new Object[]{
                    rs.getInt("codigo"),
                    rs.getString("produto"),
                    rs.getString("categoria"),
                    "R$ " + rs.getBigDecimal("preco"),
                    rs.getInt("total_estoque")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void preencherDetalhe() {
        int row = tabelaProdutos.getSelectedRow();
        if (row < 0) return;
        fNome.setText((String) modeloProdutos.getValueAt(row, 1));
        fCategoria.setText((String) modeloProdutos.getValueAt(row, 2));
        fMarca.setText(((String) modeloProdutos.getValueAt(row, 1)).split(" ")[0]);
        fPreco.setText(modeloProdutos.getValueAt(row, 3).toString());
        chkAtivo.setSelected(true);
        try (Connection con = Conexao.obterConexao()) {
            int cod = (int) modeloProdutos.getValueAt(row, 0);
            PreparedStatement ps = con.prepareStatement(
                "SELECT e.quantidade_minima FROM estoque e " +
                "JOIN armazenamento a ON a.id_estoque = e.id_estoque " +
                "WHERE a.codigo = ? LIMIT 1");
            ps.setInt(1, cod);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                lblEstoque.setText(String.valueOf(rs.getInt("quantidade_minima")));
            } else {
                lblEstoque.setText("—");
            }
        } catch (Exception e) {
            lblEstoque.setText("—");
        }
    }

    private void novoProduto() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ProdutoDialog dialog = new ProdutoDialog(parent, null);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            carregarDados();
        }
    }

    private void editarProduto() {
        int row = tabelaProdutos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar.");
            return;
        }
        int codigo = (int) modeloProdutos.getValueAt(row, 0);
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        ProdutoDialog dialog = new ProdutoDialog(parent, codigo);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            carregarDados();
        }
    }

    private void excluirProduto() {
        int row = tabelaProdutos.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir.");
            return;
        }
        int codigo = (int) modeloProdutos.getValueAt(row, 0);
        String nome = (String) modeloProdutos.getValueAt(row, 1);
        int conf = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o produto '" + nome + "' (código " + codigo + ")?",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try (Connection con = Conexao.obterConexao()) {
                con.setAutoCommit(false);
                PreparedStatement ps = con.prepareStatement("DELETE FROM armazenamento WHERE codigo = ?");
                ps.setInt(1, codigo);
                ps.executeUpdate();
                ps = con.prepareStatement("DELETE FROM produto WHERE codigo = ?");
                ps.setInt(1, codigo);
                ps.executeUpdate();
                con.commit();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                carregarDados();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao excluir: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ProdutoDialog extends JDialog {
        private boolean confirmado = false;
        private JTextField tfMarca, tfCategoria, tfPublico, tfPreco,
                           tfCor, tfNumero, tfCategModelo, tfQtd, tfEstoqueMin;
        private JComboBox<String> cbFilial;
        private Integer codigoEdicao;

        public ProdutoDialog(JFrame parent, Integer codigo) {
            super(parent, codigo == null ? "Novo Produto" : "Editar Produto", true);
            this.codigoEdicao = codigo;
            setSize(480, 520);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout());

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(Cores.BRANCO);
            form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints g = new GridBagConstraints();
            g.fill = GridBagConstraints.HORIZONTAL;
            g.insets = new Insets(4, 4, 4, 4);
            g.weightx = 1.0;

            String[] labels = {"Marca:", "Categoria:", "Público-alvo:", "Preço (R$):",
                               "Cor:", "Número (tamanho):", "Categoria do Modelo:",
                               "Quantidade inicial:", "Estoque mínimo:"};
            JTextField[] fields = new JTextField[labels.length];
            for (int i = 0; i < labels.length; i++) {
                g.gridx = 0; g.gridy = i; g.weightx = 0;
                JLabel lbl = new JLabel(labels[i]);
                lbl.setFont(Cores.LABEL);
                lbl.setForeground(Cores.CINZA_TEXTO);
                form.add(lbl, g);
                g.gridx = 1; g.weightx = 1;
                fields[i] = new JTextField(15);
                fields[i].setFont(Cores.TEXTO);
                form.add(fields[i], g);
            }
            tfMarca = fields[0];
            tfCategoria = fields[1];
            tfPublico = fields[2];
            tfPreco = fields[3];
            tfCor = fields[4];
            tfNumero = fields[5];
            tfCategModelo = fields[6];
            tfQtd = fields[7];
            tfEstoqueMin = fields[8];

            g.gridx = 0; g.gridy = labels.length; g.weightx = 0;
            JLabel lblFilial = new JLabel("Filial:");
            lblFilial.setFont(Cores.LABEL);
            lblFilial.setForeground(Cores.CINZA_TEXTO);
            form.add(lblFilial, g);
            g.gridx = 1; g.weightx = 1;
            cbFilial = new JComboBox<>();
            cbFilial.setFont(Cores.TEXTO);
            carregarFiliais();
            form.add(cbFilial, g);

            JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            botoes.setBackground(Cores.BRANCO);
            Botao btnSalvar = new Botao("💾 Salvar", Cores.BTN_VERDE);
            Botao btnCancelar = new Botao("❌ Cancelar", Cores.BTN_CINZA);
            btnSalvar.addActionListener(e -> salvar());
            btnCancelar.addActionListener(e -> dispose());
            botoes.add(btnSalvar);
            botoes.add(btnCancelar);

            add(form, BorderLayout.CENTER);
            add(botoes, BorderLayout.SOUTH);

            if (codigoEdicao != null) {
                carregarDadosEdicao();
            }
            pack();
        }

        private void carregarFiliais() {
            try (Connection con = Conexao.obterConexao()) {
                ResultSet rs = con.createStatement().executeQuery(
                    "SELECT id_filial, nome_filial FROM filial ORDER BY id_filial");
                while (rs.next()) {
                    cbFilial.addItem(rs.getInt("id_filial") + " - " + rs.getString("nome_filial"));
                }
            } catch (Exception e) {
                cbFilial.addItem("1 - Loja Centro");
            }
        }

        private void carregarDadosEdicao() {
            try (Connection con = Conexao.obterConexao()) {
                PreparedStatement ps = con.prepareStatement(
                    "SELECT p.*, m.cor, m.numero, m.categoria_modelo, " +
                    "a.quantidade AS qtd_atual, e.quantidade_minima " +
                    "FROM produto p " +
                    "JOIN modelo m ON m.codigo = p.id_modelo " +
                    "LEFT JOIN armazenamento a ON a.codigo = p.codigo " +
                    "LEFT JOIN estoque e ON e.id_estoque = a.id_estoque " +
                    "WHERE p.codigo = ?");
                ps.setInt(1, codigoEdicao);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    tfMarca.setText(rs.getString("marca"));
                    tfCategoria.setText(rs.getString("categoria"));
                    tfPublico.setText(rs.getString("publico_alvo"));
                    tfPreco.setText(rs.getBigDecimal("preco").toString());
                    tfCor.setText(rs.getString("cor"));
                    tfNumero.setText(String.valueOf(rs.getInt("numero")));
                    tfCategModelo.setText(rs.getString("categoria_modelo"));
                    tfQtd.setText(String.valueOf(rs.getInt("qtd_atual")));
                    tfEstoqueMin.setText(String.valueOf(rs.getInt("quantidade_minima")));
                    // Filial
                    int idFilial = rs.getInt("id_filial");
                    for (int i = 0; i < cbFilial.getItemCount(); i++) {
                        if (cbFilial.getItemAt(i).startsWith(idFilial + " -")) {
                            cbFilial.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + e.getMessage());
            }
        }

        private void salvar() {
            try {
                String marca = tfMarca.getText().trim();
                String categoria = tfCategoria.getText().trim();
                String publico = tfPublico.getText().trim();
                String precoStr = tfPreco.getText().trim().replace(",", ".");
                String cor = tfCor.getText().trim();
                String numeroStr = tfNumero.getText().trim();
                String categModelo = tfCategModelo.getText().trim();
                String qtdStr = tfQtd.getText().trim();
                String minStr = tfEstoqueMin.getText().trim();

                if (marca.isEmpty() || categoria.isEmpty() || publico.isEmpty() ||
                    precoStr.isEmpty() || cor.isEmpty() || numeroStr.isEmpty() ||
                    categModelo.isEmpty() || qtdStr.isEmpty() || minStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
                    return;
                }

                BigDecimal preco = new BigDecimal(precoStr);
                int numero = Integer.parseInt(numeroStr);
                int quantidade = Integer.parseInt(qtdStr);
                int estoqueMin = Integer.parseInt(minStr);

                String filialItem = (String) cbFilial.getSelectedItem();
                int idFilial = Integer.parseInt(filialItem.split(" - ")[0]);

                try (Connection con = Conexao.obterConexao()) {
                    con.setAutoCommit(false);

                    ModeloDAO modeloDAO = new ModeloDAO();
                    Modelo modelo = new Modelo(cor, numero, categModelo);
                    if (codigoEdicao == null) {
                        modeloDAO.inserir(modelo);
                    } else {
                        PreparedStatement ps = con.prepareStatement(
                            "SELECT id_modelo FROM produto WHERE codigo = ?");
                        ps.setInt(1, codigoEdicao);
                        ResultSet rs = ps.executeQuery();
                        if (rs.next()) {
                            modelo.setCodigo(rs.getInt("id_modelo"));
                            modeloDAO.atualizar(modelo);
                        }
                    }

                    ProdutoDAO produtoDAO = new ProdutoDAO();
                    Produto produto = new Produto(marca, categoria, publico, preco, modelo.getCodigo());
                    if (codigoEdicao == null) {
                        produtoDAO.inserir(produto);
                        codigoEdicao = produto.getCodigo();
                    } else {
                        produto.setCodigo(codigoEdicao);
                        produtoDAO.atualizar(produto);
                    }

                    EstoqueDAO estoqueDAO = new EstoqueDAO();
                    Estoque estoque = new Estoque(estoqueMin);
                    PreparedStatement psEst = con.prepareStatement(
                        "SELECT e.id_estoque FROM estoque e " +
                        "JOIN filial f ON f.id_estoque = e.id_estoque " +
                        "WHERE f.id_filial = ?");
                    psEst.setInt(1, idFilial);
                    ResultSet rsEst = psEst.executeQuery();
                    if (rsEst.next()) {
                        estoque.setIdEstoque(rsEst.getInt("id_estoque"));
                        estoqueDAO.atualizar(estoque);
                    } else {
                        estoqueDAO.inserir(estoque);
                        PreparedStatement psFilial = con.prepareStatement(
                            "UPDATE filial SET id_estoque = ? WHERE id_filial = ?");
                        psFilial.setInt(1, estoque.getIdEstoque());
                        psFilial.setInt(2, idFilial);
                        psFilial.executeUpdate();
                    }

                    ArmazenamentoDAO armDAO = new ArmazenamentoDAO();
                    PreparedStatement psArm = con.prepareStatement(
                        "SELECT * FROM armazenamento WHERE id_estoque = ? AND codigo = ?");
                    psArm.setInt(1, estoque.getIdEstoque());
                    psArm.setInt(2, codigoEdicao);
                    ResultSet rsArm = psArm.executeQuery();
                    if (rsArm.next()) {
                        armDAO.atualizarQuantidade(estoque.getIdEstoque(), codigoEdicao, quantidade);
                    } else {
                        Armazenamento arm = new Armazenamento(estoque.getIdEstoque(), codigoEdicao, quantidade);
                        armDAO.inserir(arm);
                    }

                    con.commit();
                    confirmado = true;
                    dispose();
                    JOptionPane.showMessageDialog(EstoquePanel.this,
                            "Produto salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Verifique os campos numéricos (preço, número, quantidade).");
            }
        }

        public boolean isConfirmado() { return confirmado; }
    }
}
