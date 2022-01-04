package org.vaadin.example;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.beans.Articulos;

@Route
@PWA(name = "Vaadin Application",
        shortName = "Vaadin App",
        description = "This is an example Vaadin application.",
        enableInstallPrompt = false)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends HorizontalLayout {

    public MainView(@Autowired GreetService service) {

        // Use TextField for standard text input
        VerticalLayout vertical_izq = new VerticalLayout();
        VerticalLayout vertical_dcha = new VerticalLayout();
        TextField textCampoNombre = new TextField("Your name");
        ComboBox<String> cmbTipoPedido = new ComboBox();
        ComboBox<String> cmbServicioSeccion = new ComboBox();
        textCampoNombre.addThemeName("bordered");
        cmbTipoPedido.setId("idTipoPedido");
        cmbTipoPedido.setLabel("Tipo de Pedido");
        cmbTipoPedido.setItems("Almacen","Compra Directa");
        cmbServicioSeccion.setId("idServicioSeccion");
        cmbServicioSeccion.setLabel("Servicio/Seccion");
        cmbServicioSeccion.setItems("Lab./Hema","Lab./Urg.","Lab./Micro");
        Articulos articulo=new Articulos();
        List<Articulos> articulosList = new ArrayList<Articulos>();
        articulosList=cargaArticulos(articulo, articulosList);
        Grid<Articulos> grid = new Grid<>(Articulos.class);
        grid.setItems(articulosList);
        
         
        
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
        vertical_izq.add(textCampoNombre, button);
        vertical_dcha.add(cmbTipoPedido,cmbServicioSeccion,grid);
        add(vertical_izq, vertical_dcha);
    }

    private List<Articulos> cargaArticulos(Articulos articulo,List<Articulos> articulosList){
        //Articulos articulo=new Articulos();
        
        articulo.setCodigoSaturno("000001");
        articulo.setDescripcion("Articulo000001");
        articulo.setStockMinimo(10);
        articulo.setCantidadPedir(0);
        articulosList.add(articulo);
        return articulosList;
        
    }
}
