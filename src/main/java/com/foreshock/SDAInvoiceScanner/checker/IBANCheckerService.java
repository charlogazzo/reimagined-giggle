package com.foreshock.SDAInvoiceScanner.checker;

import com.foreshock.SDAInvoiceScanner.parser.CountryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class provides methods to check IBANs against a list of known banned IBANS
 */
@Service
public class IBANCheckerService implements TextChecker {

    // The country parser may be used in the future where IBANs from other countries are to be verified
    // This allows the countries to be parsed from a JSON file using the Jackson ObjectMapper
    private CountryParser countryParser;
    private final List<String> bannedIbans = List.of("DE44500105155407324931", "DE55123456589123456700");

    @Autowired
    public IBANCheckerService(CountryParser countryParser) {
        this.countryParser = countryParser;
    }

    /**
     * The banned IBANs are all without spaces between them
     * @param ibans containing Strings
     * @return all banned IBANs contained within the passed collection of IBANs
     */
    @Override
    public Set<String> check(Collection<String> ibans) {
        return ibans.stream()
                .filter(bannedIbans::contains)
                .collect(Collectors.toSet());
    }

    /**
     * The pattern used within this method matches IBANs with or without spaces every four characters
     * @param text text to be scanned through for matching text
     * @return
     */
    @Override
    public Set<String> extract(String text) {
        // Set is used because duplicate representation of IBANs within a collection is not required
        Set<String> allIbans = new HashSet<>();

        Pattern ibanPattern = Pattern.compile("([A-Z]{2}(?:\\s?[A-Z0-9]){12,34})");
        Matcher matcher = ibanPattern.matcher(text);

        while (matcher.find()) {
            String rawIban = matcher.group(1);
            String formattedIban = rawIban.replace(" ", "");

            allIbans.add(formattedIban);
        }

        return allIbans;
    }
}
