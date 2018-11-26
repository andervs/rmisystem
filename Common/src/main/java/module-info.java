module common {
  requires java.rmi;
  requires java.sql;
  requires caelum.stella.core;
  requires java.mail;
  requires annotations;
  exports br.pf.anderson.common.bd;
  exports br.pf.anderson.common.javabean;
  exports br.pf.anderson.common.conf;
  exports br.pf.anderson.common.rmiserveri;
  exports br.pf.anderson.common.util;
}
