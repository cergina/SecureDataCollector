package Model.Database.Support;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AssuranceTest {

    @Test
    void isDateOk() {
        Date wannabeDate = Date.valueOf(LocalDate.now());

        assertTrue(Assurance.isDateOk(wannabeDate));

        wannabeDate = Date.valueOf(LocalDate.now().minusDays(30));

        assertTrue(Assurance.isDateOk(wannabeDate));

        wannabeDate = Date.valueOf(LocalDate.now().minusYears(17).minusMonths(5));

        assertTrue(Assurance.isDateOk(wannabeDate));
    }

    @ParameterizedTest
    @NullSource
    void isDateOk_ShouldReturnFalse(Date wannabeDate) {
        assertFalse(Assurance.isDateOk(wannabeDate));
    }

    @ParameterizedTest
    @ValueSource(ints = {-45651, -41, -1, -0, 0})
    void isFkOk_ShouldReturnFalse(int wannabeFk) {
        assertFalse(Assurance.isFkOk(wannabeFk));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 254, 13486161})
    void isFkOk_ShouldReturnTrue(int wannabeFk) {
        assertTrue(Assurance.isFkOk(wannabeFk));
    }

    @ParameterizedTest
    @NullSource
    void varcharCheck_ShouldThrowException(String value) {
        assertThrows(SQLException.class,
                () -> {
                    Assurance.varcharCheck(value);
                });
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "Hello"})
    void varcharCheck_ShouldNotThrowException(String value) {
        assertDoesNotThrow(() -> {
            Assurance.varcharCheck(value);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {-15618, -15, -1, 0})
    void idCheck_ShouldThrowException(int value) {
        assertThrows(SQLException.class,
                () -> {
                    Assurance.idCheck(value);
                });
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 10, 156, 15684})
    void idCheck_ShouldNotThrowException(int value) {
        assertDoesNotThrow(() -> {
            Assurance.idCheck(value);
        });
    }
}