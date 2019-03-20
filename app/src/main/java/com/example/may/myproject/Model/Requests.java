package com.example.may.myproject.Model;

import java.util.List;

public class Requests {
    private String phone;
    private String tableNo;
    private String name;
    private String total;
    private List<Order> foods;
    private String status;

    public Requests() {
    }

    public Requests(String phone, String name, String tableNo, String total, List<Order> foods)
    {
        this.phone = phone;
        this.tableNo = tableNo;
        this.name = name;
        this.total = total;
        this.foods = foods;
        this.status = "0";

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;

    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}
