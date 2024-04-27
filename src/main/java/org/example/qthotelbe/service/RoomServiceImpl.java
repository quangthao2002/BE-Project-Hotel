package org.example.qthotelbe.service;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.exception.InternalServerException;
import org.example.qthotelbe.exception.ResourceNotFoundException;
import org.example.qthotelbe.model.Room;
import org.example.qthotelbe.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements IRoomService {

    private final RoomRepository roomRepository;
    @Override
    public Room addRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws IOException, SQLException {

        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if (!file.isEmpty()) {
                byte[] bytes = file.getBytes(); // lay du lieu tu file
                Blob photoBlob =  new SerialBlob(bytes); // chuyen du lieu tu file sang Blob
                room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomType();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getPhotoByRoomId(Long roomId) throws SQLException {

        Optional<Room> theRoom = roomRepository.findById(roomId); // tim room theo id , neu co thi tra ve room , khong co thi tra ve null, Optional dung de tranh null pointer exception

        if (theRoom.isEmpty()   ) { // kiem tra  neu room trong  thi throw exception
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        Blob photo = theRoom.get().getPhoto(); // lay photo tu room
        if(photo != null){
                return photo.getBytes(1, (int) photo.length()); // chuyen photo tu Blob sang byte
        }
        return null;
    }

    @Override
    public void deleteRoomById(Long roomId) {
        Optional<Room> theRoom =  roomRepository.findById(roomId);
        System.out.println("RoomServiceImpl.deleteRoomById"+theRoom);
        if(theRoom.isPresent()){ // co room se xoa room
            roomRepository.deleteById(roomId);
        }
    }
    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Optional<Room> theRoom =  roomRepository.findById(roomId);
        if(theRoom.isPresent()){ // co room se update room
            Room room = theRoom.get(); // lay room tu optional
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            if(photoBytes != null && photoBytes.length > 0){
                try {
                    Blob photoBlob =  new SerialBlob(photoBytes); // chuyen du lieu tu file sang Blob
                    room.setPhoto(photoBlob);
                } catch (SQLException ex) {
                    throw new InternalServerException("Error saving photo for room with id: " + roomId);
                }
            }
            return roomRepository.save(room);
        }
        return null;
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDateAndType(checkInDate, checkOutDate, roomType);
    }


}
