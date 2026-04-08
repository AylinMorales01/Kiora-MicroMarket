package com.kiora.micromarket.service;

import com.kiora.micromarket.dto.request.SaleDetailRequestDTO;
import com.kiora.micromarket.dto.request.SaleRequestDTO;
import com.kiora.micromarket.dto.response.SaleDetailResponseDTO;
import com.kiora.micromarket.dto.response.SaleResponseDTO;
import com.kiora.micromarket.entity.Employee;
import com.kiora.micromarket.entity.Product;
import com.kiora.micromarket.entity.Sale;
import com.kiora.micromarket.entity.SaleDetail;
import com.kiora.micromarket.excepcion.InsufficientStockException;
import com.kiora.micromarket.excepcion.ResourceNotFoundException;
import com.kiora.micromarket.repository.SaleRepository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final EntityManager entityManager;

    @Transactional
    public SaleResponseDTO createSale(SaleRequestDTO requestDTO) {
        Employee employee = entityManager.find(Employee.class, requestDTO.getEmployeeId());
        if (employee == null) {
            throw new ResourceNotFoundException("Empleado no encontrado con ID: " + requestDTO.getEmployeeId());
        }

        Sale sale = new Sale();
        sale.setDate(LocalDateTime.now());
        sale.setEmployee(employee);

        double totalSubtotal = 0.0;

        for (SaleDetailRequestDTO detailDTO : requestDTO.getDetails()) {
            Product product = entityManager.find(Product.class, detailDTO.getProductId());
            if (product == null || !product.isActive()) {
                throw new ResourceNotFoundException(
                        "Producto no encontrado o inactivo con ID: " + detailDTO.getProductId());
            }

            if (product.getStock() < detailDTO.getQuantity()) {
                throw new InsufficientStockException("Stock insuficiente para el producto: " + product.getName()
                        + ". Stock actual: " + product.getStock() + ", requerido: " + detailDTO.getQuantity());
            }

            product.setStock(product.getStock() - detailDTO.getQuantity());
            entityManager.merge(product);

            SaleDetail saleDetail = new SaleDetail();
            saleDetail.setProduct(product);
            saleDetail.setQuantity(detailDTO.getQuantity());
            saleDetail.setUnitPrice(product.getPrice());
            double subtotal = detailDTO.getQuantity() * product.getPrice();
            saleDetail.setSubtotal(subtotal);

            totalSubtotal += subtotal;
            sale.addDetail(saleDetail);
        }

        sale.setSubtotal(totalSubtotal);
        double iva = totalSubtotal * 0.19;
        sale.setIva(iva);
        sale.setTotal(totalSubtotal + iva);

        Sale savedSale = saleRepository.save(sale);
        return mapToResponseDTO(savedSale);
    }

    public List<SaleResponseDTO> getAllSales() {
        return saleRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public SaleResponseDTO getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));
        return mapToResponseDTO(sale);
    }

    @Transactional
    public void cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + id));

        if (!sale.isActive()) {
            throw new IllegalArgumentException("La venta ya se encuentra anulada.");
        }

        sale.setActive(false);

        for (SaleDetail detail : sale.getDetails()) {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            entityManager.merge(product);
        }

        saleRepository.save(sale);
    }

    private SaleResponseDTO mapToResponseDTO(Sale sale) {
        SaleResponseDTO response = new SaleResponseDTO();
        response.setId(sale.getId());
        response.setDate(sale.getDate());
        response.setSubtotal(sale.getSubtotal());
        response.setIva(sale.getIva());
        response.setTotal(sale.getTotal());
        response.setEmployeeName(sale.getEmployee().getName());
        response.setActive(sale.isActive());

        List<SaleDetailResponseDTO> detailDTOs = sale.getDetails().stream().map(detail -> {
            SaleDetailResponseDTO dto = new SaleDetailResponseDTO();
            dto.setProductId(detail.getProduct().getId());
            dto.setProductName(detail.getProduct().getName());
            dto.setQuantity(detail.getQuantity());
            dto.setUnitPrice(detail.getUnitPrice());
            dto.setSubtotal(detail.getSubtotal());
            return dto;
        }).collect(Collectors.toList());

        response.setDetails(detailDTOs);
        return response;
    }
}
