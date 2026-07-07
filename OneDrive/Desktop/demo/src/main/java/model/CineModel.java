package com.cinema.cineapp.model;

// --- PATRÓN FACADE (FACHADA) ---
public class CineModel {
    public String procesarCompra(String usuario, String pelicula, String cine, String tipoEntrada) {
        // La fábrica resuelve dinámicamente qué entrada construir
        Entrada entrada = EntradaFactory.crear(tipoEntrada);

        // Retorna la confirmación simulando el aviso del sistema (Observer)
        return "✨ [Notificación del Sistema] ¡Compra Exitosa! El Socio " + usuario +
                " adquirió entrada de tipo '" + entrada.getInfo() +
                "' para ver '" + pelicula + "' en la sede " + cine + ".";
    }
}