package controller.dao;

import controller.connection.ConexionBBDD;
import models.Factura;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FacturaDAO {

    public static ArrayList<Factura> getFacturas() {
        ArrayList<Factura> facturas = new ArrayList<>();
        String consulta = "SELECT id, numero_factura, fecha, id_cliente, total FROM facturas";
        Connection con = ConexionBBDD.getConexion();
        if (con == null) {
            System.out.println("Error: conexión no inicializada");
            return facturas;
        }
        try (PreparedStatement ps = con.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Factura factura = new Factura();
                factura.setId(rs.getInt("id"));
                factura.setNumeroFactura(rs.getString("numero_factura"));
                factura.setFecha(rs.getDate("fecha").toLocalDate());
                factura.setIdCliente(rs.getInt("id_cliente"));
                factura.setTotal(rs.getBigDecimal("total"));
                facturas.add(factura);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener facturas: " + e.getMessage());
        }
        return facturas;
    }

    public static int insertarFactura(Factura factura) {
        String consulta = "INSERT INTO facturas (numero_factura, fecha, id_cliente, total) VALUES (?, ?, ?, ?)";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, factura.getNumeroFactura());
            ps.setDate(2, java.sql.Date.valueOf(factura.getFecha()));
            ps.setInt(3, factura.getIdCliente());
            ps.setBigDecimal(4, factura.getTotal());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar factura: " + e.getMessage());
        }
        return 0;
    }

    public static int editarFactura(Factura factura) {
        String consulta = "UPDATE facturas SET numero_factura = ?, fecha = ?, id_cliente = ?, total = ? WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, factura.getNumeroFactura());
            ps.setDate(2, java.sql.Date.valueOf(factura.getFecha()));
            ps.setInt(3, factura.getIdCliente());
            ps.setBigDecimal(4, factura.getTotal());
            ps.setInt(5, factura.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al editar factura: " + e.getMessage());
        }
        return 0;
    }

    public static int borrarFactura(int idFactura) {
        String eliminarLineas = "DELETE FROM lineas_factura WHERE id_factura = ?";
        String eliminarFactura = "DELETE FROM facturas WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        int cambios = 0;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psLineas = con.prepareStatement(eliminarLineas)) {
                psLineas.setInt(1, idFactura);
                cambios += psLineas.executeUpdate();
            }
            try (PreparedStatement psFactura = con.prepareStatement(eliminarFactura)) {
                psFactura.setInt(1, idFactura);
                cambios += psFactura.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            System.out.println("Error al borrar factura: " + e.getMessage());
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            cambios = 0;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error al restaurar autocommit: " + ex.getMessage());
            }
        }
        return cambios;
    }
}
