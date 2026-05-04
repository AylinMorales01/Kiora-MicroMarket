import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MiPrueba {
    @Test
    void miPrimerTest() {
        int resultado = 2 + 2;
        assertEquals(4, resultado, "El cálculo debería ser 4");
    }
}