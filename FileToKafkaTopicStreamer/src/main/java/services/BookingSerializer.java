package services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Booking;

import java.io.IOException;
import java.util.regex.Pattern;

public class BookingSerializer {
    private Double parsingDoubleNulls(String nullableValue){
        if(nullableValue.equals(""))
            return null;
        else
            return Double.parseDouble(nullableValue);
    }
    
    private Integer parsingIntegerNulls(String nullableValue){
        if(nullableValue.equals(""))
            return null;
        else
            return Integer.parseInt(nullableValue);
    }
    private Long parsingLongNulls(String nullableValue){
        if(nullableValue.equals(""))
            return null;
        else
            return Long.parseLong(nullableValue);
    }
    
    public String serializeBookingCSV(String csvBooking){
        Pattern pattern = Pattern.compile(",");
        String[] booking_fields = pattern.split(csvBooking);
        Booking booking = new Booking(
                booking_fields[0],
                parsingIntegerNulls(booking_fields[1]),
                parsingIntegerNulls(booking_fields[2]),
                parsingIntegerNulls(booking_fields[3]),
                parsingIntegerNulls(booking_fields[4]),
                parsingIntegerNulls(booking_fields[5]),
                parsingDoubleNulls(booking_fields[6]),
                parsingIntegerNulls(booking_fields[7]),
                parsingIntegerNulls(booking_fields[8]),
                parsingIntegerNulls(booking_fields[9]),
                parsingIntegerNulls(booking_fields[10]),
                booking_fields[11],
                booking_fields[12],
                parsingIntegerNulls(booking_fields[13]),
                parsingIntegerNulls(booking_fields[14]),
                parsingIntegerNulls(booking_fields[15]),
                parsingIntegerNulls(booking_fields[16]),
                parsingIntegerNulls(booking_fields[17]),
                parsingIntegerNulls(booking_fields[18]),
                parsingLongNulls(booking_fields[19]),
                parsingIntegerNulls(booking_fields[20]),
                parsingIntegerNulls(booking_fields[21]),
                parsingIntegerNulls(booking_fields[22]),
                parsingIntegerNulls(booking_fields[23])
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        //mapper.enable(SerializationFeature.INDENT_OUTPUT);
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        String result = "";
        try {
            result = mapper.writeValueAsString( booking);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
