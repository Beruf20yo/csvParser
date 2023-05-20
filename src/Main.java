import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class Main {
    public static final String csvFile = "src/movementList.csv";
    public static ArrayList<Double> credit = new ArrayList<>();
    public static ArrayList<Double> debit = new ArrayList<>();
    public static ArrayList<String> info = new ArrayList<>();

    public static void main(String[] args) {
        ArrayList<String> allTransactions = new ArrayList<>();
        ArrayList<String> allInfo = new ArrayList<>();
        String line;
        String csvSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                String[] parts = line.split(csvSplitBy, 7);
                allInfo.add(parts[5]);
                allTransactions.add(parts[6]);
            }
            allTransactions.remove(0);
            allInfo.remove(0);
            translator(allTransactions);
            ArrayList<String> helper = new ArrayList<>();
//            for (String x : allInfo) {
//                for (char ch : x.toCharArray()) {
//                    if (ch == '/')
//                        helper.add(x);
//                }
//            }
//            allInfo.removeAll(helper);
            for (String x : allInfo) {// Здесь я преобразовываю строки с информацией и вычленяю из неё только нужные слова
                List<String> support = Arrays.asList(x.split("\\\\|/")); // Убираем все знаки "\"
                List<String> addSup = support.stream().distinct().collect(Collectors.toList());//Избавляемся от множества
                addSup.remove(0);// Первый элемент нам всегда не нужен
                String[] delSpace = (addSup.get(addSup.size() - 1)).split(" ");
                addSup.set((addSup.size() - 1), delSpace[0]);
                String supporT = "";
                for (String help : addSup) {
                    supporT += " " + help;
                }
                info.add(supporT);
            }
            double debitOut = 0.0;
            for(Double deb: debit){
                debitOut +=deb;
            }
            double creditOut = 0.0;
            for(Double cre: credit){
                creditOut +=cre;
            }
            System.out.println("Сумма расходов: " + creditOut+
                    "\nСумма доходов: "+debitOut);
            System.out.println("Сумма расходов по оргонизациям:");
            for(int i = 0; i<credit.size();i++){
                if(credit.get(i) == 0.0){
                    continue;
                }else {
                    System.out.printf("%-35s  %.2f руб.",info.get(i),credit.get(i));
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void translator(ArrayList<String> money) {
        char[] moneyChars;
        String[] helper;
        for (String x : money) {
            int commaCount = 0;
            moneyChars = x.toCharArray();
            for (char ch : moneyChars) {
                if (ch == ',') {
                    commaCount++;
                }
            }
            if (commaCount == 1) {
                helper = x.split(",");
                debit.add(Double.parseDouble(helper[0]));
                credit.add(Double.parseDouble(helper[1]));
            } else if (commaCount == 3) {
                helper = x.split("\",\"");
                debit.add(removeChar(helper[0]));
                credit.add(removeChar(helper[1]));
            } else if (commaCount == 2) {
                if (moneyChars[0] == '"') {
                    helper = x.split("\",");
                    debit.add(removeChar(helper[0]));
                    credit.add(Double.parseDouble(helper[1]));
                } else if (Character.isDigit(moneyChars[0])) {
                    helper = x.split(",\"");
                    debit.add(Double.parseDouble(helper[0]));
                    credit.add(removeChar(helper[1]));
                }
            }
        }
    }
    public static double removeChar(String str) {
        String newStr = str.replace("\"", "").replace(",", ".");
        return Double.parseDouble(newStr);

    }
}