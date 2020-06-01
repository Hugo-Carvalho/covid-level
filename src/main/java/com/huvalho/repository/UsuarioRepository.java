package com.huvalho.repository;

import com.huvalho.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByTelefone(String telefone);
}
