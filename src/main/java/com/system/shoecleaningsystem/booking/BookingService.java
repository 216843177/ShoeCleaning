package com.system.shoecleaningsystem.booking;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired private BookRepository bookRepository;

    public void makeBooking(Booking booking){
        bookRepository.save(booking);
    }
}
