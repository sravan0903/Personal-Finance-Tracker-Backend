package com.example.ExpenseTracker.DTO;

public class ImageUploadResult {

    private String imageUrl;
    private String publicId;

    // No-args constructor
    public ImageUploadResult() {
    }

    // All-args constructor
    public ImageUploadResult(String imageUrl, String publicId) {
        this.imageUrl = imageUrl;
        this.publicId = publicId;
    }

    // Getters and Setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @Override
    public String toString() {
        return "ImageUploadResult{" +
                "imageUrl='" + imageUrl + '\'' +
                ", publicId='" + publicId + '\'' +
                '}';
    }
}
