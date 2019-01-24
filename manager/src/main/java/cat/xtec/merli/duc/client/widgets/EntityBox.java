package cat.xtec.merli.duc.client.widgets;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SuggestBox.*;
import com.google.gwt.user.client.ui.SuggestOracle.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.UID;


/* TODO: Implement, with remote suggestions */


/**
 * Widget that shows a suggest box to allow the user to fetch entities
 * from the remote server.
 */
public class EntityBox extends Composite implements Focusable,
    HasFocusHandlers, HasValueChangeHandlers<Entity>, HasSelectionHandlers<Entity> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-EntityBox";

    /** Nil entity value */
    private static final Entity NIL_VALUE = new Entity();

    /** Input box instance */
    private InputBox input = new InputBox();

    /** Suggest box instance */
    private SuggestBox suggestBox;

    /** Selected entity value */
    private Entity entity = NIL_VALUE;

    /** Current value */
    private Entity value = NIL_VALUE;


    /**
     * Sets the read-only mode of the editor.
     *
     * @param value     Read only state
     */
    public void setReadOnly(boolean value) {
        input.setReadOnly(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccessKey(char key) {
        suggestBox.setAccessKey(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus(boolean focused) {
        suggestBox.setFocus(focused);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabIndex() {
        return suggestBox.getTabIndex();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setTabIndex(int index) {
        suggestBox.setTabIndex(index);
    }


    /**
     * Gets the placeholder attribute of this widget.
     *
     * @return          Placeholder text
     */
    public String getPlaceholder() {
        return input.getPlaceholder();
    }


    /**
     * Sets the placeholder attribute of this widget.
     *
     * @param text      Placeholder text
     */
    public void setPlaceholder(String text) {
        input.setPlaceholder(text);
    }


    /**
     * Returns this object's value.
     *
     * @return          Entity instance or null
     */
    public Entity getValue() {
        return (entity != NIL_VALUE) ? entity : null;
    }


    /**
     * Sets the value of this object.
     *
     * @param value     Entity instance
     */
    public void setValue(Entity value) {
        this.entity = (value != null) ? value : NIL_VALUE;
        this.value = this.entity;
        this.refreshView();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return input.addFocusHandler(handler);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Entity> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Entity> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }


    /**
     * Refreshes this widget's view properties.
     */
    protected void refreshView() {
        String text = getTextFor(entity);
        suggestBox.setText(text);
    }


    /**
     * Displays the suggestions to the user. This is a work-around to fix
     * the width of the suggestions menu.
     */
    private DefaultSuggestionDisplay display = new DefaultSuggestionDisplay() {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void showSuggestions(SuggestBox box, Collection suggestions,
            boolean isHTML, boolean autoSelect, SuggestionCallback callback) {
            super.showSuggestions(box, suggestions, isHTML, autoSelect, callback);
            this.resizeMenu(box);
        }


        /**
         * Resize the suggestions menu so it has the same width as the
         * input box where it is attached.
         *
         * @param box       Suggest box instance
         */
        private void resizeMenu(SuggestBox box) {
            MenuBar menu = getSuggestionMenu();
            Element input = box.getElement();
            String px = Unit.PX.getType();
            int width = input.getClientWidth();

            menu.setWidth(input.getClientWidth() - 2 + px);
        }
    };


    /* -- */


    /**
     * {@inheritDoc}
     */
    public EntityBox() {
        super();

        /* Must be registered first! */
        input.addKeyDownHandler(e -> {
            if (e.getNativeKeyCode() == KeyCodes.KEY_TAB) {
                display.hideSuggestions();
            } else if (e.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                display.hideSuggestions();
            }
        });

        suggestBox = new SuggestBox(oracle, input, display);

        initWidget(suggestBox);
        setStylePrimaryName(STYLE_NAME);
        addStyleName(InputBox.STYLE_NAME);
        display.setPopupStyleName(STYLE_NAME + "Popup");

        suggestBox.setLimit(10);
        suggestBox.setAutoSelectEnabled(false);

        suggestBox.addSelectionHandler(e -> {
            entity = ((EntitySuggestion) e.getSelectedItem()).getEntity();
            SelectionEvent.fire(this, entity);
            ValueChangeEvent.fire(this, entity);
        });

        /* Must register after... */
        input.addKeyUpHandler(e -> {
            if (e.getNativeKeyCode() != KeyCodes.KEY_TAB && e.getNativeKeyCode() != KeyCodes.KEY_ENTER) {
                String name = input.getText();

                if (Objects.equals(name, getTextFor(entity))) {
                    return;
                }

                if (Objects.equals(name, getTextFor(value))) {
                    if (entity != value) {
                        entity = value;
                        SelectionEvent.fire(this, entity);
                    }
                } else {
                    if (entity != NIL_VALUE) {
                        entity = NIL_VALUE;
                        SelectionEvent.fire(this, null);
                    }
                }
            }
        });

        input.addChangeHandler(e -> {
            if (entity == NIL_VALUE && !hasText()) {
                setValue(value);
                SelectionEvent.fire(this, getValue());
                ValueChangeEvent.fire(this, getValue());
            } else {
                entity = hasText() ? NIL_VALUE : entity;

                if (!entity.equals(value)) {
                    setValue(entity);
                    ValueChangeEvent.fire(this, getValue());
                } else {
                    setValue(entity);
                    ValueChangeEvent.fire(this, getValue());
                }
            }
        });
    }


    private static String getTextFor(Entity entity) {
        if (entity instanceof Entity) {
            String code = getGUILocaleCode();
            LangString label = entity.getLabel(code);

            if (label instanceof LangString) {
                return label.getText();
            }
        }

        return LangString.EMPTY_TEXT;
    }


    /**
     * Obtains the currently active locale code of the graphical user
     * interface. This method may return "default" if the interface
     * locale was not set explicitly.
     *
     * @return      Locale code
     */
    private static String getGUILocaleCode() {
        LocaleInfo locale = LocaleInfo.getCurrentLocale();
        return locale.getLocaleName();
    }


    protected boolean hasText() {
        return suggestBox.getText().trim().isEmpty();
    }


    private SuggestOracle oracle = new SuggestOracle() {

        /**
         * {@inheritDoc}
         */
        @Override
        public void requestSuggestions(Request request, Callback callback) {
            Response response = new Response();
            List<Suggestion> suggestions = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                Entity entity = new Entity(UID.from("http://Xiuxiuejar" + i));
                entity.getLabels().add(LangString.from("Xiuxiuejar"));
                suggestions.add(new EntitySuggestion(entity));
            }

            response.setSuggestions(suggestions);
            callback.onSuggestionsReady(request, response);
        }


        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isDisplayStringHTML() {
            return true;
        }
    };

}
