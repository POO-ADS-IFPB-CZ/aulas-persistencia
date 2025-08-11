package view;

import dao.GenericDao;
import exceptions.AlunoExisteException;
import exceptions.MatriculaInvalidaException;
import exceptions.NomeInvalidoException;
import model.Aluno;
import service.AlunoService;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.io.IOException;
import java.text.ParseException;

public class TelaPrincipal extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField campoNome;
    private JComboBox comboCurso;
    private JFormattedTextField campoMatricula;
    private JButton listarButton;
    //Remover o genericDao após refatorar tudo
    private GenericDao<Aluno> alunoDao;
    private AlunoService alunoService;

    public TelaPrincipal() {

        try {
            alunoService = new AlunoService();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Falha ao conectar com arquivo");
        }

        try {
            alunoDao = new GenericDao<>("alunos.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Falha ao abrir arquivo",
                    "Mensagem de erro",
                    JOptionPane.ERROR_MESSAGE);
        }

        setContentPane(contentPane);
        setTitle("IFPB - Cajazeiras");
        setAlwaysOnTop(true);
        ImageIcon icon = new ImageIcon("src/imgs/chapeu.png");
        setIconImage(icon.getImage());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getRootPane().setDefaultButton(buttonOK);

        buttonCancel.addActionListener(e -> System.exit(0));
        buttonOK.addActionListener(e ->{
            if (validarFormulario()){
                long matricula = Long.parseLong(campoMatricula.getText());
                String nome = campoNome.getText();
                String curso = (String) comboCurso.getSelectedItem();
                Aluno aluno = new Aluno(matricula, nome, curso);
                try {
                    if(alunoService.salvar(aluno)){
                        JOptionPane.showMessageDialog(
                                null,
                                "Aluno salvo com sucesso!");
                    }else{
                        JOptionPane.showMessageDialog(null,
                                "Já existe aluno com essa matrícula",
                                "Mensagem de erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Falha ao manipular arquivo",
                            "Mensagem de erro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (NomeInvalidoException ex) {
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage());
                } catch (AlunoExisteException ex) {
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage());
                } catch (MatriculaInvalidaException ex) {
                    JOptionPane.showMessageDialog(null,
                            ex.getMessage());
                }
            }
        });
        listarButton.addActionListener(e->{
            TelaVisualizarAluno visualizarAluno = new TelaVisualizarAluno();
            visualizarAluno.setVisible(true);
        });
    }

    //TODO: Refatorar esse código...
    private boolean validarFormulario() {
        if(campoMatricula.getText().equals("            ")){
            JOptionPane.showMessageDialog(null,
                    "Digite sua matrícula",
                    "Mensagem de Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(campoNome.getText().equals("")){
            JOptionPane.showMessageDialog(null,
                    "Informe o seu nome",
                    "Mensagem de erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(comboCurso.getSelectedIndex()==0){
            JOptionPane.showMessageDialog(null,
                    "Escolha um curso",
                    "Mensagem de Erro",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        TelaPrincipal dialog = new TelaPrincipal();
        dialog.pack();
        dialog.setVisible(true);
    }

    private void createUIComponents() {
        try {
            MaskFormatter formatter = new MaskFormatter("############");
            campoMatricula = new JFormattedTextField(formatter);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
