package com.skm.demo.Entities;

import java.lang.reflect.Field;

public class Entity {

    public Field [] podajPola()
    {
            Class klasa = this.getClass();
            Field [] pola = klasa.getDeclaredFields();
            return pola;
    }
}
