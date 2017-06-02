package com.svenwesterlaken.gemeentebreda.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lukab on 2-6-2017.
 */

public class ServiceReport {
    @SerializedName("address")
   public String address;
    @SerializedName("status")
   public String statusGemeente;
    @SerializedName("address_id")
    public String addressnr;
    @SerializedName("service_name")
    public String serviceName;
    @SerializedName("lat")
    public double latitude;
    @SerializedName("long")
   public double  longitude;
    @SerializedName("media_url")
    public String mediaUrl;
    @SerializedName("service_request_id")
    public String id;

    public ServiceReport(String address, String statusGemeente, String addressnr, String serviceName, int latitude, int longitude, String mediaUrl, String id) {
        this.address = address;
        this.statusGemeente = statusGemeente;
        this.addressnr = addressnr;
        this.serviceName = serviceName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mediaUrl = mediaUrl;
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public String getStatusGemeente() {
        return statusGemeente;
    }

    public String getAddressnr() {
        return addressnr;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getId() {
        return id;
    }
}
//
// "service_request_id": "16",
//         "status": "open",
//         "service_name": "Openbare verlichting",
//         "service_code": "OV",
//         "agency_responsible": "Unkown",
//         "service_notice": "Public lighting data service",
//         "requested_datetime": "0001-01-01T00:00:00",
//         "address": "Lovensdijkstraat",
//         "address_id": "61",
//         "lat": 51.5851182,
//         "long": 4.7937142,
//         "media_url": "NO MEDIA URL",
//         "service_object_type": "NOT DEFINED",
//         "service_object_id": "NO ID"