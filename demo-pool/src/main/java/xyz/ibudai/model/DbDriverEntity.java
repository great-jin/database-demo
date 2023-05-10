package xyz.ibudai.model;

public class DbDriverEntity {

    private String driverName;

    private String driverLocation;

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(String driverLocation) {
        this.driverLocation = driverLocation;
    }

    @Override
    public String toString() {
        return "DbDriverEntity{" +
                "driverName='" + driverName + '\'' +
                ", driverLocation='" + driverLocation + '\'' +
                '}';
    }
}
