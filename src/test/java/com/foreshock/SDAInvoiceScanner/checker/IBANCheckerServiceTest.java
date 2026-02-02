package com.foreshock.SDAInvoiceScanner.checker;

import com.foreshock.SDAInvoiceScanner.parser.CountryParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class IBANCheckerServiceTest {

    private IBANCheckerService ibanCheckerService;

    @BeforeEach
    void setUp() {
        // CountryParser is not used by the current implementation, mock it to satisfy constructor
        CountryParser parser = mock(CountryParser.class);
        ibanCheckerService = new IBANCheckerService(parser);
    }

    @Test
    void extract_shouldFindIbansWithAndWithoutSpaces_andNormalize() {
        String text = "Payment to DE44 5001 0515 5407 3249 31 and backup DE55123456589123456700.";

        Set<String> extracted = ibanCheckerService.extract(text);

        assertThat(extracted).containsExactlyInAnyOrder(
                "DE44500105155407324931",
                "DE55123456589123456700"
        );
    }

    @Test
    void extract_shouldReturnEmptySet_whenNoIbansPresent() {
        String text = "There are no bank account identifiers here.";

        Set<String> extracted = ibanCheckerService.extract(text);

        assertThat(extracted).isEmpty();
    }

    @Test
    void check_shouldReturnOnlyBannedIbans_fromCollection() {
        Collection<String> input = List.of(
                "DE44500105155407324931", // banned
                "DE55123456589123456700", // banned
                "DE00123456789012345678"  // not banned
        );

        Set<String> banned = ibanCheckerService.check(input);

        assertThat(banned).containsExactlyInAnyOrder(
                "DE44500105155407324931",
                "DE55123456589123456700"
        );
    }

    @Test
    void check_shouldHandleDuplicates_andReturnUniqueSet() {
        Collection<String> input = List.of(
                "DE44500105155407324931",
                "DE44500105155407324931",
                "DE55123456589123456700"
        );

        Set<String> banned = ibanCheckerService.check(input);

        // duplicates in input should not produce duplicates in the output
        assertThat(banned).hasSize(2)
                .contains("DE44500105155407324931", "DE55123456589123456700");
    }

    @Test
    void check_shouldReturnEmptySet_forEmptyInput() {
        Collection<String> input = List.of();

        Set<String> banned = ibanCheckerService.check(input);

        assertThat(banned).isEmpty();
    }
}
