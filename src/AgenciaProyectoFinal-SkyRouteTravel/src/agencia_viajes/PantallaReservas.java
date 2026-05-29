/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package agencia_viajes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.SwingUtilities;

public class PantallaReservas extends javax.swing.JFrame {

    private int idCliente;

    public PantallaReservas(int idCliente) {
        this.idCliente = idCliente;
        initComponents();
        getContentPane().setComponentZOrder(jLabel1, getContentPane().getComponentCount() - 1);
        jToggleButton1.addActionListener(e -> {
            new PantallaPrincipal(idCliente).setVisible(true);
            dispose();
        });
        estilizarCampos();
        int pagina = elegirPagina();
        if (pagina < 1) pagina = 1;
        cargarViajes(pagina);
    }

    public PantallaReservas() {
        this(-1);
    }

    private int elegirPagina() {
        // Preguntar cuántas reservas totales hay para construir las opciones
        int total = contarViajes();
        int numPaginas = Math.max(1, (int) Math.ceil(total / 4.0));

        String[] opciones = new String[numPaginas];
        for (int i = 0; i < numPaginas; i++) {
            int desde = i * 4 + 1;
            int hasta = Math.min(i * 4 + 4, total);
            opciones[i] = "Reservas " + desde + " – " + hasta;
        }

        Object seleccion = javax.swing.JOptionPane.showInputDialog(
            null,
            "¿Qué reservas quieres ver?",
            "Seleccionar reservas",
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        if (seleccion == null) return 1;
        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccion)) return i + 1;
        }
        return 1;
    }

    private int contarViajes() {
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT COUNT(*) FROM Viaje WHERE Id_Cliente = ?")) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void estilizarCampos() {
        java.awt.Font f = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16);
        java.awt.Color fg = java.awt.Color.WHITE;
        for (javax.swing.JTextField tf : new javax.swing.JTextField[]{
                idviaje1, destino1, fechainicio1, fechafin1,
                idviaje2, destino2, fechainicio2, fechafin2,
                idviaje3, destino3, fechainicio3, fechafin3,
                idviaje4, destino4, fechainicio4, fechafin4}) {
            tf.setFont(f);
            tf.setForeground(fg);
            tf.setEditable(false);
            tf.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        }
    }

    private void cargarViajes(int pagina) {
        int offset = (pagina - 1) * 4;
        ConexionDB.getExecutor().submit(() -> {
            String sql = "SELECT v.idViaje, d.nombre AS destino, v.fecha_inicio, v.fecha_fin "
                       + "FROM Viaje v "
                       + "JOIN Destino d ON v.Id_Destino = d.idDestino "
                       + "WHERE v.Id_Cliente = ? "
                       + "ORDER BY v.idViaje DESC "
                       + "LIMIT 4 OFFSET ?";
            try (Connection con = ConexionDB.getConexion();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCliente);
                ps.setInt(2, offset);
                try (ResultSet rs = ps.executeQuery()) {
                    javax.swing.JTextField[] ids   = {idviaje1,    idviaje2,    idviaje3,    idviaje4};
                    javax.swing.JTextField[] dests = {destino1,    destino2,    destino3,    destino4};
                    javax.swing.JTextField[] inis  = {fechainicio1, fechainicio2, fechainicio3, fechainicio4};
                    javax.swing.JTextField[] fins  = {fechafin1,   fechafin2,   fechafin3,   fechafin4};
                    int i = 0;
                    while (rs.next() && i < 4) {
                        String id   = String.valueOf(rs.getInt("idViaje"));
                        String dest = rs.getString("destino");
                        String ini  = rs.getDate("fecha_inicio").toString();
                        String fin  = rs.getDate("fecha_fin").toString();
                        final int idx = i;
                        SwingUtilities.invokeLater(() -> {
                            ids[idx].setText("VJ - " + id);
                            dests[idx].setText(dest);
                            inis[idx].setText(ini);
                            fins[idx].setText(fin);
                        });
                        i++;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        idviaje1 = new javax.swing.JTextField();
        fechainicio1 = new javax.swing.JTextField();
        fechafin1 = new javax.swing.JTextField();
        fechainicio3 = new javax.swing.JTextField();
        destino1 = new javax.swing.JTextField();
        destino2 = new javax.swing.JTextField();
        fechainicio2 = new javax.swing.JTextField();
        fechafin2 = new javax.swing.JTextField();
        idviaje3 = new javax.swing.JTextField();
        destino3 = new javax.swing.JTextField();
        idviaje2 = new javax.swing.JTextField();
        fechafin3 = new javax.swing.JTextField();
        idviaje4 = new javax.swing.JTextField();
        destino4 = new javax.swing.JTextField();
        fechainicio4 = new javax.swing.JTextField();
        fechafin4 = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1961, 1080));
        setMinimumSize(new java.awt.Dimension(1961, 1080));
        setPreferredSize(new java.awt.Dimension(1961, 1080));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        idviaje1.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(idviaje1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 380, 80));

        fechainicio1.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(fechainicio1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 690, 380, 80));

        fechafin1.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(fechafin1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 880, 380, 80));

        fechainicio3.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(fechainicio3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 690, 380, 80));

        destino1.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(destino1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 490, 380, 80));

        destino2.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(destino2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 490, 380, 80));

        fechainicio2.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(fechainicio2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 690, 380, 80));

        fechafin2.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(fechafin2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 880, 380, 80));

        idviaje3.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(idviaje3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 300, 380, 80));

        destino3.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(destino3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 490, 380, 80));

        idviaje2.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(idviaje2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 290, 380, 80));

        fechafin3.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(fechafin3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 880, 380, 80));

        idviaje4.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(idviaje4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 300, 380, 80));

        destino4.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(destino4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 490, 380, 80));

        fechainicio4.setBackground(new java.awt.Color(0, 0, 51));
        getContentPane().add(fechainicio4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 690, 380, 80));

        fechafin4.setBackground(new java.awt.Color(0, 0, 51));
        fechafin4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechafin4ActionPerformed(evt);
            }
        });
        getContentPane().add(fechafin4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1460, 880, 380, 80));

        jToggleButton1.setBackground(new java.awt.Color(0, 0, 51));
        jToggleButton1.setText("HOME");
        getContentPane().add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(836, 60, 260, 60));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/agencia_viajes/plantilla_reservas_sin_destinos.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1910, 1160));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fechafin4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechafin4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fechafin4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PantallaReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PantallaReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PantallaReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PantallaReservas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaReservas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField destino1;
    private javax.swing.JTextField destino2;
    private javax.swing.JTextField destino3;
    private javax.swing.JTextField destino4;
    private javax.swing.JTextField fechafin1;
    private javax.swing.JTextField fechafin2;
    private javax.swing.JTextField fechafin3;
    private javax.swing.JTextField fechafin4;
    private javax.swing.JTextField fechainicio1;
    private javax.swing.JTextField fechainicio2;
    private javax.swing.JTextField fechainicio3;
    private javax.swing.JTextField fechainicio4;
    private javax.swing.JTextField idviaje1;
    private javax.swing.JTextField idviaje2;
    private javax.swing.JTextField idviaje3;
    private javax.swing.JTextField idviaje4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
