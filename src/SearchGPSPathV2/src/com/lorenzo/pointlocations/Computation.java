package com.lorenzo.pointlocations;


public class Computation {

    public Computation() {
    }
    
    public static int counterClockWise(Point p1, Point p2, Point p3) {
        int res = 0;
        double i = p2.getX() - p1.getX();
        double k = p2.getY() - p1.getY();
        double j = p3.getX() - p1.getX();
        double l = p3.getY() - p1.getY();
        if(i * l > k * j)
            res = 1;
        if(i * l < k * j)
            res = -1;
        if(i * l == k * j)
            if(i * j < 0 || k * l < 0)
                res = -1;
            else
            if(i * i + k * k >= j * j + l * l)
                res = 0;
            else
                res = 1;
        return res;
    }
    
    
    public static boolean isAbove(Point point, Point vertice1, Point vertice2) {
        int i = counterClockWise(point, vertice1, vertice2);
        return !(i < 0); //restituisce true se il punto point è sopra il segmento vertice1-vertice2, false alrimenti
    }
    
    public static void showD(Node node){
        String a = "il padre e': ";
        if("t".equals(node.node)){
            a += "t: " + node.t.name;
        } 
        else if("x".equals(node.node)){
            a += "x: " + node.p.nome;

        }
        else{
            a += "y: " + node.segment.nome;
        }
        System.out.println(a);
        if(node.left != null ){
        String b = "il figlio sx e': ";
        if("t".equals(node.left.node)){
            b += "t: " + node.left.t.name;
        } 
        else if("x".equals(node.left.node)){
            b += "x: " + node.left.p.nome;

        }
        else{
            b += "y:" + node.left.segment.nome;
        }
        System.out.println(b);
        }
        if(node.right != null){
        String c = "il figlio dx e': ";
        if("t".equals(node.right.node)){
            c += "t: " + node.right.t.name;
        } 
        else if("x".equals(node.right.node)){
            c += "x: " + node.right.p.nome ;

        }
        else{
            c += "y: " + node.right.segment.nome;
        }
        System.out.println(c);
        }
        if(node.left != null){
            showD(node.left);
        }
        if(node.right != null){
            showD(node.right);
        }
    } 
}
