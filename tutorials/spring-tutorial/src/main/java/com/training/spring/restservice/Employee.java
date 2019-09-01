package com.training.spring.restservice;

public class Employee {
  private String empName;
  private String address;
  private int empId;

  /** @return the address */
  public String getAddress() {
    return address;
  }
  /** @param address the address to set */
  public void setAddress(String address) {
    this.address = address;
  }
  /** @return the empId */
  public int getEmpId() {
    return empId;
  }
  /** @param empId the empId to set */
  public void setEmpId(int empId) {
    this.empId = empId;
  }

  @Override
  public String toString() {
    // TODO Auto-generated method stub
    return empName + ":" + empId + ":" + address;
  }
  /** @return the empName */
  public String getEmpName() {
    return empName;
  }
  /** @param empName the empName to set */
  public void setEmpName(String empName) {
    this.empName = empName;
  }
}