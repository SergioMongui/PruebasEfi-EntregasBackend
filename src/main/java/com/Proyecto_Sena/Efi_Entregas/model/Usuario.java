package com.Proyecto_Sena.Efi_Entregas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

//--Creacion de la clase usuario--//

//Utlizaremos anotaciones, que permiten utilizar metadatos

/*Data: 
 * genera metodos set, get y toString para todos los campos. 
 * Además, inicializa todos los campos.
*/
@Data

//Entity: permite identificarlo como una tabla en la BD. como una entidad JPA.
@Entity
public class Usuario {

    //Id: marca al atributo "idUsuario" como llave primaria
    @Id
    //GeneratedValue: facilita que el valor que se crea sea automatico y autoincremental
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    //Column: define las propiedades que debe tener la columna
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String contraseña;

    @Column(nullable = false)
    private String rol;

    @OneToMany(mappedBy = "usuario")
    private List<ConexAsignacion> asignaciones; 

    @Column(nullable = true)
    private String imagenPerfil;

}
