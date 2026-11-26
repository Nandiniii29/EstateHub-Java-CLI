
package com.estatehub.model;

import java.time.LocalDateTime;

public class PropertyImage {

    private int imageId;
    private int propertyId;
    private String imageUrl;
    private String caption;
    private boolean primary;
    private LocalDateTime uploadedAt;

    public PropertyImage(
            int imageId,
            int propertyId,
            String imageUrl,
            String caption,
            boolean primary,
            LocalDateTime uploadedAt
    ) {
        this.imageId = imageId;
        this.propertyId = propertyId;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.primary = primary;
        this.uploadedAt = uploadedAt;
    }

    public int getImageId() {
        return imageId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public boolean isPrimary() {
        return primary;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public String toString() {
        return "\nImage ID: " + imageId
                + "\nProperty ID: " + propertyId
                + "\nImage URL: " + imageUrl
                + "\nCaption: " + caption
                + "\nPrimary Image: " + primary
                + "\nUploaded At: " + uploadedAt;
    }
}
