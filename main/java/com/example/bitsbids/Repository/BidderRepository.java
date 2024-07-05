package com.example.bitsbids.Repository;

import com.example.bitsbids.entity.Bidder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidderRepository extends JpaRepository<Bidder,Long> {

}
