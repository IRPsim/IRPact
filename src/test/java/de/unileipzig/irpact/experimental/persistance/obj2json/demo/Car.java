package de.unileipzig.irpact.experimental.persistance.obj2json.demo;

import de.unileipzig.irpact.commons.persistence.PersistableBase;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class Car extends PersistableBase {

    protected double value;

    public Car() {
    }

    public Car(long uid, double value) {
        setUID(uid);
        setValue(value);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Car{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return Double.compare(car.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
