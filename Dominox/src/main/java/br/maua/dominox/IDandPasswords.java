package br.maua.dominox;
import java.util.HashMap;

// pseudo bando de dados
public class IDandPasswords {
    
     HashMap<String, String> logininfo = new HashMap<String, String>();
    
    IDandPasswords(){
        
        logininfo.put("felipe", "1");
        logininfo.put("pedro", "123");
   }
   
   protected HashMap getLoginInfo(){
        return logininfo;
   }
   protected int ID;
    
    public void getID(String userID){
        
        if(userID == "felipe"){
            ID += 1;
            this.ID = ID;
            
        }
        else{
            this.ID = ID;
        }
    }
}
