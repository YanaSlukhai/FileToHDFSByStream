package model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Booking {
    private String date_time;
    private Integer site_name;
    private Integer posa_continent;
    private Integer user_location_country;
    private Integer user_location_region;
    private Integer user_location_city;
    private Double orig_destination_distance;
    private Integer user_id;
    private Integer is_mobile;
    private Integer is_package;
    private Integer channel;
    private String srch_ci;
    private String srch_co;
    private Integer srch_adults_cnt;
    private Integer srch_children_cnt;
    private Integer srch_rm_cnt;
    private Integer srch_destination_id;
    private Integer srch_destination_type_id;
    private Integer is_booking;
    private Long cnt;
    private Integer hotel_continent;
    private Integer hotel_country;
    private Integer hotel_market;
    private Integer hotel_cluster;

    public Booking(String date_time, Integer site_name, Integer posa_continent, Integer user_location_country, Integer user_location_region, Integer user_location_city, Double orig_destination_distance, Integer user_id, Integer is_mobile, Integer is_package, Integer channel, String srch_ci, String srch_co, Integer srch_adults_cnt, Integer srch_children_cnt, Integer srch_rm_cnt, Integer srch_destination_id, Integer srch_destination_type_id, Integer is_booking, Long cnt, Integer hotel_continent, Integer hotel_country, Integer hotel_market, Integer hotel_cluster) {
        this.date_time = date_time;
        this.site_name = site_name;
        this.posa_continent = posa_continent;
        this.user_location_country = user_location_country;
        this.user_location_region = user_location_region;
        this.user_location_city = user_location_city;
        this.orig_destination_distance = orig_destination_distance;
        this.user_id = user_id;
        this.is_mobile = is_mobile;
        this.is_package = is_package;
        this.channel = channel;
        this.srch_ci = srch_ci;
        this.srch_co = srch_co;
        this.srch_adults_cnt = srch_adults_cnt;
        this.srch_children_cnt = srch_children_cnt;
        this.srch_rm_cnt = srch_rm_cnt;
        this.srch_destination_id = srch_destination_id;
        this.srch_destination_type_id = srch_destination_type_id;
        this.is_booking = is_booking;
        this.cnt = cnt;
        this.hotel_continent = hotel_continent;
        this.hotel_country = hotel_country;
        this.hotel_market = hotel_market;
        this.hotel_cluster = hotel_cluster;
    }
}
