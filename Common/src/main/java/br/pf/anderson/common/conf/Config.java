package br.pf.anderson.common.conf;

import br.pf.anderson.common.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * System configuration class.
 *
 * @author Anderson
 */
public class Config {

  private static final String SECURITY_POLICY = "java.policy";
  private static final String CONFIG_PROPERTIES = "config.properties";
  private static final String SERVER_PROJECT_PATH = File.separator + "conf" + File.separator;

  /**
   * Gets the data contained in the conf.properties configuration file.
   *
   * @return the contents of the configuration file
   * @throws IOException in case of error when trying to access a specific property
   *                     returned from the configuration file
   */
  private static Properties getConfig() throws IOException {
    Util util = new Util();
    // Retorna o PATH do projeto
    String projectPath = util.getCurrentPath();
    // Retorna o PATH do arquivo de Política de Segurança
    String filePath = projectPath + File.separator + SECURITY_POLICY;
    // Defini a política de segurança do sistema de acordo com o arquivo java.policy
    System.setProperty("java.security.policy", filePath);
    // Verifica se há um SecurityManager, caso contrário instancia um
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    String filePathInputStream = projectPath + SERVER_PROJECT_PATH + CONFIG_PROPERTIES;
    FileInputStream file;
    file = new FileInputStream(filePathInputStream);
    Properties config = new Properties();
    config.load(file);
    return config;
  }

  /**
   * Get the address of the RMI server.
   *
   * @return the address of the RMI server contained in the conf.properties configuration file.
   * @throws IOException in case of error when trying to access a specific property
   *                     returned from the configuration file
   */
  public String getServerAddress() throws IOException {
    String serverAddress;
    Properties config = getConfig();
    serverAddress = config.getProperty("server.address");
    return serverAddress;
  }

  /**
   * Gets the name of the RMI server.
   *
   * @return the RMI server name contained in the conf.properties configuration file.
   * @throws IOException in case of error when trying to access a specific property
   *                     returned from the configuration file
   */
  public String getRmiServerName() throws IOException {
    String rmiServerName;
    Properties configuracoes = getConfig();
    rmiServerName = configuracoes.getProperty("rmi.server.name");
    return rmiServerName;
  }

  /**
   * Gets the port of the RMI server.
   *
   * @return the RMI server port contained in the conf.properties configuration file.
   * @throws IOException in case of error when trying to access a specific property
   *                     returned from the configuration file
   */
  public String getRmiServerPort() throws IOException {
    String rmiServerPort;
    Properties config = getConfig();
    rmiServerPort = config.getProperty("rmi.server.port");
    return rmiServerPort;
  }

  /**
   * Get the connection parameters to the database.
   *
   * @return the settings related to the connection to the database contained
   * in the configuration file
   * @throws IOException in case of error when trying to access a specific property
   *                     returned from the configuration file
   */
  public String[] getDataBaseConnectionConf() throws IOException {
    String[] dataBaseConnectionConf = new String[4];

    Properties config = getConfig();
    dataBaseConnectionConf[0] = config.getProperty("server.address");
    dataBaseConnectionConf[1] = config.getProperty("database.name");
    dataBaseConnectionConf[2] = config.getProperty("database.user");
    dataBaseConnectionConf[3] = config.getProperty("database.password");

    return dataBaseConnectionConf;
  }
}
