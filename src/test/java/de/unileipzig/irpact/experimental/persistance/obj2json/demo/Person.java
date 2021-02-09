package de.unileipzig.irpact.experimental.persistance.obj2json.demo;

import de.unileipzig.irpact.commons.persistence.PersistableBase;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class Person extends PersistableBase {

    protected double value;
    protected Car car;

    public Person() {
    }

    public Person(long uid, double value, Car car) {
        setUID(uid);
        setValue(value);
        setCar(car);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Car getCar() {
        return car;
    }

    @Override
    public String toString() {
        return "Person{" +
                "value=" + value +
                ", car=" + car +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Double.compare(person.value, value) == 0 && Objects.equals(car, person.car);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, car);
    }
}
