package br.pf.anderson.rmiclient;

import br.com.caelum.stella.validation.InvalidStateException;
import br.pf.anderson.common.javabean.Candidate;
import br.pf.anderson.common.util.Util;

import javax.mail.internet.AddressException;
import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.*;


/**
 * Candidate registration form.
 *
 * @author Anderson
 */
class FormCandidate {

  private static final String DATABASE_CONNECTION_ERROR
    = "Ocorreu uma falha de conexão ao banco de dados!";
  private static final String RMI_CONNECTION_ERROR
    = "Ocorreu um erro de conexão ao objeto remoto!";
  private static final String VIOLATES_UNIQUE_CONSTRAINT
    = "duplicate key value violates unique constraint";
  private final JTable candidateTable;
  private final Util util = new Util();
  private JFrame frame;
  private CandidateTableModel model;
  private Candidate candidate = new Candidate();
  private JPanel jcontentPane;
  private JPanel jpanel;

  private JLabel jlName = null;
  private JLabel jlCpf = null;
  private JLabel jlBirthday = null;
  private JLabel jlMotherName = null;
  private JLabel jlEmail = null;
  private JLabel jlAddress = null;

  private JTextField jtfName = null;
  private JFormattedTextField jformattedTextCpf = null;
  private JFormattedTextField jformattedTextBirthday = null;
  private JTextField jtfMotherName = null;
  private JTextField jtfEmail = null;
  private JTextField jtfAddress = null;

  private JButton jbInsertCandidate = null;
  private JButton jbSearchCandidate = null;
  private JButton jbUpdateCandidate = null;
  private JButton jbDeleteCandidate = null;
  private JButton jbListAllCandidates = null;
  private JButton jbClearForm = null;
  private JButton jbExit = null;

  /**
   * Constructor.
   */
  private FormCandidate() {
    this.candidateTable = new JTable();
    registerCandidateFrame();
    rmiClienteToRmiServerConnect();
    jcontentPane = null;
    setFocusOnJtextFieldCandidateName();
    isDataBaseReady();
  }

