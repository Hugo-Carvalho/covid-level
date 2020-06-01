package com.huvalho.controller;

import com.huvalho.entity.Usuario;
import com.huvalho.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/covidlevel/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("{telefone}")
    public Usuario getUsuario(@PathVariable String telefone){
        return usuarioService.findByTelefone(telefone);
    }

    @PostMapping
    public Usuario saveUsuario(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @GetMapping("infectado-por-contatos/{telefone}/{contatos}")
    public Usuario infectadoPorContatos(@PathVariable String telefone, @PathVariable String[] contatos) {
        Usuario usuario = usuarioService.findByTelefone(telefone);
        for(String contato : contatos){
            Usuario usuarioContato = usuarioService.findByTelefone(contato);
            if(Objects.nonNull(usuarioContato)) {
                if(usuarioContato.getInfectado()){
                    usuario.setInfectado(true);
                    break;
                }
            }
        }

        return usuario;
    }



    @PatchMapping("{id}")
    public void updateUsuario(@RequestBody Usuario usuario, @PathVariable Integer id) {
        usuarioService.findById(id).map(u -> {
            usuario.setId(u.getId());
            return usuarioService.save(usuario);
        });
    }
}
