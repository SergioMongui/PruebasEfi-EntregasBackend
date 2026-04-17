package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.Usuario;
import com.Proyecto_Sena.Efi_Entregas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> getById(@NonNull Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Usuario save(@NonNull Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
    
    public void delete(@NonNull Long id) {
        usuarioRepository.deleteById(id);
    }

    public String guardarImagenPerfil(Long id, MultipartFile archivo) throws IOException {
        if (archivo.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }

        // Validar que sea imagen (content-type básico)
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("El archivo debe ser una imagen");
        }

        // Crear carpeta si no existe
        Path uploadPath = Paths.get("uploads");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Obtener extensión original
        String originalFilename = archivo.getOriginalFilename();
        String extension = ".jpg";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Generar nombre único
        String nombreArchivo = "usuario_" + id + extension;
        Path filePath = uploadPath.resolve(nombreArchivo);

        // Guardar archivo
        Files.write(filePath, archivo.getBytes());

        // Ruta relativa
        String rutaRelativa = "/uploads/" + nombreArchivo;

        // Actualizar usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IOException("Usuario no encontrado"));
        usuario.setImagenPerfil(rutaRelativa);
        usuarioRepository.save(usuario);

        return rutaRelativa;
    }

}