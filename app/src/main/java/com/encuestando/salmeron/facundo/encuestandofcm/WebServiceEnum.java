package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 17/7/2018.
 */

public enum WebServiceEnum {
    LOGIN_USER(0, "Login Usuario"),
    REGISTER_USER(1, "Registro de Usuario"),
    CARGAR_INFO(2, "Cargar Info"),
    ELIMINAR_INFO(3, "Eliminar Info"),
    CREAR_INFO(4, "Crear Info");

    private Integer codigo;
    private String descripcion;

    private WebServiceEnum(Integer codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Retorna un enum a partir del codigo
     * @param codigo
     * @return
     */
    public static WebServiceEnum getEnum(Integer codigo){
        if(codigo!=null){
            for(WebServiceEnum en: WebServiceEnum.values()){
                if(codigo.equals(en.getCodigo())){
                    return en;
                }
            }
        }
        return null;
    }

}
