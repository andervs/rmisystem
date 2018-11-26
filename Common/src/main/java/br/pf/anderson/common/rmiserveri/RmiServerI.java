package br.pf.anderson.common.rmiserveri;

import br.pf.anderson.common.javabean.Candidate;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;

/**
 * RMI Server Interface.
 *
 * @author Anderson
 */
public interface RmiServerI extends Remote {

  /**
   * dataBaseReady method signature.
   *
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  void dataBaseReady() throws RemoteException, SQLException;

  /**
   * insertCandidate method signature.
   *
   * @param candidate to be inserted
   * @return true if the inclusion of the candidate is successful
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  boolean insertCandidate(Candidate candidate) throws RemoteException, SQLException;

  /**
   * searchCandidates method signature.
   *
   * @param candidate to be searched
   * @return a list containing all previously registered candidates that match the search criteria
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  Collection<Candidate> searchCandidates(Candidate candidate, boolean returnAll, boolean verifyEmptyTable) throws RemoteException, SQLException;

  /**
   * updateCandidate method signature.
   *
   * @param candidate to be updated
   * @return true if candidate data is updated successfully
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  boolean updateCandidate(Candidate candidate) throws RemoteException, SQLException;

  /**
   * deleteCandidate method signature.
   *
   * @param candidate to be deleted
   * @return true if candidate is deleted successfully
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  boolean deleteCandidate(Candidate candidate) throws RemoteException, SQLException;
}
