package com.election.backendjava;

public class fack {
    //recursive functie uitproberen
    public static void main(String[] args) {
        int n = 6;
        int nu = 7;
        int result = fac(n);
        int result1 = fac(nu);
        System.out.println(" de fack van " + n + " is " + result + " en de fack van " + nu + " is " + result1);

    }

    public static int fac(int num) {
        if (num == 0 || num == 1) {
            return 1;
        }
        return num * fac(num - 1);
    }

}
