package br.maua.dominox;

import java.awt.event.*;

public class ActionHandler implements ActionListener {

    LoginPage lp;
    
    DataBase db;

    public ActionHandler(LoginPage lp) {
        this.lp = lp;
        this.db = new DataBase();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      
        if (e.getSource() == lp.resetButton) {
            lp.userIDField.setText("");
            lp.userPasswordField.setText("");
            lp.statusLabel.setText("");
        }
        
        if (e.getSource() == lp.loginButton) {
            db.validarAcesso(lp); 
        }
        
        
        if (e.getSource() == lp.registerButton) {
            new SignUpPage(); 
        }
    }
}