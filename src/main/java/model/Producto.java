package com.comprasweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Producto {
    private String nombre;
    private double precio;
}