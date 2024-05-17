package com.system.shoecleaningsystem.booking;

import com.system.shoecleaningsystem.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String shoeName;

    private Integer shoeSize;

    private String shoeColour;

    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User createdBy;

    public Booking(String shoeName, Integer shoeSize, String shoeColour, String paymentMethod, User createdBy) {
        this.shoeName = shoeName;
        this.shoeSize = shoeSize;
        this.shoeColour = shoeColour;
        this.paymentMethod = paymentMethod;
        this.createdBy = createdBy;
    }

    public Booking() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShoeName() {
        return shoeName;
    }

    public void setShoeName(String shoeName) {
        this.shoeName = shoeName;
    }

    public Integer getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(Integer shoeSize) {
        this.shoeSize = shoeSize;
    }

    public String getShoeColour() {
        return shoeColour;
    }

    public void setShoeColour(String shoeColour) {
        this.shoeColour = shoeColour;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
