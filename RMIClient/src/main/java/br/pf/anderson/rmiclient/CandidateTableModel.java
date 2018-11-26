package br.pf.anderson.rmiclient;

import br.pf.anderson.common.javabean.Candidate;
import br.pf.anderson.common.util.Util;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Candidate table model class.
 *
 * @author Anderson
 */
class CandidateTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 3787151231449359450L;
  private final List<Candidate> lines;
  private final String[] columns;
  private final Util util;

  CandidateTableModel(List<Candidate> candidates) {
    this.columns = new String[]{"ID", "NOME", "CPF", "NASCIMENTO", "MÃE", "E-MAIL", "ENDEREÇO"};
    lines = new ArrayList<>(candidates);
    util = new Util();
  }

  @Override
  public String getColumnName(int coluna) {
    return columns[coluna];
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public int getRowCount() {
    return lines.size();
  }

  @Override
  public Object getValueAt(int linha, int coluna) {
    Candidate candidate;
    candidate = lines.get(linha);
    switch (coluna) {
      case 0:
        return candidate.getCandidateId();
      case 1:
        return candidate.getCandidateName();
      case 2:
        return util.formattedCpf(candidate.getCpf());
      case 3:
        return util.convertLocalDateToString(candidate.getBirthday());
      case 4:
        return candidate.getMotherName();
      case 5:
        return candidate.getEmail();
      case 6:
        return candidate.getAddress();
      default:
        return null;
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    switch (columnIndex) {
      case 0:
        return String.class;
      case 1:
        return String.class;
      case 2:
        return String.class;
      case 3:
        return String.class;
      case 4:
        return String.class;
      case 5:
        return String.class;
      case 6:
        return String.class;
      default:

        throw new IndexOutOfBoundsException("Índice das columns fora dos limites!!!");
    }

  }

  Candidate getCandidate(int rowIndex) {
    if (rowIndex < 0) {
      rowIndex++;
    }
    return lines.get(rowIndex);
  }
}
