package com.huvalho.controller;

import com.huvalho.entity.Usuario;
import com.huvalho.object.UsuarioReq;
import com.huvalho.object.UsuarioRes;
import com.huvalho.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/covidlevel/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("{telefone}")
    public Usuario getUsuario(@PathVariable String telefone) {
        return usuarioService.findByTelefone(telefone);
    }

    @PostMapping
    public Usuario saveUsuario(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @PostMapping("infectado-por-contatos-cidades")
    public UsuarioRes infectadoPorContatosCidades(@RequestBody UsuarioReq usuarioReq) {
        return usuarioService.infectadoPorContatosCidades(usuarioReq);
    }


    @PatchMapping("{id}")
    public void updateUsuario(@RequestBody Usuario usuario, @PathVariable Integer id) {
        usuarioService.findById(id).map(u -> {
            usuario.setId(u.getId());
            return usuarioService.save(usuario);
        });
    }
}
