package sg.edu.nus.iss;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }
    
    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String dirPath = "\\data2";

        String filename = "";
       
        File newDirectory = new File(dirPath);
        if(newDirectory.exists()){
            System.out.println("Directory already exists");

        }else{
            newDirectory.mkdir();
        }


        System.out.println("Welcome to shopping cart");

        List<String> cartItems = new ArrayList<String>();
        Console cons = System.console();
        String input ="";

        while(!input.equals("quit")){
            input = cons.readLine("What do you want to perform ? type quit to exit program");

            switch(input){
                case "help":
                displayMessage("List to show the list of items in shopping cart");
                displayMessage("Login <name> ");
                displayMessage("add <item> ");
                displayMessage("delete <item>");
                displayMessage("quit to exit this program");
                break;
                case "list":{listCart(cartItems);}
               
                break;
               
            }
            String strValue="";
            if(input.startsWith("add")){
                input = input.replace(',',' ');
                
                Scanner scanner = new Scanner(input.substring(4));

                while(scanner.hasNext()){
                    strValue = scanner.next();
                    cartItems.add(strValue);

                }
                
            }
            if(input.startsWith("login")){
                input = input.replace(',',' ');
                
                Scanner scanner = new Scanner(input.substring(6));
                while(scanner.hasNext()){
                    filename = scanner.next();
                }

                File loginFile = new File(dirPath + File.separator + filename);
                boolean isCreated = loginFile.createNewFile();

                if (isCreated){
                    displayMessage("new file created"+loginFile.getCanonicalFile());
                }else{
                    displayMessage("File already created");
                }
            }
             if(input.startsWith("delete")){

                cartItems = deleteCartItem(cartItems,input);
                
            // //     Scanner scanner = new Scanner(input.substring(6));
            // //     while (scanner.hasNext()){
            // //         strValue = scanner.next();
            // //         int listItemIndex = Integer.parseInt(strValue);
            // //         if(listItemIndex <cartItems.size()){
            // //             cartItems.remove(listItemIndex);
            // //         }else{
            // //             displayMessage("Incorrect item index");
            // //         }
            // //     }
             }
        }

    } 

    public static List<String> deleteCartItem(List<String>cartItems, String input){
        String strValue;
        Scanner scanner = new Scanner(input.substring(6));
        while (scanner.hasNext()){
            strValue = scanner.next();
            System.out.println(strValue);
            int listItemIndex = Integer.parseInt(strValue);
            if(listItemIndex <cartItems.size()){
                cartItems.remove(listItemIndex);
            }else{
                displayMessage("Incorrect item index");
            } 
        }
        return(cartItems);
    }
    public static void listCart(List<String> cartItems){
        if(cartItems.size()>0){
            // for(String item:cartItems){
            //    displayMessage(item); 
            // }
            for(int i=0;i<cartItems.size();i++){
                System.out.printf("%d: %s\n",i,cartItems.get(i));
            }
            }else{
            displayMessage("Your cart is empty");
             }

    }
    public static void displayMessage(String message)   {
        System.out.println(message);
     }
}
