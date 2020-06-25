package com.huvalho.object;

public class UsuarioRes {

    private String result;
    private int totalCases;
    private int cases24hrs;
    private int peopleRisk;

    public UsuarioRes() {
        this.result = "safe";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getTotalCases() {
        return totalCases;
    }

    public void setTotalCases(int totalCases) {
        this.totalCases = totalCases;
    }

    public int getCases24hrs() {
        return cases24hrs;
    }

    public void setCases24hrs(int cases24hrs) {
        this.cases24hrs = cases24hrs;
    }

    public int getPeopleRisk() {
        return peopleRisk;
    }

    public void setPeopleRisk(int peopleRisk) {
        this.peopleRisk = peopleRisk;
    }
}
