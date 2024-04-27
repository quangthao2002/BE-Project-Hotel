package org.example.qthotelbe.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.example.qthotelbe.exception.PhotoRetrievalException;
import org.example.qthotelbe.exception.ResourceNotFoundException;
import org.example.qthotelbe.model.BookedRoom;
import org.example.qthotelbe.model.Room;
import org.example.qthotelbe.response.BookingRomResponse;
import org.example.qthotelbe.response.RoomResponse;
import org.example.qthotelbe.service.IBookingService;
import org.example.qthotelbe.service.IRoomService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final IBookingService bookingService;


    @PostMapping("/add/new-room")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> addRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
    ) throws SQLException, IOException {

        Room save = roomService.addRoom(photo, roomType, roomPrice); // them room vao db
        RoomResponse response = new RoomResponse(save.getId(), save.getRoomType(), save.getRoomPrice()); // tao response tu room vua duoc them de tra ve cho client
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room-types")
    public List<String> getAllRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {

        List<Room> rooms = roomService.getAllRooms();
        List<RoomResponse> responses = new ArrayList<>(); // tao 1 list response de tra ve cho client
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getPhotoByRoomId(room.getId()); // lay photo theo id room trong db
            if (photoBytes != null && photoBytes.length > 0) { // neu co photo thi tao response
                String photo = Base64.encodeBase64String(photoBytes); // chuyen photo tu byte sang String
                RoomResponse response = getRomResponse(room); // tao response tu room
                response.setPhoto(photo); // set photo cho response
                responses.add(response); // them response vao list response
            }
        }
        return ResponseEntity.ok(responses); // tra ve list response cho client
    }

    // Lay danh sach room con trong theo ngay checkin, checkout va loai phong
    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(@RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                                       @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                                        @RequestParam("roomType") String roomType) throws SQLException {

        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : availableRooms) {
            byte[] photoBytes = roomService.getPhotoByRoomId(room.getId()); //
            if (photoBytes != null && photoBytes.length > 0) {
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponse response = getRomResponse(room);
                response.setPhoto(photoBase64);
                roomResponses.add(response);
            }
        }
        if (roomResponses.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(roomResponses);
        }

    }
    private RoomResponse getRomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId()); // lay tat ca booking cua room theo id room
//        assert bookings != null; // dung de kiem tra neu bookings null thi throw exception chu khong se bi null pointer exception
//        List<BookingRomResponse> bookingInfo =  bookings.stream().map(booking -> new BookingRomResponse( // stream() :
//                booking.getBookingId(),booking.getCheckInDate(),booking.getCheckOutDate(),
//                booking.getBookingConfirmationCode())).toList(); // tao list bookingInfo tu list booking
        byte[] photoBytes = null; // tao 1 mang byte de luu photo
        Blob photoBlob = room.getPhoto(); // lay photo tu room
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length()); // chuyen photo tu Blob sang byte
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo for room with id: " + room.getId());
            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes);
    }

    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);
    }

    @PutMapping("update/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId, @RequestParam(required = false) String roomType, @RequestParam(required = false) BigDecimal roomPrice, @RequestParam(required = false) MultipartFile photo) throws SQLException, IOException {

        byte[] photoBytes = photo != null && !photo.isEmpty() ? photo.getBytes() : roomService.getPhotoByRoomId(roomId); // neu photo khac null va khong trong thi lay photo tu file , neu photo null hoac trong thi lay photo tu db
        Blob photoBlob = photoBytes != null ? new SerialBlob(photoBytes) : null; // chuyen photo tu byte sang Blob de luu vao db
        Room room = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        room.setPhoto(photoBlob);
        RoomResponse response = getRomResponse(room); // tao response tu room tra ve cho client
        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) throws SQLException {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        if (theRoom.isPresent()) { // isPresent() : kiem tra xem co room trong hay khong
            Room room = theRoom.get();
            RoomResponse response = getRomResponse(room); // tao response tu room tra ve cho client
            return ResponseEntity.ok(response); // tra ve response cho client
        } else {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
    }

}
