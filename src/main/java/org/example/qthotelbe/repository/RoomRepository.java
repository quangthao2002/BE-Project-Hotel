package org.example.qthotelbe.repository;

import org.example.qthotelbe.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r") // distinct: loai bo cac gia tri trung lap
    List<String> findDistinctRoomType();
}
