package com.encuestando.salmeron.facundo.encuestandofcm;

/**
 * Created by Facundo Salmerón on 17/7/2018.
 */

public enum TipoUsuarioEnum {
    USUARIO_ESPECIAL(1, "Usuario Especial"),
    USUARIO_NORMAL(2, "Usuario Normal");

    private Integer codigo;
    private String descripcion;

    private TipoUsuarioEnum(Integer codigo,String descripcion){
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
    public static TipoUsuarioEnum getEnum(Integer codigo){
        if(codigo!=null){
            for(TipoUsuarioEnum en: TipoUsuarioEnum.values()){
                if(codigo.equals(en.getCodigo())){
                    return en;
                }
            }
        }
        return null;
    }

}
