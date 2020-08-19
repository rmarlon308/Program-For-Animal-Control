/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sevas
 */
public class AnimalBaja {
    private final String animalId;
    private final String fechaNacimiento;
    private final String numeroPartos;
    private final String tipo;
    private final String fechaBaja;
    private final String lugar;
    private final String causa;
    private final String precio;
    
    public AnimalBaja(String animalId,String fechaNacimiento,String numeroPartos,String tipo,String fechaBaja,String lugar,String causa,String precio) throws ParseException
    {
        this.animalId = animalId;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroPartos = numeroPartos;
        this.tipo = tipo;
        this.fechaBaja = fechaBaja;
        this.lugar = lugar;
        this.causa = causa;
        this.precio = precio;
    }
    
    public String getAnimalId(){
        return animalId;
    }
    public String getFechaNacimiento(){
        return fechaNacimiento;
    }
    public String getNumeroPartos(){
        return numeroPartos;
    }
    public String getTipo(){
        return tipo;
    }
    public String getFechaBaja(){
        return fechaBaja;
    }
    public String getLugar(){
        return lugar;
    }
    public String getCausa(){
        return causa;
    }
    public String getPrecio(){
        return precio;
    }
}
