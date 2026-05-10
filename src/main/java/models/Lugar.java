package models;

public class Lugar {
    private int idLugar;
    private String codigoPostal;
    private String ciudad;
    private String lugar;

    // ===== CONSTRUCTORES ===== \\
    public Lugar(){}

    public Lugar(int idLugar, String codigoPostal, String ciudad, String lugar) {
        this.idLugar = idLugar;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.lugar = lugar;
    }

    // ===== GETTERS AND SETTERS ===== \\
    public int getIdLugar() {
        return idLugar;
    }

    public void setIdLugar(int idLugar) {
        this.idLugar = idLugar;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    // ===== TO STRING ===== \\
    @Override
    public String toString() {
        return "Lugar{" +
                "idLugar=" + idLugar +
                ", codigoPostal='" + codigoPostal + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", sitio='" + lugar + '\'' +
                '}';
    }

    // ===== METODOS LUGAR ===== \\
    /**
     * Este metodo muestra por pantalla toda la información de este objeto
     */
    public void mostrarInfo() {
        System.out.println("-".repeat(50) + "\n" +
                "ID Lugar: " + idLugar + "\n" +
                "Código Postal: " + codigoPostal + "\n" +
                "Ciudad: " + ciudad + "\n" +
                "Lugar: " + lugar + "\n" +
                "-".repeat(50));
    }
}
