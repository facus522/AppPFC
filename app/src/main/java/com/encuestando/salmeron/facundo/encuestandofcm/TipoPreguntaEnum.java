package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 17/7/2018.
 */

public enum TipoPreguntaEnum {
    MULTIPLE_CHOICE(1, "Multiple Choice"),
    RESPUESTA_UNICA(2, "Unica"),
    RESPUESTA_NUMERICA(3, "Numerica"),
    RESPUESTA_TEXTUAL(4, "Textual"),
    ESCALA(5, "Escala");

    private Integer codigo;
    private String descripcion;

    private TipoPreguntaEnum(Integer codigo, String descripcion){
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
    public static TipoPreguntaEnum getEnum(Integer codigo){
        if(codigo!=null){
            for(TipoPreguntaEnum en: TipoPreguntaEnum.values()){
                if(codigo.equals(en.getCodigo())){
                    return en;
                }
            }
        }
        return null;
    }

}
