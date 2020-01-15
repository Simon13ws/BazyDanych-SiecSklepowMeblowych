package com.skm.demo.Entities;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Entity {


    public ArrayList<String> podajPola()
    {
            Class klasa = this.getClass();
            Field [] fields = klasa.getDeclaredFields();
            ArrayList<String> pola = new ArrayList<String>();
            int i = 0;

            for(Field f: fields){
                if(!java.lang.reflect.Modifier.isStatic(f.getModifiers()))
                    pola.add(f.getName());
            }
            return pola;
    }
}
