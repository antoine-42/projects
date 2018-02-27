import javax.swing.text.*;


public class JTextFieldLimit extends PlainDocument {
    private int limit;
    private int field_number;
    private PanelSearch panel = null;

    JTextFieldLimit(int limit_, int field_number_, PanelSearch panel_) {
        super();
        this.limit = limit_;
        this.field_number = field_number_;
        this.panel = panel_;
    }
    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if (str == null){
            return;
        }
        str = str.replace("-", "");

        if (getLength() < this.limit){
            if (getLength() + str.length() > this.limit) {
                String leftover = str.substring(this.limit - getLength(), str.length());
                str = str.substring(0, this.limit - getLength());

                if (this.panel != null) {
                    this.panel.setReservationSubPanel(field_number +1, leftover);
                }
            }
            super.insertString(offset, str, attr);
        }
        else if (this.panel != null) {
            this.panel.setReservationSubPanel(field_number +1);
        }
    }
}