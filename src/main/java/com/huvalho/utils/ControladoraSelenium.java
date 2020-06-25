package com.huvalho.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ControladoraSelenium {

    private static ControladoraSelenium instanciaini = new ControladoraSelenium();

    private WebDriver webSection;

    public ControladoraSelenium() {
        webSection = getNewSecao();
    }

    public WebDriver getNewSecao() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        return new ChromeDriver(options);
    }

    public static ControladoraSelenium getInstancia() {
        return instanciaini;
    }

    public synchronized WebDriver getWebSection() {
        return webSection;
    }

    public void setWebSection(WebDriver webSection) {
        this.webSection = webSection;
    }
}

