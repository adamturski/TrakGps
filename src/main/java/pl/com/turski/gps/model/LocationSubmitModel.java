package pl.com.turski.gps.model;

/**
 * User: Adam
 */
public class LocationSubmitModel {

    private String locatorId;
    private Double latitude;
    private Double longitude;

    public LocationSubmitModel(String locatorId, Double latitude, Double longitude) {
        this.locatorId = locatorId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
