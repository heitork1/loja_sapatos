package com.lojasapatos.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Botao extends JButton {

    private Color cor;
    private boolean hover = false;

    public Botao(String texto, Color cor) {
        super(texto);
        this.cor = cor;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(Cores.LABEL);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(getPreferredSize().width + 16, 34));
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
            public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color c = hover ? cor.darker() : cor;
        g2.setColor(c);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}
