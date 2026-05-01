package br.maua.dominox;
import java.util.HashMap;

// pseudo bando de dados
public class IDandPasswords {
    
     HashMap<String, String> logininfo = new HashMap<String, String>();
    
    IDandPasswords(){
        
        logininfo.put("felipe", "1");
        
   }
   
   protected HashMap getLoginInfo(){
        return logininfo;
   }
}
