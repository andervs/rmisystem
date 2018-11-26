package br.pf.anderson.common.bd;

import br.pf.anderson.common.javabean.Candidate;
import br.pf.anderson.common.util.Util;

import java.sql.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static br.pf.anderson.common.bd.ConnectionBdFactory.getDataBaseConnection;

/**
 * Candidate Data Access Object class
 *
 * @author Anderson
 */
public class CandidateDao {

  private static final String DATABASE_CONNECTION_ERROR = "Ocorreu uma falha de conexão ao banco de dados!";
  private static final String IDCANDIDATO = "idcandidato";
  private static final String DATANASCIMENTO = "datanascimento";
  private static final String NOMEMAE = "nomemae";
  private static final String EMAIL = "email";
  private static final String ENDERECO = "endereco";
  private final Connection connection;
  private final Collection<Candidate> candidateCollection;

  /**
   * Contructor.
   *
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  public CandidateDao() throws SQLException {
    this.candidateCollection = new HashSet<>();
    this.connection = new ConnectionBdFactory().getConnection();
  }

  /**
   * Verifies that the database is active and responding.
   *
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  public void dataBaseReady() throws SQLException {
    Optional<Connection> conn = Optional.ofNullable(getDataBaseConnection());
    if (conn.isEmpty()) {
      throw new SQLException(DATABASE_CONNECTION_ERROR);
    }
  }

  /**
   * Inserts a new candidate into the system.
   *
   * @param candidate to be inserted
   * @return true if the inclusion of the candidate is successful
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  public boolean insertCandidate(Candidate candidate) throws SQLException {
    final String insertSQL = "insert into candidato (nome,cpf,dataNascimento,"
      + "nomemae,email,endereco) values (?,?,?,?,?,?)";
    int executeInsertReturn;
    dataBaseReady();
    try (this.connection;
         PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
      stmt.setString(1, candidate.getCandidateName());
      Util util = new Util();
      stmt.setString(2, util.unformattedValidCpf(candidate.getCpf()));
      stmt.setDate(3, Date.valueOf(candidate.getBirthday()));
      stmt.setString(4, candidate.getMotherName());
      stmt.setString(5, candidate.getEmail());
      stmt.setString(6, candidate.getAddress());
      executeInsertReturn = stmt.executeUpdate();
    }
    return executeInsertReturn != 0;
  }

  /**
   * Search for candidates previously registered in the system.
   *
   * @param candidate to be searched
   * @return a list containing all candidates matching the search criteria
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  public Collection<Candidate> searchCandidates(Candidate candidate, boolean returnAll, boolean verifyEmptyTable) throws SQLException {
    dataBaseReady();
    String selectSQL;
    if (returnAll) {
      selectSQL = "select * from candidato";
    } else {
      if (!verifyEmptyTable) {
        selectSQL = getSearchQuery(candidate);
      }
      else {
        selectSQL = "select * from candidato limit 1";
      }
    }
    try (this.connection;
         PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          Candidate persistentCandidate = new Candidate();
          persistentCandidate.setCandidateId(rs.getLong(IDCANDIDATO));
          persistentCandidate.setCandidateName(rs.getString("nome"));
          Util util = new Util();
          persistentCandidate.setCpf(util.formattedCpf(rs.getString("cpf")));
          Date data = rs.getDate(DATANASCIMENTO);
          persistentCandidate.setBirthday(data.toLocalDate());
          persistentCandidate.setMotherName(rs.getString(NOMEMAE));
          persistentCandidate.setEmail(rs.getString(EMAIL));
          persistentCandidate.setAddress(rs.getString(ENDERECO));
          candidateCollection.add(persistentCandidate);
        }
        return candidateCollection;
      }
    }
  }

  /**
   * Assemble a sql query to search for candidates.
   *
   * @param candidate to be searched
   * @return a sql query mounted for candidate search
   */
  private String getSearchQuery(Candidate candidate) {
    Util util = new Util();
    int parameterCounter = 0;
    StringBuilder selectSQL = new StringBuilder();
    selectSQL.append("select * from candidato where");
    if (!candidate.getCandidateName().trim().isEmpty()) {
      selectSQL.append(" nome ilike '%").append(candidate.getCandidateName().trim()).append("%'");
      parameterCounter += 1;
    }
    if (!candidate.getCpf().trim().isEmpty()) {
      if (parameterCounter >= 1) {
        selectSQL.append(" AND");
      }
      selectSQL.append(" cpf ilike '%").append(candidate.getCpf().trim()).append("%'");
      parameterCounter += 1;
    }

    Optional<LocalDate> birtDayOptional = Optional.ofNullable(candidate.getBirthday());
    if (birtDayOptional.isPresent()) {
      if (parameterCounter >= 1) {
        selectSQL.append(" AND");
      }
      selectSQL.append(" to_char(dataNascimento, 'DD/MM/YYYY') = '").append(util.convertLocalDateToString(candidate.getBirthday())).append("'");
      parameterCounter += 1;
    }

    if (!candidate.getMotherName().trim().isEmpty()) {
      if (parameterCounter >= 1) {
        selectSQL.append(" AND");
      }
      selectSQL.append(" nomeMae ilike '%").append(candidate.getMotherName().trim()).append("%'");
      parameterCounter += 1;
    }

    if (!candidate.getEmail().trim().isEmpty()) {
      if (parameterCounter >= 1) {
        selectSQL.append(" AND");
      }
      selectSQL.append(" email ilike '%").append(candidate.getEmail().trim()).append("%'");
      parameterCounter += 1;
    }

    if (!candidate.getAddress().trim().isEmpty()) {
      if (parameterCounter >= 1) {
        selectSQL.append(" AND");
      }
      selectSQL.append(" endereco ilike '%").append(candidate.getAddress().trim()).append("%'");
    }
    return selectSQL.toString();
  }

  /**
   * Updates the data of a previously registered candidate.
   *
   * @param candidate to be updated
   * @return true if candidate data is updated successfully
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  public boolean updateCandidate(Candidate candidate) throws SQLException {
    int executeUpdateReturn;
    dataBaseReady();
    String updateSQL = "update candidato set nome = ?,"
      + "cpf = ?,"
      + "dataNascimento = ?,"
      + "nomemae = ?,"
      + "email = ?,"
      + "endereco = ?"
      + " where idcandidato = ?";
    try (this.connection;
         PreparedStatement stmt = connection.prepareStatement(updateSQL)) {
      stmt.setString(1, candidate.getCandidateName());
      stmt.setString(2, candidate.getCpf());
      stmt.setDate(3, Date.valueOf(candidate.getBirthday()));
      stmt.setString(4, candidate.getMotherName());
      stmt.setString(5, candidate.getEmail());
      stmt.setString(6, candidate.getAddress());
      stmt.setLong(7, candidate.getCandidateId());
      executeUpdateReturn = stmt.executeUpdate();
    }
    return executeUpdateReturn != 0;
  }

  /**
   * Delete a candidate.
   *
   * @param candidate to be deleted
   * @return true if candidate is deleted successfully
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  public boolean deleteCandidate(Candidate candidate) throws SQLException {
    int executeDeleteReturn;
    dataBaseReady();
    String deleteSql = "delete from candidato where idcandidato = ?";
    try (this.connection;
         PreparedStatement stmt = connection.prepareStatement(deleteSql)) {
      stmt.setLong(1, candidate.getCandidateId());
      executeDeleteReturn = stmt.executeUpdate();
    }
    return executeDeleteReturn != 0;
  }
}
