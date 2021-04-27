package com.umar.apps.employee;

public abstract class Employee {

    private final double salary;
    private final String name;

    public abstract double computeCommission();

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "salary=" + salary +
                ", name='" + name + '\'' +
                '}';
    }
}

class SalesEmployee extends Employee {

    private final double sales;

    public SalesEmployee(String name, double salary, double sales) {
        super(name, salary);
        this.sales = sales;
    }

    @Override
    public double computeCommission() {
        return sales * 0.3;
    }

    public double getSales() {
        return sales;
    }

    @Override
    public String toString() {
        return "SalesEmployee{" +
                "commission=" + computeCommission() +
                '}';
    }
}

class StoreEmployee extends Employee {

    private final double thingsSorted;

    public StoreEmployee(String name, double salary, double thingsSorted) {
        super(name, salary);
        this.thingsSorted = thingsSorted;
    }

    public double getThingsSorted() {
        return thingsSorted;
    }

    @Override
    public double computeCommission() {
        return thingsSorted * 0.15;
    }

    @Override
    public String toString() {
        return "StoreEmployee{" +
                "commission=" + computeCommission() +
                '}';
    }
}
