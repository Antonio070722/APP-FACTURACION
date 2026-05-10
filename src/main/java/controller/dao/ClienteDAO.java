package controller.dao;

import controller.connection.ConexionBBDD;
import models.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;

public class ClienteDAO {

    public static ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String consulta = "SELECT id, nombre, direccion, telefono, email, nif FROM clientes";

        Connection con = ConexionBBDD.getConexion();
        if (con == null) {
            System.out.println("Error: conexión no inicializada");
            return clientes;
        }

        try (PreparedStatement ps = con.prepareStatement(consulta);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setNif(rs.getString("nif"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener clientes: " + e.getMessage());
        }
        return clientes;
    }

    public static int insertarCliente(Cliente cliente) {
        String consulta = "INSERT INTO clientes (nombre, direccion, telefono, email, nif) VALUES (?, ?, ?, ?, ?)";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getNif());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al insertar cliente: " + e.getMessage());
        }
        return 0;
    }

    public static int editarCliente(Cliente cliente) {
        String consulta = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ?, email = ?, nif = ? WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setString(5, cliente.getNif());
            ps.setInt(6, cliente.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al editar cliente: " + e.getMessage());
        }
        return 0;
    }

    public static int borrarCliente(int idCliente) {
        String eliminarLineas = "DELETE FROM lineas_factura WHERE id_factura IN (SELECT id FROM facturas WHERE id_cliente = ?)";
        String eliminarFacturas = "DELETE FROM facturas WHERE id_cliente = ?";
        String eliminarCliente = "DELETE FROM clientes WHERE id = ?";
        Connection con = ConexionBBDD.getConexion();
        int cambios = 0;
        try {
            con.setAutoCommit(false);
            try (PreparedStatement psLineas = con.prepareStatement(eliminarLineas)) {
                psLineas.setInt(1, idCliente);
                cambios += psLineas.executeUpdate();
            }
            try (PreparedStatement psFacturas = con.prepareStatement(eliminarFacturas)) {
                psFacturas.setInt(1, idCliente);
                cambios += psFacturas.executeUpdate();
            }
            try (PreparedStatement psCliente = con.prepareStatement(eliminarCliente)) {
                psCliente.setInt(1, idCliente);
                cambios += psCliente.executeUpdate();
            }
            con.commit();
        } catch (SQLException e) {
            System.out.println("Error al borrar cliente: " + e.getMessage());
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
