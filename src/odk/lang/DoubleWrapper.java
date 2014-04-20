package odk.lang;

public class DoubleWrapper {
    public double value;
    @Override
    public String toString() {
        return Double.toString(this.value);
    }
}
