package com.lojasapatos.ui.cliente;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.ui.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class CatalogoPanel extends JPanel {

    private final ClienteFrame frame;
    private JPanel gridProdutos;
    private String categoriaFiltro = "Todos";

    public CatalogoPanel(ClienteFrame frame) {
        this.frame = frame;
        setBackground(Cores.CINZA_FUNDO);
        setLayout(new BorderLayout());
        add(criarSidebar(), BorderLayout.WEST);
        add(criarArea(),    BorderLayout.CENTER);
        carregarProdutos();
    }

    /* ===== SIDEBAR DE CATEGORIAS ===== */
    private JPanel criarSidebar() {
        JPanel p=new JPanel();
        p.setPreferredSize(new Dimension(170,0));
        p.setBackground(Cores.BRANCO);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(20,16,20,16));

        JLabel lCat=new JLabel("Categorias");
        lCat.setFont(Cores.SUBTITULO); lCat.setForeground(Cores.TEXTO_ESCURO);
        lCat.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lCat); p.add(Box.createVerticalStrut(12));

        String[] cats={"Todos","Tênis","Sapatos Sociais","Sapatênis","Botas","Promoções"};
        for(String cat:cats){
            JButton btn=catBtn(cat);
            btn.addActionListener(e->{ categoriaFiltro=cat; carregarProdutos(); });
            p.add(btn); p.add(Box.createVerticalStrut(4));
        }
        return p;
    }

    private JButton catBtn(String texto) {
        JButton b=new JButton(texto);
        b.setFont(Cores.TEXTO); b.setForeground(Cores.TEXTO_ESCURO);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setContentAreaFilled(false); b.setBorderPainted(false);
        b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6,4,6,4));
        b.setMaximumSize(new Dimension(200,36));
        b.setAlignmentX(LEFT_ALIGNMENT);
        b.addMouseListener(new MouseAdapter(){
            Color orig=b.getForeground();
            public void mouseEntered(MouseEvent e){ b.setForeground(Cores.CLIENTE_ACCENT); }
            public void mouseExited(MouseEvent e){ b.setForeground(orig); }
        });
        return b;
    }

    private JScrollPane criarArea() {
        gridProdutos=new JPanel(new WrapLayout(FlowLayout.LEFT,16,16));
        gridProdutos.setBackground(Cores.CINZA_FUNDO);
        JScrollPane sp=new JScrollPane(gridProdutos);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }
  
    private JPanel criarCardProduto(int codigo, String nome, String cat,
                                    BigDecimal preco, BigDecimal precoAnt, boolean oferta) {
        JPanel card=new JPanel(new BorderLayout(0,8)){
            @Override protected void paintComponent(Graphics g){
                super.paintComponent(g);
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Cores.BRANCO);
                g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,14,14);
                g2.setColor(Cores.CINZA_BORDA);
                g2.drawRoundRect(0,0,getWidth()-2,getHeight()-2,14,14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(200,280));
        card.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        // Badge
        JPanel topo=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        topo.setOpaque(false);
        if(oferta){
            JLabel badge=new JLabel(precoAnt!=null?"PROMOÇÃO":"OFERTA");
            badge.setFont(new Font("Segoe UI",Font.BOLD,10));
            badge.setForeground(Cores.BRANCO);
            badge.setBackground(oferta&&precoAnt!=null?Cores.CLIENTE_ACCENT:Cores.CARD_LARANJA);
            badge.setOpaque(true);
            badge.setBorder(BorderFactory.createEmptyBorder(2,6,2,6));
            topo.add(badge);
        }
        card.add(topo,BorderLayout.NORTH);

        // Imagem (placeholder com ícone)
        JPanel imgPanel=new JPanel(new GridBagLayout());
        imgPanel.setBackground(new Color(245,245,250));
        imgPanel.setPreferredSize(new Dimension(180,110));
        JLabel icone=new JLabel("👟",SwingConstants.CENTER);
        icone.setFont(new Font("Segoe UI",Font.PLAIN,52));
        imgPanel.add(icone);
        card.add(imgPanel,BorderLayout.CENTER);

        // Info
        JPanel info=new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));

        JLabel lNome=new JLabel(nome);
        lNome.setFont(Cores.LABEL); lNome.setForeground(Cores.TEXTO_ESCURO);
        info.add(lNome); info.add(Box.createVerticalStrut(2));

        if(precoAnt!=null){
            JLabel lAntes=new JLabel("<html><strike>R$ "+precoAnt+"</strike></html>");
            lAntes.setFont(Cores.PEQUENO); lAntes.setForeground(Cores.CINZA_TEXTO);
            info.add(lAntes);
        }
        JLabel lPreco=new JLabel("R$ "+preco);
        lPreco.setFont(new Font("Segoe UI",Font.BOLD,14));
        lPreco.setForeground(oferta&&precoAnt!=null?Cores.CLIENTE_ACCENT:Cores.TEXTO_ESCURO);
        info.add(lPreco); info.add(Box.createVerticalStrut(6));

        Botao btnVer=new Botao("Ver detalhes",Cores.CLIENTE_PRIMARY);
        btnVer.setAlignmentX(LEFT_ALIGNMENT);
        btnVer.addActionListener(e->{
            int conf=JOptionPane.showConfirmDialog(frame,
                "<html><b>"+nome+"</b><br>Categoria: "+cat+"<br>Preço: R$ "+preco+
                "<br><br>Adicionar ao carrinho?</html>","Produto",JOptionPane.YES_NO_OPTION);
            if(conf==JOptionPane.YES_OPTION)
                frame.adicionarAoCarrinho(codigo,new Object[]{nome,preco,cat});
        });
        info.add(btnVer);
        card.add(info,BorderLayout.SOUTH);
        return card;
    }

    private void carregarProdutos() {
        gridProdutos.removeAll();
        try(Connection con=Conexao.obterConexao()){
            String where=categoriaFiltro.equals("Todos")||categoriaFiltro.equals("Promoções")?"":
                         " WHERE LOWER(p.categoria) LIKE LOWER('%"+categoriaFiltro+"%')";
            ResultSet rs=con.createStatement().executeQuery(
                "SELECT p.codigo, p.marca||' '||m.cor, p.categoria, p.preco " +
                "FROM produto p JOIN modelo m ON m.codigo=p.id_modelo"+where+" ORDER BY p.codigo LIMIT 20");
            while(rs.next()){
                boolean oferta=rs.getInt(1)%3==0;
                BigDecimal preco=rs.getBigDecimal(4);
                BigDecimal antes=oferta?preco.multiply(new java.math.BigDecimal("1.25")):null;
                gridProdutos.add(criarCardProduto(
                    rs.getInt(1),rs.getString(2),rs.getString(3),preco,antes,oferta));
            }
        } catch(Exception e){
            // dados de exemplo
            Object[][] mock={
                {1,"Tênis Runner","Tênis",new BigDecimal("189.90"),new BigDecimal("229.90"),true},
                {2,"Sapatênis Casual","Sapatênis",new BigDecimal("159.90"),null,false},
                {3,"Sapato Social Couro","Social",new BigDecimal("250.90"),new BigDecimal("310.00"),true},
                {4,"Bota Adventure","Botas",new BigDecimal("299.90"),null,false},
                {5,"Sapato Derby","Social",new BigDecimal("239.90"),null,false},
                {6,"Tênis Street","Tênis",new BigDecimal("179.00"),null,false}
            };
            for(Object[] m:mock)
                gridProdutos.add(criarCardProduto((int)m[0],(String)m[1],(String)m[2],
                    (BigDecimal)m[3],(BigDecimal)m[4],(boolean)m[5]));
        }
        gridProdutos.revalidate(); gridProdutos.repaint();
    }

    static class WrapLayout extends FlowLayout {
        WrapLayout(int align,int hgap,int vgap){super(align,hgap,vgap);}
        @Override public Dimension preferredLayoutSize(Container target){
            return computeSize(target, true);}
        @Override public Dimension minimumLayoutSize(Container target){
            return computeSize(target, false);}
        private Dimension computeSize(Container target, boolean preferred){
            synchronized(target.getTreeLock()){
                int tw=target.getWidth(); if(tw==0) tw=800;
                Insets in=target.getInsets();
                int w=tw-in.left-in.right-getHgap()*2;
                int h=in.top+in.bottom+getVgap()*2;
                int rowW=0, rowH=0;
                for(Component c:target.getComponents()){
                    if(!c.isVisible()) continue;
                    Dimension d=preferred?c.getPreferredSize():c.getMinimumSize();
                    if(rowW>0&&rowW+d.width+getHgap()>w){h+=rowH+getVgap();rowW=0;rowH=0;}
                    rowW+=d.width+getHgap(); rowH=Math.max(rowH,d.height);
                }
                h+=rowH;
                return new Dimension(tw,h);
            }
        }
    }
}
