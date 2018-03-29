package sample.Objects;

public class EmpiricalCoefficients {

    private double consistention;
    private double viscosity;
    private double alignmentTemperature;
    private double indexMaterial;
    private double heatTransfer;

    public EmpiricalCoefficients(double consistention, double viscosity, double alignmentTemperature, double indexMaterial, double heatTransfer) {
        this.consistention = consistention;
        this.viscosity = viscosity;
        this.alignmentTemperature = alignmentTemperature;
        this.indexMaterial = indexMaterial;
        this.heatTransfer = heatTransfer;
    }

    public double getConsistention() {
        return consistention;
    }

    public void setConsistention(double consistention) {
        this.consistention = consistention;
    }

    public double getViscosity() {
        return viscosity;
    }

    public void setViscosity(double viscosity) {
        this.viscosity = viscosity;
    }

    public double getAlignmentTemperature() {
        return alignmentTemperature;
    }

    public void setAlignmentTemperature(double alignmentTemperature) {
        this.alignmentTemperature = alignmentTemperature;
    }

    public double getIndexMaterial() {
        return indexMaterial;
    }

    public void setIndexMaterial(double indexMaterial) {
        this.indexMaterial = indexMaterial;
    }

    public double getHeatTransfer() {
        return heatTransfer;
    }

    public void setHeatTransfer(double heatTransfer) {
        this.heatTransfer = heatTransfer;
    }
}
