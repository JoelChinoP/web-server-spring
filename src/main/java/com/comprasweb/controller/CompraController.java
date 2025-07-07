package com.comprasweb.controller;

import com.comprasweb.service.CompraService;
import com.comprasweb.model.Producto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class CompraController {
    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    // Muestra tienda y último historial de compra
    @GetMapping("/compras")
    public String verCompras(Model model, HttpSession sesion) {
        if (sesion.getAttribute("usuario") == null) return "redirect:/login";
        model.addAttribute("productos", compraService.getCatalogo());
        // Obtiene última compra de la sesión
        List<Map<String, Integer>> historial = (List<Map<String, Integer>>) sesion.getAttribute("historial");
        Map<String, Integer> ultimasCompras = (historial != null && !historial.isEmpty()) ? historial.get(historial.size() - 1) : null;
        model.addAttribute("ultimasCompras", ultimasCompras);
        return "index";
    }

    // Procesa compra y guarda en historial
    @PostMapping("/comprar")
    public String comprar(@RequestParam Map<String, String> params, Model model, HttpSession sesion) {
        if (sesion.getAttribute("usuario") == null) return "redirect:/login";
        Map<String, Producto> catalogo = compraService.getCatalogo();
        Map<String, Integer> cantidades = new LinkedHashMap<>();
        List<String> errores = new ArrayList<>();
        for (String key : catalogo.keySet()) {
            try {
                int q = Integer.parseInt(params.getOrDefault(key, "0"));
                if (q < 0) errores.add("Cantidad inválida para " + catalogo.get(key).getNombre());
                cantidades.put(key, Math.max(0, q));
            } catch (Exception e) {
                errores.add("Cantidad inválida para " + catalogo.get(key).getNombre());
                cantidades.put(key, 0);
            }
        }
        if (!errores.isEmpty()) {
            model.addAttribute("errores", errores);
            model.addAttribute("productos", catalogo);
            return "index";
        }
        // Guarda compra en historial de sesión
        List<Map<String, Integer>> historial = (List<Map<String, Integer>>) sesion.getAttribute("historial");
        if (historial == null) historial = new ArrayList<>();
        historial.add(new LinkedHashMap<>(cantidades));
        sesion.setAttribute("historial", historial);

        model.addAttribute("productos", catalogo);
        model.addAttribute("cantidades", cantidades);
        model.addAttribute("total", compraService.calcularTotal(cantidades));
        return "resumen";
    }
}