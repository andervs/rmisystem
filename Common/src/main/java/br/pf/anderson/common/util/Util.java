package br.pf.anderson.common.util;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.tinytype.CPF;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class of utilitarian methods.
 *
 * @author Anderson
 */
public class Util implements Serializable {

  private static final String DATE_MASK = "dd/MM/yyyy";
  private static final long serialVersionUID = -3476775714485232713L;

  /**
   * Checks if the informed email is valid.
   *
   * @param email to be validated
   * @throws AddressException if the email is invalid
   */
  public void validEmail(String email) throws AddressException {
    InternetAddress internetAddress = new InternetAddress(email);
    internetAddress.validate();
  }

  /**
   * Returns a valid, unformatted CPF (unmasked).
   *
   * @param cpf is an official national identification document of the Brazilian citizen
   *            (similar to the Social Security Number (SSN) in the USA)
   * @return cpf without formatting, without the mask "###.###.###-##"
   */
  public String unformattedValidCpf(String cpf) {
    CPFValidator cpfValidator = new CPFValidator();
    cpfValidator.assertValid(cpf);
    return cpf;
  }

  /**
   * Returns a valid, formatted CPF with mask.
   *
   * @param cpf is an official national identification document of the Brazilian citizen
   *            (similar to the Social Security Number (SSN) in the USA)
   * @return cpf with formatting, with the mask "###.###.###-##"
   */
  public String formattedCpf(String cpf) {
    CPF formattedCpf = new CPF(cpf);
    return formattedCpf.getNumeroFormatado();
  }

  /**
   * Checks if the entered date is valid.
   *
   * @param date in format dd/mm/yyyy
   */
  public void validDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_MASK);
    LocalDate.parse(date, formatter);
  }

  /**
   * Get the value of the form's date field without mask.
   *
   * @param date in format dd/mm/yyyy
   * @return date without mask
   */
  public String getFormBirthdayWithoutMask(String date) {
    date = date.replaceAll("[/_]", "").trim();
    if (!date.isEmpty()) {
      return date;
    }
    return "";
  }

  /**
   * Get the value of the form's cpf field without mask.
   *
   * @param cpf in format "###.###.###-##"
   * @return cpf without mask
   */
  public String getFormCpfWithoutMask(String cpf) {
    cpf = cpf.replaceAll("[_.-]", "").trim();
    if (!cpf.isEmpty()) {
      return cpf;
    }
    return "";
  }

  /**
   * Convert a String to a LocalDate type.
   *
   * @param date in format dd/mm/yyyy
   * @return the date converted to the LocalDate type
   * @throws DateTimeParseException in case of attempted conversion of an invalid date
   */
  public LocalDate convertStringToLocalDate(String date) {
    LocalDate dataComoLocalDate;
    DateTimeFormatter formatter;
    formatter = DateTimeFormatter.ofPattern(DATE_MASK);
    dataComoLocalDate = LocalDate.parse(date, formatter);
    return dataComoLocalDate;
  }

  /**
   * Convert a LocalDate to a String type.
   *
   * @param date in format dd/mm/yyyy
   * @return the date converted to the String type
   * @throws DateTimeParseException in case of attempted conversion of an invalid date
   */
  public String convertLocalDateToString(LocalDate date) {
    String dataComoString;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_MASK);
    dataComoString = date.format(formatter);
    return dataComoString;
  }

  /**
   * Gets the current application directory.
   *
   * @return the current directory (PATH) of the application
   */
  public String getCurrentPath() {
    Path path;
    path = Paths.get(System.getProperty("user.dir"));
    return path.toString();
  }
}
