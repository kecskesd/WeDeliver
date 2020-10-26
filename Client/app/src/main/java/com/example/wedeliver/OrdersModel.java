package com.example.wedeliver;

import java.util.ArrayList;
import java.util.List;

public class OrdersModel {

    private static OrdersModel instance = null;
    public String name, address, store, products;
    public int id, customerId, status;
    public List<OrdersModel> listOfOrders = new ArrayList<OrdersModel>();
    public List<OrdersModel> listOfRequests = new ArrayList<>();

    OrdersModel(){
        String[] listOfBuyers = null;
    };

    public static OrdersModel getInstance(){
        if(instance == null){
            instance = new OrdersModel();
        }
        return instance;
    }

    // naplnanie listu objektami typu OrdersModel
    public void fill(int index, String name, String address, String store, String products, int customerId, int status, int id){
        OrdersModel order = new OrdersModel();
        order.name = name;
        order.id = id;
        order.customerId = customerId;
        order.address = address;
        order.store = store;
        order.products = products;
        order.status = status;

        listOfOrders.add(index, order);
    }

    // naplnanie listu objektami typu OrdersModel
    public void fill_request(int index, String name, String address, String store, String products, int customerId, int id){
        OrdersModel order = new OrdersModel();
        order.name = name;
        order.id = id;
        order.customerId = customerId;
        order.address = address;
        order.store = store;
        order.products = products;

        listOfRequests.add(index, order);
    }

    // vymazanie celeho listu
    public void drop(){
        listOfOrders.clear();
    }
    public void drop_requests(){
        listOfRequests.clear();
    }


}
