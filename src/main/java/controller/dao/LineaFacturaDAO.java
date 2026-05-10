package controller.dao;

import controller.connection.ConexionBBDD;
import models.LineaFactura;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LineaFacturaDAO {

    public static ArrayList<LineaFactura> getLineasFactura() {
        ArrayList<LineaFactura> lineas = new ArrayList<>();
        String consulta = "SELECT id, id_factura, id_producto, cantidad, precio_unitario, subtotal FROM lineas_factura";
        Connection con = ConexionBBDD.getConexion();
        if (con == null) {
            System.out.println("Error: conexión no inicializada");
            return lineas;
        }
        try (PreparedStatement ps = con.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LineaFactura linea = new LineaFactura();
                linea.setId(rs.getInt("id"));
                linea.setIdFactura(rs.getInt("id_factura"));
                linea.setIdProducto(rs.getInt("id_producto"));
                linea.setCantidad(rs.getInt("cantidad"));
                linea.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                linea.setSubtotal(rs.getBigDecimal("subtotal"));
                lineas.add(linea);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener lineas de factura: " + e.getMessage());
        }
        return lineas;
    }

    public static int insertarLineaFactura(LineaFactura linea) {
        String consulta = "INSERT INTO lineas_factura (id_factura, id_producto, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setInt(1, linea.getIdFactura());
            ps.setInt(2, linea.getIdProducto());
            ps.setInt(3, linea.getCantidad());
            ps.setBigDecimal(4, linea.getPrecioUnitario());
            ps.setBigDecimal(5, linea.getSubtotal());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar linea de factura: " + e.getMessage());
        }
        return 0;
    }

    public static int editarLineaFactura(LineaFactura linea) {
        String consulta = "UPDATE lineas_factura SET id_factura = ?, id_producto = ?, cantidad = ?, precio_unitario = ?, subtotal = ? WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setInt(1, linea.getIdFactura());
            ps.setInt(2, linea.getIdProducto());
            ps.setInt(3, linea.getCantidad());
            ps.setBigDecimal(4, linea.getPrecioUnitario());
            ps.setBigDecimal(5, linea.getSubtotal());
            ps.setInt(6, linea.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al editar linea de factura: " + e.getMessage());
        }
        return 0;
    }

    public static int borrarLineaFactura(int idLinea) {
        String consulta = "DELETE FROM lineas_factura WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setInt(1, idLinea);
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al borrar linea de factura: " + e.getMessage());
        }
        return 0;
    }
}
