//objeto creado para traer la informacion y solo presentarla 
package com.Proyecto_Sena.Efi_Entregas.model;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PlanTrabajoDTO {

    private Long idPlanTrabajo;
    private String estado;
    private String nombre;
    private String telefono;
    private String email;
    private LocalDateTime fecha;

    
}


