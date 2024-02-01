package org.example.qthotelbe.service;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.model.BookedRoom;
import org.example.qthotelbe.repository.BookingRepository;
import org.example.qthotelbe.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
//        return bookingRepository.findAllById(1);
        return  null;
    }
}
