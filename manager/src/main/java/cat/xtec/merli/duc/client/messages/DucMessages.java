package cat.xtec.merli.duc.client.messages;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;
import com.google.gwt.i18n.client.LocalizableResource;


/**
 * Main interface for this module's translations.
 *
 * {@inheritDoc}
 */
@DefaultLocale("en")
@LocalizableResource.Generate(
  fileName = "DucMessages",
  format =   "com.google.gwt.i18n.server.PropertyCatalogFactory",
  locales =  "default"
)
@LocalizableResource.GenerateKeys(
  value =    "com.google.gwt.i18n.server.keygen.MD5KeyGenerator"
)
public interface DucMessages extends Messages {}
