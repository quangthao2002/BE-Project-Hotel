package org.example.qthotelbe.service;


import org.example.qthotelbe.model.BookedRoom;

import java.util.List;

public interface IBookingService  {
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

    List<BookedRoom> getAllBookings();


    String saveBooking(Long roomId, BookedRoom requestBooking);

    void cancelBooking(Long bookingId);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getBookingsByUserEmail(String email);
}
