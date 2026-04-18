package com.Proyecto_Sena.Efi_Entregas.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorCargaDTO {
    private int fila;
    private String campo;
    private String error;
}
