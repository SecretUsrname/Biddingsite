package com.example.bitsbids.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long price;
    private String imageName;
    private BigDecimal bidAmount;
    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }

    @Column(name = "bid_end_time")
    private LocalDateTime bidEndTime;

    public LocalDateTime getBidDeadline() {
        return bidEndTime;
    }

    public boolean isBidEnded() {
        LocalDateTime now = LocalDateTime.now();
        return bidEndTime.isBefore(now);
    }
}
