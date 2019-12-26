package com.skm.demo.Entities;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromocjaProduktu extends Entity{
    private int nr_serii;
    private int id_promocji;
    private Date od_kiedy;
    private Date do_kiedy;

    public PromocjaProduktu()
    {

    }

    public PromocjaProduktu(int nr_serii, int id_promocji, Date od_kiedy, Date do_kiedy)
    {
        this.nr_serii = nr_serii;
        this.id_promocji = id_promocji;
        this.od_kiedy = od_kiedy;
        this.do_kiedy = do_kiedy;
    }
}
