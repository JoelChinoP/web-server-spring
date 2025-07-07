package com.comprasweb.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.*;

@Controller
public class AuthController {
    // Credenciales válidas
    private static final String USUARIO = "admin";
    private static final String PASS = "1234";

    // Redirige raíz a login
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    // Muestra formulario login
    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("usuario") != null) return "redirect:/compras";
        return "login";
    }

    // Procesa login
    @PostMapping("/login")
    public String procesar(@RequestParam String usuario, @RequestParam String contrasenia, HttpSession sesion, Model model) {
        if (USUARIO.equals(usuario) && PASS.equals(contrasenia)) {
            sesion.setAttribute("usuario", usuario);
            return "redirect:/compras";
        }
        model.addAttribute("mensajes", Collections.singletonList("Usuario o contraseña incorrectos"));
        return "error";
    }

    // Cierra sesión y vuelve al login
    @GetMapping("/logout")
    public String logout(HttpSession sesion) {
        sesion.invalidate();
        return "redirect:/login";
    }
}