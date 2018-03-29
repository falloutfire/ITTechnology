package sample.Objects;

public class Material {

    private double density;
    private double heat;
    private double meltingTemperature;

    public Material(double density, double heat, double meltingTemperature) {
        this.density = density;
        this.heat = heat;
        this.meltingTemperature = meltingTemperature;
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public double getHeat() {
        return heat;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public double getMeltingTemperature() {
        return meltingTemperature;
    }

    public void setMeltingTemperature(double meltingTemperature) {
        this.meltingTemperature = meltingTemperature;
    }
}
