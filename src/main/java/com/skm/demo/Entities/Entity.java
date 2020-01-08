package com.skm.demo.Entities;

import java.lang.reflect.Field;

public class Entity {

    public String [] podajPola()
    {
            Class klasa = this.getClass();
            Field [] fields = klasa.getDeclaredFields();
            String [] pola = new String[fields.length];
            int i = 0;

            for(Field f: fields){
                pola[i] = f.getName();
                i++;
            }
            return pola;
    }
}
