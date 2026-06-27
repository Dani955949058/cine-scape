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

    // --- BASE DE DATOS SIMULADA ---
    // Guardaremos el DNI como clave y el Nombre Completo como valor
    private static final Map<String, String> baseDeDatosUsuarios = new HashMap<>();

    static {
        // Aquí registramos los DNI de prueba y sus nombres reales
        baseDeDatosUsuarios.put("12345678", "Juan Pérez");
        baseDeDatosUsuarios.put("87654321", "Maria Rodriguez");
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

    // Al iniciar sesión desde registro.html
    @PostMapping("/registrar")
    public String procesarRegistro(@RequestParam String nombre, @RequestParam String peli, @RequestParam String cine) {
        this.logueado = true;

        // Buscamos si lo que ingresó es un DNI que existe en nuestra base de datos
        if (baseDeDatosUsuarios.containsKey(nombre)) {
            // Si existe, extraemos su nombre real
            this.nombreUsuario = baseDeDatosUsuarios.get(nombre);
        } else {
            // Si no existe el DNI o ingresó un texto cualquiera, usamos el texto tal cual
            this.nombreUsuario = nombre;
        }

        return "redirect:/comprar?pelicula=" + peli + "&cine=" + cine;
    }

    @GetMapping("/unete")
    public String mostrarPantallaUnete() {
        return "unete";
    }

    // Al registrar un nuevo usuario desde unete.html
    @PostMapping("/procesar-registro")
    public String procesarRegistroCompleto(@RequestParam("nombre") String nombre,
                                           @RequestParam("correo") String correo,
                                           @RequestParam("dni") String dni) {
        this.logueado = true;
        this.nombreUsuario = nombre;

        // Guardamos dinámicamente este nuevo DNI en la base de datos para que funcione en el futuro
        baseDeDatosUsuarios.put(dni, nombre);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String cerrarSesion() {
        this.logueado = false;
        this.nombreUsuario = "";
        return "redirect:/";
    }

    // Agrega este método dentro de tu CineController, justo arriba del método "/finalizar"
    @GetMapping("/dulceria")
    public String irADulceria(Model m) {
        // Compartimos las variables logueado y usuario para que controle los bloqueos en el navegador
        m.addAttribute("logueado", logueado);
        m.addAttribute("usuario", nombreUsuario);
        return "dulceria";
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(@RequestParam String peli, @RequestParam String cine, @RequestParam String tipo, Model m) {
        String resultado = fachada.procesarCompra(nombreUsuario, peli, cine, tipo);
        m.addAttribute("ticket", resultado);
        return "exito";
    }
}