  public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(FormCandidate::new);
  }

  /**
   * Checks whether an available RMI server is waiting for connections and returns an object
   * of the RmiClient class, or terminates the system if there is no ready-made RMI server.
   *
   * @return RmiClient object
   */
  private RmiClient rmiClienteToRmiServerConnect() {
    try {
      return new RmiClient();
    } catch (NotBoundException | IOException e) {
      showMessage("Não há um servidor RMI disponível para conexão! \nO sistema será encerrado.");
      System.exit(0);
      return null;
    }
  }

  /**
   * Configuration parameters of the candidate registration form.
   */
  private void registerCandidateFrame() {
    frame = new JFrame("Cadastro de Candidatos");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(600, 315);
    frame.setResizable(false);
    frame.setContentPane(getJcontentPane());
    Dimension dim;
    dim = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (dim.width - frame.getWidth()) / 2;
    int y = (dim.height - frame.getWidth()) / 2;
    frame.setLocation(x, y + 150);
    frame.setVisible(true);
  }

  private JPanel getJcontentPane() {
    if (jcontentPane == null) {
      jcontentPane = new JPanel();
      jcontentPane.setLayout(new BorderLayout());
      jcontentPane.add(getJpanel(), BorderLayout.CENTER);
    }
    return jcontentPane;
  }

  /**
   * Mounts the candidate registration panel.
   *
   * @return a jpanel
   */
  private JPanel getJpanel() {

    if (jpanel == null) {
      jpanel = new JPanel();
      jpanel.setLayout(null);

      getJlabelName();
      jpanel.add(jlName, null);
      getJlabelCpf();
      jpanel.add(jlCpf, null);
      getJlabelBirthday();
      jpanel.add(jlBirthday, null);
      getJlabelMotherName();
      jpanel.add(jlMotherName, null);
      getJlabelEmail();
      jpanel.add(jlEmail, null);
      getJlabelAddress();
      jpanel.add(jlAddress, null);

      jpanel.add(getJtextFieldName(), null);
      jpanel.add(getJformattedTextCpf(), null);
      jpanel.add(getJformattedTextBirthday(), null);
      jpanel.add(getJtextFieldMotherName(), null);
      jpanel.add(getJtextFieldEmail(), null);
      jpanel.add(getJtextFieldAddress(), null);

      jpanel.add(getJbuttonInsertCandidate(), null);
      jpanel.add(getJbuttonSearchCandidate(), null);
      jpanel.add(getJbuttonUpdateCandidate(), null);
      jpanel.add(getJbuttonDeleteCandidate(), null);
      jpanel.add(getJbuttonListAllCandidates(), null);
      jpanel.add(getJbuttonClearForm(), null);
      jpanel.add(getJbuttonExit(), null);
    }
    return jpanel;
  }

  private void getJlabelName() {
    jlName = new JLabel();
    jlName.setBounds(new Rectangle(13, 40, 170, 16));
    jlName.setText("Nome:");
  }

  private void getJlabelCpf() {
    jlCpf = new JLabel();
    jlCpf.setBounds(new Rectangle(13, 65, 170, 16));
    jlCpf.setText("CPF:");
  }

  private void getJlabelBirthday() {
    jlBirthday = new JLabel();
    jlBirthday.setBounds(new Rectangle(13, 90, 170, 16));
    jlBirthday.setText("Data de Nascimento:");
  }

  private void getJlabelMotherName() {
    jlMotherName = new JLabel();
    jlMotherName.setBounds(new Rectangle(13, 115, 170, 16));
    jlMotherName.setText("Nome da Mãe:");
  }

  private void getJlabelEmail() {
    jlEmail = new JLabel();
    jlEmail.setBounds(new Rectangle(13, 140, 170, 16));
    jlEmail.setText("E-mail:");
  }

  private void getJlabelAddress() {
    jlAddress = new JLabel();
    jlAddress.setBounds(new Rectangle(13, 165, 170, 16));
    jlAddress.setText("Endereço:");
  }

  private JTextField getJtextFieldName() {
    if (jtfName == null) {
      jtfName = new JTextField();
      jtfName.setBounds(new Rectangle(175, 40, 400, 20));
    }
    return jtfName;
  }

  private JFormattedTextField getJformattedTextCpf() {
    MaskFormatter cpfMask = null;
    if (jformattedTextCpf == null) {
      try {
        cpfMask = new MaskFormatter("###.###.###-##");
        cpfMask.setPlaceholderCharacter('_');
      } catch (ParseException e) {
        showMessage(e.getLocalizedMessage());
      }
      jformattedTextCpf = new JFormattedTextField(cpfMask);
      jformattedTextCpf.setFocusLostBehavior(JFormattedTextField.COMMIT);
      jformattedTextCpf.setBounds(new Rectangle(175, 65, 110, 20));
    }
    return jformattedTextCpf;
  }

  private JFormattedTextField getJformattedTextBirthday() {
    MaskFormatter dateMask = null;
    if (jformattedTextBirthday == null) {
      try {
        dateMask = new MaskFormatter("##/##/####");
        dateMask.setPlaceholderCharacter('_');
      } catch (ParseException e) {
        showMessage(e.getLocalizedMessage());
      }
      jformattedTextBirthday = new JFormattedTextField(dateMask);
      jformattedTextBirthday.setFocusLostBehavior(JFormattedTextField.COMMIT);
      jformattedTextBirthday.setBounds(new Rectangle(175, 90, 80, 20));
    }
    return jformattedTextBirthday;
  }

  private JTextField getJtextFieldMotherName() {
    if (jtfMotherName == null) {
      jtfMotherName = new JTextField();
      jtfMotherName.setBounds(new Rectangle(175, 115, 400, 20));
    }
    return jtfMotherName;
  }

  private JTextField getJtextFieldEmail() {
    if (jtfEmail == null) {
      jtfEmail = new JTextField();
      jtfEmail.setBounds(new Rectangle(175, 140, 400, 20));
    }
    return jtfEmail;
  }

  private JTextField getJtextFieldAddress() {
    if (jtfAddress == null) {
      jtfAddress = new JTextField();
      jtfAddress.setBounds(new Rectangle(175, 165, 400, 20));
    }
    return jtfAddress;
  }

  // Método disparado quando o botão Inserir é pressionado
  private JButton getJbuttonInsertCandidate() {
    if (jbInsertCandidate == null) {
      jbInsertCandidate = new JButton();
      jbInsertCandidate.setBounds(new Rectangle(12, 200, 120, 22));
      jbInsertCandidate.setText("Inserir");
      jbInsertCandidate.setMnemonic(KeyEvent.VK_I);
      jbInsertCandidate.addActionListener((java.awt.event.ActionEvent e) -> insertCandidate());

      jbInsertCandidate.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            jbInsertCandidate.doClick();
          }
        }
      });
    }
    return jbInsertCandidate;
  }

  // Método disparado quando o botão Pesquisar é pressionado
  private JButton getJbuttonSearchCandidate() {
    if (jbSearchCandidate == null) {
      jbSearchCandidate = new JButton();
      jbSearchCandidate.setBounds(new Rectangle(157, 200, 120, 22));
      jbSearchCandidate.setText("Pesquisar");
      jbSearchCandidate.setMnemonic(KeyEvent.VK_P);
      jbSearchCandidate.addActionListener((java.awt.event.ActionEvent e)
        -> searchCandidates(false));
      jbSearchCandidate.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            jbSearchCandidate.doClick();
          }
        }
      });
    }
    return jbSearchCandidate;
  }

  // Método disparado quando o botão Atualizar é pressionado
  private JButton getJbuttonUpdateCandidate() {
    if (jbUpdateCandidate == null) {
      jbUpdateCandidate = new JButton();
      jbUpdateCandidate.setBounds(new Rectangle(305, 200, 120, 22));
      jbUpdateCandidate.addActionListener((java.awt.event.ActionEvent e) -> updateCandidate());
      jbUpdateCandidate.setText("Atualizar");
      jbUpdateCandidate.setMnemonic(KeyEvent.VK_A);
      jbUpdateCandidate.setEnabled(false);

      jbUpdateCandidate.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            jbUpdateCandidate.doClick();
          }
        }
      });
    }
    return jbUpdateCandidate;
  }

  // Método disparado quando o botão Excluir é pressionado
  private JButton getJbuttonDeleteCandidate() {
    if (jbDeleteCandidate == null) {
      jbDeleteCandidate = new JButton();
      jbDeleteCandidate.setBounds(new Rectangle(485, 200, 90, 22));
      jbDeleteCandidate.addActionListener(e -> FormCandidate.this.deleteCandidate());
      jbDeleteCandidate.setText("Excluir");
      jbDeleteCandidate.setMnemonic(KeyEvent.VK_E);
      jbDeleteCandidate.setEnabled(false);

      jbDeleteCandidate.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            jbDeleteCandidate.doClick();
          }
        }
      });
    }
    return jbDeleteCandidate;
  }

  // Método disparado quando o botão "Listar todos os Candidatos" é
  // pressionado
  private JButton getJbuttonListAllCandidates() {
    if (jbListAllCandidates == null) {
      jbListAllCandidates = new JButton();
      jbListAllCandidates.setBounds(new Rectangle(12, 235, 265, 22));
      jbListAllCandidates.setText("Listar todos os Candidatos");
      jbListAllCandidates.setMnemonic(KeyEvent.VK_L);
      jbListAllCandidates.addActionListener((java.awt.event.ActionEvent e)
        -> searchCandidates(true));
      jbListAllCandidates.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            jbListAllCandidates.doClick();
          }
        }
      });
    }
    return jbListAllCandidates;
  }

  // Método disparado quando o botão "Listar todos os Candidatos" é
  // pressionado
  private JButton getJbuttonClearForm() {
    if (jbClearForm == null) {
      jbClearForm = new JButton();
      jbClearForm.setBounds(new Rectangle(305, 235, 120, 22));
      jbClearForm.setText("Limpar");
      jbClearForm.setMnemonic(KeyEvent.VK_M);
      jbClearForm.addActionListener((java.awt.event.ActionEvent e) -> cleanFormCandidate());
      jbClearForm.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            jbClearForm.doClick();
          }
        }
      });
    }
    return jbClearForm;
  }

  /**
   * Action triggered by the "Sair" button.
   *
   * @return exit button
   */
  private JButton getJbuttonExit() {
    if (jbExit == null) {
      jbExit = new JButton();
      jbExit.setBounds(new Rectangle(485, 235, 90, 22));
      jbExit.addActionListener((java.awt.event.ActionEvent e) -> System.exit(0));
      jbExit.setText("Sair");
      jbExit.setMnemonic(KeyEvent.VK_S);
      jbExit.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            jbExit.doClick();
          }
        }
      });
    }
    return jbExit;
  }

  /**
   * Displays a message to the user.
   *
   * @param message to be displayed to the user
   */
  private void showMessage(String message) {
    JOptionPane.showMessageDialog(null, message);
  }

  /**
   * Clear form fields.
   */
  private void cleanFormCandidate() {
    jtfName.setText(null);
    jformattedTextCpf.setText(null);
    jformattedTextBirthday.setText(null);
    jtfMotherName.setText(null);
    jtfEmail.setText(null);
    jtfAddress.setText(null);
    enableDisableUpdateAndDeleteButtons(false);
    setFocusOnJtextFieldCandidateName();
  }

  private void setFocusOnJtextFieldCandidateName() {
    jtfName.setVisible(true);
    jtfName.requestFocusInWindow();
  }

  private void setFocusOnJtextFieldCandidateCpf() {
    jformattedTextCpf.setVisible(true);
    jformattedTextCpf.requestFocusInWindow();
  }

  private void setFocusOnJtextFieldCandidateBirthday() {
    jformattedTextBirthday.setVisible(true);
    jformattedTextBirthday.requestFocusInWindow();
  }

  private void setFocusOnJtextFieldCandidateEmail() {
    jtfEmail.setVisible(true);
    jtfEmail.requestFocusInWindow();
  }

  /**
   * Enables or disables the "Atualizar" and "Excluir" buttons on the form.
   *
   * @param value true for enable, false for disable the buttons
   */
  private void enableDisableUpdateAndDeleteButtons(boolean value) {
    jbUpdateCandidate.setEnabled(value);
    jbDeleteCandidate.setEnabled(value);
  }

  /**
   * Fills out the form data.
   *
   * @param candidate object
   */
  private void fillDataFormCandidate(Candidate candidate) {
    jtfName.setText(candidate.getCandidateName());
    jformattedTextCpf.setText(candidate.getCpf());
    jformattedTextBirthday.setText(util.convertLocalDateToString(candidate.getBirthday()));
    jtfMotherName.setText(candidate.getMotherName());
    jtfEmail.setText(candidate.getEmail());
    jtfAddress.setText(candidate.getAddress());
    jbUpdateCandidate.setEnabled(true);
    jbDeleteCandidate.setEnabled(true);
  }

  /**
   * Get candidate form data.
   *
   * @return all the data entered in the form
   */
  private String[] getCandidateFormData() {
    return new String[]{
      jtfName.getText().trim(),
      //jformattedTextCpf.getText().replaceAll("[.-]", "").trim(),
      util.getFormCpfWithoutMask(jformattedTextCpf.getText()),
      jformattedTextBirthday.getText().trim(),
      jtfMotherName.getText().trim(),
      jtfEmail.getText().trim(),
      jtfAddress.getText().trim()};
  }

  /**
   * Count the number of fields filled in the form.
   *
   * @param dataFormCandidate is an array of strings corresponding to the fields present in
   *                          the candidate registration form
   * @return an integer corresponding to the number of fields filled in the form
   */
  private int fieldsAreFilledInTheForm(String[] dataFormCandidate) {
    return (int) Arrays.stream(dataFormCandidate).map(
      data -> data.replaceAll("[-./_ ]", "")).filter(data -> !data.isEmpty()).count();
  }

  /**
   * Fill in the candidate object with the data coming from the form.
   *
   * @param candidateData data from the candidate registration form
   * @return a candidate object
   */
  private Candidate setCandidateData(String[] candidateData) {
    this.candidate.setCandidateName(candidateData[0]);
    this.candidate.setCpf(candidateData[1]);
    if (!util.getFormBirthdayWithoutMask(candidateData[2]).equals("")) {
      this.candidate.setBirthday(util.convertStringToLocalDate(candidateData[2]));
    } else {
      this.candidate.setBirthday(null);
    }
    this.candidate.setMotherName(candidateData[3]);
    this.candidate.setEmail(candidateData[4]);
    this.candidate.setAddress(candidateData[5]);
    return this.candidate;
  }

  /**
   * Verifies that the database is active and responding.
   *
   * @return true if a database server is available
   */
  private boolean isDataBaseReady() {
    try {
      rmiClienteToRmiServerConnect().dataBaseReady();
    } catch (RemoteException e) {
      showMessage(RMI_CONNECTION_ERROR);
      return false;
    } catch (SQLException e) {
      showMessage(DATABASE_CONNECTION_ERROR);
      return false;
    }
    return true;
  }

  /**
   * Checks for data compliance before attempting an insert or update.
   *
   * @param candidateFormData candidate data array
   * @return a message stating whether something is not in compliance
   */
  private String whyDataIsNotValidForInsertionOrUpdate(String[] candidateFormData) {

    String message;

    // Verifica se todos os campos do fomulário foram preenchidos
    if (fieldsAreFilledInTheForm(candidateFormData) != 6) {
      message = "Ao incluir ou atualizar um candidato é obrigatório\nque"
        + " todos os campos estejam preenchidos!";
      setFocusOnJtextFieldCandidateName();
      return message;
    }

    try {
      util.unformattedValidCpf(util.getFormCpfWithoutMask(candidateFormData[1]));
    } catch (InvalidStateException e) {
      message = "O documento CPF informado é inválido!";
      setFocusOnJtextFieldCandidateCpf();
      return message;
    }

    try {
      util.validDate(candidateFormData[2]);
    } catch (DateTimeParseException e) {
      message = "A data de nascimento informada é inválida!";
      setFocusOnJtextFieldCandidateBirthday();
      return message;
    }

    try {
      util.validEmail(candidateFormData[4]);
    } catch (AddressException e) {
      message = "O e-mail informado é inválido!";
      setFocusOnJtextFieldCandidateEmail();
      return message;
    }
    return null;
  }

  /**
   * Inserts a new candidate into the system.
   */
  private void insertCandidate() {
    if (!isDataBaseReady()) {
      return;
    }

    String[] candidateFormData = getCandidateFormData();

    String message = whyDataIsNotValidForInsertionOrUpdate(candidateFormData);
    if (message != null) {
      showMessage(message);
      return;
    }

    try {
      rmiClienteToRmiServerConnect().insertCandidate(setCandidateData(candidateFormData));
      showMessage("Candidato inserido com sucesso!");
      cleanFormCandidate();
    } catch (RemoteException e) {
      showMessage(RMI_CONNECTION_ERROR);
      setFocusOnJtextFieldCandidateName();
    } catch (SQLException e) {
      if (e.getMessage().contains(VIOLATES_UNIQUE_CONSTRAINT)
        && e.getMessage().contains("cpf")) {
        showMessage("Falhou tentativa de inserção: CPF duplicado!\nJá existe um candidato"
          + " cadastrado no sistema\ncom este CPF.");
        setFocusOnJtextFieldCandidateCpf();
        return;
      } else {
        if (e.getMessage().contains(VIOLATES_UNIQUE_CONSTRAINT)
          && e.getMessage().contains("email")) {
          showMessage("Falhou tentativa de inserção: e-mail duplicado!\nJá existe um candidato"
            + " cadastrado no sistema\ncom este e-mail.");
          setFocusOnJtextFieldCandidateEmail();
          return;
        } else {
          showMessage(DATABASE_CONNECTION_ERROR);
          setFocusOnJtextFieldCandidateName();
        }
      }
      setFocusOnJtextFieldCandidateName();
    }
  }

  /**
   * Checks for data compliance before attempting a search.
   *
   * @param candidateFormData candidate data array
   * @return a message stating whether something is not in compliance
   */
  private String whyDataIsNotValidForSearch(String[] candidateFormData) {

    String message;

    if (fieldsAreFilledInTheForm(candidateFormData) == 0) {
      message = "Ao menos um campo deve ser preenchido para realizar a pesquisa!";
      setFocusOnJtextFieldCandidateName();
      return message;
    }
    try {
      if (!util.getFormBirthdayWithoutMask(candidateFormData[2]).equals("")) {
        util.validDate(candidateFormData[2]);
      }
    } catch (DateTimeParseException e) {
      message = "A data de nascimento informada é inválida!";
      setFocusOnJtextFieldCandidateBirthday();
      return message;
    }
    return null;
  }

  /**
   * Search for candidates previously registered in the system.
   * Returns candidates who match the reported criteria.
   */
  private void searchCandidates(boolean returnAll) {
    String[] candidateFormData = getCandidateFormData();
    Collection<Candidate> candidateCollection;

    if (!isDataBaseReady()) {
      return;
    }

    try {
      // Retorna candidatos de acordo com os critérios informados na pesquisa
      if (!returnAll) {

        String message = whyDataIsNotValidForSearch(candidateFormData);
        if (message != null) {
          showMessage(message);
          return;
        }

        candidateCollection = rmiClienteToRmiServerConnect()
          .searchCandidates(setCandidateData(candidateFormData), false, false);
      } else {
        // Retorna todos os candidatos cadastrados no sistema
        candidateCollection = rmiClienteToRmiServerConnect()
          .searchCandidates(null, true, false);
      }
    } catch (RemoteException e) {
      showMessage(RMI_CONNECTION_ERROR);
      return;
    } catch (SQLException e) {
      showMessage(DATABASE_CONNECTION_ERROR);
      return;
    }
    if (!candidateCollection.isEmpty()) {
      displayTableGrid((HashSet<Candidate>) candidateCollection);
    } else {
      try {
        candidateCollection = rmiClienteToRmiServerConnect()
          .searchCandidates(null, false, true);
        if (candidateCollection.isEmpty()) {
          showMessage("Não há candidatos cadastrados no sistema.\nO sistema está vazio!");
        } else {
          showMessage("Não foram localizados candidatos com base\nnos critérios"
            + " informados na pesquisa!");
        }
        setFocusOnJtextFieldCandidateName();
      } catch (RemoteException e) {
        showMessage(RMI_CONNECTION_ERROR);
      } catch (SQLException e) {
        showMessage(DATABASE_CONNECTION_ERROR);
      }
    }
  }

  /**
   * Updates candidate data.
   */
  private void updateCandidate() {

    if (!isDataBaseReady()) {
      return;
    }

    String[] candidateFormData = getCandidateFormData();

    String message = whyDataIsNotValidForInsertionOrUpdate(candidateFormData);
    if (message != null) {
      showMessage(message);
      return;
    }

    try {
      rmiClienteToRmiServerConnect().updateCandidate(setCandidateData(candidateFormData));
      showMessage("Os dados do candidato foram atualizados com sucesso!");
      cleanFormCandidate();
      setFocusOnJtextFieldCandidateName();
    } catch (RemoteException e) {
      showMessage(RMI_CONNECTION_ERROR);
      setFocusOnJtextFieldCandidateName();
    } catch (SQLException e) {
      if (e.getMessage().contains(VIOLATES_UNIQUE_CONSTRAINT)
        && e.getMessage().contains("cpf")) {
        showMessage("Falhou tentativa de atualização: CPF duplicado!\nJá existe um candidato"
          + " cadastrado no sistema\ncom este CPF.");
        setFocusOnJtextFieldCandidateCpf();
      } else {
        if (e.getMessage().contains(VIOLATES_UNIQUE_CONSTRAINT)
          && e.getMessage().contains("email")) {
          showMessage("Falhou tentativa de atualização: e-mail duplicado!\nJá existe um candidato"
            + " cadastrado no sistema\ncom este e-mail.");
          setFocusOnJtextFieldCandidateEmail();
        } else {
          showMessage(DATABASE_CONNECTION_ERROR);
          setFocusOnJtextFieldCandidateName();
        }
      }
      setFocusOnJtextFieldCandidateName();
    }
  }

  /**
   * Delete a candidate.
   */
  private void deleteCandidate() {
    if (!isDataBaseReady()) {
      return;
    }
    try {
      rmiClienteToRmiServerConnect().deleteCandidate(setCandidateData(getCandidateFormData()));
      showMessage("Candidato excluído com sucesso!");
      setFocusOnJtextFieldCandidateName();
    } catch (RemoteException e) {
      showMessage(RMI_CONNECTION_ERROR);
      setFocusOnJtextFieldCandidateName();
      return;
    } catch (SQLException e) {
      showMessage(DATABASE_CONNECTION_ERROR);
      setFocusOnJtextFieldCandidateName();
      return;
    }
    cleanFormCandidate();
    setFocusOnJtextFieldCandidateName();
  }

  /**
   * Displays the grid containing the returned candidates in the search.
   *
   * @param candidateCollection a collection of candidates returned from the database
   */
  private void displayTableGrid(HashSet<Candidate> candidateCollection) {
    List<Candidate> listCandidates = new ArrayList<>(candidateCollection);
    Collections.sort(listCandidates);
    EventQueue.invokeLater(() -> jtableDisplay(listCandidates));
  }

  /**
   * Displays a frame containing a grid with all candidates already registered.
   *
   * @param listCandidates a list of all candidates returned from the database
   */
  private void jtableDisplay(List<Candidate> listCandidates) {
    JFrame frameCandidateTable = new JFrame("Lista de candidatos cadastrados");
    JPanel panelCandidateTable = new JPanel();
    frameCandidateTable.getContentPane().add(panelCandidateTable);
    this.model = new CandidateTableModel(listCandidates);
    this.candidateTable.setModel(this.model);
    JScrollPane scroll = new JScrollPane(this.candidateTable,
      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    frameCandidateTable.add(scroll);
    this.candidateTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    adjustColumnSize();
    frameCandidateTable.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - frame.getWidth()) / 2;
    int y = (screenSize.height - frame.getWidth()) / 2;
    frameCandidateTable.setLocation(x + 10, y + 190);
    frameCandidateTable.setSize(580, 260);
    frameCandidateTable.setVisible(true);

    // Dispara evento quando se clica sobre um registro da grid
    this.candidateTable.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
        jtableCandidateMouseClicked();
        // O evento disparado preenche o FormCandidate com os dados do registro selecionado
        // na grid
        frameCandidateTable.dispose();
        enableDisableUpdateAndDeleteButtons(true);
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // Not used

      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // Not used
      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // Not used
      }

      // Not used
      @Override
      public void mouseExited(MouseEvent e) {
        // Not used
      }
    });

    this.candidateTable.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          // O evento disparado fecha a grid
          frameCandidateTable.dispose();
          setFocusOnJtextFieldCandidateName();
        }
      }
    });

    this.candidateTable.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          jtableCandidateMouseClicked();
          // O evento disparado preenche o FormCandidate com os dados do registro selecionado
          // na grid
          frameCandidateTable.dispose();
          enableDisableUpdateAndDeleteButtons(true);
        }
      }
    });
  }

  /**
   * Select (mouse click event) the grid candidate and fill out the form with your data.
   * * @param evt
   */
  private void jtableCandidateMouseClicked() {
    this.candidate = this.model.getCandidate(
      candidateTable.getSelectedRow());
    fillDataFormCandidate(this.candidate);
    setFocusOnJtextFieldCandidateName();
  }

  /**
   * Sets the size of table columns.
   */
  private void adjustColumnSize() {
    candidateTable.getColumnModel().getColumn(0).setPreferredWidth(40);
    candidateTable.getColumnModel().getColumn(1).setPreferredWidth(240);
    candidateTable.getColumnModel().getColumn(2).setPreferredWidth(110);
    candidateTable.getColumnModel().getColumn(3).setPreferredWidth(80);
    candidateTable.getColumnModel().getColumn(4).setPreferredWidth(240);
    candidateTable.getColumnModel().getColumn(5).setPreferredWidth(150);
    candidateTable.getColumnModel().getColumn(6).setPreferredWidth(300);
  }
}
