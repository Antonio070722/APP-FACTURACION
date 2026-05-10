package view;

import controller.dao.ClienteDAO;
import controller.dao.FacturaDAO;
import controller.dao.LineaFacturaDAO;
import controller.dao.ProductoDAO;
import models.Cliente;
import models.Factura;
import models.LineaFactura;
import models.Producto;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FacturacionDialog extends JDialog {
    private final JTextField txtClienteId = new JTextField();
    private final JTextField txtClienteNombre = new JTextField();
    private final JTextField txtClienteDireccion = new JTextField();
    private final JTextField txtClienteTelefono = new JTextField();
    private final JTextField txtClienteEmail = new JTextField();
    private final JTextField txtClienteNif = new JTextField();

    private final JTextField txtFacturaId = new JTextField();
    private final JTextField txtFacturaNumero = new JTextField();
    private final JTextField txtFacturaFecha = new JTextField();
    private final JComboBox<ComboItem> comboFacturaCliente = new JComboBox<>();
    private final JTextField txtFacturaTotal = new JTextField();

    private final JTextField txtProductoId = new JTextField();
    private final JTextField txtProductoNombre = new JTextField();
    private final JTextField txtProductoDescripcion = new JTextField();
    private final JTextField txtProductoPrecio = new JTextField();

    private final JTextField txtLineaId = new JTextField();
    private final JComboBox<ComboItem> comboLineaFactura = new JComboBox<>();
    private final JComboBox<ComboItem> comboLineaProducto = new JComboBox<>();
    private final JTextField txtLineaCantidad = new JTextField();
    private final JTextField txtLineaPrecioUnitario = new JTextField();
    private final JTextField txtLineaSubtotal = new JTextField();

    private boolean saved = false;

    public FacturacionDialog(JFrame owner, String accion, String selectedTab, Object entidad) {
        super(owner, true);
        setTitle(accion + " " + selectedTab);
        setSize(540, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel panelContenido = new JPanel(new BorderLayout());
        JTabbedPane panelTabs = new JTabbedPane();
        panelTabs.addTab("Clientes", crearPanelCliente());
        panelTabs.addTab("Facturas", crearPanelFactura());
        panelTabs.addTab("Productos", crearPanelProducto());
        panelTabs.addTab("Lineas_factura", crearPanelLineaFactura());

        int selectedIndex = 0;
        switch (selectedTab) {
            case "Facturas" -> selectedIndex = 1;
            case "Productos" -> selectedIndex = 2;
            case "Lineas_factura" -> selectedIndex = 3;
        }

        for (int i = 0; i < panelTabs.getTabCount(); i++) {
            panelTabs.setEnabledAt(i, i == selectedIndex);
        }
        panelTabs.setSelectedIndex(selectedIndex);

        cargarCombos();
        if (entidad != null) {
            cargarEntidad(entidad, selectedTab);
        }

        panelContenido.add(panelTabs, BorderLayout.CENTER);
        add(panelContenido, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnConfirmar = new JButton(accion);
        btnConfirmar.addActionListener(e -> onConfirm(accion, selectedTab));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
        panelBotones.add(btnConfirmar);
        add(panelBotones, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private JPanel crearPanelCliente() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        prepararIdField(txtClienteId);

        panel.add(new JLabel("ID Cliente:"));
        panel.add(txtClienteId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtClienteNombre);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtClienteDireccion);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtClienteTelefono);
        panel.add(new JLabel("Email:"));
        panel.add(txtClienteEmail);
        panel.add(new JLabel("NIF:"));
        panel.add(txtClienteNif);
        return panel;
    }

    private JPanel crearPanelFactura() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        prepararIdField(txtFacturaId);

        panel.add(new JLabel("ID Factura:"));
        panel.add(txtFacturaId);
        panel.add(new JLabel("Número factura:"));
        panel.add(txtFacturaNumero);
        panel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panel.add(txtFacturaFecha);
        panel.add(new JLabel("Cliente:"));
        panel.add(comboFacturaCliente);
        panel.add(new JLabel("Total:"));
        panel.add(txtFacturaTotal);
        return panel;
    }

    private JPanel crearPanelProducto() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        prepararIdField(txtProductoId);

        panel.add(new JLabel("ID Producto:"));
        panel.add(txtProductoId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtProductoNombre);
        panel.add(new JLabel("Descripción:"));
        panel.add(txtProductoDescripcion);
        panel.add(new JLabel("Precio:"));
        panel.add(txtProductoPrecio);
        return panel;
    }

    private JPanel crearPanelLineaFactura() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        prepararIdField(txtLineaId);
        prepararIdField(txtLineaSubtotal);

        panel.add(new JLabel("ID Línea:"));
        panel.add(txtLineaId);
        panel.add(new JLabel("Factura:"));
        panel.add(comboLineaFactura);
        panel.add(new JLabel("Producto:"));
        panel.add(comboLineaProducto);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtLineaCantidad);
        panel.add(new JLabel("Precio unitario:"));
        panel.add(txtLineaPrecioUnitario);
        panel.add(new JLabel("Subtotal:"));
        panel.add(txtLineaSubtotal);
        return panel;
    }

    private void prepararIdField(JTextField field) {
        field.setEditable(false);
        field.setEnabled(false);
    }

    private void cargarCombos() {
        comboFacturaCliente.removeAllItems();
        for (Cliente cliente : ClienteDAO.getClientes()) {
            comboFacturaCliente.addItem(new ComboItem(cliente.getId(), cliente.getNombre()));
        }

        comboLineaFactura.removeAllItems();
        for (Factura factura : FacturaDAO.getFacturas()) {
            comboLineaFactura.addItem(new ComboItem(factura.getId(), factura.getNumeroFactura()));
        }

        comboLineaProducto.removeAllItems();
        for (Producto producto : ProductoDAO.getProductos()) {
            comboLineaProducto.addItem(new ComboItem(producto.getId(), producto.getNombre()));
        }
    }

    private void cargarEntidad(Object entidad, String selectedTab) {
        switch (selectedTab) {
            case "Clientes" -> cargarCliente((Cliente) entidad);
            case "Facturas" -> cargarFactura((Factura) entidad);
            case "Productos" -> cargarProducto((Producto) entidad);
            case "Lineas_factura" -> cargarLineaFactura((LineaFactura) entidad);
        }
    }

    private void cargarCliente(Cliente cliente) {
        txtClienteId.setText(String.valueOf(cliente.getId()));
        txtClienteNombre.setText(cliente.getNombre());
        txtClienteDireccion.setText(cliente.getDireccion());
        txtClienteTelefono.setText(cliente.getTelefono());
        txtClienteEmail.setText(cliente.getEmail());
        txtClienteNif.setText(cliente.getNif());
    }

    private void cargarFactura(Factura factura) {
        txtFacturaId.setText(String.valueOf(factura.getId()));
        txtFacturaNumero.setText(factura.getNumeroFactura());
        txtFacturaFecha.setText(factura.getFecha() != null ? factura.getFecha().toString() : "");
        txtFacturaTotal.setText(factura.getTotal() != null ? factura.getTotal().toPlainString() : "");
        seleccionarComboItem(comboFacturaCliente, factura.getIdCliente());
    }

    private void cargarProducto(Producto producto) {
        txtProductoId.setText(String.valueOf(producto.getId()));
        txtProductoNombre.setText(producto.getNombre());
        txtProductoDescripcion.setText(producto.getDescripcion());
        txtProductoPrecio.setText(producto.getPrecio() != null ? producto.getPrecio().toPlainString() : "");
    }

    private void cargarLineaFactura(LineaFactura linea) {
        txtLineaId.setText(String.valueOf(linea.getId()));
        txtLineaCantidad.setText(String.valueOf(linea.getCantidad()));
        txtLineaPrecioUnitario.setText(linea.getPrecioUnitario() != null ? linea.getPrecioUnitario().toPlainString() : "");
        txtLineaSubtotal.setText(linea.getSubtotal() != null ? linea.getSubtotal().toPlainString() : "");
        seleccionarComboItem(comboLineaFactura, linea.getIdFactura());
        seleccionarComboItem(comboLineaProducto, linea.getIdProducto());
    }

    private void seleccionarComboItem(JComboBox<ComboItem> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            ComboItem item = combo.getItemAt(i);
            if (item.getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void onConfirm(String accion, String selectedTab) {
        boolean exito = false;
        switch (selectedTab) {
            case "Clientes" -> exito = guardarCliente(accion);
            case "Facturas" -> exito = guardarFactura(accion);
            case "Productos" -> exito = guardarProducto(accion);
            case "Lineas_factura" -> exito = guardarLineaFactura(accion);
        }
        if (exito) {
            saved = true;
            dispose();
        }
    }

    private boolean guardarCliente(String accion) {
        String nombre = txtClienteNombre.getText().trim();
        String direccion = txtClienteDireccion.getText().trim();
        String telefono = txtClienteTelefono.getText().trim();
        String email = txtClienteEmail.getText().trim();
        String nif = txtClienteNif.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre no puede estar vacío.");
            return false;
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setNif(nif);

        int resultado;
        if (accion.equals("Añadir")) {
            resultado = ClienteDAO.insertarCliente(cliente);
        } else {
            cliente.setId(Integer.parseInt(txtClienteId.getText()));
            resultado = ClienteDAO.editarCliente(cliente);
        }
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Cliente guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        mostrarError("No se pudo guardar el cliente.");
        return false;
    }

    private boolean guardarFactura(String accion) {
        String numero = txtFacturaNumero.getText().trim();
        String fechaTexto = txtFacturaFecha.getText().trim();
        ComboItem clienteSeleccionado = (ComboItem) comboFacturaCliente.getSelectedItem();
        String totalTexto = txtFacturaTotal.getText().trim();

        if (numero.isEmpty()) {
            mostrarError("El número de factura no puede estar vacío.");
            return false;
        }
        if (fechaTexto.isEmpty()) {
            mostrarError("La fecha no puede estar vacía.");
            return false;
        }
        if (clienteSeleccionado == null) {
            mostrarError("Debes seleccionar un cliente.");
            return false;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaTexto);
        } catch (DateTimeParseException e) {
            mostrarError("La fecha debe tener el formato YYYY-MM-DD.");
            return false;
        }
        BigDecimal total;
        try {
            total = new BigDecimal(totalTexto);
        } catch (NumberFormatException e) {
            mostrarError("El total debe ser un número válido.");
            return false;
        }

        Factura factura = new Factura();
        factura.setNumeroFactura(numero);
        factura.setFecha(fecha);
        factura.setIdCliente(clienteSeleccionado.getId());
        factura.setTotal(total);

        int resultado;
        if (accion.equals("Añadir")) {
            resultado = FacturaDAO.insertarFactura(factura);
        } else {
            factura.setId(Integer.parseInt(txtFacturaId.getText()));
            resultado = FacturaDAO.editarFactura(factura);
        }
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Factura guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        mostrarError("No se pudo guardar la factura.");
        return false;
    }

    private boolean guardarProducto(String accion) {
        String nombre = txtProductoNombre.getText().trim();
        String descripcion = txtProductoDescripcion.getText().trim();
        String precioTexto = txtProductoPrecio.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre del producto no puede estar vacío.");
            return false;
        }
        BigDecimal precio;
        try {
            precio = new BigDecimal(precioTexto);
        } catch (NumberFormatException e) {
            mostrarError("El precio debe ser un número válido.");
            return false;
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);

        int resultado;
        if (accion.equals("Añadir")) {
            resultado = ProductoDAO.insertarProducto(producto);
        } else {
            producto.setId(Integer.parseInt(txtProductoId.getText()));
            resultado = ProductoDAO.editarProducto(producto);
        }
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Producto guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        mostrarError("No se pudo guardar el producto.");
        return false;
    }

    private boolean guardarLineaFactura(String accion) {
        ComboItem facturaSeleccionada = (ComboItem) comboLineaFactura.getSelectedItem();
        ComboItem productoSeleccionado = (ComboItem) comboLineaProducto.getSelectedItem();
        String cantidadTexto = txtLineaCantidad.getText().trim();
        String precioTexto = txtLineaPrecioUnitario.getText().trim();

        if (facturaSeleccionada == null) {
            mostrarError("Debes seleccionar una factura.");
            return false;
        }
        if (productoSeleccionado == null) {
            mostrarError("Debes seleccionar un producto.");
            return false;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadTexto);
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número entero.");
            return false;
        }
        BigDecimal precioUnitario;
        try {
            precioUnitario = new BigDecimal(precioTexto);
        } catch (NumberFormatException e) {
            mostrarError("El precio unitario debe ser un número válido.");
            return false;
        }
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        txtLineaSubtotal.setText(subtotal.toPlainString());

        LineaFactura linea = new LineaFactura();
        linea.setIdFactura(facturaSeleccionada.getId());
        linea.setIdProducto(productoSeleccionado.getId());
        linea.setCantidad(cantidad);
        linea.setPrecioUnitario(precioUnitario);
        linea.setSubtotal(subtotal);

        int resultado;
        if (accion.equals("Añadir")) {
            resultado = LineaFacturaDAO.insertarLineaFactura(linea);
        } else {
            linea.setId(Integer.parseInt(txtLineaId.getText()));
            resultado = LineaFacturaDAO.editarLineaFactura(linea);
        }
        if (resultado > 0) {
            JOptionPane.showMessageDialog(this, "Línea de factura guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        mostrarError("No se pudo guardar la línea de factura.");
        return false;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isSaved() {
        return saved;
    }

    private static class ComboItem {
        private final int id;
        private final String label;

        public ComboItem(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
