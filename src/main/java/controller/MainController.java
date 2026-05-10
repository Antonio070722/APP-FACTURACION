package controller;

import controller.dao.ClienteDAO;
import controller.dao.FacturaDAO;
import controller.dao.LineaFacturaDAO;
import controller.dao.ProductoDAO;
import models.Cliente;
import models.Factura;
import models.LineaFactura;
import models.Producto;
import view.FacturacionDialog;
import view.MainView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.util.ArrayList;

public class MainController {
    private final MainView mainView;

    public MainController(MainView mainView) {
        this.mainView = mainView;

        cargarClientes();
        cargarFacturas();
        cargarProductos();
        cargarLineasFactura();

        mainView.getBtnRefresh().addActionListener(e -> refrescarTabla());
        mainView.getBtnDelete().addActionListener(e -> eliminarTabla());
        mainView.getBtnAdd().addActionListener(e -> agregarTabla());
        mainView.getBtnModify().addActionListener(e -> modificarTabla());

        mainView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int resultado = JOptionPane.showConfirmDialog(
                        mainView,
                        "¿Estás seguro de que te marchas ya?",
                        "¿Seguro?",
                        JOptionPane.YES_NO_OPTION
                );
                if (resultado == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void agregarTabla() {
        String tab = obtenerTabSeleccionado();
        FacturacionDialog dialog = new FacturacionDialog(mainView, "Añadir", tab, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refrescarTabla();
        }
    }

    private void modificarTabla() {
        String tab = obtenerTabSeleccionado();
        Object entidad = obtenerEntidadSeleccionada(tab);
        if (entidad == null) {
            return;
        }

        FacturacionDialog dialog = new FacturacionDialog(mainView, "Modificar", tab, entidad);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            refrescarTabla();
        }
    }

    private void eliminarTabla() {
        String tab = obtenerTabSeleccionado();
        int fila = obtenerFilaSeleccionada(tab);
        if (fila == -1) {
            JOptionPane.showMessageDialog(mainView, "Debes seleccionar un registro para eliminar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = obtenerIdSeleccionado(tab, fila);
        if (id == -1) {
            mostrarError("No se pudo obtener la fila seleccionada.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(mainView,
                "¿Estás seguro de que quieres eliminar el registro seleccionado?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        int cambios = switch (tab) {
            case "Clientes" -> ClienteDAO.borrarCliente(id);
            case "Facturas" -> FacturaDAO.borrarFactura(id);
            case "Productos" -> ProductoDAO.borrarProducto(id);
            case "Lineas_factura" -> LineaFacturaDAO.borrarLineaFactura(id);
            default -> 0;
        };

        if (cambios > 0) {
            JOptionPane.showMessageDialog(mainView, "Registro eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            refrescarTabla();
        } else {
            mostrarError("No se pudo eliminar el registro.");
        }
    }

    private void refrescarTabla() {
        String tab = obtenerTabSeleccionado();
        switch (tab) {
            case "Clientes" -> cargarClientes();
            case "Facturas" -> cargarFacturas();
            case "Productos" -> cargarProductos();
            case "Lineas_factura" -> cargarLineasFactura();
        }
    }

    private String obtenerTabSeleccionado() {
        int tabSelected = mainView.getPanelTablas().getSelectedIndex();
        return mainView.getPanelTablas().getTitleAt(tabSelected);
    }

    private int obtenerFilaSeleccionada(String tab) {
        return switch (tab) {
            case "Clientes" -> mainView.getClientePanel().getTablaVista().getSelectedRow();
            case "Facturas" -> mainView.getFacturaPanel().getTablaVista().getSelectedRow();
            case "Productos" -> mainView.getProductoPanel().getTablaVista().getSelectedRow();
            case "Lineas_factura" -> mainView.getLineaFacturaPanel().getTablaVista().getSelectedRow();
            default -> -1;
        };
    }

    private int obtenerIdSeleccionado(String tab, int fila) {
        Object valor = switch (tab) {
            case "Clientes" -> mainView.getClientePanel().getModeloTabla().getValueAt(fila, 0);
            case "Facturas" -> mainView.getFacturaPanel().getModeloTabla().getValueAt(fila, 0);
            case "Productos" -> mainView.getProductoPanel().getModeloTabla().getValueAt(fila, 0);
            case "Lineas_factura" -> mainView.getLineaFacturaPanel().getModeloTabla().getValueAt(fila, 0);
            default -> null;
        };
        if (valor == null) {
            return -1;
        }
        return Integer.parseInt(valor.toString());
    }

    private Object obtenerEntidadSeleccionada(String tab) {
        int fila = obtenerFilaSeleccionada(tab);
        if (fila == -1) {
            JOptionPane.showMessageDialog(mainView, "Debes seleccionar un registro para modificar.", "Sin selección", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return switch (tab) {
            case "Clientes" -> crearClienteDesdeFila(fila);
            case "Facturas" -> crearFacturaDesdeFila(fila);
            case "Productos" -> crearProductoDesdeFila(fila);
            case "Lineas_factura" -> crearLineaFacturaDesdeFila(fila);
            default -> null;
        };
    }

    private Cliente crearClienteDesdeFila(int fila) {
        DefaultTableModel modelo = mainView.getClientePanel().getModeloTabla();
        return new Cliente(
                Integer.parseInt(modelo.getValueAt(fila, 0).toString()),
                modelo.getValueAt(fila, 1).toString(),
                modelo.getValueAt(fila, 2).toString(),
                modelo.getValueAt(fila, 3).toString(),
                modelo.getValueAt(fila, 4).toString(),
                modelo.getValueAt(fila, 5).toString()
        );
    }

    private Factura crearFacturaDesdeFila(int fila) {
        DefaultTableModel modelo = mainView.getFacturaPanel().getModeloTabla();
        return new Factura(
                Integer.parseInt(modelo.getValueAt(fila, 0).toString()),
                modelo.getValueAt(fila, 1).toString(),
                java.time.LocalDate.parse(modelo.getValueAt(fila, 2).toString()),
                Integer.parseInt(modelo.getValueAt(fila, 3).toString()),
                new BigDecimal(modelo.getValueAt(fila, 4).toString())
        );
    }

    private Producto crearProductoDesdeFila(int fila) {
        DefaultTableModel modelo = mainView.getProductoPanel().getModeloTabla();
        return new Producto(
                Integer.parseInt(modelo.getValueAt(fila, 0).toString()),
                modelo.getValueAt(fila, 1).toString(),
                modelo.getValueAt(fila, 2).toString(),
                new BigDecimal(modelo.getValueAt(fila, 3).toString())
        );
    }

    private LineaFactura crearLineaFacturaDesdeFila(int fila) {
        DefaultTableModel modelo = mainView.getLineaFacturaPanel().getModeloTabla();
        return new LineaFactura(
                Integer.parseInt(modelo.getValueAt(fila, 0).toString()),
                Integer.parseInt(modelo.getValueAt(fila, 1).toString()),
                Integer.parseInt(modelo.getValueAt(fila, 2).toString()),
                Integer.parseInt(modelo.getValueAt(fila, 3).toString()),
                new BigDecimal(modelo.getValueAt(fila, 4).toString()),
                new BigDecimal(modelo.getValueAt(fila, 5).toString())
        );
    }

    private void cargarClientes() {
        ArrayList<Cliente> clientes = ClienteDAO.getClientes();
        DefaultTableModel modelo = mainView.getClientePanel().getModeloTabla();
        modelo.setRowCount(0);
        for (Cliente cliente : clientes) {
            modelo.addRow(new Object[]{
                    cliente.getId(), cliente.getNombre(), cliente.getDireccion(), cliente.getTelefono(), cliente.getEmail(), cliente.getNif()
            });
        }
    }

    private void cargarFacturas() {
        ArrayList<Factura> facturas = FacturaDAO.getFacturas();
        DefaultTableModel modelo = mainView.getFacturaPanel().getModeloTabla();
        modelo.setRowCount(0);
        for (Factura factura : facturas) {
            modelo.addRow(new Object[]{
                    factura.getId(), factura.getNumeroFactura(), factura.getFecha() != null ? factura.getFecha().toString() : "", factura.getIdCliente(), factura.getTotal()
            });
        }
    }

    private void cargarProductos() {
        ArrayList<Producto> productos = ProductoDAO.getProductos();
        DefaultTableModel modelo = mainView.getProductoPanel().getModeloTabla();
        modelo.setRowCount(0);
        for (Producto producto : productos) {
            modelo.addRow(new Object[]{
                    producto.getId(), producto.getNombre(), producto.getDescripcion(), producto.getPrecio()
            });
        }
    }

    private void cargarLineasFactura() {
        ArrayList<LineaFactura> lineas = LineaFacturaDAO.getLineasFactura();
        DefaultTableModel modelo = mainView.getLineaFacturaPanel().getModeloTabla();
        modelo.setRowCount(0);
        for (LineaFactura linea : lineas) {
            modelo.addRow(new Object[]{
                    linea.getId(), linea.getIdFactura(), linea.getIdProducto(), linea.getCantidad(), linea.getPrecioUnitario(), linea.getSubtotal()
            });
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(mainView, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
