package cat.xtec.merli.duc.client.editors.lists;

import java.util.List;
import java.util.ArrayList;
import com.google.gwt.editor.client.*;
import com.google.gwt.user.client.ui.*;


/**
 * This abstract widget is extended by most of the list editors and
 * implements a list editor where new editors can be added dynamically
 * by the user. Note also that child editors must be widgets that
 * implement a certain set of event handlers.
 */
public abstract class DynamicListEditor<T, E extends LeafEditorWidget<T>>
    extends Composite implements CompositeEditor<List<T>, T, E> {

    /** Primary CSS style for this widget */
    private static final String STYLE_NAME = "duc-ListEditor";

    /** Flow panel for the editors */
    private FlowPanel panel = new FlowPanel();

    /** Editors chain */
    private EditorChain<T, E> chain;

    /** List sub-editors */
    private List<E> editors = new ArrayList<>();

    /** New line editor */
    private E creator;

    /** Current editor values */
    private List<T> values;


    /**
     * Editor constructor.
     */
    public DynamicListEditor() {
        super();
        initWidget(panel);
        setStylePrimaryName(STYLE_NAME);
        addAttachHandler(e -> ensureCreatorExists());
    }


    /**
     * Creates a new subeditor instance.
     *
     * @return          A new editor instance
     */
    @Editor.Ignore
    protected abstract E createSubeditor();


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(List<T> values) {
        this.values = values;
        this.clear();
        this.ensureCreatorExists();

        values.forEach(value -> add(value));
        creator.setValue(null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        values.clear();

        editors.forEach(editor -> {
            T value = chain.getValue(editor);
            if (value != null) values.add(value);
        });
    }


    /**
     * Adds a new editor for a value to the panel.
     *
     * @param value         Value for the editor
     */
    protected void add(T value) {
        E editor = createSubeditor();
        IsWidget widget = editor.asWidget();

        panel.insert(widget, editors.size());
        attachHandlers(editor);
        attach(value, editor);
    }


    /**
     * Removes an editor from the panel.
     *
     * @param editor        Editor instance
     */
    protected void remove(E editor) {
        IsWidget widget = editor.asWidget();
        panel.remove(widget);
        detach(editor);
    }


    /**
     * Removes all the editors from the panel.
     */
    protected void clear() {
        editors.forEach(editor -> {
            panel.remove(editor.asWidget());
            chain.detach(editor);
        });

        editors.clear();
    }


    /**
     * Attaches an editor to the editor chain.
     *
     * @param value         Value for the editor
     * @param editor        Editor instance
     */
    private void attach(T value, E editor) {
        editors.add(editor);
        chain.attach(value, editor);
    }


    /**
     * Detaches an editor from the editor chain.
     *
     * @param editor        Editor instance
     */
    private void detach(E editor) {
        chain.detach(editor);
        editors.remove(editor);
    }


    /**
     * Attaches the current creator editor to the editor chain and
     * appends a new creator to the panel.
     *
     * @param value     Value for the editor
     */
    private void promoteCreator(T value) {
        attach(value, this.creator);
        this.creator.setValue(value);
        this.creator = appendCreator();
    }


    /**
     * Instantiates a new creator editor and adds it to the end of
     * the panel. A creator editor is the editor used by the user to
     * add new values to the list.
     *
     * @return          New editor instance
     */
    private E appendCreator() {
        E creator = createSubeditor();
        IsWidget widget = creator.asWidget();

        attachHandlers(creator);
        panel.add(widget);

        return creator;
    }


    /**
     * Makes sure a creator editor widget has been initialized.
     */
    private void ensureCreatorExists() {
        if (creator == null) {
            creator = appendCreator();
        }
    }


    /**
     * Attaches the required event handlers to an editor.
     *
     * @param editor        Editor instance
     */
    private void attachHandlers(E editor) {
        editor.addValueChangeHandler(e -> {
            if (editor != creator) {
                T value = editor.getValue();
                if (value == null) remove(editor);
            }
        });

        editor.addSelectionHandler(e -> {
            if (editor == creator) {
                T value = e.getSelectedItem();
                if (value != null) promoteCreator(value);
            }
        });
    }


    /** {@inheritDoc} */
    @Override
    public void setEditorChain(EditorChain<T, E> chain) {
        this.chain = chain;
    }


    /** {@inheritDoc} */
    @Override
    public E createEditorForTraversal() {
        return createSubeditor();
    }


    /** {@inheritDoc} */
    @Override
    public String getPathElement(E editor) {
        return "[" + editors.indexOf(editor) + "]";
    }


    /** {@inheritDoc} */
    @Override
    public void onPropertyChange(String... paths) {}


    /** {@inheritDoc} */
    @Override
    public void setDelegate(EditorDelegate<List<T>> delegate) {}

}
