package view;

import models.Conductor;

import javax.swing.*;
import java.awt.*;

public class VistaDetallesView extends JFrame {

    // ===== ETIQUETAS ===== \\
    private JLabel nombre;
    private JLabel apellidos;
    private JLabel numeroConductor;
    private JLabel paginaActual;
    private JLabel imagenConductor;
    private JLabel lblNombre;
    private JLabel lblApellidos;
    private JLabel lblNumero;

    private Conductor conductor;


    // ===== BOTONES ===== \\
    private JButton botonSiguiente;
    private JButton botonAnterior;
    private JButton botonEditar;
    private JButton botonCargarImg;

    // ====== PANELES ===== \\
    private JPanel panelFoto;
    private JPanel panelDetalles;
    private JPanel panelSur;
    private JPanel panelPrincipal;
    private JPanel panelInfo;
    private JPanel panelBotones;
    private JPanel panelBotonEditar;


    // ===== CONSTRUCTOR ===== \\
    public VistaDetallesView() {}

    public VistaDetallesView(Conductor conductor) {

        detallesVentana();
        iniciarComponentes();

        cargarPanelSuperior();
        cargarPanelPrincipal();

        this.conductor = conductor;

        // ====== PANEL PRINCIPAL ====== \\
        panelPrincipal = new JPanel(new BorderLayout(20,20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelPrincipal.setBackground(Color.WHITE);


        imagenConductor.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        panelFoto = new JPanel(new BorderLayout());
        panelFoto.setBackground(Color.WHITE);
        panelFoto.add(imagenConductor,  BorderLayout.CENTER);
        panelFoto.add(botonCargarImg,  BorderLayout.SOUTH);


        panelPrincipal.add(panelFoto, BorderLayout.WEST);

        // ====== PANEL INFORMACIÓN ======
        panelInfo = new JPanel();
        panelInfo.setLayout(new GridLayout(3, 2, 15, 15));
        panelInfo.setBackground(Color.WHITE);

        lblNombre = new JLabel("Nombre:");

        nombre = new JLabel(conductor.getNombreConductor());

        lblApellidos = new JLabel("Apellidos:");

        apellidos = new JLabel(conductor.getApellidoConductor());

        lblNumero = new JLabel("Número:");

        numeroConductor = new JLabel(String.valueOf(conductor.getNumeroConductor()));

        panelInfo.add(lblNombre);
        panelInfo.add(nombre);
        panelInfo.add(lblApellidos);
        panelInfo.add(apellidos);
        panelInfo.add(lblNumero);
        panelInfo.add(numeroConductor);

        panelPrincipal.add(panelInfo, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);

        panelSur = new JPanel(new BorderLayout());

        panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(botonAnterior, BorderLayout.WEST);
        panelBotones.add(paginaActual, BorderLayout.CENTER);
        panelBotones.add(botonSiguiente, BorderLayout.EAST);

        panelSur.add(panelBotones, BorderLayout.CENTER);

        panelBotonEditar = new JPanel(new FlowLayout());
        panelBotonEditar.add(botonEditar);
        panelSur.add(panelBotonEditar, BorderLayout.EAST);

        add(panelSur, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarPanelPrincipal() {
    }

    private void cargarPanelSuperior() {

        // ====== TÍTULO ====== \\
        JLabel titulo = new JLabel("DETALLES DEL CONDUCTOR", SwingConstants.CENTER);
        titulo.setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
        add(titulo, BorderLayout.NORTH);
    }

    private void detallesVentana() {
        setTitle("Detalles del Conductor");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15,15));
        getContentPane().setBackground(new Color(245, 245, 245));
    }

    // ===== GETTERS && SETTERS ===== \\

    public JButton getBotonCargarImg() {
        return botonCargarImg;
    }

    public void setBotonCargarImg(JButton botonCargarImg) {
        this.botonCargarImg = botonCargarImg;
    }

    public JLabel getNombre() {
        return nombre;
    }

    public void setNombre(JLabel nombre) {
        this.nombre = nombre;
    }

    public JLabel getApellidos() {
        return apellidos;
    }

    public void setApellidos(JLabel apellidos) {
        this.apellidos = apellidos;
    }

    public JLabel getNumeroConductor() {
        return numeroConductor;
    }

    public void setNumeroConductor(JLabel numeroConductor) {
        this.numeroConductor = numeroConductor;
    }

    public JLabel getImagenConductor() {
        return imagenConductor;
    }

    public void setImagenConductor(JLabel imagenConductor) {
        this.imagenConductor = imagenConductor;
    }

    public JButton getBotonSiguiente() {
        return botonSiguiente;
    }

    public void setBotonSiguiente(JButton botonSiguiente) {
        this.botonSiguiente = botonSiguiente;
    }

    public JLabel getPaginaActual() {
        return paginaActual;
    }

    public void setPaginaActual(JLabel paginaActual) {
        this.paginaActual = paginaActual;
    }

    public JButton getBotonAnterior() {
        return botonAnterior;
    }

    public void setBotonAnterior(JButton botonAnterior) {
        this.botonAnterior = botonAnterior;
    }

    public JButton getBotonEditar() {
        return botonEditar;
    }

    public void setBotonEditar(JButton botonEditar) {
        this.botonEditar = botonEditar;
    }

    public JPanel getPanelFoto() {
        return panelFoto;
    }

    public void setPanelFoto(JPanel panelFoto) {
        this.panelFoto = panelFoto;
    }

    public JPanel getPanelDetalles() {
        return panelDetalles;
    }

    public void setPanelDetalles(JPanel panelDetalles) {
        this.panelDetalles = panelDetalles;
    }

    public JPanel getPanelSur() {
        return panelSur;
    }

    public void setPanelSur(JPanel panelSur) {
        this.panelSur = panelSur;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setConductor(Conductor conductor) {
        this.conductor = conductor;
    }

    // ===== METODOS VISTADETALLESVIEW ===== \\

    private void iniciarComponentes() {
        paginaActual = new JLabel("Páginas");
        imagenConductor = new JLabel();


        //Botones
        botonSiguiente = new JButton("Siguiente");
        botonAnterior = new JButton("Anterior");
        botonEditar = new JButton("Editar");
        botonCargarImg = new JButton("Actualizar imagen");
    }
}