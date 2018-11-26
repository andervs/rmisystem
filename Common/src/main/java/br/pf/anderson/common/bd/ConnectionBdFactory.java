package br.pf.anderson.common.bd;

import br.pf.anderson.common.conf.Config;
import br.pf.anderson.common.util.Util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * factory connections to the database.
 *
 * @author Anderson
 */
class ConnectionBdFactory {

  private static final String SECURITY_POLICY = "java.policy";
  private String host;
  private String dataBase;
  private String user;
  private String password;
  private String urlJdbcPostgreSql;
  private boolean complete;

  ConnectionBdFactory() {
    this.user = null;
    this.dataBase = null;
    this.host = null;
    this.password = null;
    this.urlJdbcPostgreSql = null;
    this.complete = true;
  }

  /**
   * Get a connection to the database.
   *
   * @return a connection to the database.
   * @throws SQLException when it is not possible to establish a connection to the database
   */
  static Connection getDataBaseConnection() throws SQLException {
    try (Connection conexaoBd = new ConnectionBdFactory().getConnection()) {
      return conexaoBd;
    }
  }

  /**
   * CONFIGURE THE CONNECTION PARAMETERS IN THE CONFIG.PROPERTIES FILE
   * Change the following parameters according to your configuration:
   * machine.server, name.dataBase, user.dataBase and password.dataBase
   *
   * @return a connection url to the database
   * @throws SQLException if it is not possible to form the connection url
   */
  Connection getConnection() throws SQLException {
    Util util = new Util();
    // Retorna o PATH do projeto
    String projetPath = util.getCurrentPath();

    // Retorna o PATH do arquivo de Política de Segurança
    String filePath = projetPath + File.pathSeparator + SECURITY_POLICY;

    // Defini a política de segurança do sistema de acordo com o arquivo java.policy
    System.setProperty("java.security.policy", filePath);

    // Verifica se há um SecurityManager, caso contrário instancia um
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    // Instanciando classe reponsável pelos dados de Conexão ao Banco de Dados PostgreSQL
    Config config = new Config();
    // Recuperando nome do banco de dados, usuário e password
    String[] conf = null;
    try {
      conf = config.getDataBaseConnectionConf();
    } catch (IOException ex) {
      System.out.print("Não existe arquivo de configuração para conexão ao banco de dados!!!");
      System.exit(0);
    }

    for (String item : conf) {
      if (item.isEmpty()) {
        this.complete = false;
        System.out.print("Parâmetros imcompletos para conexão ao banco de dados!!!"
          + " \nO sistema será encerrado.");
        System.exit(0);
      }
    }

    if (this.complete) {
      host = conf[0];
      dataBase = conf[1];
      user = conf[2];
      password = conf[3];

      // URL de conexão ao dataBase de dados
      urlJdbcPostgreSql = "jdbc:postgresql://" + host + "/" + dataBase;
    }

    // Retorna uma conexão ao dataBase de dados
    return DriverManager.getConnection(urlJdbcPostgreSql, user, password);
  }
}
