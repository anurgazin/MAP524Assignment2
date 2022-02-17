package com.example.map524assignment2;

public class Items {
    String name;
    int quantity;
    double price;
    Items(){
        name = "";
        quantity = 1;
        price = 0.0;
    }
    Items(String title, int qty, double p){
        name = title;
        quantity = qty;
        price = p;
    }
}
