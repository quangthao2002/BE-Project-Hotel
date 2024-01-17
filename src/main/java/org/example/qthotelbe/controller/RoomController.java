package org.example.qthotelbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.model.Room;
import org.example.qthotelbe.reponse.RoomResponse;
import org.example.qthotelbe.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService roomService;


    @PostMapping("/add/new-room")
    public ResponseEntity<RoomResponse> addRoom(
            @RequestParam("photo")MultipartFile photo,
            @RequestParam("roomType")String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice
            ) throws SQLException, IOException {

        Room save = roomService.addRoom(photo, roomType, roomPrice); // them room vao db
        RoomResponse response =  new RoomResponse(save.getId(), save.getRoomType(), save.getRoomPrice()); // tao response tu room vua duoc them de tra ve cho client
        return ResponseEntity.ok(response);
    }
    @GetMapping("/room-types")
    public List<String> getAllRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    public ResponseEntity<List<RoomResponse>> getAllRooms(){
        return null;
    }
}
