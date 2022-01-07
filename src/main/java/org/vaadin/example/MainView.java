package org.vaadin.example;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.Registration;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.beans.Articulos;
import org.vaadin.beans.Ficheros;
import org.vaadin.utils.LeerFicherosXML;
import org.vaadin.utils.LeerServiciosSeccionesXML;

@Route
@PWA(name = "Vaadin Application",
        shortName = "PedidosSaturno",
        description = "This is my first example fernando's  Vaadin application.",
        enableInstallPrompt = false)
//@CssImport("./styles/shared-styles.css")
//@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends HorizontalLayout {

    boolean hayTipo = false;
    boolean hayServicio = false;
    Articulos articulo = new Articulos();
    List<Articulos> articulosList = new ArrayList<Articulos>();
    Ficheros ficheros = new Ficheros();
    List<Ficheros> ficherosList= new ArrayList<Ficheros>();
    LeerServiciosSeccionesXML leerServiciosSeccionesXML= new LeerServiciosSeccionesXML();
    Grid<Articulos> gridArticulos = new Grid<>(Articulos.class);
    

    public MainView(@Autowired GreetService service) {
        try {
            ficherosList=leerServiciosSeccionesXML.LeerXML();
        } catch (IOException ex) {
            Logger.getLogger(MainView.class.getName()).log(Level.SEVERE, null, ex);
        }
        VerticalLayout vertical_izq = new VerticalLayout();
        VerticalLayout vertical_dcha = new VerticalLayout();
        vertical_dcha.setWidth("80%");
        vertical_izq.setWidth("20%");
        TextField textCampoNombre = new TextField("Your name");
        ComboBox<String> cmbTipoPedido = new ComboBox();
        ComboBox<String> cmbServicioSeccion = new ComboBox();
        textCampoNombre.addThemeName("bordered");
        cmbTipoPedido.setId("idTipoPedido");
        cmbTipoPedido.setLabel("Tipo de Pedido");
        cmbTipoPedido.setItems("Almacen", "Compra Directa");
        cmbServicioSeccion.setId("idServicioSeccion");
        cmbServicioSeccion.setLabel("Servicio/Seccion");
        cmbServicioSeccion.setItems("Lab./Hema", "Lab./Urg.", "Lab./Micro");

        Span spanAclaracionTipo = new Span("-");
        Span spanAclaracionServicioSeccion = new Span("-");
        /*cmbTipoPedido.addValueChangeListener(event
                -> spanAclaracionTipo.setText("El tipo es: " + event.getValue())
                );*/
 /*cmbServicioSeccion.addValueChangeListener(event
                -> spanAclaracionServicioSeccion.setText("El Servicio es: " + event.getValue()));*/
        cmbTipoPedido.addValueChangeListener(event
                -> {
            spanAclaracionTipo.setText("El tipo es: " + event.getValue());
            hayTipo = actulizaTipoPedido(event.getValue());
            if (hayTipo && hayServicio) {
                gridArticulos.setVisible(true);
                articulosList = cargaArticulos(articulo, articulosList);
                gridArticulos.setItems(articulosList);
            }

        }
        );
        cmbServicioSeccion.addValueChangeListener(event
                -> {
            spanAclaracionServicioSeccion.setText("El tipo es: " + event.getValue());
            hayServicio = actulizaServicioSeccion(event.getValue());
            if (hayTipo && hayServicio) {
                gridArticulos.setVisible(true);
                articulosList = cargaArticulos(articulo, articulosList);
                gridArticulos.setItems(articulosList);
            }
        }
        );
        if (hayTipo && hayServicio) {
            articulosList = cargaArticulos(articulo, articulosList);
        }

        gridArticulos.setVisible(false);

        //grid.setWidthFull();
        //grid.setSizeFull();
        gridArticulos.addThemeVariants(GridVariant.LUMO_NO_BORDER);//No pone bordes verticales.
        

        textCampoNombre.setId("idCampoNombre");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Say hello",
                e -> Notification.show(service.greet(textCampoNombre.getValue())));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button has a more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");
        vertical_izq.add(textCampoNombre, button, spanAclaracionTipo, spanAclaracionServicioSeccion);
        vertical_dcha.add(cmbTipoPedido, cmbServicioSeccion, gridArticulos);
        add(vertical_izq, vertical_dcha);
    }

    private List<Articulos> cargaArticulos(Articulos articulo, List<Articulos> articulosList) {
        //Articulos articulo=new Articulos();
        try {
            LeerFicherosXML leerFicherosXML = new LeerFicherosXML();
            articulosList = leerFicherosXML.LeerXML("LAB_RUTINA.xml");
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
