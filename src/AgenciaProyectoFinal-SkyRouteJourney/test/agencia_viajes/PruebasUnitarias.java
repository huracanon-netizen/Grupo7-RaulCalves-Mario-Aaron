package agencia_viajes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PruebasUnitarias {

    // ── Métodos auxiliares que replican la lógica de la aplicación ────────────

    private boolean emailEsValido(String email) {
        return email != null && email.contains("@");
    }

    private boolean puntuacionEsValida(int puntuacion) {
        return puntuacion >= 1 && puntuacion <= 5;
    }

    private boolean estadoEsValido(String estado) {
        return estado != null &&
               (estado.equals("Pendiente") || estado.equals("Confirmada") || estado.equals("Cancelada"));
    }

    private double calcularPrecioTotal(double precioBase, int numPersonas) {
        return precioBase * numPersonas;
    }

    private int contarActividades(String actividades) {
        if (actividades == null || actividades.trim().isEmpty()) return 0;
        return actividades.split(",").length;
    }

    // ── Prueba 1: Email con '@' es válido ─────────────────────────────────────
    @Test
    public void testEmailValido() {
        assertTrue(emailEsValido("mario@gmail.com"),
                "Un email con '@' debe considerarse válido");
    }

    // ── Prueba 2: Email sin '@' no es válido ──────────────────────────────────
    @Test
    public void testEmailInvalido() {
        assertFalse(emailEsValido("mariogmail.com"),
                "Un email sin '@' debe considerarse inválido");
    }

    // ── Prueba 3: Puntuación fuera del rango 1-5 no es válida ─────────────────
    @Test
    public void testPuntuacionFueraDeRango() {
        assertFalse(puntuacionEsValida(0),  "0 no debe ser válido");
        assertFalse(puntuacionEsValida(6),  "6 no debe ser válido");
        assertTrue(puntuacionEsValida(1),   "1 sí debe ser válido");
        assertTrue(puntuacionEsValida(5),   "5 sí debe ser válido");
    }

    // ── Prueba 4: El cálculo del precio total es correcto ─────────────────────
    @Test
    public void testCalculoPrecioTotal() {
        double resultado = calcularPrecioTotal(500.0, 3);
        assertEquals(1500.0, resultado, 0.01,
                "500 € × 3 personas debe ser 1500 €");
    }

    // ── Prueba 5: Se cuentan bien las actividades separadas por coma ──────────
    @Test
    public void testContarActividades() {
        assertEquals(3, contarActividades("Buceo, Senderismo, Surf"),
                "Debe detectar 3 actividades");
        assertEquals(0, contarActividades(null),
                "Sin actividades debe devolver 0");
    }

    // ── Prueba 6: Un estado inválido no se acepta ─────────────────────────────
    @Test
    public void testEstadoInvalido() {
        assertFalse(estadoEsValido("Activa"),
                "'Activa' no es un estado permitido");
        assertFalse(estadoEsValido(null),
                "null no debe ser un estado válido");
        assertTrue(estadoEsValido("Confirmada"),
                "'Confirmada' sí debe ser válido");
    }

    // ── Prueba 7: El descuento del 20% se aplica correctamente ───────────────
    @Test
    public void testDescuento20Porciento() {
        double subtotal  = 1000.0;
        double descuento = subtotal * 0.20;
        double total     = subtotal - descuento;
        assertEquals(200.0, descuento, 0.01,
                "El descuento sobre 1000 € debe ser 200 €");
        assertEquals(800.0, total, 0.01,
                "El precio final sobre 1000 € debe ser 800 €");
    }

    // ── Prueba 8: El nombre completo del cliente se forma correctamente ────────
    @Test
    public void testNombreCompletoCliente() {
        String nombre   = "Mario";
        String apellido = "López";
        String completo = nombre + " " + apellido;
        assertEquals("Mario López", completo,
                "El nombre completo debe ser nombre + espacio + apellido");
    }

    // ── Prueba 9: Actividades vacías o en blanco devuelven 0 ──────────────────
    @Test
    public void testActividadesVacias() {
        assertEquals(0, contarActividades(""),
                "Una cadena vacía debe contar como 0 actividades");
        assertEquals(0, contarActividades("   "),
                "Una cadena de espacios debe contar como 0 actividades");
    }
}
