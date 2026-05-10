package view;

import view.cliente.ClientePanel;
import view.factura.FacturaPanel;
import view.linea.LineaFacturaPanel;
import view.producto.ProductoPanel;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame{

    // ===== PANELES ===== \\
    JPanel panelNorte = new JPanel();
    JPanel panelBotones = new JPanel();

    // ===== PANELES PERSONALIZADOS ===== \\
    private JTabbedPane panelTablas = new JTabbedPane();
    private ClientePanel clientePanel = new ClientePanel();
    private FacturaPanel facturaPanel = new FacturaPanel();
    private ProductoPanel productoPanel = new ProductoPanel();
    private LineaFacturaPanel lineaFacturaPanel = new LineaFacturaPanel();

    // ===== BOTONES ===== \\
    JButton btnAdd = new JButton("Añadir");
    JButton btnDelete = new JButton("Borrar");
    JButton btnRefresh = new JButton("Refrescar");
    JButton btnModify = new JButton("Modificar");

    // ===== CONSTRUCTOR ===== \\
    public MainView(){
        establecerPredeterminado();
        añadirPestañas();
        añadirBotones();
    }

    // ===== GETTERS AND SETTERS ===== \\
    public JTabbedPane getPanelTablas() { return panelTablas; }
    public void setPanelTablas(JTabbedPane panelTablas) { this.panelTablas = panelTablas; }

    public ClientePanel getClientePanel() { return clientePanel; }
    public void setClientePanel(ClientePanel clientePanel) { this.clientePanel = clientePanel; }

    public FacturaPanel getFacturaPanel() { return facturaPanel; }
    public void setFacturaPanel(FacturaPanel facturaPanel) { this.facturaPanel = facturaPanel; }

    public ProductoPanel getProductoPanel() { return productoPanel; }
    public void setProductoPanel(ProductoPanel productoPanel) { this.productoPanel = productoPanel; }

    public LineaFacturaPanel getLineaFacturaPanel() { return lineaFacturaPanel; }
    public void setLineaFacturaPanel(LineaFacturaPanel lineaFacturaPanel) { this.lineaFacturaPanel = lineaFacturaPanel; }

    public JButton getBtnAdd() { return btnAdd; }
    public void setBtnAdd(JButton btnAdd) { this.btnAdd = btnAdd; }

    public JButton getBtnDelete() { return btnDelete; }
    public void setBtnDelete(JButton btnDelete) { this.btnDelete = btnDelete; }

    public JButton getBtnRefresh() { return btnRefresh; }
    public void setBtnRefresh(JButton btnRefresh) { this.btnRefresh = btnRefresh; }

    public JButton getBtnModify() { return btnModify; }
    public void setBtnModify(JButton btnModify) { this.btnModify = btnModify; }

    public JPanel getPanelNorte() { return panelNorte; }
    public void setPanelNorte(JPanel panelNorte) { this.panelNorte = panelNorte; }

    public JPanel getPanelBotones() { return panelBotones; }
    public void setPanelBotones(JPanel botoneriaPanel) { this.panelBotones = botoneriaPanel; }


    // ===== MÉTODOS VISTA ===== \\
    private void establecerPredeterminado(){
        this.setTitle("Facturación - Ventana Principal");
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(800, 600);

        setLayout(new BorderLayout());
    }

    private void añadirPestañas(){
        panelTablas.add("Clientes", clientePanel);
        panelTablas.add("Facturas", facturaPanel);
        panelTablas.add("Productos", productoPanel);
        panelTablas.add("Lineas_factura", lineaFacturaPanel);
        add(panelTablas, BorderLayout.CENTER);
    }

    private void añadirBotones(){
        panelNorte.setLayout(new BorderLayout());
        panelBotones.setLayout(new FlowLayout());

        panelBotones.add(btnAdd);
        panelBotones.add(btnDelete);
        panelBotones.add(btnModify);
        panelBotones.add(btnRefresh);

        panelNorte.add(panelBotones, BorderLayout.NORTH);
        add(panelNorte, BorderLayout.NORTH);
    }
}
