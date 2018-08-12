package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 17/7/2018.
 */

public enum ErrorRegisterEnum {
    ERROR_CODIGO_VALIDACION(1, "Error Codigo Validacion"),
    ERROR_NOMBRE_REPETIDO(2, "Error Nombre Usuario"),
    ERROR_NOMBRE_MIN(3, "Error minimo Nombre Usuario"),
    ERROR_NOMBRE_MAX(4, "Error maximo Nombre Usuario"),
    ERROR_PASSWORD_MIN(5, "Error password minimo"),
    ERROR_PASSWORD_MAX(6, "Error password maximo"),
    ERROR_NOMBRE_INVALIDO(7, "Error Nombre Usuario invalido"),
    ERROR_MAIL_REPETIDO(8, "Error Mail repetido");

    private Integer codigo;
    private String descripcion;

    private ErrorRegisterEnum(Integer codigo, String descripcion){
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
    public static ErrorRegisterEnum getEnum(Integer codigo){
        if(codigo!=null){
            for(ErrorRegisterEnum en: ErrorRegisterEnum.values()){
                if(codigo.equals(en.getCodigo())){
                    return en;
                }
            }
        }
        return null;
    }


}
