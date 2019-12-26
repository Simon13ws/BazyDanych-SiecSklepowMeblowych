package com.skm.demo.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Promocja extends Entity{
    private String nazwa;
    private int id_promocji;
    private float wysokosc_promocji;

    public Promocja()
    {

    }

    public Promocja(String nazwa, int id_promocji, float wysokosc_promocji)
    {
        this.nazwa = nazwa;
        this.id_promocji = id_promocji;
        this.wysokosc_promocji = wysokosc_promocji;
    }
}
