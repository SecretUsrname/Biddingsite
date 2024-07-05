package com.example.bitsbids.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.math.BigDecimal;

@Entity
@Data
public class Bidder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private Long pdtid;
    private BigDecimal bidamount;
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPdtid(Long pdtid) {
        this.pdtid = pdtid;
    }

    public void setBidamount(BigDecimal bidamount) {
        this.bidamount = bidamount;
    }

}
