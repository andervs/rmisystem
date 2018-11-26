package br.pf.anderson.rmiserver;

import br.pf.anderson.common.bd.CandidateDao;
import br.pf.anderson.common.javabean.Candidate;
import br.pf.anderson.common.rmiserveri.RmiServerI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.Collection;

/**
 * This class implements the RmiServerI interface methods.
 *
 * @author Anderson
 */
public class RmiServerImp extends UnicastRemoteObject implements RmiServerI {

  private static final long serialVersionUID = -3197531549011162925L;

  /**
   * Constructor.
   *
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   */
  RmiServerImp() throws RemoteException {
  }

  /**
   * Verifies that the database is active and responding.
   *
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  @Override
  public void dataBaseReady() throws SQLException {
    CandidateDao candidateDao = new CandidateDao();
    candidateDao.dataBaseReady();
  }

  /**
   * Inserts a new candidate into the system.
   *
   * @param candidate to be inserted
   * @return true if candidate inclusion was successful
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  @Override
  public boolean insertCandidate(Candidate candidate) throws SQLException {
    CandidateDao candidateDao = new CandidateDao();
    return candidateDao.insertCandidate(candidate);
  }

  /**
   * Search for candidates previously registered in the system.
   *
   * @param candidate to be searched
   * @return a list containing all previously registered candidates that match the search criteria
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  @Override
  public Collection<Candidate> searchCandidates(Candidate candidate, boolean returnAll, boolean verifyEmptyTable) throws SQLException {
    CandidateDao candidateDao = new CandidateDao();
    return candidateDao.searchCandidates(candidate, returnAll, verifyEmptyTable);
  }

  /**
   * Updates the data of a previously registered candidate.
   *
   * @param candidate to be updated
   * @return true if candidate data is updated successfully
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  @Override
  public boolean updateCandidate(Candidate candidate) throws SQLException {
    CandidateDao candidateDao = new CandidateDao();
    return candidateDao.updateCandidate(candidate);
  }

  /**
   * Delete a candidate.
   *
   * @param candidate to be deleted
   * @return true if candidate data is deleted successfully
   * @throws SQLException is thrown in the event of a failure to connect to the database
   */
  @Override
  public boolean deleteCandidate(Candidate candidate) throws SQLException {
    CandidateDao candidateDao = new CandidateDao();
    return candidateDao.deleteCandidate(candidate);
  }
}
