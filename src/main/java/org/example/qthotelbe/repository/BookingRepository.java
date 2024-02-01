package org.example.qthotelbe.repository;

import org.example.qthotelbe.model.BookedRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookedRoom,Long> {
}
