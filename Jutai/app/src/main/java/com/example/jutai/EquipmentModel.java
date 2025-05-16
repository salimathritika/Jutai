package com.example.jutai;

import java.io.Serializable;

public class EquipmentModel implements Serializable {
    private int id;
    private String name;
    private String category;
    private String description;
    private double price;
    private byte[] image;
    private String availabilityStatus;  // New field

    // Constructor
    public EquipmentModel(int id, String name, String category, String description, double price, byte[] image, String availabilityStatus) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.image = image;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters
    public int getId() { return id; }

    public String getName() { return name; }

    public String getCategory() { return category; }

    public String getDescription() { return description; }

    public double getPrice() { return price; }

    public byte[] getImage() { return image; }

    public String getAvailabilityStatus() { return availabilityStatus; }

    public void setAvailabilityStatus(String newStatus) {
        this.availabilityStatus = newStatus;
    }
}
