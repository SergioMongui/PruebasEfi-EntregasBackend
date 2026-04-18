package com.Proyecto_Sena.Efi_Entregas.model;

import lombok.Data;

@Data
public class OrdenCargaDTO {
    private String nombreCliente;
    private String direccion;
    private String telefono;
    private String listaProductos;
    private Double valor;
}
