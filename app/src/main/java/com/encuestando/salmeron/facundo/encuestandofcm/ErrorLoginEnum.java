package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 17/7/2018.
 */

public enum  ErrorLoginEnum {
    ERROR_USUARIO(1, "Error Usuario"),
    ERROR_PASSWORD(2, "Error Password");

    private Integer codigo;
    private String descripcion;

    private ErrorLoginEnum(Integer codigo,String descripcion){
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
    public static ErrorLoginEnum getEnum(Integer codigo){
        if(codigo!=null){
            for(ErrorLoginEnum en: ErrorLoginEnum.values()){
                if(codigo.equals(en.getCodigo())){
                    return en;
                }
            }
        }
        return null;
    }


}
