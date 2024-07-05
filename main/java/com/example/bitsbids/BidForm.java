package com.example.bitsbids;

public class BidForm {

    private Long productId;
    private Double bidAmount;

    private String bidderName;
    public Long getProductId() {
        return productId;
    }

    public Double getBidAmount() {
        return bidAmount;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setBidAmount(Double bidAmount) {
        this.bidAmount = bidAmount;
    }

    public void setBidderName(String bidderName) {
        this.bidderName = bidderName;
    }

    public String getBidderName() {
        return bidderName;
    }
}

