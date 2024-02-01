package org.example.qthotelbe.service;


import org.example.qthotelbe.model.BookedRoom;

import java.util.List;

public interface IBookingService  {
    List<BookedRoom> getAllBookingsByRoomId(Long roomId);

}
