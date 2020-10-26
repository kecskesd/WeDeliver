package reminderserver.models;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

    //definovanie tabulky Order v databaze a getters a setters pre kazdu premennu v tabulke

    public Order(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "buyerId")
    private Long buyerId;

    @Column(name = "buyerName")
    private String buyerName;

    @Column(name = "customerId")
    private Long customerId;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "status")
    private Integer status;

    @Column(name = "products")
    private String products;

    @Column(name = "shop")
    private String shop;

    @Column(name = "address")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("Order[id=%d, buyerId=%d, buyerName='%s', customerId=%d, customerName='%s', " +
                "status='%d', products='%s', shop='%s', address='%s']", id, buyerId, buyerName, customerId,
                customerName, status, products, shop, address);
    }
}
