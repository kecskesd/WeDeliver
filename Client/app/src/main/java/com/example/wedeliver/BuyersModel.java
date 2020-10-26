package com.example.wedeliver;

import java.util.ArrayList;
import java.util.List;

public class BuyersModel {

    private static BuyersModel instance = null;
    public String firstname, lastname, email;
    public int id;
    public List<BuyersModel> listOfBuyers = new ArrayList<BuyersModel>();

    BuyersModel(){
        String[] listOfBuyers = null;
    };

    public static BuyersModel getInstance(){
        if(instance == null){
            instance = new BuyersModel();
        }
        return instance;
    }

    // funkcia na naplnenie listu pouzivatelov, ktory pre niekoho kupuju
    public void fill(int index, String meno, String priezvisko, String email, int id){
        BuyersModel novy = new BuyersModel();
        novy.firstname = meno;
        novy.lastname = priezvisko;
        novy.email = email;
        novy.id = id;

        listOfBuyers.add(index, novy);
    }

    // premazanie listu kupujucich pri znobunacitani hlavnej stranky
    public void drop(){
        listOfBuyers.clear();
    }
}
