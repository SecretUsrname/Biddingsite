package com.example.bitsbids.Controller;

import com.example.bitsbids.Repository.BidderRepository;
import com.example.bitsbids.entity.Bidder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class BidController {
    @Autowired
    BidderRepository bRepo;

    @GetMapping("/product-view")
    public ModelAndView addBidForm()
    {
        ModelAndView mav = new ModelAndView("product-view");
        Bidder newbidder = new Bidder();
        mav.addObject("Bidder",newbidder);
        return mav;
    }

    @PostMapping("/submit-bid/{productId}")
    public String SaveBid(@PathVariable Long productId, @ModelAttribute Bidder bidder){
        bRepo.save(bidder);
        return "redirect:/product-view/" + productId;

    }
}
