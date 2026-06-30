package com.lojasapatos;

import com.lojasapatos.dao.Conexao;
import com.lojasapatos.ui.login.LoginFrame;

import javax.swing.*;
import java.sql.Connection;

public class App {

    public static void main(String[] args) {
        Connection c = Conexao.getConexao();
        if (c != null) {
            System.out.println("O sistema está online e conectado ao Postgres!");
            try { c.close(); } catch (Exception ignored) {}
        } else {
            System.out.println("O sistema está offline. Verifique o banco de dados.");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
