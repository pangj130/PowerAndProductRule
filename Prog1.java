import java.util.*;
import java.io.*;
public class Prog1{
    private static Stack <String> s = new Stack <String>();
    private static ArrayList <String> a = new ArrayList <String>();
    public static void main(String [] args){
        String input = getInput();
        System.out.println();
        s = storage(input);
        convertStorage();
        modPower();
        differentiate();
        System.out.println("dy/dx = " + combineFin());
    }
    static String getInput(){ // gets the initial function
        Scanner kb = new Scanner(System.in);
        System.out.println("Enter a function. ");
        return kb.nextLine();
    }
    static int getNumOfDeriv(){ // gets the order of derivative
        Scanner kb = new Scanner(System.in);
        System.out.println("Enter to what derivative do you want to take? ");
        return kb.nextInt();
    } 
    static void modInput(String input){ // changes the function if the function is written with y = 
        int index = input.indexOf("=");
        input = input.substring(index+1);
    }
    static Stack <String> storage(String input){ // creates a Stack to store each term of the function
        String temp  = input;
        Stack <String> s= new Stack <String>();
        while(temp!=""){
            if(temp.contains("+") || temp.contains("-") && temp.indexOf("-", 1)!=temp.indexOf("^")+1){
                int indexpos = temp.lastIndexOf("+");
                int indexneg = temp.lastIndexOf("-");
                if(indexpos>indexneg){
                    s.push(temp.substring(indexpos));
                    temp = temp.substring(0, indexpos);
                }
                else{
                    s.push(temp.substring(indexneg));
                    temp = temp.substring(0, indexneg);
                }
            }
            else{
                temp = "+" + temp;
                s.push(temp);
                temp = "";
            }
            System.out.println(s);
        }
        if(s.peek().equals("+")){
            s.pop();
        }
        return s;
    }
    static void convertStorage(){ // converts the stack to arraylist, so each term can be accessed
        while(!s.empty()){
            try{
                int temp = Integer.parseInt(s.peek());
                a.add("0");
                s.pop();
            }
            catch(Exception e){
                a.add(s.pop());
            }
        }
    }
    static void modPower(){ // adds a power if there are no powers displayed in the input
        for(int i = 0; i < a.size(); i++){
            if(!a.get(i).contains("^") && a.get(i).contains("x")){
                a.set(i, a.get(i) + "^1");
            }
            if(a.get(i).length()>1 && a.get(i).substring(1, 2).equals("x")){
                a.set(i, a.get(i).substring(0, 1) + "1" + a.get(i).substring(1));
            }
        }
    }
    static void differentiate(){ // differentiate equations
        for(int j = 0; j < a.size(); j++){
            try{
                if(a.size()==1){
                    int x = Integer.parseInt(a.get(j));
                    a.set(j, "0");
                }
                else if(a.size()>1){
                    int x = Integer.parseInt(a.get(j));
                    a.remove(j);
                    j--;
                }
            } 
            catch (Exception e) {
                if(a.get(j).contains("^") && !(a.get(j).contains("sin") || a.get(j).contains("cos") || a.get(j).contains("tan") || a.get(j).contains("csc") || a.get(j).contains("sec") || a.get(j).contains("cot"))){
                    power(j);
                }
                else if(a.get(j).indexOf("^") == 4){
                    trig(j);
                }
                else if(a.get(j).indexOf("^") > 4){
                    productTrig(j);
                }
            }
        }
    }
    static void power(int j){ // general power rule
        int coeff = Integer.parseInt(a.get(j).substring(1, a.get(j).indexOf("x")));
        int exp = Integer.parseInt(a.get(j).substring(a.get(j).indexOf("^")+1));
        coeff*=exp;
        exp-=1;
        a.set(j, a.get(j).substring(0, 1) + coeff + "x^" + exp);
    }
    static String trig(String t, String sign){ // general trig rules with 2 parameters 
        if(t.equals("sin")){
            t = "cosx";
        }
        else if(t.equals("cos")){
            if(sign.equals("-"))
                 sign = "+";
            else
                sign = "-";
            t = "sinx";
        }
        else if(t.equals("tan")){
            t = "secxsecx";
        }
        else if(t.equals("sec")){
            t = "secxtanx";
        }
        else if(t.equals("csc")){
            if(sign.equals("-"))
                sign = "+";
            else
                sign = "-";
            t = "cscxcotx";
        }
        else{
            if(sign.equals("-"))
                sign = "+";
            else
                sign = "-";
            t = "cscxcscx";
         }
         return t;
    }
    static String trig(String t){ // single parameter
        if(t.equals("sin")){
            t = "cosx";
        }
        else if(t.equals("cos")){
            t = "-sinx";
        }
        else if(t.equals("tan")){
            t = "secxsecx";
        }
        else if(t.equals("sec")){
            t = "secxtanx";
        }
        else if(t.equals("csc")){
            t = "-cscxcotx";
        }
        else{
            t = "-cscxcscx";
        }
        return t;
    }
    static void trig(int j){ // trig rule without product or chain for element of a
        String sign = a.get(j).substring(0, 1);
        String t = a.get(j).substring(1, 4);
        t = trig(t, sign);
        a.set(j, sign + t);
    }
    static String trig(ArrayList <String> temp, String sign){ // helper method for product rule
        String res = "";
        HashMap <String, String> hash = new HashMap <String, String>();
        for(int k = 0; k < temp.size(); k++){
            hash.put(temp.get(k), trig(temp.get(k).substring(0, 3)));
        }
        String term = "";
        int count = 0;
        int index = 0;
        Iterator it1 = hash.entrySet().iterator();
        Iterator it2 = hash.entrySet().iterator();
        while(it1.hasNext()){ // assign value
            Map.Entry<String, String> pair1 = (Map.Entry<String, String>) it1.next();
            term = pair1.getValue();
            while(it2.hasNext()){ // assign key
                Map.Entry<String, String> pair2 = (Map.Entry<String, String>) it2.next();
                if(index!=count){
                    term+=pair2.getKey();
                }
                index++;
            }
            if(term.substring(0, 1)!="+" && !term.contains("-")){
                term = "+" + term;
            }
            index = 0;
            it2 = hash.entrySet().iterator();
            res+=term;
            count++;
        }
        if(res.substring(0, 1).equals("+")){
            res = res.substring(1);
        }
        return res;
    }
    static void productTrig(int j){ // product rule
        String sign = a.get(j).substring(0, 1);
        String t = a.get(j).substring(1, a.get(j).indexOf("^"));
        ArrayList <String> temp = new ArrayList <String>();
        for(int k = 0; k <= t.length()-4; k+=4){
            temp.add(t.substring(k, k+4));
        }
        String fin = trig(temp, sign);
        s = storage(fin);
        a.remove(j);
        while(!s.empty()){
            if(sign.equals("-")){
                String value = s.pop();
                if(value.substring(0, 1).equals("-")){
                    value = "+" + value.substring(1);
                }
                else if(value.substring(0,1)!="-"){
                    value = "-" + value.substring(1);
                }
                a.add(j, value);
            }
            else{
                a.add(j, s.pop());
            } 
        }
    }
    static void simplify(){
        for(int i = a.size()-1; i >= 0; i--){
            if(a.get(i).indexOf("^")>-1){
                if(Integer.parseInt(a.get(i).substring(a.get(i).indexOf("^")+1))==0){
                    a.set(i, a.get(i).substring(0, a.get(i).indexOf("x")));
                }
                else if(Integer.parseInt(a.get(i).substring(a.get(i).indexOf("^")+1))==1){
                    a.set(i, a.get(i).substring(0, a.get(i).indexOf("^"))); 
                }
            }
            if(a.get(i).contains("sin") && a.get(i).contains("csc")){
                int index1 = a.get(i).indexOf("sin");
                int index2 = a.get(i).indexOf("csc");
                if(index1>index2){
                    a.set(i, a.get(i).substring(0, index2) + a.get(i).substring(index2+4, index1) + a.get(i).substring(index1+4, a.get(i).length()));
                }
                else{
                    a.set(i, a.get(i).substring(0, index1) + a.get(i).substring(index1+4, index2) + a.get(i).substring(index2+4, a.get(i).length()));   
                }
                simplify();
            }
            if(a.get(i).contains("cos") && a.get(i).contains("sec")){
                int index1 = a.get(i).indexOf("cos");
                int index2 = a.get(i).indexOf("sec");
                if(index1>index2){
                    a.set(i, a.get(i).substring(0, index2) + a.get(i).substring(index2+4, index1) + a.get(i).substring(index1+4, a.get(i).length()));
                }
                else{
                    a.set(i, a.get(i).substring(0, index1) + a.get(i).substring(index1+4, index2) + a.get(i).substring(index2+4, a.get(i).length()));
                }
                simplify();
            }
            if(a.get(i).contains("tan") && a.get(i).contains("cot")){
                int index1 = a.get(i).indexOf("tan");
                int index2 = a.get(i).indexOf("cot");
                if(index1>index2){
                    a.set(i, a.get(i).substring(0, index2) + a.get(i).substring(index2+4, index1) + a.get(i).substring(index1+4, a.get(i).length()));
                }
                else{
                    a.set(i, a.get(i).substring(0, index1) + a.get(i).substring(index1+4, index2) + a.get(i).substring(index2+4, a.get(i).length()));
                }
                simplify();
            }
            if(a.get(i).contains("cos") && a.get(i).contains("tan")){
                int index1 = a.get(i).indexOf("cos");
                int index2 = a.get(i).indexOf("tan");
                if(index1>index2){
                    a.set(i, a.get(i).substring(0, index2) + a.get(i).substring(index2+4, index1) + a.get(i).substring(index1+4, a.get(i).length()) + "sinx");
                }
                else{
                    a.set(i, a.get(i).substring(0, index1) + a.get(i).substring(index1+4, index2) + a.get(i).substring(index2+4, a.get(i).length()) + "sinx");
                }
                simplify();
            }
            if(a.get(i).contains("sin") && a.get(i).contains("sec")){
                int index1 = a.get(i).indexOf("sin");
                int index2 = a.get(i).indexOf("sec");
                if(index1>index2){
                    a.set(i, a.get(i).substring(0, index2) + a.get(i).substring(index2+4, index1) + a.get(i).substring(index1+4, a.get(i).length()) + "tanx");
                }
                else{
                    a.set(i, a.get(i).substring(0, index1) + a.get(i).substring(index1+4, index2) + a.get(i).substring(index2+4, a.get(i).length()) + "tanx");
                }
                simplify();
            }
            if(a.get(i).contains("cos") && a.get(i).contains("csc")){
                int index1 = a.get(i).indexOf("cos");
                int index2 = a.get(i).indexOf("csc");
                if(index1>index2){
                    a.set(i, a.get(i).substring(0, index2) + a.get(i).substring(index2+4, index1) + a.get(i).substring(index1+4, a.get(i).length()) + "cotx");
                }
                else{
                    a.set(i, a.get(i).substring(0, index1) + a.get(i).substring(index1+4, index2) + a.get(i).substring(index2+4, a.get(i).length()) + "cotx");
                }
                simplify();
            }
            if(a.get(i).contains("cot") && a.get(i).contains("sin")){
                int index1 = a.get(i).indexOf("cot");
                int index2 = a.get(i).indexOf("sin");
                if(index1>index2){
                    a.set(i, a.get(i).substring(0, index2) + a.get(i).substring(index2+4, index1) + a.get(i).substring(index1+4, a.get(i).length()) + "cosx");
                }
                else{
                    a.set(i, a.get(i).substring(0, index1) + a.get(i).substring(index1+4, index2) + a.get(i).substring(index2+4, a.get(i).length()) + "cosx");
                }
                simplify();
            }
        }
    }
    static String combineFin(){ // prints result
        String fin = "";
        simplify();
        for(int i = 0; i < a.size(); i++){
            fin+=a.get(i);
        }
        if(fin.substring(0,1).equals("+")){
            fin = fin.substring(1);
        }
        return fin;
    }
}