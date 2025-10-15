/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.libronova.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Coder
 */
public class Config {
    
    private final Properties props = new Properties();

    public Config() {
        try (InputStream in = getClass().getResourceAsStream("/config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration", e);
        }
       
    }
    

    public String get(String key) { return props.getProperty(key); }
}





