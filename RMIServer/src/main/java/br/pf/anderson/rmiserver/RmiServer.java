package br.pf.anderson.rmiserver;

import br.pf.anderson.common.conf.Config;
import br.pf.anderson.common.util.Util;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;

/**
 * RmiServer class responsible for receiving RmiClient connections.
 *
 * @author Anderson
 */
class RmiServer implements Remote {
  private static final String SECURITY_POLICY = "java.policy";

  public static void main(String[] args) {
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
    // Instancia classe reponsável pela Configuração do Serviço e do Cliente RMI
    Config config = new Config();
    // Recupera nome do serviço RMI
    String rmiServerName = null;
    try {
      rmiServerName = config.getRmiServerName();
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
    }
    // Recupera a porta onde o serviço RMI estará esperando chamadas (conexões)
    String rmiServerPort = null;
    try {
      rmiServerPort = config.getRmiServerPort();
    } catch (IOException e) {
      System.out.println(e.getLocalizedMessage());
    }
    // Instancia o objeto do Servidor RMI, que disponibilizará os métodos remotos
    RmiServerImp remoteObject = null;
    try {
      remoteObject = new RmiServerImp();
    } catch (RemoteException e) {
      System.out.println(e.getLocalizedMessage());
    }
    Registry registry = null;
    // Cria o Registro RMI esperando chamadas na porta especificada
    try {
      Optional port = Optional.ofNullable(rmiServerPort);
      if (port.isPresent()) {
        registry = LocateRegistry.createRegistry(Integer.parseInt(rmiServerPort));
      } else {
        System.out.println("A porta do serviço RMI não pode ser nula!");
        return;
      }

    } catch (RemoteException e) {
      if (e.getMessage().contains("Port already in use")) {
        System.out.println("\n*****  Registro RMI já ocupando a porta:"
          + rmiServerPort + "!  *****");
        System.out.println("*****  Abortando a execução dessa nova"
          + " instância do serviço.  *****\n");
        System.exit(0);
      }
    }
    // Vincula ao objeto remoto, o nome especificado para o serviço
    try {
      if (registry != null) {
        registry.rebind(rmiServerName, remoteObject);
      }
    } catch (RemoteException e) {
      System.out.println(e.getLocalizedMessage());
    }
    System.out.println("\n" + rmiServerName + " registrado com sucesso!!!");
  }
}
