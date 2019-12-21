package com.skm.demo.RowMappers;

import com.skm.demo.Entities.PromocjaProduktu;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PromocjaProduktuRowMapper implements RowMapper<PromocjaProduktu> {

    @Override
    public PromocjaProduktu mapRow(ResultSet rs, int rowNum) throws SQLException {

        PromocjaProduktu promocja = new PromocjaProduktu(rs.getInt("produkty_numer_serii"),
                rs.getInt("id_promocji"),
                rs.getDate("od"),
                rs.getDate("do")
        );
        return promocja;

    }
}

