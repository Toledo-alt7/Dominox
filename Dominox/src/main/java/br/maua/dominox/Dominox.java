
package br.maua.dominox; 

import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Dominox {
    public static void main(String[] args) {   
        try {
            //Tenta obter a conexão
            Connection conn = ConnectionDB.getConexao();
            
            if (conn != null) {
                System.out.println("Conexão bem-sucedida!");
                
                // Garante que as tabelas existem antes de abrir a UI
                DataBase db = new DataBase();
                db.setDB(conn); 
                
                System.out.println("Estrutura do banco verificada. Iniciando jogo...");
                
                SwingUtilities.invokeLater(() -> {
                    new LoginPage();
                });
                
                // Fecha a conexão após preparar o ambiente se desejar, 
                // ou mantenha-a aberta se o ConnectionDB gerenciar isso.
                conn.close(); 
            } else {
                JOptionPane.showMessageDialog(null, "Erro: Não foi possível conectar ao banco MySQL. Verifique o arquivo .env.");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro crítico ao inicializar o sistema: " + e.getMessage());
        }
    }
}
