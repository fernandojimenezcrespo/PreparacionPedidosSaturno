package org.vaadin.example;

import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.vaadin.pdf.GenerarPdf;

@Service
public class GreetService implements Serializable {

    public String greet(String name) throws FileNotFoundException {
        if (name == null || name.isEmpty()) {
            return "En busca de generar....PDF";
        } else {
            return "Hello " + name;
        }
    }
}
