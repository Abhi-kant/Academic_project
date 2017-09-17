package DB;


import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Connect {

    public static Connection conn = null;
    public static Statement stat, stat1 = null;
    static Connection con = null;
    public static ResultSet rs1;
    static PreparedStatement ps = null;
    public static ResultSet rs = null;

  

    public static Connection openConnection() {
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/twitter_data?user='root'& password=" );

            stat = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stat1 = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("Connection done");
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return conn;
    }

    public static boolean validateLogin(String username, String password) {
        boolean b = false;
        try {
            if (!(username.isEmpty() || password.isEmpty())) {
                openConnection();
                rs = stat.executeQuery("select * from tbluser where (emailid='" + username + "') && (password='" + password + "' && (status='True'))");
                if (rs.next()) {
                    b = true;
                    System.out.println("user validated");
                }
                closeConnection();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("user validated" + b);

        return b;
    }

    public static void closeConnection() {
        try {
            stat.close();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static String getUserPassword(String id) {
        openConnection();
        String i = "";
        try {
            rs = stat.executeQuery("select password from tbluser where userid='" + id + "'");
            if (rs.next()) {
                i = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }

    public static String getUserID(String email) {
        openConnection();
        String i = "";
        try {
            rs = stat.executeQuery("select userid from tbluser where emailid='" + email + "'");
            if (rs.next()) {
                i = rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }

    public static void changePass(String id, String pass) {

        conn = openConnection();
        try {
            String sql = "update tbluser set password='" + pass + "' where userid='" + id + "' ";
            ps = conn.prepareStatement(sql);//to update
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }
//txtfirstname, txtmiddlename, txtlastname, txtemailid, txtmobile, cmbgender, txtaddress, txtpassword, "True", "user"

    public static int saveUsers(String txtfirstname, String txtmiddlename, String txtlastname, String txtemailid, String txtmobile, String cmbgender,
            String txtaddress, String txtpassword, String status, String usertype) {
        openConnection();
        int i = 0;
        try {
            DateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");

            Calendar cal = Calendar.getInstance();

            String reg_date = dateFormat.format(cal.getTime());// "11/03/14 12:33:43";

            String sql = "insert into tbluser (fname,mname,lname,emailid,password,rdate,mobile,address,usertype,status)values(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            statement.setString(1, txtfirstname);
            statement.setString(2, txtmiddlename);
            statement.setString(3, txtlastname);
            statement.setString(4, txtemailid);
            statement.setString(5, txtpassword);
            statement.setString(6, reg_date);
            statement.setString(7, txtmobile);
            statement.setString(8, txtaddress);
            statement.setString(9, usertype);
            statement.setString(10, status);

            i = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }

    public static void updateField(String table, String field, String value, String matching_field, String match) {

        conn = openConnection();
        try {
            String sql = "update " + table + " set " + field + "='" + value + "' where " + matching_field + "='" + match + "' ";
            ps = conn.prepareStatement(sql);//to update
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public static int saveTweets(String tweet, String user, String location, String place,String created_at,String tweetid) {
        
        int i = 0;

        try {

            String sql = "insert into tbltweet (tweet,location,place,user,rdate,created_at,tweetid)values(?,?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, tweet);
            statement.setString(2, location);
            statement.setString(3, place);
            statement.setString(4, user);
            statement.setString(5, getDate());
            statement.setString(6, created_at);
            statement.setString(7, tweetid);
            i = statement.executeUpdate();
        } catch (Exception e) {
         //   e.printStackTrace();
        }
     
        return i;
    }

    public static int saveComment(String userid, String rating, String comment, String gameid) {
        openConnection();
        int i = 0;

        try {

            String sql = "insert into tblcomment (userid,rating,comment,gameid,rdate)values(?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userid);
            statement.setString(2, rating);
            statement.setString(3, comment);
            statement.setString(4, gameid);
            statement.setString(5, getDate());
            i = statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
        return i;
    }

  
    public static String getFileDateTime() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy_MM_dd_hh_mm_ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd:hh:mm:ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }

    public static String getTime() {
        DateFormat dateFormat1 = new SimpleDateFormat(
                "HH:mm:ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat1.format(cal.getTime());// "11/03/14 12:33:43";
    }

    public static String getDT() {
        DateFormat dateFormat = new SimpleDateFormat(
                "yyyy_MM_dd_hh_mm_ss");

        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
    }

}
