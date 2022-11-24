import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompareLeader {
    public static void main(String[] args) throws FileNotFoundException {
        File f = new File(Objects.requireNonNull(CompareLeader.class.getResource("")).getPath());
        File[] result = f.listFiles();
        assert result != null;
        List<File> fileList = Arrays.asList(result);
        String regex = ".*name.txt";
        Pattern pattern = Pattern.compile(regex);
        List<String> leader = new ArrayList<>();

        for (File fs : fileList) {
            Matcher matcher = pattern.matcher(fs.getName());
            while (matcher.find()) {
                Scanner input = new Scanner(new File(matcher.group(0).toString()));
                leader.add(input.nextLine());
            }
        }
        int k = 1;
        for (int i = 1; i < leader.size(); i++) {
            if (Objects.equals(leader.get(i - 1), leader.get(i))) {
                k++;
            }
        }
        if (k == leader.size()) {
            System.out.println("They elected leader. " + leader.get(0));
        } else {
            System.out.println("They did not elect leader.");
        }

    }
}
