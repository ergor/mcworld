package st.netb.mc.mcworld.datastructs.raw;

public class UTM {
    private double northing;
    private double easting;

    public UTM(double northing, double easting) {
        this.northing = northing;
        this.easting = easting;
    }

    public double getN() {
        return northing;
    }

    public double getE() {
        return easting;
    }
}
