 
package org.vaadin.beans;


public class BeansArticulos {
   private String codigoSaturno; 
   private String Descripcion;
   private int stockMinimo;
   private int cantidadPedir;

    public String getCodigoSaturno() {
        return codigoSaturno;
    }

    public void setCodigoSaturno(String codigoSaturno) {
        this.codigoSaturno = codigoSaturno;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.Descripcion = Descripcion;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public int getCantidadPedir() {
        return cantidadPedir;
    }

    public void setCantidadPedir(int cantidadPedir) {
        this.cantidadPedir = cantidadPedir;
    }
}
