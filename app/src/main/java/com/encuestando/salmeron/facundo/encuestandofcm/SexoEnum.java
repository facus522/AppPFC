package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 17/7/2018.
 */

public enum SexoEnum {
    MASCULINO(1, "Masculino"),
    FEMENINO(2, "Femenino");

    private Integer codigo;
    private String descripcion;

    private SexoEnum(Integer codigo,String descripcion){
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
    public static SexoEnum getEnum(Integer codigo){
        if(codigo!=null){
            for(SexoEnum en: SexoEnum.values()){
                if(codigo.equals(en.getCodigo())){
                    return en;
                }
            }
        }
        return null;
    }

}
