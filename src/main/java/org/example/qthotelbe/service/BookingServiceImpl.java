package org.example.qthotelbe.service;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.exception.InvalidBookingRequestException;
import org.example.qthotelbe.exception.ResourceNotFoundException;
import org.example.qthotelbe.model.BookedRoom;
import org.example.qthotelbe.model.Room;
import org.example.qthotelbe.repository.BookingRepository;
import org.example.qthotelbe.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final IRoomService roomService;

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }


    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {

        if (bookingRequest.getCheckOutDate() != null && bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new IllegalArgumentException("Check out date must be after check in date");
        }
        Room theRoom = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = theRoom.getBookings(); // danh sach booking cua room
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailable) {
            theRoom.addBooking(bookingRequest); // them booking vao room
            bookingRepository.save(bookingRequest); // save booking vao db
        } else {
            throw new InvalidBookingRequestException("Room is not available for the requested dates");
        }
        return bookingRequest.getBookingConfirmationCode();// tra ve code booking

    }

    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream().noneMatch(existingBooking ->
                bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate()) || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate()) || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate()) && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate())) || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate())) || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())

                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))

                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))

                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate()) && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate())));
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :" + confirmationCode));

    }
}
