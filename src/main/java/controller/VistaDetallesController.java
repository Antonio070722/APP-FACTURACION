package controller;

import controller.dao.ConductorDAO;
import models.Conductor;
import view.VistaDetallesView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class VistaDetallesController {

    // ==================== ATRIBUTOS ====================

    /** Vista asociada a este controlador */
    private final VistaDetallesView vistaDetallesView;

    /** DAO para acceder y modificar los datos de conductores en la BBDD */
    private final ConductorDAO conductorDAO = new ConductorDAO();

    /** Lista completa de conductores cargados desde la BBDD (para la navegación) */
    private ArrayList<Conductor> conductores;

    /** Conductor que se está visualizando/editando en este momento */
    private Conductor conductor;

    // ==================== CONSTRUCTOR ====================

    /**
     * Crea el controlador, asocia la vista, carga los datos y registra los listeners
     * de todos los botones de la vista
     * @param vistaDetallesView La vista de detalles del conductor
     */
    public VistaDetallesController(VistaDetallesView vistaDetallesView) {
        this.vistaDetallesView = vistaDetallesView;
        this.conductores       = conductorDAO.getConductores();
        this.conductor         = vistaDetallesView.getConductor(); // Este conductor no tiene imagen personalizada

        this.conductor = conductores.get(getPosicionConductorArrayList()); // Aqui se carga el conductor junto con su imagen personalizada, por eso se sobreescribe

        // Cargar la imagen del conductor al iniciar la vista
        cargarImagen();

        // Registrar listeners de los botones de navegación
        vistaDetallesView.getBotonSiguiente().addActionListener(e -> cambiarSiguienteConductor());
        vistaDetallesView.getBotonAnterior() .addActionListener(e -> cambiarAnteriorConductor());

        // Registrar listener del botón de actualizar imagen
        vistaDetallesView.getBotonCargarImg().addActionListener(e -> importarImagen());

        // Registrar listener del botón de editar conductor
        vistaDetallesView.getBotonEditar()   .addActionListener(e -> editarConductor());
    }

    // ==================== EDICIÓN DE CONDUCTOR ====================

    /**
     * Muestra un diálogo para editar el nombre y apellido del conductor actual
     * El número de conductor no se puede modificar por ser clave primaria
     * Si el usuario confirma los cambios, estos se persisten en la BBDD mediante
     * el metodo editarConductor y la vista se actualiza
     * inmediatamente
     */
    private void editarConductor() {

        // ---- Construir el formulario de edición ----
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));

        // El número no es editable (es la PK)
        JLabel lblNumero     = new JLabel(String.valueOf(conductor.getNumeroConductor()));
        JTextField txtNombre   = new JTextField(conductor.getNombreConductor(), 15);
        JTextField txtApellido = new JTextField(conductor.getApellidoConductor(), 15);

        formulario.add(new JLabel("Número:"));
        formulario.add(lblNumero);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Apellido:"));
        formulario.add(txtApellido);

        panel.add(formulario, BorderLayout.CENTER);

        // ---- Mostrar el diálogo de confirmación ----
        int opcion = JOptionPane.showConfirmDialog(
                vistaDetallesView,
                panel,
                "Editar Conductor",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return; // El usuario canceló, no se hace nada
        }

        // ---- Validar los datos introducidos ----
        String nuevoNombre   = txtNombre.getText().trim();
        String nuevoApellido = txtApellido.getText().trim();

        if (nuevoNombre.isEmpty()) {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "El nombre no puede estar vacío.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nuevoApellido.isEmpty()) {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "El apellido no puede estar vacío.",
                    "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ---- Persistir los cambios en la BBDD ----
        conductor.setNombreConductor(nuevoNombre);
        conductor.setApellidoConductor(nuevoApellido);

        int resultado = conductorDAO.editarConductor(conductor);

        if (resultado > 0) {
            // Actualizar la vista con los nuevos datos
            cargarDatosConductor(conductor);

            // Actualizar también el objeto en la lista local para mantener la coherencia
            conductores.set(getPosicionConductorArrayList(), conductor);

            JOptionPane.showMessageDialog(vistaDetallesView,
                    "Conductor modificado correctamente.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "No se pudo modificar el conductor. Comprueba la conexión con la BBDD.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== GESTIÓN DE IMAGEN ====================

    /**
     * Abre un selector de ficheros para que el usuario elija una imagen,
     * la copia al directorio de recursos y guarda
     * el nombre del fichero en la BBDD
     */
    private void importarImagen() {

        // ---- Abrir el selector de ficheros filtrando por imágenes ----
        JFileChooser selectorFichero = new JFileChooser();
        selectorFichero.setDialogTitle("Seleccionar imagen del conductor");
        selectorFichero.setFileFilter(new FileNameExtensionFilter(
                "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        int resultado = selectorFichero.showOpenDialog(vistaDetallesView);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            return; // El usuario canceló la selección
        }

        File ficheroSeleccionado = selectorFichero.getSelectedFile();

        // ---- Obtener la URL del directorio de imágenes en el classpath ----
        URL urlDirectorioImagenes = getClass().getResource("/images/");

        if (urlDirectorioImagenes == null) {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "No se encontró el directorio de imágenes en los recursos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ---- Construir el nombre y la ruta de destino ----
        String extension     = obtenerExtension(ficheroSeleccionado.getName());
        String nombreFichero = "driver" + conductor.getNumeroConductor() + "_custom." + extension;

        try {
            File directorioDestino = new File(urlDirectorioImagenes.toURI());
            File ficheroDestino    = new File(directorioDestino, nombreFichero);

            // Copiar el fichero al directorio de recursos (sobreescribe si ya existe)
            Files.copy(
                    ficheroSeleccionado.toPath(),
                    ficheroDestino.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            // ---- Guardar el nombre del fichero en la BBDD ----
            conductor.setImagen(nombreFichero);
            int filaActualizada = conductorDAO.actualizarImagen(conductor);

            if (filaActualizada > 0) {
                // Actualizar también el objeto de la lista local
                conductores.set(getPosicionConductorArrayList(), conductor);

                // Refrescar la imagen en la vista
                cargarImagen();

                JOptionPane.showMessageDialog(vistaDetallesView,
                        "Imagen actualizada correctamente.",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Si la BBDD falló, revertir el campo en memoria para no quedar en estado inconsistente
                conductor.setImagen(null);
                JOptionPane.showMessageDialog(vistaDetallesView,
                        "La imagen se copió pero no se pudo guardar en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (URISyntaxException e) {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "Error al resolver la ruta del directorio de imágenes: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "Error al copiar el fichero de imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga y muestra la imagen del conductor actual en la vista
     */
    private void cargarImagen() {
        String imagenBBDD = conductor.getImagen();

        if (imagenBBDD != null && !imagenBBDD.isBlank()) {
            // El conductor tiene imagen personalizada registrada en la BBDD
            cargarImagenDesdeRecurso("/images/" + imagenBBDD);
        } else {
            // Sin imagen personalizada: usar la foto identificativa por número
            cargarImagenDesdeRecurso("/images/driver" + conductor.getNumeroConductor() + ".jpg");
        }
    }

    /**
     * Intenta cargar una imagen desde el classpath y la muestra en la vista
     * @param ruta Ruta relativa al classpath del fichero de imagen
     */
    private void cargarImagenDesdeRecurso(String ruta) {
        URL urlImagen = getClass().getResource(ruta);

        if (urlImagen != null) {
            vistaDetallesView.getImagenConductor().setIcon(
                    new ImageIcon(
                            new ImageIcon(urlImagen)
                                    .getImage()
                                    .getScaledInstance(220, 220, Image.SCALE_SMOOTH)
                    )
            );
        } else {
            // El recurso no existe, usar imagen por defecto
            cargarImagenDefault();
        }
    }

    /**
     * Carga y muestra la imagen por defecto en la vista
     */
    private void cargarImagenDefault() {
        vistaDetallesView.getImagenConductor().setIcon(
                new ImageIcon(
                        new ImageIcon(getClass().getResource("/images/default.jpeg"))
                                .getImage()
                                .getScaledInstance(220, 220, Image.SCALE_SMOOTH)
                )
        );
    }

    // ==================== NAVEGACIÓN ENTRE CONDUCTORES ====================

    /**
     * Avanza al conductor siguiente en la lista y actualiza la vista
     */
    private void cambiarSiguienteConductor() {
        int posicion = getPosicionConductorArrayList();

        if (posicion == conductores.size() - 1) {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "No hay más conductores.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            cargarDatosConductor(conductores.get(posicion + 1));
            cargarImagen();
        }
    }

    /**
     * Retrocede al conductor anterior en la lista y actualiza la vista
     */
    private void cambiarAnteriorConductor() {
        int posicion = getPosicionConductorArrayList();

        if (posicion == 0) {
            JOptionPane.showMessageDialog(vistaDetallesView,
                    "No hay más conductores.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            cargarDatosConductor(conductores.get(posicion - 1));
            cargarImagen();
        }
    }

    // ==================== MÉTODOS PRIVADOS DE AYUDA ====================

    /**
     * Actualiza el conductor activo y refresca los campos de texto de la vista
     * con los datos del nuevo conductor
     *
     * @param conductor El conductor cuyos datos se deben mostrar en la vista
     */
    private void cargarDatosConductor(Conductor conductor) {
        this.conductor = conductor;
        vistaDetallesView.getNombre()         .setText(conductor.getNombreConductor());
        vistaDetallesView.getApellidos()       .setText(conductor.getApellidoConductor());
        vistaDetallesView.getNumeroConductor() .setText(Integer.toString(conductor.getNumeroConductor()));
    }

    /**
     * Devuelve el índice del conductor actual dentro del ArrayList de conductores
     * @return El índice (base 0) del conductor actual en la lista, o el tamaño
     *         de la lista si no se encontró (no debería ocurrir en condiciones normales)
     */
    private int getPosicionConductorArrayList() {
        int posicion = 0;

        for (Conductor c : conductores) {
            if (c.getNumeroConductor() == conductor.getNumeroConductor()) {
                break;
            }
            posicion++;
        }

        return posicion;
    }

    /**
     * Extrae la extensión de un nombre de fichero
     * @param nombreFichero El nombre completo del fichero
     * @return La extensión en minúsculas
     *         si no se encontró extensión
     */
    private String obtenerExtension(String nombreFichero) {
        int indicePunto = nombreFichero.lastIndexOf('.');
        return (indicePunto >= 0)
                ? nombreFichero.substring(indicePunto + 1).toLowerCase()
                : "jpg";
    }
}