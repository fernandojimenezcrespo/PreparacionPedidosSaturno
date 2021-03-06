package org.vaadin.example;

import com.itextpdf.text.DocumentException;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.Registration;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.beans.Articulos;
import org.vaadin.beans.Ficheros;
import org.vaadin.fechas.Hoy;
import org.vaadin.pdf.GenerarPdf;
import org.vaadin.utils.LeerFicherosXML;
import org.vaadin.utils.LeerServiciosSeccionesXML;

@Route
@PWA(name = "Vaadin Application",
        shortName = "PedidosSaturno",
        description = "This is my first example fernando's  Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends HorizontalLayout {

    boolean hayTipo = false;
    boolean hayServicio = false;
    String ficheroSeleccionado = "";
    Articulos articulo = new Articulos();
    List<Articulos> articulosList = new ArrayList<Articulos>();
    Ficheros ficheros = new Ficheros();
    List<Ficheros> ficherosList = new ArrayList<Ficheros>();
    LeerServiciosSeccionesXML leerServiciosSeccionesXML = new LeerServiciosSeccionesXML();
    Grid<Articulos> gridArticulos = new Grid<>(Articulos.class);
    ComboBox<String> cmbTipoPedido = new ComboBox("Tipo de Pedido");
    ComboBox<Ficheros> cmbServicioSeccion = new ComboBox<>("Servicio/Seccion");
    

    public MainView(@Autowired GreetService service) {
        try {
            ficheroSeleccionado = "";
            ficherosList = leerServiciosSeccionesXML.LeerXML();
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        VerticalLayout vertical_izq = new VerticalLayout();
        VerticalLayout vertical_dcha = new VerticalLayout();
        vertical_dcha.setWidth("80%");
        vertical_izq.setWidth("20%");
        TextField textCampoFicheroXML = new TextField("Fichero XML");
        textCampoFicheroXML.addThemeName("bordered");

        textCampoFicheroXML.setEnabled(false);
        Hoy hoy = new Hoy();
        TextField textFecha = new TextField("Hoy");
        textFecha.addThemeName("bordered");
        textFecha.setEnabled(false);
        textFecha.setValue(hoy.Hoy());

        Locale finnishLocale = new Locale("fi", "FI");

        cmbServicioSeccion.setItemLabelGenerator(Ficheros::getDescripcion);
        cmbServicioSeccion.setItems(ficherosList);

        cmbTipoPedido.setId("idTipoPedido");
        //.setLabel("Tipo de Pedido");
        cmbTipoPedido.setItems("Almacen", "Compra Directa");

        cmbTipoPedido.addValueChangeListener(event
                -> {

            hayTipo = actulizaTipoPedido(event.getValue());
            if (hayTipo && hayServicio) {
                gridArticulos.setVisible(true);
                articulosList = cargaArticulos(articulo, articulosList, ficheroSeleccionado);
                gridArticulos.setItems(articulosList);
            }

        }
        );
        cmbServicioSeccion.addValueChangeListener(event
                -> {
            textCampoFicheroXML.setValue(event.getValue().getFichero());
            hayServicio = actulizaServicioSeccion(event.getValue().getDescripcion());
            ficheroSeleccionado = event.getValue().getFichero();
            if (hayTipo && hayServicio) {
                gridArticulos.setVisible(true);
                articulosList = cargaArticulos(articulo, articulosList, ficheroSeleccionado);
                gridArticulos.setItems(articulosList);
            }
        }
        );
        if (hayTipo && hayServicio) {
            articulosList = cargaArticulos(articulo, articulosList, ficheroSeleccionado);
        }

        gridArticulos.setVisible(false);

        //grid.setWidthFull();
        //grid.setSizeFull();
        gridArticulos.addThemeVariants(GridVariant.LUMO_NO_BORDER);//No pone bordes verticales.
        gridArticulos.addThemeVariants(GridVariant.MATERIAL_COLUMN_DIVIDERS);

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello",
                e -> {
            try {
                if (!articulosList.isEmpty())
                {   try {
                    GenerarPdf generarPdf = new GenerarPdf(articulosList);
                    } catch (DocumentException ex) {
                        Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
                    }
}
                
                Notification.show(service.greet(textFecha.getValue()));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        vertical_izq.add(textFecha, textCampoFicheroXML, button);
        vertical_dcha.add(cmbTipoPedido, cmbServicioSeccion, gridArticulos);
        add(vertical_izq, vertical_dcha);
    }

    private List<Articulos> cargaArticulos(Articulos articulo, List<Articulos> articulosList, String ficheroSelecionado) {
        //Articulos articulo=new Articulos();
        try {
            LeerFicherosXML leerFicherosXML = new LeerFicherosXML();
            articulosList = leerFicherosXML.LeerXML(ficheroSelecionado);
            /*articulo.setCodigoSaturno("000001");
            articulo.setDescripcion("Articulo000001");
            articulo.setStockMinimo(10);
            articulo.setCantidadPedir(1);
            articulosList.add(articulo);*/
            return articulosList;
        } catch (Exception e) {
        }
        return null;

    }

    private boolean actulizaTipoPedido(String value) {
        if (!value.isEmpty() && !value.trim().equals("")) {


            return true;
        } else {
            return false;
        }
    }

    private boolean actulizaServicioSeccion(String value) {
        if (!value.isEmpty() && !value.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
