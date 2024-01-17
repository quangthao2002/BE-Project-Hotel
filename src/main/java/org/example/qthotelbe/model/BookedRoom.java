package org.example.qthotelbe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    @Column(name = "check_in")
    private LocalDate checkInDate;
    @Column(name = "check_out")
    private LocalDate checkOutDate;
    @Column(name = "guest_FullName")
    private String guestFullName;
    @Column(name = "guest_Email")
    private String guestEmail;
    @Column(name="number_children")
    private int numberOfChildren;
    @Column(name="number_adults")
    private int numberOfAdults;
    @Column(name = "total_guest")
    private int totalNumberOfGuest;
    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode;

    // bookedroom : moi bookedroom chi thuoc ve 1 room , nhung 1 room co the co nhieu bookedroom vi moi lan booking khac nhau
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalNumberOfGuest() {

        this.totalNumberOfGuest = this.numberOfAdults + this.numberOfChildren;

    }


    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
        calculateTotalNumberOfGuest(); // goi lai ham tinh khi set lai so tre em de cap nhat lai tong so khach
    }

    public void setNumberOfAdults(int numberOfAdults) {
        this.numberOfAdults = numberOfAdults;
        calculateTotalNumberOfGuest(); //  goi lai ham tinh khi set lai so nguoi lon de cap nhat lai tong so khach
    }





}
