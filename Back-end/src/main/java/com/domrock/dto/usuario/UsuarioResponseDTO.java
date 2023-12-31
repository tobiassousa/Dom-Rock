package com.domrock.dto.usuario;

import com.domrock.model.Usuario;

public record UsuarioResponseDTO(Long id, String nome, String email, String senha, String cpf, String contato, String acesso) {

    public UsuarioResponseDTO(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getCpf(), usuario.getContato(), usuario.getAcesso());
    }

}
