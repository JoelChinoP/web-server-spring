package com.comprasweb.service;

import com.comprasweb.model.Producto;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CompraService {
    private final Map<String, Producto> catalogo = new LinkedHashMap<>();

    public CompraService() {
        catalogo.put("pan", new Producto("Pan", 1.50));
        catalogo.put("queso", new Producto("Queso", 2.00));
        catalogo.put("platanos", new Producto("Plátano", 0.50));
        catalogo.put("naranjas", new Producto("Naranja", 0.75));
    }
        public Map<String, Producto> getCatalogo() {
        return catalogo;
    }

    public double calcularTotal(Map<String, Integer> cantidades) {
        return catalogo.entrySet().stream()
                .mapToDouble(e -> e.getValue().getPrecio() * cantidades.getOrDefault(e.getKey(), 0))
                .sum();
    }

}