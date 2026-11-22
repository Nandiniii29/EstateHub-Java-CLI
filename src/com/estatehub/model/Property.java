package com.estatehub.model;

public class Property {

    private int propertyId;
    private String title;
    private PropertyPurpose purpose;
    private PropertyType type;
    private String city;
    private String locality;
    private String address;
    private double price;
    private int bedrooms;
    private int bathrooms;
    private double area;
    private FurnishingStatus furnishingStatus;
    private String description;
    private ListingStatus listingStatus;
    private int ownerId;

    public Property(int propertyId, String title,
            PropertyPurpose purpose, PropertyType type,
            String city, String locality, String address,
            double price, int bedrooms, int bathrooms,
            double area, FurnishingStatus furnishingStatus,
            String description, ListingStatus listingStatus,
            int ownerId) {

        this.propertyId = propertyId;
        this.title = title;
        this.purpose = purpose;
        this.type = type;
        this.city = city;
        this.locality = locality;
        this.address = address;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.area = area;
        this.furnishingStatus = furnishingStatus;
        this.description = description;
        this.listingStatus = listingStatus;
        this.ownerId = ownerId;
    }

    public int getPropertyId() { return propertyId; }
    public String getTitle() { return title; }
    public PropertyPurpose getPurpose() { return purpose; }
    public PropertyType getType() { return type; }
    public String getCity() { return city; }
    public String getLocality() { return locality; }
    public String getAddress() { return address; }
    public double getPrice() { return price; }
    public int getBedrooms() { return bedrooms; }
    public int getBathrooms() { return bathrooms; }
    public double getArea() { return area; }
    public FurnishingStatus getFurnishingStatus() {
        return furnishingStatus;
    }
    public String getDescription() { return description; }
    public ListingStatus getListingStatus() { return listingStatus; }
    public int getOwnerId() { return ownerId; }

    public void setTitle(String title) { this.title = title; }
    public void setPurpose(PropertyPurpose purpose) { this.purpose = purpose; }
    public void setType(PropertyType type) { this.type = type; }
    public void setCity(String city) { this.city = city; }
    public void setLocality(String locality) { this.locality = locality; }
    public void setAddress(String address) { this.address = address; }
    public void setPrice(double price) { this.price = price; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }
    public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }
    public void setArea(double area) { this.area = area; }
    public void setFurnishingStatus(FurnishingStatus status) {
        this.furnishingStatus = status;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setListingStatus(ListingStatus status) {
        this.listingStatus = status;
    }

    @Override
    public String toString() {
        return "Property ID: " + propertyId +
                "\nTitle: " + title +
                "\nPurpose: " + purpose +
                "\nType: " + type +
                "\nCity: " + city +
                "\nLocality: " + locality +
                "\nPrice: " + price +
                "\nBedrooms: " + bedrooms +
                "\nBathrooms: " + bathrooms +
                "\nArea: " + area +
                "\nFurnishing: " + furnishingStatus +
                "\nStatus: " + listingStatus;
    }
}