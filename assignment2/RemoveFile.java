import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoveFile {
    private static ArrayList<String> file = new ArrayList<>();

    protected static void getFile() {
        File f = new File(Objects.requireNonNull(RemoveFile.class.getResource("")).getPath());
        File[] result = f.listFiles();
        String regex="CS.*.txt|HeartBeat.*.txt|Client.*.txt";
        Pattern pattern = Pattern.compile(regex);
        for(File fs : result){
            Matcher matcher = pattern.matcher(fs.getName());
            while(matcher.find()){
                RemoveFile.file.add(matcher.group(0).toString());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        getFile();
        for (String s : RemoveFile.file) {
            Files.delete(Paths.get(s));
        }
    }
}
