package br.pf.anderson.common.javabean;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Candidate JavaBean class.
 *
 * @author Anderson
 */
public class Candidate implements Comparable<Candidate>, Serializable {

  private static final long serialVersionUID = -1034555083035316766L;
  private Long candidateId;
  private String candidateName;
  private String cpf;
  private LocalDate birthday;
  private String motherName;
  private String email;
  private String address;

  public Long getCandidateId() {
    return candidateId;
  }

  public void setCandidateId(Long candidateId) {
    this.candidateId = candidateId;
  }

  public String getCandidateName() {
    return candidateName;
  }

  public void setCandidateName(String candidateName) {
    this.candidateName = candidateName;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  public String getMotherName() {
    return motherName;
  }

  public void setMotherName(String motherName) {
    this.motherName = motherName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public int compareTo(@NotNull Candidate other) {
    return this.getCandidateName().compareTo(other.getCandidateName());
  }

  @Override
  public int hashCode() {
    return this.getCandidateName().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Candidate)) {
      return false;
    }
    Candidate other = (Candidate) obj;
    if (this.getCandidateName() == null) {
      return other.getCandidateName() == null;
    }
    return this.getCandidateName().equals(other.getCandidateName());
  }

}
