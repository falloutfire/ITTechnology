package sample.Objects;

public class ValueAdapter {

    //разарботка прогарммных комплексов для исследований в химии и хим технологии
    private long number;
    private String lenght;
    private String consist;
    private String temperature;

    public ValueAdapter(long number, String lenght, String consist, String temperature) {

        this.number = number;
        this.lenght = lenght;
        this.consist = consist;
        this.temperature = temperature;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getLenght() {
        return lenght;
    }

    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    public String getConsist() {
        return consist;
    }

    public void setConsist(String consist) {
        this.consist = consist;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
