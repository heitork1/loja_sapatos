package com.lojasapatos.ui.admin;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;

public class VendasPanel extends JPanel {

    private DefaultTableModel modelo;
    private JLabel lblTotal, lblQtd;

    public VendasPanel() {
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout(0, 0));
        add(criarTopo(),   BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
        carregarDados();
    }

    private JPanel criarTopo() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JLabel titulo = new JLabel("Relatorio de Vendas");
        titulo.setFont(Cores.SUBTITULO); titulo.setForeground(Cores.TEXTO_ESCURO);
        p.add(titulo, BorderLayout.WEST);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filtros.setOpaque(false);

        JComboBox<String> periodo = new JComboBox<>(
                new String[]{"Personalizado", "Hoje", "Esta semana", "Este mes"});
        periodo.setFont(Cores.TEXTO);

        JLabel lDe = new JLabel("De:"); lDe.setFont(Cores.TEXTO);
        JTextField txtDe = campo("01/01/2026");
        JLabel lAte = new JLabel("Ate:"); lAte.setFont(Cores.TEXTO);
        JTextField txtAte = campo(java.time.LocalDate.now().toString());

        Botao btnFiltrar  = new Botao("Filtrar",      Cores.BTN_AZUL);
        Botao btnNova     = new Botao("+ Nova Venda", Cores.BTN_VERDE);

        btnFiltrar.addActionListener(e -> carregarDados());
        btnNova.addActionListener(e -> abrirNovaVenda());

