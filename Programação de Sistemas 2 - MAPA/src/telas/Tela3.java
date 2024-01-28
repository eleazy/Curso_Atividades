package telas;
import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import modelos.Crianca;
import modelos.Estadia;

public class Tela3 extends JFrame {
    private Crianca crianca;

    public Tela3(Crianca crianca) {
        this.crianca = crianca;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));        
        JLabel titleLabel = new JLabel("Etapa 3 de 3 - Dados da Estadia");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));       
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        panel.add("North", titleLabel);

        String[] labels = {"Responsável", "Criança", "Tempo Utilizado"};
        ArrayList<JTextField> textFields = new ArrayList<>();

        for (String label : labels) {
            JLabel jLabel = new JLabel(label);     
            jLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JTextField jTextField = new JTextField(20);     
            if (label.equals("Responsável")) { 
                jTextField.setEnabled(false); 
                jTextField.setText(crianca.getResponsavel().getNome());
            } 
            if (label.equals("Criança")) { 
                jTextField.setEnabled(false); 
                jTextField.setText(crianca.getNome());
            }      
            textFields.add(jTextField);
            
            JPanel rowPanel = new JPanel(new GridLayout(1, 2));
            rowPanel.add(jLabel);
            rowPanel.add(jTextField);
            rowPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 2));            
            panel.add(rowPanel);
        }

        JButton calcularBotao = new JButton("Calcular Cobrança");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));        
        buttonPanel.add(calcularBotao);
        panel.add(buttonPanel);

        Map<String, Function<String, String>> resumo = new HashMap<>();
        resumo.put("Responsável:", (String s) -> crianca.getResponsavel().getNome());
        resumo.put("CPF:", (String s) -> crianca.getResponsavel().getCpf());
        resumo.put("Telefone:", (String s) -> crianca.getResponsavel().getTelefone());
        resumo.put("Email:", (String s) -> crianca.getResponsavel().getEmail());
        resumo.put("Endereço:", (String s) -> crianca.getResponsavel().getEndereco());
        resumo.put("Idade Responsável:", (String s) -> String.valueOf(crianca.getResponsavel().getIdade()));
        resumo.put("Criança:", (String s) -> crianca.getNome());
        resumo.put("Idade Criança:", (String s) -> String.valueOf(crianca.getIdade()));
        resumo.put("Sexo Criança:", (String s) -> crianca.getSexo());
        resumo.put("Tempo Utilizado:", (String s) -> textFields.get(2).getText());

        calcularBotao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tempoUtilizado = Integer.parseInt(textFields.get(2).getText());
                Estadia estadia = new Estadia(crianca.getResponsavel(), crianca, tempoUtilizado);
                double valorCobrado = estadia.calcularCustoEstadia();
                JLabel valorCobradoLabel = new JLabel("Valor Cobrado: R$" + valorCobrado);
                
                JDialog resumoMensagem = new JDialog();
                resumoMensagem.setTitle("Resumo");
                resumoMensagem.setResizable(false);
                resumoMensagem.setSize(new Dimension(400, 500));
                resumoMensagem.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                JPanel resumoPanel = new JPanel();
                resumoPanel.setLayout(new BoxLayout(resumoPanel, BoxLayout.Y_AXIS));
                resumoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel titleLabel = new JLabel("Dados da Estadia");
                titleLabel.setFont(new Font("Arial", Font.BOLD, 18));       
                titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
                resumoPanel.add("North", titleLabel);

                for (String key : resumo.keySet()){
                    JLabel info = new JLabel(key +"  " +resumo.get(key).apply(key));
                    info.setFont(new Font("Arial", Font.BOLD, 14));
                    info.setBorder(BorderFactory.createEmptyBorder(7, 0, 0, 0));
                    resumoPanel.add(info);                   
                }

                valorCobradoLabel.setFont(new Font("Arial", Font.BOLD, 14));
                valorCobradoLabel.setBorder(BorderFactory.createEmptyBorder(7, 0, 0, 0));
                resumoPanel.add(valorCobradoLabel);
                resumoMensagem.add(resumoPanel);

                resumoMensagem.pack();
                resumoMensagem.setVisible(true);
            }
        });

        JFrame frame = new JFrame("Tela3");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
