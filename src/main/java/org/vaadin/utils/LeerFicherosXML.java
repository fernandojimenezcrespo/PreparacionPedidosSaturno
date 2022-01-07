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
import org.vaadin.beans.Articulos;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ferna
 */
public class LeerFicherosXML {

    final String DIRECTORIOXML = "C:\\Users\\ferna\\Documents\\Programacion\\Vaadin\\PreparacionPedidosSaturno\\src\\main\\java\\org\\vaadin\\ficherosXML\\";
   
    public List<Articulos> LeerXML(String ficheroXML) throws IOException {
        try {

            List<Articulos> articulosList = new ArrayList<Articulos>();
            ficheroXML = DIRECTORIOXML.trim() + ficheroXML.trim();
            File fileXML = new File(ficheroXML);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(fileXML);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("articulo");
            String referencia = null;
            String codigo = null;
            String descripcion = null;
            String stock_minimo = null;
            int minimo = 0;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Articulos articulo = new Articulos();
                    Element eElement = (Element) nNode;
                    referencia = eElement.getElementsByTagName("referencia_hnss").item(0).getTextContent();
                    codigo = eElement.getElementsByTagName("codigo").item(0).getTextContent();
                    descripcion = eElement.getElementsByTagName("descripcion").item(0).getTextContent();
                    stock_minimo = eElement.getElementsByTagName("stock_minimo").item(0).getTextContent();
                    minimo = Integer.parseInt(stock_minimo);
                    articulo.setReferencia_hnss(referencia);
                    articulo.setCodigoSaturno(codigo);
                    articulo.setDescripcion(descripcion);
                    articulo.setStockMinimo(minimo);
                    articulo.setCantidadPedir(1);
                    articulosList.add(temp, articulo);
                    // Recuerda que si  no creas el objeto  articulo cada iteración siempre te graba lo mismo.

                    System.out.println("Codigo:" + codigo + "\n");
                    System.out.println("Descrip " + eElement.getElementsByTagName("descripcion").item(0).getTextContent());
                }
            }

            return articulosList;
        } catch (Exception e) {
        }
        return null;
    }

}
