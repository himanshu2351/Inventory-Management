package com.inventory.management.model;

import javax.persistence.*;

import com.inventory.management.dto.enums.OrderStatus;

import java.util.Date;

@Entity
@Table(name = "order1")
public class Order1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long order_id;

    @Column(name = "status")
    private String status;

    @Column(name = "quantity")
    private float quantity;

    @Column(name = "price")
    private String price;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column
    private int CustomerId;

    @Column(name = "product_id")
    private long productId;

    public Order1() {
        super();
    }


    public long getOrder_id() {
        return order_id;
    }


    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }


    public String getPrice() {
        return price;
    }


    public void setPrice(String price) {
        this.price = price;
    }


    public float getQuantity() {
        return quantity;
    }


    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
