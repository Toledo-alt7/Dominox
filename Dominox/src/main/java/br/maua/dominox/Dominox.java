/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package br.maua.dominox; 
/**
 *
 * @author felipe
 */
public class Dominox {

    public static void main(String[] args) {
        		
		IDandPasswords idandPasswords = new IDandPasswords();
				
		LoginPage loginPage = new LoginPage(idandPasswords.getLoginInfo());


    }
}
