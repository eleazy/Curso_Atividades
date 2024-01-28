import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

/**
* Descrição do programa: Sistema de cadastro de doadores de sangue. Armazena os dados em um arquivo CSV, 
* efetua leitura, inclusão e exclusão de registros. 
* 
* @author Eleazy Soares da Silva
* @version 1.0
* 
*/
public class App {

    /**
     * Classe interna para armazenar os código das linhas do arquivo CSV.
     * 
     * @param ids array contendo os códigos
    */
    public class CsvFile {
        private static String[] ids;

        public CsvFile(String[] ids) {
            App.CsvFile.ids = ids;
        }            

        public static String[] getIds() {
            return ids;
        }
    }

    /**
     * Método principal. Inicia o scanner e efetua a leitura do caminho do arquivo CSV através do console.
     * @param args
    */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o caminho do arquivo:");
        String csvPath = scanner.nextLine(); 
        displayMenu(scanner, csvPath);
                
        scanner.close();
    }

    /**
     * Exibe o menu de opções, efetua a leitura da opção selecionada pelo usuário e chama o método correspondente.      
     * 
     * @param scanner objeto scanner
     * @param csvPath caminho do arquivo CSV
    */
    public static void displayMenu(Scanner scanner, String csvPath) {
        displayTable(csvPath);  /* A tabela contendo os registros é exibida sempre que o menu é chamado. */

        String[] menuOption = {"1. Adicionar registro", "2. Excluir registro","3. Trocar o caminho do arquivo", "4. Sair"}; 
        String choice;

        System.out.println("\n\nSelecione uma opção:");
        while (true) {
            for (String option : menuOption) System.out.println(option);        
            choice = scanner.nextLine();   
            if (choice.matches("[1-4]")) break;           
            System.out.println("\nOpção inválida. Tente novamente.");            
        }/*  Loop para garantir que o usuário digite uma opção válida. Percorre o array menuOption e exibe as opções. */

        if (choice.equals("1")) {
            addData(scanner, csvPath);
            displayMenu(scanner, csvPath); /*  Menu é chamado novamente nas opções 1 e 2 para exibir a tabela atualizada e permitir que o usuário continue usando o programa. */
        } else if (choice.equals("2")) {
            deleteData(scanner, csvPath);
            displayMenu(scanner, csvPath); 
        } else if (choice.equals("3")) {/*  Para trocar o caminho do arquivo, o programa é reiniciado. */
            String[] args = {};
            main(args);
        } else {
            System.out.println("\nPrograma encerrado.");
        }
    }

    /**
     * Exibe a tabela contendo os registros e armazena os códigos em uma instância da classe CsvFile.
     * 
     * @param csvPath caminho do arquivo CSV
    */
    public static void displayTable(String csvPath) {
        try (BufferedReader records = new BufferedReader(new FileReader(csvPath))) {
            ArrayList<String> temp = new ArrayList<>();
            ArrayList<String> ids = new ArrayList<>();
            String line;

            while ((line = records.readLine()) != null) {  /* Percorre o arquivo e extraí os dados de cada linha, armazenando-os em um array. */
                ids.add(line.substring(0, line.indexOf(','))); 

                String[] fields = line.split(",");
                for (String field : fields){
                    temp.add(field);
                }
            }      
            String[] idsArray = ids.toArray(new String[0]);
            new App().new CsvFile(idsArray); /* Os códigos são guardados em um objeto da classe CsvFile. */
            
            String[] rawLines = temp.toArray(new String[0]);
            System.out.print(formatColumns(rawLines, 6)); /* A tabela é formatada e exibida no console. */

        } catch (FileNotFoundException e) {
            System.out.println("O arquivo não foi encontrado. Verifique o caminho e tente novamente."); /* Caso o arquivo não seja encontrado, o programa é reiniciado. */
            String[] args = {};
            main(args);
        } catch (IOException e) {
            e.printStackTrace(); 
        } 
    }

    /**
     * Adiciona um novo registro ao arquivo CSV. Solicita e coleta dados do usuário, verifica se são válidos e os armazena no arquivo.
     * 
     * @param scanner objeto scanner
     * @param csvPath caminho do arquivo CSV
    */
    public static void addData(Scanner scanner, String csvPath) {
        String[] fields = {"Código (apenas números)","Nome e Sobrenome", "CPF (no formato: 000000000-00)", "Data de nascimento (no formato: aaaa-mm-dd)", "Tipo Sanguíneo (exemplo: O+, AB-)", "Quantidade doada (ml)"};
        String[] newLine = new String[fields.length];

        System.out.println("Adicionar registro:\n");
        for (int i = 0; i < fields.length; i++) { /* Percore o array fields e exibe cada campo. */
            System.out.println("\n"+ fields[i] + ":");
            newLine[i] = scanner.nextLine();            
            if (!isValid(newLine[i], i)) i--;  /* Após a leitura de cada campo, o método isValid verifica o valor inserido. Caso não seja válido, o índice é decrementado para que o usuário digite novamente.  */
        }
        newLine[1] = formatName(newLine[1]); 
        newLine[4] = newLine[4].toUpperCase(); 

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath, true))) { /* Após validar e tratar todas as entradas, o arquivo é aberto em modo de escrita e o novo registro é adicionado. */
            String line = String.join(",", newLine);
            writer.write( "\n" + line );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exclui um registro do arquivo CSV através do código inserido pelo usuário. Exclusão é realizada copiando todos os registros para um arquivo temporário, exceto o registro a ser excluído.
     * 
     * @param scanner objeto scanner
     * @param csvPath caminho do arquivo CSV
    */
    public static void deleteData(Scanner scanner, String csvPath) {
        System.out.println("\nDigite o código da linha que deseja excluir:");
        String idToDelete = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath));
            BufferedWriter writer = new BufferedWriter(new FileWriter("temp.csv"))) { /*  O arquivo é aberto em modo de leitura e escrita. Uma cópia temporária é criada para armazenar os registros que não serão excluídos. */

            String line;
            while ((line = reader.readLine()) != null) { /*  Percorre o arquivo e verifica se o código da linha é igual ao código digitado pelo usuário. Caso não seja, a linha é copiada para o arquivo temporário. */
                if (!line.substring(0, line.indexOf(',')).equals(idToDelete)) {
                    writer.write(line + System.lineSeparator());
                }                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(csvPath); /* O arquivo original é excluído e o arquivo temporário é renomeado para o nome do arquivo original. */
        if (file.delete()) {
            boolean rename = new java.io.File("temp.csv").renameTo(new java.io.File(csvPath));
            if (rename) {
                System.out.println("O registro de código " + idToDelete + " foi excluído.");
            } else {
                System.err.println("Ocorreu um erro ao excluir o registro.");
            }
        } else {
            System.err.println("Ocorreu um erro ao excluir o registro.");
        }
    }

    /**
     * Verifica se o valor inserido pelo usuário é válido, comparando-o com uma expressão regular. 
     * 
     * @param newLine valor inserido pelo usuário
     * @param index índice do campo
     * @return true se o valor for válido, false se não for
    */
    public static boolean isValid(String newLine, Integer index) {     
        switch (index) {
            case 0:            
            if (!newLine.matches("\\d+") || (Arrays.asList(CsvFile.getIds()).contains(newLine))) {
                System.out.println("Código inválido ou já existente.");
                return false;
            }
            break;
            case 1:
            if (!newLine.matches("^[A-Za-z -]+ [A-Za-z -]+$")) {
                System.out.println("Nome inválido. Digite apenas letras e espaços.");
                return false;
            } 
            break;
            case 2:
            if (!newLine.matches("^\\d{9}-\\d{2}$")) {
                System.out.println("CPF inválido");
                return false;
            }
            break;
            case 3:
            if (!newLine.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])")) {
                System.out.println("Data de nascimento inválida.");
                return false;
            }
            break;
            case 4:
            if (!newLine.matches("^[AaBbOo]+[\\+\\-]?$")) {
                System.out.println("Tipo sanguíneo inválido. Digite A, B, AB ou O e o fator RH (+ ou -).");
                return false;
            }
            break;
            case 5:
            if (!newLine.matches("\\d+")) {
                System.out.println("Quantidade doada inválida. Digite apenas números.");
                return false;
            }
            break;
            default:
            break;
        }        
        return true;
    }
    
    /**
     * Formata o nome para que a primeira letra de cada palavra seja maiúscula e as demais minúsculas.
     * 
     * @param name nome completo
     * @return nome formatado
    */
    public static String formatName(String name) {
        String[] parts = name.split(" ");
        StringBuilder formattedName = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                formattedName.append(part.substring(0, 1).toUpperCase());
                formattedName.append(part.substring(1).toLowerCase()); 
                formattedName.append(" "); 
            }
        }

        String result = formattedName.toString().trim(); 
        return result;
    }

    /**
     * Formata a tabela para que os campos fiquem alinhados.
     * 
     * @param input array contendo os dados da tabela
     * @param colNum número de colunas
     * @return tabela formatada como string
    */
    public static String formatColumns(String[] input, int colNum) {   
        int len = 0;
        for (int d = 0; d < colNum; d++) {
            len = 0;
            for (int i = d; i < input.length; i += colNum) {
                len = input[i].length() > len ? input[i].length() : len; /*  Verifica o tamanho do maior campo em cada coluna. */
            }
            for (int b = d; b < input.length; b += colNum) {
                while (input[b].length() < len) {
                    input[b] += ' '; /*  Compensa a diferença de tamanho entre os campos adicionando espaços. */
                }
            }
        }

        StringBuilder r = new StringBuilder();
        int ii = 0;
        for (int j = 0; j < input.length; j += colNum) {
            for (int i = ii; i < ii + colNum; i++) {
                if ((i == (ii + colNum - 1)) && (i < input.length)) { /*  Adiciona uma barra vertical ao final de cada linha. */
                    r.append(input[i]);
                } else if (i < input.length) {
                    r.append(input[i] + " | ");
                }
            }
            if (j < input.length-colNum ) { 
                r.append("\n"); /*  Adiciona uma quebra de linha ao final de cada linha. */
            }
            ii += colNum; 
        }
        if (r.length() > 5 && r.charAt(r.length() - 2) == '|') {
            r.delete(r.length() - 3, r.length()); /* Remove a barra vertical da última linha. */
        }
        return r.toString();
    }

}