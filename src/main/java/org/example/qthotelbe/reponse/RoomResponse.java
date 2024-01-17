package org.example.qthotelbe.reponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class RoomResponse {

    private Long id;
    private String roomType;

    private BigDecimal roomPrice;

    private String photo;

    private List<BookingRomResponse> bookings;


    public RoomResponse(Long id, String roomType, BigDecimal roomPrice) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
    }

    public RoomResponse(Long id, String roomType, BigDecimal roomPrice, byte[] photoBytes, List<BookingRomResponse> bookings) {
        this.id = id;
        this.roomType = roomType;
        this.roomPrice = roomPrice;
        this.photo =  photoBytes != null ? Base64.encodeBase64String(photoBytes) : null; // kiem tra neu khong trong thi se  nhan 1 chuoi tu db va ma hoa thanh buc anh
        this.bookings = bookings;
    }
}
