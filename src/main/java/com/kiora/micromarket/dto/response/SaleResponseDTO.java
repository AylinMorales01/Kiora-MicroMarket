package com.kiora.micromarket.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class SaleResponseDTO {

    private Long id;
    private LocalDateTime date;
    private double subtotal;
    private double iva;
    private double total;
    private String employeeName;
    private List<SaleDetailResponseDTO> details;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public List<SaleDetailResponseDTO> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetailResponseDTO> details) {
        this.details = details;
    }
}
