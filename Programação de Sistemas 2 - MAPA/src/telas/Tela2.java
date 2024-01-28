package telas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import modelos.Responsavel;
import modelos.Crianca;

public class Tela2 extends JFrame {
    private Responsavel responsavel;

    public Tela2(Responsavel responsavel) {
        this.responsavel = responsavel;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Etapa 2 de 3 - Cadastro de Criança");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        panel.add("North", titleLabel);

        String nomeResponsavel = responsavel.getNome();
        String[] labels = { "Responsável", "Nome", "Idade", "Sexo" };

        ArrayList<JTextField> textFields = new ArrayList<>();

        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JTextField jTextField = new JTextField(20);
            if (label.equals("Responsável")) {
                jTextField.setEnabled(false);
                jTextField.setText(nomeResponsavel);
            }
            textFields.add(jTextField);

            JPanel rowPanel = new JPanel(new GridLayout(1, 2));
            rowPanel.add(jLabel);
            rowPanel.add(jTextField);
            rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));
            panel.add(rowPanel);
        }

        JButton proximoBotao = new JButton("Próximo");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        buttonPanel.add(proximoBotao);
        panel.add(buttonPanel);

        proximoBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeCrianca = textFields.get(1).getText();
                int idadeCrianca = Integer.parseInt(textFields.get(2).getText());
                String sexo = textFields.get(3).getText();

                try {
                    Crianca crianca = new Crianca(responsavel, nomeCrianca, idadeCrianca, sexo);
                    Tela3 tela3 = new Tela3(crianca);
                    tela3.setVisible(true);
                    dispose();
                    
                } catch (Exception f) {
                    JOptionPane.showMessageDialog(panel, f.getMessage(), "Ocorreu um erro", JOptionPane.WARNING_MESSAGE);
                }                
            }
        });

        JFrame frame = new JFrame("Tela2");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
