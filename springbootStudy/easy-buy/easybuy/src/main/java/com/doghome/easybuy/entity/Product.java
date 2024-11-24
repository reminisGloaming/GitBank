package com.doghome.easybuy.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Document(indexName = "product")
public class Product {

  @Id
  private int id;

  @Field(type=FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
  private String name;

  @Field(type=FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
  private String description;

  @Field(type = FieldType.Double,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
  private double price;

  @Transient
  private int stock;

  @Field(type = FieldType.Integer)
  private int categoryLevel1Id;

  @Field(type = FieldType.Integer)
  private int categoryLevel2Id;

  @Field(type = FieldType.Integer)
  private int categoryLevel3Id;

  @Field(type=FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
  private String fileName;


  @Transient
  private int isDelete;

  @Field(type = FieldType.Date,format = DateFormat.date_time)
////  @Field(type = FieldType.Date, format = DateFormat.date)
  private Date createTime;


  @Transient
  private int preStock;



  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public int getStock() {
    return stock;
  }

  public void setStock(int stock) {
    this.stock = stock;
  }


  public int getCategoryLevel1Id() {
    return categoryLevel1Id;
  }

  public void setCategoryLevel1Id(int categoryLevel1Id) {
    this.categoryLevel1Id = categoryLevel1Id;
  }


  public int getCategoryLevel2Id() {
    return categoryLevel2Id;
  }

  public void setCategoryLevel2Id(int categoryLevel2Id) {
    this.categoryLevel2Id = categoryLevel2Id;
  }


  public int getCategoryLevel3Id() {
    return categoryLevel3Id;
  }

  public void setCategoryLevel3Id(int categoryLevel3Id) {
    this.categoryLevel3Id = categoryLevel3Id;
  }


  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }


  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public int getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(int isDelete) {
    this.isDelete = isDelete;
  }


  public int getPreStock() {
    return preStock;
  }

  public void setPreStock(int preStock) {
    this.preStock = preStock;
  }

}
