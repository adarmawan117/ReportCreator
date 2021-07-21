package reportcreator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Dialog {
    
    public void information(String pesan) {
        JOptionPane.showMessageDialog(null, pesan, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void warning(String pesan) {
        JOptionPane.showMessageDialog(null, pesan, "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public void error(String pesan) {
        JOptionPane.showMessageDialog(null, pesan, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void exception(Exception e, String namaClass, String namaMethod) {
        e.printStackTrace();
        
        String fileName = "log.txt";
        BufferedWriter writer = null;
        String errorInformation = namaClass +"::"+ namaMethod +"::"+ e.getMessage();
        
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(errorInformation);
            writer.append("\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        JOptionPane.showMessageDialog(null, errorInformation, "Exception", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void logcat(String log) {
        String fileName = "logcat.txt";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(log);
            writer.append("\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
