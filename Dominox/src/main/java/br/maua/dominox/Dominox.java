
package br.maua.dominox; 

public class Dominox {

    public static void main(String[] args) {
        		
		IDandPasswords idandPasswords = new IDandPasswords();
				
		LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());


    }
}
