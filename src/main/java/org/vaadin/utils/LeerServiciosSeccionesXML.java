/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.vaadin.beans.Ficheros;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ferna
 */
public class LeerServiciosSeccionesXML {

    //final String DIRECTORIOXML = "C:\\Users\\ferna\\Documents\\Programacion\\Vaadin\\PreparacionPedidosSaturno\\src\\main\\java\\org\\vaadin\\ficherosXML\\";
    final String DIRECTORIOXML="\\ProgramasHnss\\PreparacionPedidosSaturno\\ficherosXML\\";
    final String FICHEROXML = "FICHEROS.xml";

    public List<Ficheros> LeerXML() throws IOException {
        try {

            List<Ficheros> ficherosList = new ArrayList<Ficheros>();
            String ficheroXML = DIRECTORIOXML.trim() + FICHEROXML;
            File fileXML = new File(ficheroXML);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileXML);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("fichero");
            String servicio = null;
            String seccion = null;
            String descripcion = null;
            String nombreFichero = null;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Ficheros fichero = new Ficheros();
                    Element eElement = (Element) nNode;
                    servicio = eElement.getElementsByTagName("servicio").item(0).getTextContent();
                    seccion = eElement.getElementsByTagName("seccion").item(0).getTextContent();
                    descripcion = eElement.getElementsByTagName("descripcion").item(0).getTextContent();
                    nombreFichero = eElement.getElementsByTagName("nombre_fichero").item(0).getTextContent();
                    fichero.setServicio(servicio);
                    fichero.setSeccion(seccion);
                    fichero.setDescripcion(descripcion);
                    fichero.setFichero(nombreFichero);
                    ficherosList.add(temp, fichero);
                    // Recuerda que si  no creas el objeto  fichero cada iteración siempre te graba lo mismo.
                }
            }

            return ficherosList;
        } catch (Exception e) {
        }
        return null;
    }

}
