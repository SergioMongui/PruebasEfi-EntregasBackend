package com.Proyecto_Sena.Efi_Entregas.controller;

import com.Proyecto_Sena.Efi_Entregas.model.LoginRequest;
import com.Proyecto_Sena.Efi_Entregas.model.Usuario;
import com.Proyecto_Sena.Efi_Entregas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

import java.util.Map;
import java.util.HashMap;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

//indica que esta clase es un controlador de spring y maneja solicitudes http
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    /*
     * Autowired permite tomar las instancias creadas en
     * UsuarioService y usarlas en UsuarioController de forma automatica,
     * brinda comunicasciones entre el http y la logica almacenada en service
     */

    @Autowired
    private UsuarioService usuarioService;

    // Mapea metodos a operaciones http, en este caso crear un usuario
    @PostMapping

    // atributo usuario y RequestBody indica que el parametro "Usuario usuario" se
    // obtenga del cuerpo de la solictud HTTP
    public Usuario create(@RequestBody @NonNull Usuario usuario) {
        return usuarioService.save(usuario);
    }

    // Tomar todos los usuarios
    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.getAll();
    }

    // Tomar un usuario por ID
    @GetMapping("/{id}")

    /*
     * PathVariable toma valores de la url en este caso id
     * que pasa a ser parametro del metodo, si no encuentra devuelve nulo
     */
    public ResponseEntity<Usuario> getById(@PathVariable @NonNull Long id) {
        return usuarioService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable @NonNull Long id, @RequestBody @NonNull Usuario usuario) {
        return usuarioService.getById(id)
                .map(existing -> {
                    usuario.setIdUsuario(id);
                    return ResponseEntity.ok(usuarioService.save(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NonNull Long id) {
        Optional<Usuario> usuario = usuarioService.getById(id);

        if (usuario.isPresent()) {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    // Nuevo endpoint para subir imagen de perfil
    @PutMapping("/{id}/imagen")
    public ResponseEntity<Map<String, String>> uploadImagen(@PathVariable Long id, @RequestParam("archivo") MultipartFile archivo) {
        try {
            String ruta = usuarioService.guardarImagenPerfil(id, archivo);
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Imagen subida correctamente");
            response.put("ruta", ruta);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al subir la imagen: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    //validacion de identidad inicio de sesion PROVISIONAL
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {
        List<Usuario> usuarios = usuarioService.getAll();

        for (Usuario u : usuarios) {
            if (u.getEmail().equals(loginRequest.getEmail()) &&
                    u.getContraseña().equals(loginRequest.getContraseña())) {
                return ResponseEntity.ok(u);
            }
        }

        return ResponseEntity.status(401).build();
    }

}
