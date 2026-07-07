package com.cinema.cineapp.controller;

import com.cinema.cineapp.model.CineModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CineController {

    private boolean logueado = false;
    private String nombreUsuario = "";
    private CineModel fachada = new CineModel();

    // BASE DE DATOS SIMULADA
    private static final Map<String, String> baseDeDatosUsuarios = new HashMap<>();
    static {
        baseDeDatosUsuarios.put("12345678", "Juan Pérez");
        baseDeDatosUsuarios.put("87654321", "María Rodríguez");
        baseDeDatosUsuarios.put("77777777", "Carlos Mendoza");
    }

    @GetMapping("/")
    public String verCartelera(Model m) {
        m.addAttribute("logueado", logueado);
        m.addAttribute("usuario", nombreUsuario);
        return "cartelera";
    }

    @GetMapping("/comprar")
    public String intentarComprar(@RequestParam String pelicula, @RequestParam String cine, Model m) {
        m.addAttribute("peli", pelicula);
        m.addAttribute("cine", cine);
        if (!logueado) {
            return "registro";
        }
        m.addAttribute("usuario", nombreUsuario);
        return "pasarela";
    }

    @PostMapping("/procesar-login")
    public String procesarLogin(@RequestParam String nombre, @RequestParam String dni, @RequestParam String peli, @RequestParam String cine) {
        if (baseDeDatosUsuarios.containsKey(dni)) {
            this.nombreUsuario = baseDeDatosUsuarios.get(dni);
        } else {
            this.nombreUsuario = nombre;
        }
        this.logueado = true;
        return "redirect:/comprar?pelicula=" + peli + "&cine=" + cine;
    }

    @GetMapping("/unete")
    public String mostrarPantallaUnete() {
        return "unete";
    }

    @PostMapping("/procesar-registro")
    public String procesarRegistroCompleto(@RequestParam String nombre, @RequestParam String correo, @RequestParam String dni) {
        this.logueado = true;
        this.nombreUsuario = nombre;
        baseDeDatosUsuarios.put(dni, nombre);
        return "redirect:/";
    }

    @GetMapping("/dulceria")
    public String irADulceria(Model m) {
        m.addAttribute("logueado", logueado);
        m.addAttribute("usuario", nombreUsuario);
        return "dulceria";
    }

    @GetMapping("/logout")
    public String cerrarSesion() {
        this.logueado = false;
        this.nombreUsuario = "";
        return "redirect:/";
    }

    // SEGURIDAD: Evita la pantalla blanca White Label si el usuario refresca /finalizar
    @GetMapping("/finalizar")
    public String redirigirErrorFinalizar() {
        return "redirect:/";
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(@RequestParam String peli, @RequestParam String cine, @RequestParam String tipo, Model m) {
        String resultado = fachada.procesarCompra(nombreUsuario, peli, cine, tipo);
        m.addAttribute("resultado", resultado);
        return "exito";
    }
}