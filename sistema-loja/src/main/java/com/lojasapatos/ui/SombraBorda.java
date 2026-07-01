package com.lojasapatos.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.AbstractBorder;

//Borda com sombra suave para os cards brancos.
public class SombraBorda extends AbstractBorder {
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Color sombra = new Color(0, 0, 0, 18);
        for (int i = 0; i < 4; i++) {
            g2.setColor(new Color(0, 0, 0, 12 - i * 2));
            g2.drawRoundRect(x + i, y + i, w - i * 2, h - i * 2, 12, 12);
        }
        g2.setColor(Cores.CINZA_BORDA);
        g2.drawRoundRect(x, y, w - 1, h - 1, 12, 12);
        g2.dispose();
    }
    @Override public Insets getBorderInsets(Component c) { return new Insets(4,4,4,4); }
}
