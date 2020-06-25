package com.huvalho.service;

import com.huvalho.entity.Usuario;
import com.huvalho.object.UsuarioReq;
import com.huvalho.object.UsuarioRes;
import com.huvalho.repository.UsuarioRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario findByTelefone(String telefone) {
        return usuarioRepository.findByTelefone(telefone);
    }

    public Optional<Usuario> findById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public UsuarioRes infectadoPorContatosCidades(UsuarioReq usuarioReq) {
        UsuarioRes usuarioRes = new UsuarioRes();
        Usuario usuario = usuarioRepository.findByTelefone(usuarioReq.getTelefone());
        for (String contato : usuarioReq.getContatos()) {
            Usuario usuarioContato = usuarioRepository.findByTelefoneAndInfectado(contato, true);
            if (Objects.nonNull(usuarioContato)) {
                usuario.setInfectado(true);
                usuarioRes.setResult("alert");
                usuarioRes.setPeopleRisk(usuarioRes.getPeopleRisk() + 1);
            }
        }

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

                if (confirmadosDia > 50 || obitosDia > 25 || confirmadosTotal > 1000 || obitosTotal > 500) {
                    usuario.setInfectado(true);
                    usuarioRes.setResult("alert");
                    usuarioRes.setCases24hrs(usuarioRes.getCases24hrs() + confirmadosDia);
                    usuarioRes.setTotalCases(usuarioRes.getTotalCases() + confirmadosTotal);
                }

            } catch (NumberFormatException e) {
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        browser.close();

        if (usuarioReq.getInfectado()) {
            usuario.setInfectado(true);
            usuarioRes.setResult("infected");
        }
        
        usuarioRepository.save(usuario);
        return usuarioRes;
    }
}
