package reportcreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ReportCreator {

    public String SQLITE_URL;
    public Statement STATEMENT = null;
    public PreparedStatement PREPARED_STMT = null;
    public Connection CONN = null;
    public ResultSet RS;

    private String FILE_NAME;
    ArrayList<String> listReportPath = new ArrayList<>();
//    ArrayList<String> listReportName = new ArrayList<>();

    Dialog dialog = new Dialog();
    Reportt report = new Reportt();

    // current root directory
    String reportPath;

    ReportCreator() {
        String namaMethod = new   Throwable() 
                            .getStackTrace()[0] 
                            .getMethodName();
        String namaClass = this.getClass().getSimpleName();
        try {
            File dir1 = new File(".");
            String canonicalPath = dir1.getCanonicalPath();

            reportPath = canonicalPath + "/src/reportcreator/";
            SQLITE_URL = "jdbc:sqlite:" + canonicalPath + "/dbPutraMandiriExpedisi,db.db";
            FILE_NAME = canonicalPath +"/Resources/file for print.txt";
            
            listReportPath.add(reportPath + "LaporanBiayaExpedisi/ReportBiayaExpedisi.jrxml");
            listReportPath.add(reportPath + "LaporanPenerima/ReportPenerima.jrxml");
            listReportPath.add(reportPath + "LaporanSuratJalan/SuratJalan.jrxml");
            listReportPath.add(reportPath + "Report/LaporanPerbulan/ReportPerbulan.jrxml");
            listReportPath.add(reportPath + "Report/Pengirim/ReportPengirim.jrxml");
            listReportPath.add(reportPath + "ReportKaryawan/ReportKaryawan2.jrxml");
            listReportPath.add(reportPath + "Invoice/Invoice.jrxml");

            /*
            // untuk mengecek apakah file ada atau tidak
            for(String temp : listReportPath) {
                if(new File(temp).exists()) {
                    System.out.println("File ada");
                }
            }
             */
        } catch (IOException ex) {
            dialog.exception(ex, namaClass, namaMethod);
        }
    }

    private String getReportPath(String reportName) {
        switch (reportName) {
            case "BiayaExpedisi":
                return listReportPath.get(0);
            case "Penerima":
                return listReportPath.get(1);
            case "SuratJalan":
                return listReportPath.get(2);
            case "LaporanPerbulan":
                return listReportPath.get(3);
            case "Pengirim":
                return listReportPath.get(4);
            case "Karyawan":
                return listReportPath.get(5);
            case "Invoice":
                return listReportPath.get(6);
            default:
                return "";
        }
    }

    private void setReportProperties() {
        String namaMethod = new   Throwable() 
                            .getStackTrace()[0] 
                            .getMethodName();
        String namaClass = this.getClass().getSimpleName();
        
        BufferedReader br;
        String line;
        String separator = "::";
        try {
            br = new BufferedReader(new FileReader(FILE_NAME));
            while ((line = br.readLine()) != null) {
                String[] hasil = line.split(separator);
                
                report.setReportName(hasil[0]);
                report.setQuery(hasil[1]);

                report.setReportPath(getReportPath(hasil[0].substring(3, hasil[0].length())));
                
//                dialog.logcat("Report name  : "+ report.getReportName());
//                dialog.logcat("Report query : "+ report.getQuery());
//                dialog.logcat("Hasil[0]     : "+ hasil[0].substring(3, hasil[0].length()));
//                dialog.logcat("Report path  : "+ report.getReportPath());
                
//                System.out.println("Report name : "+ report.getReportName());
//                System.out.println("Query       : "+ report.getQuery());
//                System.out.println("Report path : "+ report.getReportPath());
            }
            br.close();
        } catch (FileNotFoundException ex) {
            dialog.exception(ex, namaClass, namaMethod);
        } catch (IOException ex) {
            dialog.exception(ex, namaClass, namaMethod);
        }
    }

    private void mainFunction() {
        String namaMethod = new   Throwable() 
                            .getStackTrace()[0] 
                            .getMethodName();
        String namaClass = this.getClass().getSimpleName();
        // pengesetan properties untuk report
        setReportProperties();

        // proses print report
        try {
//            dialog.logcat("Class.forName(\"org.sqlite.JDBC\");");
            Class.forName("org.sqlite.JDBC");
            
//            dialog.logcat("Connection conn = DriverManager.getConnection(SQLITE_URL);");
            Connection conn = DriverManager.getConnection(SQLITE_URL);

//            dialog.logcat("Statement st = conn.createStatement();");
            Statement st = conn.createStatement();

//            dialog.logcat("JasperDesign disain = JRXmlLoader.load(report.getReportPath());");
//            dialog.logcat(report.getReportPath());
            JasperDesign disain = JRXmlLoader.load(report.getReportPath());
            
//            dialog.logcat("JasperReport nilaiLaporan = JasperCompileManager.compileReport(disain);");
            JasperReport nilaiLaporan = JasperCompileManager.compileReport(disain);
            
//            dialog.logcat("ResultSet rs = st.executeQuery(report.getQuery());");
            ResultSet rs = st.executeQuery(report.getQuery());

//            dialog.logcat("JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);");
            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(rs);
            
//            dialog.logcat("JasperPrint cetak = JasperFillManager.fillReport(nilaiLaporan, new HashMap(), resultSetDataSource);");
            JasperPrint cetak = JasperFillManager.fillReport(nilaiLaporan, new HashMap(), resultSetDataSource);
            
//            dialog.logcat("JasperViewer.viewReport(cetak, false);");
            JasperViewer.viewReport(cetak, false);
        } catch (ClassNotFoundException | SQLException | JRException e) {
            dialog.exception(e, namaClass, namaMethod);
        }
    }

    public static void main(String[] args) {
        ReportCreator r = new ReportCreator();
        r.mainFunction();
    }

}
