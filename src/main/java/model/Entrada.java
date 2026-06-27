package com.cinema.cineapp.model;

// --- PATRÓN FACTORY METHOD ---
public abstract class Entrada {
    protected String tipo;
    protected double precio;
    public String getInfo() { return tipo + " - S/ " + precio; }
}

class EntradaAdulto extends Entrada { public EntradaAdulto() { tipo = "Adulto Regular"; precio = 15.0; } }
class EntradaNino extends Entrada { public EntradaNino() { tipo = "Niño (Menor de 12)"; precio = 11.0; } }
class EntradaVip extends Entrada { public EntradaVip() { tipo = "VIP Preferencial"; precio = 25.0; } }

class EntradaFactory {
    public static Entrada crear(String tipo) {
        if (tipo.equalsIgnoreCase("NIÑO")) return new EntradaNino();
        if (tipo.equalsIgnoreCase("VIP")) return new EntradaVip();
        return new EntradaAdulto();
    }
}