package com.lojasapatos.ui.cliente;

import com.lojasapatos.model.Cliente;
import com.lojasapatos.ui.*;
import com.lojasapatos.ui.login.LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ClienteFrame extends JFrame {

    private Cliente clienteLogado = null;
    private List<int[]> carrinho = new ArrayList<>(); // {codProduto, qty}
    private Map<Integer,Object[]> produtosCache = new HashMap<>();

    private CardLayout cardLayout;
    private JPanel painelConteudo;
    private JLabel lblContadorCarrinho;
    private CatalogoPanel catalogoPanel;
    private CarrinhoPanel carrinhoPanel;
    private MinhaContaPanel minhaContaPanel;

    public ClienteFrame() {
        setTitle("Loja de Sapatos — Loja Virtual");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);
        painelConteudo.setBackground(Cores.CINZA_FUNDO);

        catalogoPanel  = new CatalogoPanel(this);
        carrinhoPanel  = new CarrinhoPanel(this);
        minhaContaPanel= new MinhaContaPanel(this);

        painelConteudo.add(catalogoPanel,   "Catalogo");
        painelConteudo.add(carrinhoPanel,   "Carrinho");
        painelConteudo.add(minhaContaPanel, "MinhaConta");
        add(painelConteudo, BorderLayout.CENTER);

        cardLayout.show(painelConteudo, "Catalogo");
    }

    private JPanel criarHeader() {
        JPanel h = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g;
                g2.setColor(Cores.CLIENTE_PRIMARY);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        h.setPreferredSize(new Dimension(0,60));
        h.setBorder(BorderFactory.createEmptyBorder(0,24,0,24));

        JLabel logo=new JLabel("👟  Loja de Sapatos");
        logo.setFont(new Font("Segoe UI",Font.BOLD,16));
        logo.setForeground(Cores.BRANCO);
        h.add(logo,BorderLayout.WEST);

        // Navegar
        JPanel nav=new JPanel(new FlowLayout(FlowLayout.CENTER,6,12));
        nav.setOpaque(false);
        String[] items={"Catálogo","Promoções"};
        for(String item:items){
            JButton btn=navBtn(item);
            btn.addActionListener(e->cardLayout.show(painelConteudo,"Catalogo"));
            nav.add(btn);
        }
        h.add(nav,BorderLayout.CENTER);

        JPanel direita=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,12));
        direita.setOpaque(false);

        JTextField busca=new JTextField(16);
        busca.setFont(Cores.TEXTO);
        busca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180,50,80),1,true),
            BorderFactory.createEmptyBorder(4,8,4,8)));
        busca.putClientProperty("JTextField.placeholderText","Buscar produtos...");
        direita.add(busca);

        // Carrinho com contador
        lblContadorCarrinho=new JLabel("🛒  0");
        lblContadorCarrinho.setFont(new Font("Segoe UI",Font.BOLD,14));
        lblContadorCarrinho.setForeground(Cores.BRANCO);
        lblContadorCarrinho.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblContadorCarrinho.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent e){irParaCarrinho();}});
        direita.add(lblContadorCarrinho);

        // Conta
        JButton btnConta=navBtn("👤  Minha Conta");
        btnConta.addActionListener(e->cardLayout.show(painelConteudo,"MinhaConta"));
        direita.add(btnConta);

        JButton btnSair=navBtn("Sair");
        btnSair.addActionListener(e->{dispose(); new LoginFrame().setVisible(true);});
        direita.add(btnSair);

        h.add(direita,BorderLayout.EAST);
        return h;
    }

    private JButton navBtn(String texto) {
        JButton b=new JButton(texto);
        b.setFont(Cores.TEXTO); b.setForeground(Cores.BRANCO);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public void adicionarAoCarrinho(int codigoProduto, Object[] dadosProduto) {
        produtosCache.put(codigoProduto, dadosProduto);
        for(int[] item:carrinho){
            if(item[0]==codigoProduto){ item[1]++; atualizarContador(); return; }
        }
        carrinho.add(new int[]{codigoProduto,1});
        atualizarContador();
        JOptionPane.showMessageDialog(this,"Produto adicionado ao carrinho!",
                "Carrinho",JOptionPane.INFORMATION_MESSAGE);
    }

    public void irParaCarrinho(){
        carrinhoPanel.recarregar(carrinho, produtosCache);
        cardLayout.show(painelConteudo,"Carrinho");
    }

    public void irParaCatalogo(){ cardLayout.show(painelConteudo,"Catalogo"); }

    public void setClienteLogado(Cliente c){ this.clienteLogado=c; }
    public Cliente getClienteLogado(){ return clienteLogado; }

    private void atualizarContador(){
        int total=carrinho.stream().mapToInt(i->i[1]).sum();
        lblContadorCarrinho.setText("🛒  "+total);
    }
}
