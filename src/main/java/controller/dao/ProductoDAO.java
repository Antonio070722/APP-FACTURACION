package controller.dao;

import controller.connection.ConexionBBDD;
import models.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductoDAO {

    public static ArrayList<Producto> getProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        String consulta = "SELECT id, nombre, descripcion, precio FROM productos";
        Connection con = ConexionBBDD.getConexion();
        if (con == null) {
            System.out.println("Error: conexión no inicializada");
            return productos;
        }
        try (PreparedStatement ps = con.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener productos: " + e.getMessage());
        }
        return productos;
    }

    public static int insertarProducto(Producto producto) {
        String consulta = "INSERT INTO productos (nombre, descripcion, precio) VALUES (?, ?, ?)";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
        return 0;
    }

    public static int editarProducto(Producto producto) {
        String consulta = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ? WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setBigDecimal(3, producto.getPrecio());
            ps.setInt(4, producto.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al editar producto: " + e.getMessage());
        }
        return 0;
    }

    public static int borrarProducto(int idProducto) {
        String eliminarLineas = "DELETE FROM lineas_factura WHERE id_producto = ?";
        String eliminarProducto = "DELETE FROM productos WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        int cambios = 0;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psLineas = con.prepareStatement(eliminarLineas)) {
                psLineas.setInt(1, idProducto);
                cambios += psLineas.executeUpdate();
            }
            try (PreparedStatement psProducto = con.prepareStatement(eliminarProducto)) {
                psProducto.setInt(1, idProducto);
                cambios += psProducto.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            System.out.println("Error al borrar producto: " + e.getMessage());
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
