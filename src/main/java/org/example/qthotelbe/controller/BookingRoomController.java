package org.example.qthotelbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.exception.InvalidBookingRequestException;
import org.example.qthotelbe.exception.ResourceNotFoundException;
import org.example.qthotelbe.model.BookedRoom;
import org.example.qthotelbe.model.Room;
import org.example.qthotelbe.response.BookingRomResponse;
import org.example.qthotelbe.response.RoomResponse;
import org.example.qthotelbe.service.IBookingService;
import org.example.qthotelbe.service.IRoomService;
import org.example.qthotelbe.service.MailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingRoomController {
    private final IBookingService bookingService;
    private final IRoomService roomService;
    private final MailService mailService;

    @GetMapping("/all-bookings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingRomResponse>> getAllBookings(){
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingRomResponse> bookingResponses =  new ArrayList<>();

        for (BookedRoom booking : bookings){
            BookingRomResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return  ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try{
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingRomResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    // Dat phong theo id cua room
    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId, @RequestBody BookedRoom bookingRequest){
        try {
            String confirmCode = bookingService.saveBooking(roomId, bookingRequest);
            sendMail(bookingRequest,confirmCode);


            return ResponseEntity.ok("Room Booked Successfully with confirmation code: " + confirmCode);
        }catch (InvalidBookingRequestException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


    @DeleteMapping("/booking/{bookingId}/delete")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId){
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking deleted successfully");
        }catch (ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingRomResponse>> getBookingByUserEmail(@PathVariable String email){
       List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
       List<BookingRomResponse> bookingResponses = new ArrayList<>();
       for (BookedRoom booking : bookings){
           BookingRomResponse bookingResponse = getBookingResponse(booking);
           bookingResponses.add(bookingResponse);
       }
         return ResponseEntity.ok(bookingResponses);
    }

    private BookingRomResponse getBookingResponse(BookedRoom booking) {

        Optional<Room> theRoom = roomService.getRoomById(booking.getRoom().getId());
        RoomResponse roomResponse = new RoomResponse(
                theRoom.get().getId(),
                theRoom.get().getRoomType(),
                theRoom.get().getRoomPrice()
        );
        return  new BookingRomResponse(
                booking.getBookingId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                booking.getGuestFullName(),
                booking.getGuestEmail(),
                booking.getNumberOfAdults(),
                booking.getNumberOfChildren(),
                booking.getTotalNumberOfGuest(),
                booking.getBookingConfirmationCode(),
                roomResponse);
    }
    public void sendMail(BookedRoom bookingRequest,String confirmCode){
        mailService.sendEmail(bookingRequest.getGuestEmail(), "Booking Confirmation", "Your booking has been confirmed with confirmation code: " + confirmCode
                + "\nCheck in date: " + bookingRequest.getCheckInDate()
                + "\nCheck out date: " + bookingRequest.getCheckOutDate()
                + "\nGuest name: " + bookingRequest.getGuestFullName()
                + "\nGuest email: " + bookingRequest.getGuestEmail()
                + "\nNumber of adults: " + bookingRequest.getNumberOfAdults()
                + "\nNumber of children: " + bookingRequest.getNumberOfChildren()
                + "\nTotal number of guests: " + bookingRequest.getTotalNumberOfGuest()
                + "\nRoom type: " + bookingRequest.getRoom().getRoomType()
                + "\nRoom price: " + bookingRequest.getRoom().getRoomPrice()
        );
    }
}
