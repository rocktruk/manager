package com.online.mall.manager.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Table(name="MER_ADMIN")
@Entity
public class MerAdmin {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="PASSWORD")
	private String pwd;
	
	
	@Column(name="ACCOUNT_NAME")
	private String acctName;
	
	@Column(name="PHONE_NO")
	private String phoneNo;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	
	public String getToken(MerAdmin user) {
        String token="";
        token= JWT.create().withAudience(user.getId()+"")
                .sign(Algorithm.HMAC256(user.getPwd()));
        return token;
    }
}
