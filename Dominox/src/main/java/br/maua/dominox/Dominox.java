
package br.maua.dominox; 

import javax.swing.SwingUtilities;


public class Dominox {
    public static void main(String[] args) {   

        SwingUtilities.invokeLater(() -> {
            new LoginPage();
        });
    }
}
