package org.example.qthotelbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomType;

    private BigDecimal roomPrice;

    private  boolean isBooked;
    @Lob
    private Blob photo;


    @OneToMany( mappedBy =  "room",fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 1 room co nhieu booking cho moi lan booking khac nhau ,
    // cascade = CascadeType.ALL : khi xoa 1 room thi tất ca booking cua room do cung bi xoa theo , khi update 1 room th tất ca booking cua room do cung bi update theo
    // fetch = FetchType.LAZY : Dữ liệu booking sẽ được load khi cần thiết, khi gọi đến phương thức getBookings() thì dữ liệu mới được load
    private List<BookedRoom> bookings;

    public Room() {
        this.bookings = new ArrayList<>(); // khoi tao 1 list rong
    }

    public void addBooking(BookedRoom booking) {

        if(bookings == null){
            bookings = new ArrayList<>(); //
        }
        bookings.add(booking); // them booking vao list booking cua room
        booking.setRoom(this); // set room cho booking , this la room hien tai, cho biet booking nay thuoc ve room nao trong list room
        isBooked = true;
        String codeBooking = RandomStringUtils.random(10);
        booking.setBookingConfirmationCode(codeBooking); // set code booking cho booking
    }







}
