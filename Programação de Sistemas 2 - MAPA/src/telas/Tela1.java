package telas;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import modelos.Responsavel;

public class Tela1 extends JFrame {

    public Tela1() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Etapa 1 de 3 - Cadastro de Responsável");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        panel.add("North", titleLabel);

        String[] labels = { "Nome", "CPF", "Telefone", "Email", "Endereço", "Idade" };
        ArrayList<JTextField> textFields = new ArrayList<>();

        for (String label : labels) {
            JLabel jLabel = new JLabel(label);
            jLabel.setFont(new Font("Arial", Font.BOLD, 14));

            JTextField jTextField = new JTextField(20);
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
                String nome = textFields.get(0).getText();
                String cpf = textFields.get(1).getText();
                String telefone = textFields.get(2).getText();
                String email = textFields.get(3).getText();
                String endereco = textFields.get(4).getText();
                int idade = Integer.parseInt(textFields.get(5).getText());
                
                try {
                    Responsavel responsavel = new Responsavel(nome, cpf, telefone, email, endereco, idade);
                    Tela2 tela2 = new Tela2(responsavel);
                    tela2.setVisible(true);
                    dispose();
            
                } catch (Exception f) {
                    System.out.println(f.getMessage());
                    JOptionPane.showMessageDialog(panel, f.getMessage(), "Ocorreu um erro", JOptionPane.WARNING_MESSAGE);
                }
               
            }
        });

        JFrame frame = new JFrame("Tela1");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
