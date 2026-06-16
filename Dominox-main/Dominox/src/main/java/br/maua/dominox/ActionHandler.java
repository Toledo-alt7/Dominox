package br.maua.dominox;

import java.awt.event.*;

public class ActionHandler implements ActionListener {

    LoginPage lp;
    
    DataBase db;

    ValidateAccess va;

    public ActionHandler(LoginPage lp) {
        this.lp = lp;
        this.va = new ValidateAccess();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      
        if (e.getSource() == lp.resetButton) {
            lp.userIDField.setText("");
            lp.userPasswordField.setText("");
            lp.statusLabel.setText("");
        }
        
        if (e.getSource() == lp.loginButton) {
            va.validarAcesso(lp);
        }
        
        
        if (e.getSource() == lp.registerButton) {
            new SignUpPage(); 
        }
    }
}