        filtros.add(periodo); filtros.add(lDe); filtros.add(txtDe);
        filtros.add(lAte); filtros.add(txtAte); filtros.add(btnFiltrar); filtros.add(btnNova);
        p.add(filtros, BorderLayout.EAST);
        return p;
    }

    private JScrollPane criarTabela() {
        String[] cols = {"Nr Venda", "Data", "Cliente", "Filial", "Total", "Forma Pgto", "Itens"};
        modelo = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = DashboardPanel.estilizarTabela(new JTable(modelo));
        // Largura das colunas
        t.getColumnModel().getColumn(0).setMaxWidth(90);
        t.getColumnModel().getColumn(3).setPreferredWidth(160);
        t.getColumnModel().getColumn(5).setPreferredWidth(120);
        t.getColumnModel().getColumn(6).setMaxWidth(60);

        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createCompoundBorder(
                new SombraBorda(), BorderFactory.createEmptyBorder(0, 16, 0, 16)));
        return sp;
    }

    private JPanel criarRodape() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 24, 10));
        p.setBackground(Cores.BRANCO);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Cores.CINZA_BORDA));
        lblQtd   = new JLabel("Total de Vendas: 0");
        lblTotal = new JLabel("Total Geral: R$ 0,00");
        lblQtd.setFont(Cores.LABEL);   lblQtd.setForeground(Cores.TEXTO_ESCURO);
        lblTotal.setFont(Cores.LABEL); lblTotal.setForeground(Cores.CARD_VERDE);
        p.add(lblQtd); p.add(lblTotal);
        return p;
    }

    private void carregarDados() {
        modelo.setRowCount(0);
        int qtd = 0; double total = 0;
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT v.id_venda, TO_CHAR(v.data_venda,'DD/MM/YYYY HH24:MI'), " +
                "COALESCE(cl.nome,'Sem cadastro'), f.nome_filial, " +
                "COALESCE(SUM(c.quantidade*c.preco_unitario),0), v.forma_pagamento, " +
                "COUNT(c.codigo) " +
                "FROM venda v " +
                "JOIN filial f ON f.id_filial=v.id_filial " +
                "LEFT JOIN compra cp ON cp.id_venda=v.id_venda " +
                "LEFT JOIN cliente cl ON cl.cpf=cp.cpf " +
                "LEFT JOIN composicao c ON c.id_venda=v.id_venda " +
                "GROUP BY v.id_venda,v.data_venda,cl.nome,f.nome_filial,v.forma_pagamento " +
                "ORDER BY v.data_venda DESC");
            while (rs.next()) {
                double val = rs.getDouble(5); total += val; qtd++;
                modelo.addRow(new Object[]{
                    String.format("%06d", rs.getInt(1)), rs.getString(2),
                    rs.getString(3), rs.getString(4),
                    String.format("R$ %,.2f", val), rs.getString(6),
                    rs.getInt(7) + " item(s)"});
            }
        } catch (Exception e) {
            modelo.addRow(new Object[]{"000001","27/05/2026","Maria Oliveira","Filial Centro","R$ 299,90","Cartao","2 item(s)"});
        }
        lblQtd.setText("Total de Vendas: " + qtd);
        lblTotal.setText(String.format("Total Geral: R$ %,.2f", total));
    }

    //nova venda
    private void abrirNovaVenda() {
        JDialog dlg = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Registrar Nova Venda", true);
        dlg.setSize(620, 520);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout(0, 8));

        //cabecalho da dialog
        JPanel cabec = new JPanel(new GridLayout(1, 3, 10, 0));
        cabec.setBackground(Cores.BRANCO);
        cabec.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));

        JComboBox<String> cbFilial     = new JComboBox<>();
        JComboBox<String> cbFuncionario= new JComboBox<>();
        JComboBox<String> cbPagamento  = new JComboBox<>(
            new String[]{"Dinheiro","PIX","Cartao de Debito","Cartao de Credito"});
        cbPagamento.setFont(Cores.TEXTO);

        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT id_filial, nome_filial FROM filial ORDER BY id_filial");
            while (rs.next()) cbFilial.addItem(rs.getInt(1) + " - " + rs.getString(2));

            rs = con.createStatement().executeQuery(
                "SELECT codigo, nome FROM funcionario ORDER BY nome");
            while (rs.next()) cbFuncionario.addItem(rs.getInt(1) + " - " + rs.getString(2));
        } catch (Exception ignored) {
            cbFilial.addItem("1 - Filial");
            cbFuncionario.addItem("1 - Funcionario");
        }
        cbFilial.setFont(Cores.TEXTO); cbFuncionario.setFont(Cores.TEXTO);

        JPanel pFilial = rotulado("Filial:", cbFilial);
        JPanel pFunc   = rotulado("Funcionario:", cbFuncionario);
        JPanel pPag    = rotulado("Pagamento:", cbPagamento);
        cabec.add(pFilial); cabec.add(pFunc); cabec.add(pPag);

        //adicionar produto
        JPanel addProd = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        addProd.setBackground(Cores.CINZA_FUNDO);
        addProd.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));

        JComboBox<String> cbProduto = new JComboBox<>();
        cbProduto.setFont(Cores.TEXTO);
        cbProduto.setPreferredSize(new Dimension(240, 32));
        JTextField txtQtd = campo("1");
        txtQtd.setPreferredSize(new Dimension(50, 32));
        Botao btnAdd = new Botao("+ Adicionar", Cores.BTN_AZUL);

        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery(
                "SELECT p.codigo, p.marca||' '||m.cor||' (Nr '||m.numero||') R$ '||p.preco " +
                "FROM produto p JOIN modelo m ON m.codigo=p.id_modelo ORDER BY p.marca");
            while (rs.next()) cbProduto.addItem(rs.getInt(1) + " | " + rs.getString(2));
        } catch (Exception ignored) {}

        addProd.add(new JLabel("Produto:")); addProd.add(cbProduto);
        addProd.add(new JLabel("Qtd:"));    addProd.add(txtQtd);
        addProd.add(btnAdd);

        //tabela de itens
        String[] cols = {"Codigo","Produto","Qtd","Preco Unit.","Subtotal"};
        DefaultTableModel mdlItens = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tItens = DashboardPanel.estilizarTabela(new JTable(mdlItens));
        JPanel pItens = new JPanel(new BorderLayout());
        pItens.setBackground(Cores.BRANCO);
        pItens.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        pItens.add(new JScrollPane(tItens), BorderLayout.CENTER);

        //total e botoes
        JLabel lblTotalVenda = new JLabel("Total: R$ 0,00");
        lblTotalVenda.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalVenda.setForeground(Cores.CARD_VERDE);

        JPanel pBotoes = new JPanel(new BorderLayout());
        pBotoes.setBackground(Cores.BRANCO);
        pBotoes.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        pBotoes.add(lblTotalVenda, BorderLayout.WEST);

        JPanel botDireita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botDireita.setOpaque(false);
        Botao btnSalvar   = new Botao("Confirmar Venda", Cores.BTN_VERDE);
        Botao btnCancelar = new Botao("Cancelar",        Cores.BTN_CINZA);
        botDireita.add(btnCancelar); botDireita.add(btnSalvar);
        pBotoes.add(botDireita, BorderLayout.EAST);

        java.util.Map<Integer,BigDecimal> precos = new java.util.HashMap<>();
        try (Connection con = Conexao.obterConexao()) {
            ResultSet rs = con.createStatement().executeQuery("SELECT codigo, preco FROM produto");
            while (rs.next()) precos.put(rs.getInt(1), rs.getBigDecimal(2));
        } catch (Exception ignored) {}

        //acao adicionar item
        btnAdd.addActionListener(e -> {
            try {
                String sel = (String) cbProduto.getSelectedItem();
                if (sel == null) return;
                int cod = Integer.parseInt(sel.split(" \\| ")[0].trim());
                int qtd = Integer.parseInt(txtQtd.getText().trim());
                BigDecimal preco = precos.getOrDefault(cod, BigDecimal.ZERO);
                BigDecimal sub = preco.multiply(new BigDecimal(qtd));
                // Verificar se ja existe e incrementar
                for (int r = 0; r < mdlItens.getRowCount(); r++) {
                    if ((int)mdlItens.getValueAt(r,0) == cod) {
                        int novaQtd = (int)mdlItens.getValueAt(r,2) + qtd;
                        BigDecimal novoSub = preco.multiply(new BigDecimal(novaQtd));
                        mdlItens.setValueAt(novaQtd, r, 2);
                        mdlItens.setValueAt(String.format("R$ %,.2f", novoSub), r, 4);
                        recalcularTotal(mdlItens, lblTotalVenda);
                        return;
                    }
                }
                String nomeProd = sel.split(" \\| ")[1].split(" R\\$ ")[0].trim();
                mdlItens.addRow(new Object[]{cod, nomeProd, qtd,
                    String.format("R$ %,.2f", preco), String.format("R$ %,.2f", sub)});
                recalcularTotal(mdlItens, lblTotalVenda);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dlg, "Quantidade invalida.");
            }
        });

        //remover item com duplo clique
        tItens.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int r = tItens.getSelectedRow();
                    if (r >= 0 && JOptionPane.showConfirmDialog(dlg,
                            "Remover item?","Confirmar",JOptionPane.YES_NO_OPTION)==0) {
                        mdlItens.removeRow(r);
                        recalcularTotal(mdlItens, lblTotalVenda);
                    }
                }
            }
        });

        //acao confirmar venda
        btnSalvar.addActionListener(e -> {
            if (mdlItens.getRowCount() == 0) {
                JOptionPane.showMessageDialog(dlg, "Adicione pelo menos um produto."); return;
            }
            try (Connection con = Conexao.obterConexao()) {
                con.setAutoCommit(false);
                String filialItem = (String)cbFilial.getSelectedItem();
                String funcItem   = (String)cbFuncionario.getSelectedItem();
                int idFilial = Integer.parseInt(filialItem.split(" - ")[0].trim());
                int idFunc   = Integer.parseInt(funcItem.split(" - ")[0].trim());
                String pag   = (String)cbPagamento.getSelectedItem();

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO venda(data_venda,forma_pagamento,id_filial,id_funcionario) " +
                    "VALUES(NOW(),?,?,?) RETURNING id_venda");
                ps.setString(1,pag); ps.setInt(2,idFilial); ps.setInt(3,idFunc);
                ResultSet rs = ps.executeQuery();
                int idVenda = rs.next() ? rs.getInt(1) : 0;

                PreparedStatement psItem = con.prepareStatement(
                    "INSERT INTO composicao(id_venda,codigo,quantidade,preco_unitario) VALUES(?,?,?,?)");
                for (int r = 0; r < mdlItens.getRowCount(); r++) {
                    int cod = (int)mdlItens.getValueAt(r,0);
                    int qtd = (int)mdlItens.getValueAt(r,2);
                    BigDecimal preco = precos.getOrDefault(cod,BigDecimal.ONE);
                    psItem.setInt(1,idVenda); psItem.setInt(2,cod);
                    psItem.setInt(3,qtd); psItem.setBigDecimal(4,preco);
                    psItem.executeUpdate();

                    con.createStatement().executeUpdate(
                        "UPDATE armazenamento a SET quantidade = quantidade - " + qtd +
                        " FROM filial f WHERE f.id_estoque=a.id_estoque AND f.id_filial=" + idFilial +
                        " AND a.codigo=" + cod);
                }
                con.commit();
                dlg.dispose();
                carregarDados();
                JOptionPane.showMessageDialog(this,
                    "Venda #" + String.format("%06d", idVenda) + " registrada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dlg, "Erro ao salvar venda: " + ex.getMessage());
            }
        });

        btnCancelar.addActionListener(e -> dlg.dispose());

        dlg.add(cabec, BorderLayout.NORTH);
        JPanel centro = new JPanel(new BorderLayout(0,6));
        centro.setBackground(Cores.CINZA_FUNDO);
        centro.add(addProd, BorderLayout.NORTH);
        centro.add(pItens, BorderLayout.CENTER);
        dlg.add(centro, BorderLayout.CENTER);
        dlg.add(pBotoes, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    private void recalcularTotal(DefaultTableModel m, JLabel lbl) {
        BigDecimal total = BigDecimal.ZERO;
        for (int r = 0; r < m.getRowCount(); r++) {
            String sub = m.getValueAt(r,4).toString().replace("R$ ","").replace(",","").replace(".","");
            try { total = total.add(new BigDecimal(sub).divide(new BigDecimal(100))); } catch (Exception ignored){}
        }
        //recalcular a partir de qtd x preco
        total = BigDecimal.ZERO;
        for (int r = 0; r < m.getRowCount(); r++) {
            try {
                String precStr = m.getValueAt(r,3).toString().replace("R$ ","").replace(",",".");
                int qtd = (int)m.getValueAt(r,2);
                total = total.add(new BigDecimal(precStr).multiply(new BigDecimal(qtd)));
            } catch (Exception ignored){}
        }
        lbl.setText(String.format("Total: R$ %,.2f", total));
    }

    private JPanel rotulado(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0,3)); p.setBackground(Cores.BRANCO);
        JLabel l = new JLabel(label); l.setFont(Cores.LABEL); l.setForeground(Cores.CINZA_TEXTO);
        p.add(l, BorderLayout.NORTH); p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JTextField campo(String texto) {
        JTextField t = new JTextField(texto); t.setFont(Cores.TEXTO);
        t.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Cores.CINZA_BORDA,1,true),
            BorderFactory.createEmptyBorder(4,6,4,6)));
        return t;
    }
}
