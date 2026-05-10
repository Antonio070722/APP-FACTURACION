package controller.dao;

import models.BCL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BCLDAO extends DAO{
    /**
     * Obtiene todos los BCL de la BBDD y los devuelve
     * @return La lista con todos los lugares
     */
    public static ArrayList<BCL> getBCL(){
        String consulta = "SELECT * FROM BDP";
        ArrayList<BCL> bcls = new ArrayList<>();
        try{
            PreparedStatement ps = con.prepareStatement(consulta);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                BCL bcl = new BCL();
                bcl.setIdBus(rs.getString(1));
                bcl.setNumeroConductor(rs.getInt(2));
                bcl.setIdLugar(rs.getInt(3));
                bcl.setDiaSemana(rs.getString(4));
                bcls.add(bcl);
            }
            return bcls;
        }catch (SQLException e){
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return null;
    }

    /**
     * Introduce un nuevo BCL en la BBDD
     * @param bcl El lugar que se introduce
     * @return El número de registros alterados
     */
    public static int insertarBCL(BCL bcl){
        String consulta = "INSERT INTO BDP (register, numdriver, idplace, day_of_week) VALUES (?, ?, ?, ?)";
        try{
            PreparedStatement ps = con.prepareStatement(consulta);
            ps.setString(1, bcl.getIdBus());
            ps.setInt(2, bcl.getNumeroConductor());
            ps.setInt(3, bcl.getIdLugar());
            ps.setString(4, bcl.getDiaSemana());
            return ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Edita los datos de un BCL ya existente
     * @param bcl El BCL que se edita
     * @return El número de lugares modificados
     */
    public static int editarBCL(BCL bcl){
        String consulta = "UPDATE BDP SET day_of_week = ? WHERE idplace = ? AND numdriver = ? AND register = ?";
        try {
            PreparedStatement ps = con.prepareStatement(consulta);
            ps.setString(1, bcl.getDiaSemana());
            ps.setInt(2, bcl.getIdLugar());
            ps.setInt(3, bcl.getNumeroConductor());
            ps.setString(4, bcl.getIdBus());
            return ps.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error con la base de datos: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Elimina un BCL de la BBDD
     * @param bcl El BCL que se elimina
     * @return El número de cambios en la BBDD
     */
    public static int borrarBCL(BCL bcl) throws SQLException{
        String consulta = "DELETE FROM BDP WHERE idplace = ? AND numdriver = ? AND register = ?";
        int numeroCambios = 0;

        try{
            con.setAutoCommit(false);

            try(PreparedStatement ps = con.prepareStatement(consulta)){
                ps.setInt(1, bcl.getIdLugar());
                ps.setInt(2, bcl.getNumeroConductor());
                ps.setString(3, bcl.getIdBus());
                numeroCambios += ps.executeUpdate();
            }

            con.commit();
        } catch (SQLException e){
            System.out.println("Error con la base de datos: " + e.getMessage());
            con.rollback();
        } finally {
            con.setAutoCommit(true);
        }
        return numeroCambios;
    }
}
