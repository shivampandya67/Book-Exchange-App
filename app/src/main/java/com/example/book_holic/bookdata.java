package com.example.book_holic;

public class bookdata {
    String Title,Description,Category,Amount,Type,ImageUrl,phone;

    public bookdata() {
    }

    public bookdata(String title, String description, String category, String amount, String type, String imageUrl, String phoneNo) {
        Title = title;
        Description = description;
        Category = category;
        Amount = amount;
        Type = type;
        ImageUrl = imageUrl;
        phone = phoneNo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getphone() {
        return phone;
    }

    public void setPhoneNo(String phoneNo) {
        phone = phoneNo;
    }
}