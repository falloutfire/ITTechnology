package sample.Objects;

import javafx.beans.property.*;

public class MaterialBase {

    private IntegerProperty material_id;
    private StringProperty materialName;
    private DoubleProperty density;
    private DoubleProperty capacity;
    private DoubleProperty melting;
    private DoubleProperty coverSpeed;
    private DoubleProperty coverTemp;
    private DoubleProperty consist;
    private DoubleProperty viscosity;
    private DoubleProperty temp;
    private DoubleProperty flow;
    private DoubleProperty heatTrans;

    public MaterialBase() {
        this.material_id = new SimpleIntegerProperty();
        this.materialName = new SimpleStringProperty();
        this.density = new SimpleDoubleProperty();
        this.capacity = new SimpleDoubleProperty();
        this.melting = new SimpleDoubleProperty();
        this.coverSpeed = new SimpleDoubleProperty();
        this.coverTemp = new SimpleDoubleProperty();
        this.consist = new SimpleDoubleProperty();
        this.viscosity = new SimpleDoubleProperty();
        this.temp = new SimpleDoubleProperty();
        this.flow = new SimpleDoubleProperty();
        this.heatTrans = new SimpleDoubleProperty();
    }

    public String getMaterialName() {
        return materialName.get();
    }

    public StringProperty materialNameProperty() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName.set(materialName);
    }

    public int getMaterial_id() {
        return material_id.get();
    }

    public IntegerProperty material_idProperty() {
        return material_id;
    }

    public void setMaterial_id(int material_id) {
        this.material_id.set(material_id);
    }

    public double getDensity() {
        return density.get();
    }

    public DoubleProperty densityProperty() {
        return density;
    }

    public void setDensity(double density) {
        this.density.set(density);
    }

    public double getCapacity() {
        return capacity.get();
    }

    public DoubleProperty capacityProperty() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity.set(capacity);
    }

    public double getMelting() {
        return melting.get();
    }

    public DoubleProperty meltingProperty() {
        return melting;
    }

    public void setMelting(double melting) {
        this.melting.set(melting);
    }

    public double getCoverSpeed() {
        return coverSpeed.get();
    }

    public DoubleProperty coverSpeedProperty() {
        return coverSpeed;
    }

    public void setCoverSpeed(double coverSpeed) {
        this.coverSpeed.set(coverSpeed);
    }

    public double getCoverTemp() {
        return coverTemp.get();
    }

    public DoubleProperty coverTempProperty() {
        return coverTemp;
    }

    public void setCoverTemp(double coverTemp) {
        this.coverTemp.set(coverTemp);
    }

    public double getConsist() {
        return consist.get();
    }

    public DoubleProperty consistProperty() {
        return consist;
    }

    public void setConsist(double consist) {
        this.consist.set(consist);
    }

    public double getViscosity() {
        return viscosity.get();
    }

    public DoubleProperty viscosityProperty() {
        return viscosity;
    }

    public void setViscosity(double viscosity) {
        this.viscosity.set(viscosity);
    }

    public double getTemp() {
        return temp.get();
    }

    public DoubleProperty tempProperty() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp.set(temp);
    }

    public double getFlow() {
        return flow.get();
    }

    public DoubleProperty flowProperty() {
        return flow;
    }

    public void setFlow(double flow) {
        this.flow.set(flow);
    }

    public double getHeatTrans() {
        return heatTrans.get();
    }

    public DoubleProperty heatTransProperty() {
        return heatTrans;
    }

    public void setHeatTrans(double heatTrans) {
        this.heatTrans.set(heatTrans);
    }
}
