package com.foreshock.SDAInvoiceScanner.model;

import lombok.Data;
import lombok.Getter;

/**
 * Model class used to represent country objects with IBAN data
 */
@Data
public class CountryIBAN {
    private String code;
    private String name;
    private Integer length;

    public CountryIBAN() {

    }

    public CountryIBAN(String code, String name, Integer length) {
        this.code = code;
        this.name = name;
        this.length = length;
    }
}
