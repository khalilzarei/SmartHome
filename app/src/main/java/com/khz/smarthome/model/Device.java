package com.khz.smarthome.model;

import com.google.gson.annotations.SerializedName;

public class Device {

    @SerializedName("id")
    private Integer id;
    @SerializedName("d_id")
    private Integer dId;
    @SerializedName("d_type")
    private String  dType;
    @SerializedName("d_a0")
    private String  dA0;
    @SerializedName("d_a1")
    private String  dA1;
    @SerializedName("d_a2")
    private String  dA2;
    @SerializedName("r_id")
    private Integer rId;
    @SerializedName("p_id")
    private Integer pId;
    @SerializedName("c_id")
    private Integer cId;
    @SerializedName("l")
    private double  l;
    @SerializedName("t")
    private double  t;
    @SerializedName("w")
    private Double  w;
    @SerializedName("h")
    private Double  h;
    @SerializedName("bg")
    private String  bg;
    @SerializedName("ic")
    private String  ic;
    @SerializedName("bw")
    private Integer bw;
    @SerializedName("bc")
    private String  bc;
    @SerializedName("in")
    private String  in;
    @SerializedName("is")
    private Integer is;
    @SerializedName("v")
    private String  dim;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getdId() {
        return dId;
    }

    public void setdId(Integer dId) {
        this.dId = dId;
    }

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }

    public String getdA0() {
        return dA0;
    }

    public void setdA0(String dA0) {
        this.dA0 = dA0;
    }

    public String getdA1() {
        return dA1;
    }

    public void setdA1(String dA1) {
        this.dA1 = dA1;
    }

    public String getdA2() {
        return dA2;
    }

    public void setdA2(String dA2) {
        this.dA2 = dA2;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public double getL() {
        return l;
    }

    public void setL(Double l) {
        this.l = l;
    }

    public double getT() {
        return t;
    }

    public void setT(Double t) {
        this.t = t;
    }

    public Double getW() {
        return w;
    }

    public void setW(Double w) {
        this.w = w;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public Integer getBw() {
        return bw;
    }

    public void setBw(Integer bw) {
        this.bw = bw;
    }

    public String getBc() {
        return bc;
    }

    public void setBc(String bc) {
        this.bc = bc;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public Integer getIs() {
        return is;
    }

    public void setIs(Integer is) {
        this.is = is;
    }

    public String getDim() {
        return dim;
    }

    public void setDim(String dim) {
        this.dim = dim;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", dId=" + dId +
                ", dType='" + dType + '\'' +
                ", dA0='" + dA0 + '\'' +
                ", dA1='" + dA1 + '\'' +
                ", dA2='" + dA2 + '\'' +
                ", rId=" + rId +
                ", pId=" + pId +
                ", cId=" + cId +
                ", l=" + l +
                ", t=" + t +
                ", w=" + w +
                ", h=" + h +
                ", bg='" + bg + '\'' +
                ", ic='" + ic + '\'' +
                ", bw=" + bw +
                ", bc='" + bc + '\'' +
                ", in='" + in + '\'' +
                ", is=" + is +
                ", v='" + dim + '\'' +
                '}';
    }
}