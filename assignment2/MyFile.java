import java.io.File;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class MyFile implements Serializable {
    private String title, subtitle, link, updated, author, id, summary, name, heart, status;
    private int num, time, key;
    private boolean lost;
    private String path = null;

    public MyFile(int num, String name, int time){
        this.title = "";
        this.subtitle = "";
        this.link = "";
        this.updated = "";
        this.author = "";
        this.id = "";
        this.summary = "";
        this.num = num;
        this.name = name;
        this.heart = null;
        this.time = time;
        this.key = -1;
        this.status = null;
        this.lost = false;
    }

    public MyFile(int num, String name, int time, int key){
        this.title = "";
        this.subtitle = "";
        this.link = "";
        this.updated = "";
        this.author = "";
        this.id = "";
        this.summary = "";
        this.num = num;
        this.name = name;
        this.heart = null;
        this.time = time;
        this.key = key;
        this.status = null;
        this.lost = false;
    }

    public int getTime() {
        return time;
    }
    public void setTime(int time) {
        this.time = time;
    }


    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public MyFile[] read_file(MyFile file, String path){
        MyFile[] myFile = {file};
        try {
            Scanner input  = new Scanner(new File(path));
            MyFile tem1 = new MyFile(this.num, this.name, this.time, this.key);
            MyFile tem = new MyFile(this.num, this.name, this.time, this.key);
            while (input.hasNext()){
                String[] in = input.nextLine().split(":");
                if (Objects.equals(in[0], "title")) {
                    tem.title = getString(in);
                }
                if (Objects.equals(in[0], "subtitle")) {
                    tem.subtitle = getString(in);
                }
                if (Objects.equals(in[0], "link")) {
                    tem.link = getString(in);
                }
                if (Objects.equals(in[0], "updated")) {
                    tem.updated = getString(in);
                }
                if (Objects.equals(in[0], "author")) {
                    tem.author = getString(in);
                }
                if (Objects.equals(in[0], "id")) {
                    tem.id = getString(in);
                }
                if (Objects.equals(in[0], "summary")) {
                    tem.summary = getString(in);
                }
                if (Objects.equals(in[0], "entry")) {
                    MyFile[] update = new MyFile[myFile.length+1];
                    System.arraycopy(myFile, 0, update, 0, myFile.length);
                    update[update.length-1] = tem;
                    myFile = update;
                    tem = new MyFile(this.num, this.name, this.time, this.key);
                }
                if (in[0].length()>4 && Objects.equals(in[0].substring(in[0].length()-4), ".txt")) {
                    myFile[0].setPath(in[0]);
                }
                tem1 = tem;
            }
            MyFile[] update = new MyFile[myFile.length+1];
            System.arraycopy(myFile, 0, update, 0, myFile.length);
            update[update.length-1] = tem1;
            myFile = update;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myFile;
    }

    // Read file ignore the error feeds
    public MyFile[] read_right_file(MyFile file, String path){
        MyFile[] myFile = new MyFile[0];
        try {
            Scanner input  = new Scanner(new File(path));
            MyFile tem1 = new MyFile(this.num, this.name, this.time, this.key);
            MyFile tem = new MyFile(this.num, this.name, this.time, this.key);
            while (input.hasNext()){
                String[] in = input.nextLine().split(":");
                if (Objects.equals(in[0], "title")) {
                    tem.title = getString(in);
                }
                if (Objects.equals(in[0], "subtitle")) {
                    tem.subtitle = getString(in);
                }
                if (Objects.equals(in[0], "link")) {
                    tem.link = getString(in);
                }
                if (Objects.equals(in[0], "updated")) {
                    tem.updated = getString(in);
                }
                if (Objects.equals(in[0], "author")) {
                    tem.author = getString(in);
                }
                if (Objects.equals(in[0], "id")) {
                    tem.id = getString(in);
                }
                if (Objects.equals(in[0], "summary")) {
                    tem.summary = getString(in);
                }
                if (Objects.equals(in[0], "entry")) {
                    if (!Objects.equals(tem.getTitle(), "") &&
                            !Objects.equals(tem.getLink(), "") &&
                            !Objects.equals(tem.getId(), "")) {
                        MyFile[] update = new MyFile[myFile.length+1];
                        System.arraycopy(myFile, 0, update, 0, myFile.length);
                        update[update.length-1] = tem;
                        myFile = update;
                    }
                    tem = new MyFile(this.num, this.name, this.time, this.key);
                }
                if (in[0].length()>4 && Objects.equals(in[0].substring(in[0].length()-4), ".txt")) {
                    tem.setPath(in[0]);
                    MyFile[] update = new MyFile[myFile.length+1];
                    System.arraycopy(myFile, 0, update, 0, myFile.length);
                    update[update.length-1] = tem;
                    myFile = update;
                    tem = new MyFile(this.num, this.name, this.time, this.key);
                }
                tem1 = tem;
            }
            if (!Objects.equals(tem1.getTitle(), "") &&
                    !Objects.equals(tem1.getLink(), "") &&
                    !Objects.equals(tem1.getId(), "")) {
                MyFile[] update = new MyFile[myFile.length+1];
                System.arraycopy(myFile, 0, update, 0, myFile.length);
                update[update.length-1] = tem1;
                myFile = update;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myFile;
    }

    public static boolean write_file(MyFile[] myFile) {
        boolean make_sense = true;
        try {
            PrintStream console = System.out;
            String path = "";
            if (Objects.equals(myFile[0].getName(), "CS")) {
                path = myFile[0].getName() + myFile[0].getKey() + ".txt";
            } else {
                path = myFile[0].getName() + myFile[0].getNum() + ".txt";
            }
            PrintStream printStream = new PrintStream(path);
            System.setOut(printStream);
            int n = 1;
            for (int i = 0; i < myFile.length; i++) {
                if (!Objects.equals(myFile[i].getPath(), null)) {
                    System.out.println(myFile[i].getPath());
                } else {
                    if (!Objects.equals(myFile[i].getTitle(), "") &&
                            !Objects.equals(myFile[i].getLink(), "") &&
                            !Objects.equals(myFile[i].getId(), "")) {
//                    System.setOut(printStream);
                        myFile[i].printMyFile(myFile[i]);
                        if (i != myFile.length-1) {
                            System.out.println("entry");

                        }
                    } else {
                        n++;
                    }
                }
            }
            if (n == myFile.length) {
                make_sense = false;
            }
//            for (MyFile m : myFile) {
//                if (!Objects.equals(m.getTitle(), "") &&
//                        !Objects.equals(m.getLink(), "") &&
//                        !Objects.equals(m.getId(), "")) {
//                    System.setOut(printStream);
//                    m.printMyFile(m);
//                    System.out.println("entry");
//                }
//            }
            System.setOut(console);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return make_sense;
    }

    public void printMyFile(MyFile myFile) {
        if (!Objects.equals(myFile.getTitle(), "")) {
            System.out.println("title:" + myFile.getTitle());
        }
        if (!Objects.equals(myFile.getSubtitle(), "")) {
            System.out.println("subtitle:" + myFile.getSubtitle());
        }
        if (!Objects.equals(myFile.getLink(), "")) {
            System.out.println("link:" + myFile.getLink());
        }
        if (!Objects.equals(myFile.getUpdated(), "")) {
            System.out.println("updated:" + myFile.getUpdated());
        }
        if (!Objects.equals(myFile.getAuthor(), "")) {
            System.out.println("author:" + myFile.getAuthor());
        }
        if (!Objects.equals(myFile.getId(), "")) {
            System.out.println("id:" + myFile.getId());
        }
        if (!Objects.equals(myFile.getSummary(), "")) {
            System.out.println("summary:" + myFile.getSummary());
        }
    }
    private static String getString(String[] in) {
        String res = "";
        for (int i = 1; i < in.length; i++) {
            res = res + in[i];
            if (i != in.length-1) {
                res = res + ":";
            }
        }
        return res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getName() {
        return name;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    public static void main(String[] args) {
//        MyFile myFile = new MyFile(1, "cs", 2);
//        MyFile[] re = myFile.read_file(myFile,"input.txt");
//        for (MyFile i : re){
//            i.printMyFile(i);
//
//        }
//    }
}
