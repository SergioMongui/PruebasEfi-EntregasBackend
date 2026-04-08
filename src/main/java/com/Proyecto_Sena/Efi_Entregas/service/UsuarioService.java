package com.Proyecto_Sena.Efi_Entregas.service;

import com.Proyecto_Sena.Efi_Entregas.model.Usuario;
import com.Proyecto_Sena.Efi_Entregas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

}