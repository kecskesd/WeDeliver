package com.example.wedeliver;


public class UserModel {

//    Singleton trieda, ktora obsahuje vsetky potrebne premenne pre pouzivatela

    private static UserModel instance = null;
    public String name, first_name, last_name, email, password;
    public int id;
    public boolean active;

    private UserModel(){
        name = "Neprihlásený";
    }

    public static UserModel getInstance(){
        if(instance == null){
            instance = new UserModel();
        }
        return instance;
    }

}
