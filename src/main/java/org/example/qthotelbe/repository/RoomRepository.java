package org.example.qthotelbe.repository;

import org.example.qthotelbe.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r") // distinct: loai bo cac gia tri trung lap
    List<String> findDistinctRoomType();


    // tim kiem cac phong con trong theo loai phong va ngay checkin, checkout
    @Query("select r from Room  r where r.roomType like  %:roomType%" +
            " and r.id not in (" +
            "select bk.room.id from BookedRoom  bk " +
            "where ((bk.checkInDate <= :checkOutDate) and (bk.checkOutDate >= :checkInDate))" +
            ")")
    List<Room> findAvailableRoomsByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType);
}
