package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 17/7/2018.
 */

public enum WebServiceEnum {
    LOGIN_USER(0, "Login Usuario"),
    REGISTER_USER(1, "Registro de Usuario"),
    CARGAR_INFO(2, "Cargar Info"),
    ELIMINAR_INFO(3, "Eliminar Info"),
    CREAR_INFO(4, "Crear Info"),
    CREAR_ENCUESTA(5, "Crear Encuesta"),
    CREAR_PREGUNTA(6, "Crear Pregunta"),
    CREAR_RESPUESTA(7, "Crear Respuesta"),
    CARGAR_ENCUESTAS(8, "Cargar Encuestas"),
    ELIMINAR_ENCUESTA(9, "Eliminar Encuesta"),
    OPEN_ENCUESTA(10, "Abrir Encuesta"),
    ELIMINAR_PREGUNTA(11, "Eliminar Pregunta"),
    ELIMINAR_RESPUESTA(12, "Eliminar Respuesta"),
    MODIFICAR_PREGUNTA(13, "Modificar Pregunta"),
    MODIFICAR_RESPUESTA(14, "Modificar Respuesta"),
    MODIFICAR_ENCUESTA(15, "Modificar Encuesta"),
    CONTESTAR_PREGUNTA(16, "Contestar Pregunta"),
    INCREMENTAR_RESULTADO(17, "Incrementar Resultado"),
    OPEN_ENCUESTA_VALIDAR(18, "Abrir Encuesta y Validar"),
    RESULTADOS_ENCUESTA(19, "Resultados Encuesta"),
    CARGAR_ENCUESTAS_HABILITADAS(20, "Cargar Encuestas Habilitadas"),
    HABILITAR_ENCUESTA(21, "Habilitar Encuesta"),
    INHABILITAR_ENCUESTA(22, "Inhabilitar Encuesta");

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
