package com.yinker.etl.trans.model.base;

public class SyncTableInfo {

    /* 数据库概要 */
    private String tableSchema;

    /* 表名 */
    private String tableName;

    /* 列名 */
    private String columnName;

    /* 键值 */
    private String columnKey;

    /* 默认值 */
    private String columnDefault;

    /* 是否为空值 */
    private String isNullable;

    /* 数据类型 */
    private String dataType;

    /* 列类型 */
    private String columnType;

    /* 列备注 */
    private String columnComment;

    /* 主键顺序(若不是主键则为null) */
    private String keyOrdinalPosition;

    /* 额外信息：是否自增等 */
    private String extra;

    private String idName;


    public String getTableSchema () {
        return tableSchema;
    }

    public void setTableSchema (String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName () {
        return tableName;
    }

    public void setTableName (String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName () {
        return columnName;
    }

    public String getColumnKey () {
        return columnKey;
    }

    public void setColumnKey (String columnKey) {
        this.columnKey = columnKey;
    }

    public void setColumnName (String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDefault () {
        return columnDefault;
    }

    public void setColumnDefault (String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getIsNullable () {
        return isNullable;
    }

    public void setIsNullable (String isNullable) {
        this.isNullable = isNullable;
    }

    public String getDataType () {
        return dataType;
    }

    public void setDataType (String dataType) {
        this.dataType = dataType;
    }

    public String getColumnType () {
        return columnType;
    }

    public void setColumnType (String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment () {
        return columnComment;
    }

    public void setColumnComment (String columnComment) {
        this.columnComment = columnComment;
    }

    public String getKeyOrdinalPosition() {
        return keyOrdinalPosition;
    }

    public void setKeyOrdinalPosition(String keyOrdinalPosition) {
        this.keyOrdinalPosition = keyOrdinalPosition;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getIdName () {
        return idName;
    }

    public void setIdName (String idName) {
        this.idName = idName;
    }
}
