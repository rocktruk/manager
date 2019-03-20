package com.online.mall.manager.entity;

import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


@Table(name="GOODS")
@Entity
public class GoodsWithoutDetail {

	@Id
	private String id;
	
	@Column(name="PRICE")
	private BigDecimal price;
	
	@Column(name="BRAND")
	private String brand;

	
	@Column(name="INVENTORY")
	private long inventory;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="IMG_PATH")
	private String imgPath;
	
	@Column(name="GOODS_MENU_ID")
	private int goodsMenuId;
	
	@Column(name="SPECIFICATION")
	private String specification;

	@Column(name="title")
	private String title;
	
	@Column(name="GOODS_DESC")
	private String desc;
	
	@Column(name="MONTH_SALES")
	private long monthSales;
	
	@Column(name="TOTAL_SALES")
	private long totalSales;
	
	
	@Column(name="BANNER_IMAGES")
	private String banerImages;
	
	@Column(name="CARRIAGE")
	private int carriage;
	
	@Column(name="ORI_PRICE")
	private BigDecimal oriPrice;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	

	public long getInventory() {
		return inventory;
	}

	public void setInventory(long inventory) {
		this.inventory = inventory;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public int getGoodsMenuId() {
		return goodsMenuId;
	}

	public void setGoodsMenuId(int goodsMenuId) {
		this.goodsMenuId = goodsMenuId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(long monthSales) {
		this.monthSales = monthSales;
	}

	public long getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(long totalSales) {
		this.totalSales = totalSales;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getBanerImages() {
		return banerImages;
	}

	public void setBanerImages(String banerImages) {
		this.banerImages = banerImages;
	}

	public int getCarriage() {
		return carriage;
	}

	public void setCarriage(int carriage) {
		this.carriage = carriage;
	}

	public BigDecimal getOriPrice() {
		return oriPrice;
	}

	public void setOriPrice(BigDecimal oriPrice) {
		this.oriPrice = oriPrice;
	}

	
	
}
