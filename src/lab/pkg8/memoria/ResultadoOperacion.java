/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg8.memoria;

/**
 *
 * @author diego
 */
class ResultadoOperacion {
    private boolean exito;
    private String mensaje;
    private int elementosProcesados;
    private int errores;

    public ResultadoOperacion(boolean exito, String mensaje, int elementosProcesados, int errores) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.elementosProcesados = elementosProcesados;
        this.errores = errores;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getElementosProcesados() {
        return elementosProcesados;
    }

    public void setElementosProcesados(int elementosProcesados) {
        this.elementosProcesados = elementosProcesados;
    }

    public int getErrores() {
        return errores;
    }

    public void setErrores(int errores) {
        this.errores = errores;
    }

    @Override
    public String toString() {
        return  mensaje;
    }
    
    
}
