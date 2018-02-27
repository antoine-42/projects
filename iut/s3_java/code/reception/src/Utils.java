import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.*;
import java.util.*;

class Utils {
    static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    static final DateFormat DATE_FORMAT_HUMAN = new SimpleDateFormat("dd MMMM yyyy", new Locale("fr", "fr"));

    static final Insets MARGIN_DEFAULT = new Insets(5, 5, 5, 5);
    static final Insets MARGIN_NONE = new Insets(0, 0, 0, 0);
    static final Insets MARGIN_NO_DOWN = new Insets(5, 5, 0, 5);
    static final Insets MARGIN_RIGHT = new Insets(0, 0, 0, 20);
    static final Insets MARGIN_LEFT = new Insets(0, 20, 0, 0);

    static GridBagConstraints buttonConstraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, MARGIN_NONE, 10, 10);
    static GridBagConstraints labelTitleConstraints = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, MARGIN_NONE, 25, 25);
    static GridBagConstraints cellConstraints = new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, MARGIN_NONE, 0, 0);

    static final Color PRIMARY_COLOR = Color.decode("#EAEAEA");
    private static final Color SECONDARY_COLOR = Color.black;
    static final Color THIRD_COLOR = Color.WHITE;

    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font TITLE_FONT = new Font("SansSerif", Font.PLAIN, 23);

    private static final Border DEFAULT_JTEXTFIELD_BORDER = BorderFactory.createLineBorder(Color.GRAY, 1);
    static final Border EMPTY_BORDER = BorderFactory.createEmptyBorder();


    static JPanel createJPanel(){
        JPanel jPanel = new JPanel(new GridBagLayout());
        jPanel.setOpaque(false);

        return jPanel;
    }

    static JLabel createTitleJLabel(String text){
        return createTitleJLabel(text, JLabel.LEFT);
    }
    static JLabel createTitleJLabel(String text, int alignment){
        JLabel titleJLabel = new JLabel(text, alignment);
        titleJLabel.setFont(Utils.TITLE_FONT);
        titleJLabel.setForeground(Utils.SECONDARY_COLOR);

        return titleJLabel;
    }
    static JLabel createContentJLabel(String text){
        JLabel contentLabel = new JLabel(text, JLabel.LEFT);
        contentLabel.setFont(Utils.DEFAULT_FONT);
        contentLabel.setForeground(Utils.SECONDARY_COLOR);

        return contentLabel;
    }

    static JButton createJButton(String text){
        JButton jButton = new JButton(text);
        jButton.setFont(Utils.DEFAULT_FONT);

        return jButton;
    }
    static JButton createJButton(String text, ActionListener listener){
        JButton jButton = new JButton(text);
        jButton.addActionListener(listener);
        jButton.setFont(Utils.DEFAULT_FONT);

        return jButton;
    }

    static JTextField createJTextField(String text){
        JTextField jTextField = new JTextField(text);
        jTextField.setFont(Utils.DEFAULT_FONT);
        jTextField.setBorder(Utils.DEFAULT_JTEXTFIELD_BORDER);

        return jTextField;
    }
    static JTextField createJTextField(){
        return createJTextField("");
    }
    static JTextField createReferenceInputJTextField(){
        JTextField jTextField = new JTextField();
        jTextField.setFont(Utils.DEFAULT_FONT);
        jTextField.setBorder(Utils.EMPTY_BORDER);
        jTextField.setHorizontalAlignment(JTextField.CENTER);

        return jTextField;
    }


    static Boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty();
    }


    //make constructor inaccessible
    private Utils(){}
}
