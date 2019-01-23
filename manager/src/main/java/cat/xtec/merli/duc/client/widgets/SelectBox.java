package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.ListBox;


/**
 * A {@code ListBox} wrapped with a &lt;div&gt; element.
 *
 * {@inheritDoc}
 */
public class SelectBox extends ListBox {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-SelectBox";

    /** Wrapper element */
    private Element wrapper;


    /**
     * {@inheritDoc}
     */
    public SelectBox() {
        this(false);
    }


    /**
     * {@inheritDoc}
     */
    public SelectBox(boolean multiple) {
        super(Document.get().createSelectElement(multiple));
        wrapper = Document.get().createDivElement();
        wrapper.addClassName(STYLE_NAME + "Wrapper");
        setStylePrimaryName(STYLE_NAME);
    }


    /**
     * Returns if an item is selected.
     *
     * @return      If an item was selected
     */
    public boolean hasSelection() {
        return getSelectedIndex() >= 0;
    }


    /**
     * Clears the selected value.
     */
    public void clearSelection() {
        setSelectedIndex(-1);
    }


    /**
     *
     */
    public String getPlaceholder() {
        return wrapper.getAttribute("data-placeholder");
    }


    /**
     *
     */
    public void setPlaceholder(String text) {
        wrapper.setAttribute("data-placeholder", text);
    }


    /**
     *
     */
    @Override
    public void setSelectedIndex(int index) {
        super.setSelectedIndex(index);
        updateStyles();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLoad() {
        super.onLoad();

        Element select = this.getElement();
        Element parent = select.getParentElement();

        if (Document.get().isOrHasChild(wrapper) == false) {
            addChangeHandler(e -> updateStyles());
            wrapper.appendChild(select);
            parent.insertBefore(wrapper, null);
        }
    }


    /**
     * Updates the CSS styles of this widget.
     */
    private void updateStyles() {
        if (hasSelection() == true) {
            wrapper.removeClassName("duc-EMPTY");
        } else {
            wrapper.addClassName("duc-EMPTY");
        }
    }

}
