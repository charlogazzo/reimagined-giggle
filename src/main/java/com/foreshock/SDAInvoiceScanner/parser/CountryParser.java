package com.foreshock.SDAInvoiceScanner.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foreshock.SDAInvoiceScanner.model.CountryIBAN;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Service
public class CountryParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Set<CountryIBAN> readIBANData() throws IOException {

        ClassPathResource resource = new ClassPathResource("iban-country-length.json");

        try(InputStream stream = resource.getInputStream()) {
            return mapper.readValue(stream, new TypeReference<Set<CountryIBAN>>() { });
        }
    }
}
