package com.zfhy.egold.domain.member.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "member_address")
public class Address {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    
    @Column(name = "member_id")
    private Integer memberId;

    
    private String receiver;

    
    @Column(name = "receiver_tel")
    private String receiverTel;

    
    @Column(name = "receiver_sec_tel")
    private String receiverSecTel;

    
    @Column(name = "zip_code")
    private String zipCode;

    
    @Column(name = "country_id")
    private Integer countryId;

    
    @Column(name = "province_id")
    private Integer provinceId;

    
    @Column(name = "city_id")
    private Integer cityId;

    
    @Column(name = "county_id")
    private Integer countyId;

    
    @Column(name = "town_id")
    private Integer townId;

    
    private String country;

    
    private String province;

    
    private String city;

    
    private String county;

    
    private String town;

    
    private String address;

    
    @Column(name = "default_address")
    private String defaultAddress;

    
    @Column(name = "create_date")
    private Date createDate;

    
    @Column(name = "update_date")
    private Date updateDate;

    
    @Column(name = "del_flag")
    private String delFlag;

    
    private String remarks;

    
    public Integer getId() {
        return id;
    }

    
    public void setId(Integer id) {
        this.id = id;
    }

    
    public Integer getMemberId() {
        return memberId;
    }

    
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    
    public String getReceiver() {
        return receiver;
    }

    
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    
    public String getReceiverTel() {
        return receiverTel;
    }

    
    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    
    public String getReceiverSecTel() {
        return receiverSecTel;
    }

    
    public void setReceiverSecTel(String receiverSecTel) {
        this.receiverSecTel = receiverSecTel;
    }

    
    public String getZipCode() {
        return zipCode;
    }

    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    
    public Integer getCountryId() {
        return countryId;
    }

    
    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    
    public Integer getProvinceId() {
        return provinceId;
    }

    
    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    
    public Integer getCityId() {
        return cityId;
    }

    
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    
    public Integer getCountyId() {
        return countyId;
    }

    
    public void setCountyId(Integer countyId) {
        this.countyId = countyId;
    }

    
    public Integer getTownId() {
        return townId;
    }

    
    public void setTownId(Integer townId) {
        this.townId = townId;
    }

    
    public String getCountry() {
        return country;
    }

    
    public void setCountry(String country) {
        this.country = country;
    }

    
    public String getProvince() {
        return province;
    }

    
    public void setProvince(String province) {
        this.province = province;
    }

    
    public String getCity() {
        return city;
    }

    
    public void setCity(String city) {
        this.city = city;
    }

    
    public String getCounty() {
        return county;
    }

    
    public void setCounty(String county) {
        this.county = county;
    }

    
    public String getTown() {
        return town;
    }

    
    public void setTown(String town) {
        this.town = town;
    }

    
    public String getAddress() {
        return address;
    }

    
    public void setAddress(String address) {
        this.address = address;
    }

    
    public String getDefaultAddress() {
        return defaultAddress;
    }

    
    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    
    public Date getCreateDate() {
        return createDate;
    }

    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    
    public Date getUpdateDate() {
        return updateDate;
    }

    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    
    public String getDelFlag() {
        return delFlag;
    }

    
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    
    public String getRemarks() {
        return remarks;
    }

    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
