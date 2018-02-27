import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


class PanelWindow extends JPanel {
    private static final String[] panels = {"search", "results", "roomSelect", "finalValidation"};

    private Reception reception;

    private CardLayout mainPanelCard = new CardLayout();
    private JPanel mainPanel = new JPanel();
    private JPanel buttonWrapper;

    private int currAddPanel = 0;


    PanelWindow(Reception reception_){
        this.reception = reception_;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(10, 10, 10, 10));
        this.setBackground(Utils.PRIMARY_COLOR);

        this.createBackButton();
        this.add(this.mainPanel);

        this.mainPanel.setLayout(this.mainPanelCard);
        this.mainPanel.setOpaque(false);
    }
    private void createBackButton(){
        ListenerReturnButton returnButtonListener = new ListenerReturnButton(this.reception);
        JButton backButton = Utils.createJButton("←", returnButtonListener);

        this.buttonWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.buttonWrapper.add(backButton);
        this.buttonWrapper.setAlignmentX(Component.RIGHT_ALIGNMENT);
        this.buttonWrapper.setOpaque(false);

        this.add(buttonWrapper);
    }

    void addPanel(JPanel jPanel){
        this.mainPanel.add(jPanel, panels[currAddPanel]);
        currAddPanel++;
    }
    void addPanel(JScrollPane jScrollPane){
        this.mainPanel.add(jScrollPane, panels[currAddPanel]);
        currAddPanel++;
    }
    void setVisiblePanel(int panelN){
        this.mainPanelCard.show(this.mainPanel, panels[panelN]);
    }
    void setBackButtonVisible(boolean visible){
        this.buttonWrapper.setVisible(visible);
    }
}
