package br.pf.anderson.rmiclient;

import br.pf.anderson.common.javabean.Candidate;
import br.pf.anderson.common.conf.Config;
import br.pf.anderson.common.rmiserveri.RmiServerI;
import br.pf.anderson.common.util.Util;

import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.Collection;

/**
 * RmiClient class responsible for connecting to RmiServer.
 *
 * @author Anderson
 */
@SuppressWarnings("UnusedReturnValue")
class RmiClient {

  private static final String SECURITY_POLICY = "java.policy";
  private final RmiServerI remoteObject;

  RmiClient() throws NotBoundException, IOException {
    Util util = new Util();
    // Retorna o PATH do projeto
    String projectPath = util.getCurrentPath();
    // Retorna o PATH do arquivo de Política de Segurança
    String filePath = projectPath + File.separator + SECURITY_POLICY;
    // Defini a política de segurança do sistema de acordo com o arquivo java.policy
    System.setProperty("java.security.policy", filePath);
    // Se SecurityManager for nulo, instancia um novo objeto
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    // Instancia a classe reponsável pela configuração do serviço e do cliente RMI
    Config config = new Config();
    // Recupera nome da máquina servidor RMI
    String serverAddress = config.getServerAddress();
    // Recupera a porta onde o serviço RMI estará esperando chamadas (conexões)
    String rmiServerPort = config.getRmiServerPort();
    // Retorna referência ao Registro de objetos remotos, para o host e portas especificados
    Registry registry = LocateRegistry.getRegistry(serverAddress,
      Integer.parseInt(rmiServerPort));
    // Recupera nome do Serviço RMI
    String rmiServerName = config.getRmiServerName();
    // Localiza pelo nome, o Serviço que foi previamente registrado pelo Servidor RMI
    remoteObject = (RmiServerI) registry.lookup(rmiServerName);
  }

  /**
   * Verifies that the database is active and responding.
   *
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  void dataBaseReady() throws RemoteException, SQLException {
    remoteObject.dataBaseReady();
  }

  /**
   * Inserts a new candidate into the system.
   *
   * @param candidate to be inserted
   * @return true if candidate inclusion was successful
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  boolean insertCandidate(Candidate candidate) throws RemoteException, SQLException {
    return remoteObject.insertCandidate(candidate);
  }

  /**
   * Search for candidates previously registered in the system.
   *
   * @param candidate to be searched
   * @return a list containing all previously registered candidates that match the search criteria
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  Collection<Candidate> searchCandidates(Candidate candidate, boolean returnAll, boolean verifyEmptyTable) throws RemoteException, SQLException {
    return remoteObject.searchCandidates(candidate, returnAll, verifyEmptyTable);
  }

  /**
   * Updates the data of a previously registered candidate.
   *
   * @param candidate to be updated
   * @return true if candidate data is updated successfully
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  boolean updateCandidate(Candidate candidate) throws RemoteException, SQLException {
    return remoteObject.updateCandidate(candidate);
  }

  /**
   * Delete a candidate.
   *
   * @param candidate to be deleted
   * @return true if candidate is deleted successfully
   * @throws RemoteException is thrown in case of failure to access the remote object made
   *                         available by the RMI server
   * @throws SQLException    is thrown in the event of a failure to connect to the database
   */
  boolean deleteCandidate(Candidate candidate) throws RemoteException, SQLException {
    return remoteObject.deleteCandidate(candidate);
  }
}
