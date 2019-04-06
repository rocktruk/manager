package com.online.mall.manager.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Table(name="GOODS")
@Entity
public class Goods {


	@Id
	private String id;
	
	@Column(name="PRICE")
	private BigDecimal price;
	
	@Column(name="BRAND")
	private String brand;

	@Lob
	@Basic(fetch=FetchType.LAZY)
	@Column(name="DETAIL",columnDefinition="blob")
	private byte[] detail;
	
	
	@Column(name="INVENTORY")
	private long inventory;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="IMG_PATH")
	private String imgPath;
	
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
	
	@Column(name="CREATE_TIME",columnDefinition="timestamp")
	private Date createTime;
	
	@Transient
	private String banners;
	
	@OneToOne
	@JoinColumn(name="GOODS_MENU_ID")
	private GoodsMenu menu;
	
	public GoodsMenu getMenu() {
		return menu;
	}

	public void setMenu(GoodsMenu menu) {
		this.menu = menu;
	}

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
	

	public byte[] getDetail() {
		return detail;
	}

	public void setDetail(byte[] detail) {
		this.detail = detail;
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

	public String getBanerImages() {
		return banerImages;
	}

	public void setBanerImages(String banerImages) {
		this.banerImages = banerImages;
	}

	public String getBanners() {
		return banners;
	}

	public void setBanners(String banners) {
		this.banners = banners;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}