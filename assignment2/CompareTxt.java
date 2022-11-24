import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompareTxt {
    public static void main(String[] args) {

        compare(args[0]);
    }

    private static void compare(String output) {
        MyFile myFile = new MyFile(-1, "compare", -1);
        MyFile[] txt = myFile.read_right_file(myFile, output);
        List<String> input = new ArrayList<>();
        List<MyFile> txt_output = new ArrayList<>();
//        System.out.println(txt.length);
        for (MyFile file : txt) {
            if (!Objects.equals(file.getPath(), null)) {
                input.add(file.getPath());
//                System.out.println(file.getPath());
            } else {
                txt_output.add(file);
            }
        }
        MyFile[] txt2 = new MyFile[0];
        // Read input files
        for (String s : input) {
            MyFile[] tem = myFile.read_right_file(myFile, s);
            MyFile[] update = new MyFile[tem.length + txt2.length];
            System.arraycopy(txt2, 0, update, 0, txt2.length);
            System.arraycopy(tem, 0, update, txt2.length, tem.length);
            txt2 = update;
        }
        // Compare output and input
        if (txt_output.size() == txt2.length) {
            int feed = 1;
            for (int i = 1; i < txt_output.size(); i++) {
                if (Objects.equals(txt_output.get(i).getTitle(), txt2[i].getTitle()) &&
                        Objects.equals(txt_output.get(i).getSubtitle(), txt2[i].getSubtitle()) &&
                        Objects.equals(txt_output.get(i).getLink(), txt2[i].getLink()) &&
                        Objects.equals(txt_output.get(i).getAuthor(), txt2[i].getAuthor()) &&
                        Objects.equals(txt_output.get(i).getId(), txt2[i].getId()) &&
                        Objects.equals(txt_output.get(i).getSummary(), txt2[i].getSummary())) {
                    feed++;
                }
                else {
                    break;
                }
            }
            if (feed == txt_output.size()) {
                System.out.println("The result are same as input files.");
            } else {
                System.out.println("The result  are different from input files.");
            }
        } else {
            System.out.println("The result  are different from input files.hhhhhhhhhhh");
        }
    }
}
