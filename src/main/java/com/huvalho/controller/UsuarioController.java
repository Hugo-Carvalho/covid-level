package com.huvalho.controller;

import com.huvalho.entity.Usuario;
import com.huvalho.object.UsuarioReq;
import com.huvalho.service.UsuarioService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public Usuario infectadoPorContatosCidades(@RequestBody UsuarioReq usuarioReq) {
        Usuario usuario = usuarioService.findByTelefone(usuarioReq.getTelefone());
        for (String contato : usuarioReq.getContatos()) {
            Usuario usuarioContato = usuarioService.findByTelefone(contato);
            if (Objects.nonNull(usuarioContato)) {
                if (usuarioContato.getInfectado()) {
                    usuario.setInfectado(true);
                    break;
                }
            }
        }

        if (!usuario.getInfectado()) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            WebDriver browser = new ChromeDriver(options);

            for (String cidadeUf : usuarioReq.getCidades()) {
                String url = "https://brasil.io/dataset/covid19/caso_full/?search=&epidemiological_week=&date=&order_for_place=&state=" + cidadeUf.split("-")[1] + "&city=" + cidadeUf.split("-")[0].replaceAll(" ", "+") + "&city_ibge_code=&place_type=&last_available_date=&is_last=True&is_repeated=";
                browser.get(url);

                int confirmadosDia, confirmadosTotal, obitosDia, obitosTotal;
                try {
                    Thread.sleep(1000);
                    confirmadosDia = Integer.parseInt(browser.findElement(By.xpath("//tr[@class='odd']/td[10]")).getText());
                    confirmadosTotal = Integer.parseInt(browser.findElement(By.xpath("//tr[@class='odd']/td[8]")).getText());
                    obitosDia = Integer.parseInt(browser.findElement(By.xpath("//tr[@class='odd']/td[12]")).getText());
                    obitosTotal = Integer.parseInt(browser.findElement(By.xpath("//tr[@class='odd']/td[11]")).getText());

                    if(confirmadosDia > 50 || obitosDia > 25 || confirmadosTotal > 1000 || obitosTotal > 500) {
                        usuario.setInfectado(true);
                        break;
                    }

                } catch (NumberFormatException e) {} catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            browser.close();
        }

        return usuarioService.save(usuario);
    }


    @PatchMapping("{id}")
    public void updateUsuario(@RequestBody Usuario usuario, @PathVariable Integer id) {
        usuarioService.findById(id).map(u -> {
            usuario.setId(u.getId());
            return usuarioService.save(usuario);
        });
    }
}
