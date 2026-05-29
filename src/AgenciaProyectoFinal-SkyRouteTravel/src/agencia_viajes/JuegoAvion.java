package agencia_viajes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Ventana principal del minijuego "Vuelo Infinito".
 * Extiende JFrame para mostrarse como una ventana independiente
 * sin cerrar la aplicación al cerrarse (DISPOSE_ON_CLOSE).
 */
public class JuegoAvion extends JFrame {

    public JuegoAvion(int idCliente) {
        setTitle("SkyRouteJourney | Vuelo Infinito");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        PanelJuego panel = new PanelJuego(idCliente);
        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Panel donde ocurre todo el juego.
     * Implementa ActionListener para recibir los ticks del Timer (bucle de juego).
     */
    static class PanelJuego extends JPanel implements ActionListener {

        // ── Dimensiones del área de juego ──────────────────────────────────
        static final int ANCHO = 800;  // Ancho en píxeles del panel
        static final int ALTO  = 350;  // Alto en píxeles del panel
        static final int SUELO = 290;  // Coordenada Y donde está el suelo (pista)

        // ── ID del cliente logueado (-1 = anónimo, no guarda) ─────────────
        final int idCliente;

        // ── Estado del avión ───────────────────────────────────────────────
        int avionY     = SUELO - 36;  // Posición vertical del avión (aumenta = baja)
        int velY       = 0;           // Velocidad vertical actual (positivo = cae, negativo = sube)
        boolean enSuelo = true;       // Verdadero si el avión está tocando el suelo

        // Posición horizontal fija del avión (no se mueve en X, el mundo se mueve hacia él)
        static final int AVION_X = 110;
        static final int AVION_W = 64;   // Ancho del sprite del avión
        static final int AVION_H = 28;   // Alto del sprite del avión
        static final int GRAVEDAD = 1;   // Píxeles que se suman a velY cada tick (atrae hacia abajo)
        static final int SALTO    = -16; // Impulso hacia arriba al saltar (negativo = sube)

        // ── Obstáculos ─────────────────────────────────────────────────────
        // Lista de rectángulos que representan los edificios/obstáculos en pantalla
        List<Rectangle> obstaculos = new ArrayList<>();
        int timerObs = 0;   // Contador de ticks desde el último obstáculo generado
        int gapObs   = 90;  // Ticks de espera hasta generar el siguiente obstáculo
        Random rnd   = new Random();

        // ── Nubes decorativas ──────────────────────────────────────────────
        // Cada nube es un array [x, y, ancho] para saber dónde dibujarla
        List<int[]> nubes = new ArrayList<>();

        // ── Estado general del juego ───────────────────────────────────────
        boolean jugando    = false; // El juego está activo (el timer corre)
        boolean muerto     = false; // El avión ha colisionado
        int puntuacion     = 0;     // Puntuación actual (sube con el tiempo)
        int record         = 0;     // Máxima puntuación alcanzada en la sesión
        int tick           = 0;     // Contador de frames desde que empezó la partida
        int velJuego       = 7;     // Velocidad horizontal a la que se mueven los obstáculos
        int offsetSuelo    = 0;     // Desplazamiento de las rayas de la pista (efecto movimiento)
        int parpadeo       = 0;     // Contador para el efecto de parpadeo al morir

        // Timer que dispara actionPerformed cada 16ms (~60 fotogramas por segundo)
        Timer timer = new Timer(16, this);

        // ── Paleta de colores (consistente con la app) ─────────────────────
        static final Color C_FONDO   = new Color(18, 18, 48);
        static final Color C_SUELO   = new Color(38, 38, 72);
        static final Color C_LINEA   = new Color(0, 153, 153);
        static final Color C_AVION   = new Color(0, 210, 210);
        static final Color C_ALA     = new Color(0, 170, 170);
        static final Color C_COLA    = new Color(0, 130, 130);
        static final Color C_VENTANA = new Color(200, 240, 255, 200);
        static final Color C_OBS_TOP = new Color(210, 90, 60);
        static final Color C_OBS_BOT = new Color(160, 50, 30);
        static final Color C_NUBE    = new Color(55, 55, 95);
        static final Color C_HUD     = new Color(0, 200, 200);
        static final Color C_MSG     = new Color(180, 180, 255);
        static final Color C_MUERTO  = new Color(220, 80, 80);

        PanelJuego(int idCliente) {
            this.idCliente = idCliente;
            setPreferredSize(new Dimension(ANCHO, ALTO));
            setFocusable(true); // Necesario para que el panel reciba eventos de teclado

            // Escucha la tecla ESPACIO
            addKeyListener(new KeyAdapter() {
                @Override public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) accion();
                }
            });

            // También acepta clic del ratón como alternativa al ESPACIO
            addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) { accion(); }
            });

            // Generar 6 nubes en posiciones aleatorias para poblar el fondo inicial
            for (int i = 0; i < 6; i++)
                nubes.add(new int[]{rnd.nextInt(ANCHO), rnd.nextInt(110) + 20, rnd.nextInt(70) + 50});
        }

        /**
         * Acción central del jugador (ESPACIO o clic).
         * Según el estado del juego hace una cosa distinta:
         * - Si no ha empezado → arranca el juego
         * - Si está muerto    → reinicia
         * - Si está jugando y en el suelo → salta
         */
        void accion() {
            if (!jugando && !muerto) {
                jugando = true;
                timer.start();
            } else if (muerto) {
                reiniciar();
            } else if (enSuelo) {
                velY    = SALTO;   // Aplicar impulso hacia arriba
                enSuelo = false;
            }
        }

        /** Reinicia todas las variables a su estado inicial para una nueva partida */
        void reiniciar() {
            avionY     = SUELO - AVION_H;
            velY       = 0;
            enSuelo    = true;
            obstaculos.clear();
            puntuacion = 0;
            tick       = 0;
            velJuego   = 7;
            timerObs   = 0;
            gapObs     = 90;
            muerto     = false;
            jugando    = true;
            parpadeo   = 0;
            timer.start();
        }

        /**
         * Bucle de juego — se ejecuta cada 16ms gracias al Timer.
         * Aquí se actualiza la física, se generan obstáculos y se detectan colisiones.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!jugando || muerto) return;

            tick++;
            puntuacion = tick / 6; // Cada 6 ticks sube 1 punto
            if (puntuacion > record) record = puntuacion;

            // Cada 600 ticks aumenta la velocidad del juego (hasta un máximo de 20)
            if (tick % 600 == 0 && velJuego < 20) velJuego++;

            // ── Física del avión ──
            velY   += GRAVEDAD;  // La gravedad aumenta la velocidad de caída cada tick
            avionY += velY;      // Aplicar la velocidad a la posición

            // Límite inferior: no puede bajar del suelo
            if (avionY >= SUELO - AVION_H) {
                avionY  = SUELO - AVION_H;
                velY    = 0;
                enSuelo = true;
            }
            // Límite superior: no puede salirse por arriba
            if (avionY < 5) { avionY = 5; velY = 0; }

            // Animar las rayas de la pista desplazándolas hacia la izquierda
            offsetSuelo = (offsetSuelo + velJuego) % 50;

            // ── Mover nubes hacia la izquierda ──
            for (int[] n : nubes) {
                n[0] -= 2; // Las nubes se mueven más lento que los obstáculos (sensación de profundidad)
                if (n[0] < -130) { // Si sale por la izquierda, reaparece por la derecha
                    n[0] = ANCHO + 10;
                    n[1] = rnd.nextInt(110) + 20;
                    n[2] = rnd.nextInt(70) + 50;
                }
            }

            // ── Generar obstáculos ──
            timerObs++;
            if (timerObs >= gapObs) {
                timerObs = 0;
                gapObs   = rnd.nextInt(70) + 55; // Distancia aleatoria hasta el siguiente
                int h    = rnd.nextInt(55) + 30;  // Alto aleatorio del edificio
                // El obstáculo aparece justo fuera del borde derecho
                obstaculos.add(new Rectangle(ANCHO + 5, SUELO - h, 26, h));
            }

            // ── Mover obstáculos y detectar colisión ──
            List<Rectangle> fuera = new ArrayList<>();
            // Hitbox reducida respecto al sprite para que sea más justo (no pilla píxeles de esquina)
            Rectangle hitbox = new Rectangle(AVION_X + 12, avionY + 6, AVION_W - 24, AVION_H - 10);
            for (Rectangle obs : obstaculos) {
                obs.x -= velJuego; // Desplazar obstáculo a la izquierda
                if (obs.x + obs.width < 0) {
                    fuera.add(obs); // Marcar para eliminar si ya salió por la izquierda
                    continue;
                }
                // Comprobar si la hitbox del avión toca el obstáculo
                if (hitbox.intersects(obs)) {
                    muerto  = true;
                    jugando = false;
                    timer.stop();
                    guardarRecord(); // Guardar puntuación en la BD si hay usuario logueado
                }
            }
            obstaculos.removeAll(fuera); // Limpiar obstáculos que ya pasaron

            repaint(); // Pedir al panel que se redibuje
        }

        // ── Guardar record en BD ──────────────────────────────────────────
        /**
         * Llama a sp_guardar_record en un hilo del pool.
         * Solo actúa si hay un cliente logueado (idCliente > 0) y la puntuación > 0.
         * La BD solo actualiza si la nueva puntuación supera el record guardado.
         */
        void guardarRecord() {
            if (idCliente <= 0 || puntuacion <= 0) return;

            ConexionDB.getExecutor().submit(() -> {
                try (java.sql.Connection con = ConexionDB.getConexion();
                     java.sql.CallableStatement cs = con.prepareCall("{CALL sp_guardar_record(?, ?)}")) {
                    cs.setInt(1, idCliente);
                    cs.setInt(2, puntuacion);
                    cs.execute();
                } catch (java.sql.SQLException ex) {
                    // Error silencioso — no interrumpir la experiencia de juego
                }
            });
        }

        // ── RENDER ────────────────────────────────────────────────────────

        /**
         * Dibuja cada frame del juego.
         * Se llama automáticamente cuando se invoca repaint().
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            // Activar antialiasing para que los bordes curvos se vean suaves
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // ── Fondo: cielo degradado de azul muy oscuro a azul medio ──
            GradientPaint cielo = new GradientPaint(0, 0, new Color(12, 12, 40), 0, ALTO, new Color(28, 28, 65));
            g2.setPaint(cielo);
            g2.fillRect(0, 0, ANCHO, ALTO);

            // ── Estrellas: posiciones "aleatorias" pero fijas (misma semilla siempre) ──
            g2.setColor(new Color(200, 200, 255, 80));
            long s = 42; // Semilla fija para que las estrellas no cambien de sitio cada frame
            for (int i = 0; i < 40; i++) {
                s = s * 1664525L + 1013904223L; // Generador congruencial lineal
                int sx = (int) ((s & 0x7FFFFFFF) % ANCHO);
                s = s * 1664525L + 1013904223L;
                int sy = (int) ((s & 0x7FFFFFFF) % (SUELO - 40));
                g2.fillOval(sx, sy, 2, 2);
            }

            // ── Nubes de fondo ──
            for (int[] n : nubes) dibujarNube(g2, n[0], n[1], n[2]);

            // ── Suelo ──
            g2.setColor(C_SUELO);
            g2.fillRect(0, SUELO, ANCHO, ALTO - SUELO);

            // Línea que separa el cielo del suelo
            g2.setColor(C_LINEA);
            g2.setStroke(new BasicStroke(2f));
            g2.drawLine(0, SUELO, ANCHO, SUELO);

            // Rayas de pista animadas (se desplazan con offsetSuelo para simular movimiento)
            g2.setColor(new Color(0, 153, 153, 70));
            g2.setStroke(new BasicStroke(2f));
            for (int x = -offsetSuelo; x < ANCHO; x += 50)
                g2.drawLine(x, SUELO + 14, x + 28, SUELO + 14);

            // ── Obstáculos ──
            for (Rectangle obs : obstaculos) dibujarObstaculo(g2, obs);

            // ── Avión (con efecto de parpadeo al morir) ──
            // parpadeo/4 hace que cambie de estado cada 4 frames → efecto parpadeo lento
            parpadeo = muerto ? parpadeo + 1 : 0;
            if (!muerto || (parpadeo / 4) % 2 == 0)
                dibujarAvion(g2, AVION_X, avionY);

            // ── HUD: puntuación y récord en la esquina superior derecha ──
            g2.setFont(new Font("Tempus Sans ITC", Font.PLAIN, 18));
            g2.setColor(C_HUD);
            g2.drawString(String.format("Puntos: %05d", puntuacion), ANCHO - 190, 30);
            g2.setColor(new Color(0, 153, 153, 140));
            g2.drawString(String.format("Record: %05d", record), ANCHO - 190, 52);

            // ── Mensajes de estado ──
            if (!jugando && !muerto) {
                // Pantalla de inicio: todavía no se ha pulsado nada
                centrar(g2, "SkyRoute | Vuelo Infinito", 26, C_HUD,   ALTO / 2 - 30);
                centrar(g2, "Pulsa ESPACIO o haz CLIC para despegar", 15, C_MSG, ALTO / 2 + 8);
            } else if (muerto) {
                // Pantalla de game over
                centrar(g2, "¡Colisión! — " + puntuacion + " puntos", 24, C_MUERTO, ALTO / 2 - 28);
                centrar(g2, "Pulsa ESPACIO o CLIC para reintentar",    15, C_MSG,    ALTO / 2 + 10);
            }
        }

        /**
         * Dibuja el sprite del avión en la posición (x, y).
         * Está compuesto por varias formas geométricas de Graphics2D.
         */
        void dibujarAvion(Graphics2D g2, int x, int y) {
            // Estela: una serie de óvalos cada vez más transparentes hacia atrás
            for (int i = 5; i >= 1; i--) {
                g2.setColor(new Color(0, 200, 200, 15 * i));
                g2.fillOval(x - i * 10, y + AVION_H / 2 - 3, 12, 6);
            }

            // Cuerpo principal: elipse alargada
            g2.setColor(C_AVION);
            g2.fillOval(x + 4, y + 6, AVION_W - 10, AVION_H - 12);

            // Nariz: triángulo que apunta hacia la derecha
            int[] nx = {x + AVION_W - 6, x + AVION_W + 10, x + AVION_W - 6};
            int[] ny = {y + 7, y + AVION_H / 2, y + AVION_H - 7};
            g2.fillPolygon(nx, ny, 3);

            // Ala superior: triángulo hacia arriba
            int[] wax = {x + 16, x + 44, x + 22};
            int[] way = {y + 8,  y + 8,  y - 2};
            g2.setColor(C_ALA);
            g2.fillPolygon(wax, way, 3);

            // Ala inferior: triángulo hacia abajo (simétrico al ala superior)
            int[] wbx = {x + 16, x + 44, x + 22};
            int[] wby = {y + AVION_H - 8, y + AVION_H - 8, y + AVION_H + 2};
            g2.fillPolygon(wbx, wby, 3);

            // Cola vertical: pequeño triángulo en la parte trasera
            int[] cx = {x + 4, x + 14, x + 8};
            int[] cy = {y + 8, y + 8,  y};
            g2.setColor(C_COLA);
            g2.fillPolygon(cx, cy, 3);

            // Motor: rectángulo redondeado debajo del ala
            g2.setColor(new Color(0, 140, 140));
            g2.fillRoundRect(x + 22, y + AVION_H - 4, 18, 7, 4, 4);

            // Ventanilla: pequeño óvalo brillante
            g2.setColor(C_VENTANA);
            g2.fillOval(x + AVION_W - 22, y + AVION_H / 2 - 4, 10, 8);

            // Contorno del cuerpo
            g2.setColor(new Color(0, 110, 110));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawOval(x + 4, y + 6, AVION_W - 10, AVION_H - 12);
        }

        /**
         * Dibuja un obstáculo (edificio) con degradado y ventanas iluminadas.
         */
        void dibujarObstaculo(Graphics2D g2, Rectangle r) {
            // Degradado vertical: más claro arriba, más oscuro abajo
            GradientPaint gp = new GradientPaint(r.x, r.y, C_OBS_TOP, r.x, r.y + r.height, C_OBS_BOT);
            g2.setPaint(gp);
            g2.fillRect(r.x, r.y, r.width, r.height);

            // Ventanas: pequeños cuadrados amarillos a intervalos de 10px
            g2.setColor(new Color(255, 220, 100, 160));
            for (int wy = r.y + 6; wy < r.y + r.height - 6; wy += 10)
                g2.fillRect(r.x + 5, wy, 5, 5);

            // Borde del edificio
            g2.setColor(C_OBS_TOP.brighter());
            g2.setStroke(new BasicStroke(1f));
            g2.drawRect(r.x, r.y, r.width, r.height);
        }

        /**
         * Dibuja una nube con tres óvalos superpuestos para darle volumen.
         */
        void dibujarNube(Graphics2D g2, int x, int y, int w) {
            g2.setColor(C_NUBE);
            g2.fillOval(x,           y,          w,      w / 2); // Óvalo base (el más grande)
            g2.fillOval(x + w / 4,   y - w / 5,  w / 2,  w / 2); // Óvalo central superior
            g2.fillOval(x + w / 2,   y + w / 10, w / 2,  w / 3); // Óvalo derecho
        }

        /**
         * Dibuja un texto centrado horizontalmente en la pantalla.
         * @param y  coordenada vertical donde dibujar el texto
         */
        void centrar(Graphics2D g2, String txt, int size, Color c, int y) {
            g2.setFont(new Font("Tempus Sans ITC", Font.PLAIN, size));
            g2.setColor(c);
            int w = g2.getFontMetrics().stringWidth(txt); // Ancho en píxeles del texto
            g2.drawString(txt, (ANCHO - w) / 2, y);       // Centrarlo restando la mitad del ancho
        }
    }
}
