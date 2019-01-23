package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.event.logical.shared.*;
import com.google.gwt.editor.client.*;
import com.google.gwt.user.client.ui.*;


/**
 * This is a convenience interface that must be implemented by editors
 * that can participate on a dynamic list (@see DynamicListEditor).
 */
public interface LeafEditorWidget<T> extends
    IsWidget,
    LeafValueEditor<T>,
    HasValueChangeHandlers<T>,
    HasSelectionHandlers<T> {}
