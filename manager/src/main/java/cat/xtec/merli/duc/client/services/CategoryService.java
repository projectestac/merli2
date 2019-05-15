package cat.xtec.merli.duc.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cat.xtec.merli.domain.taxa.Category;


/**
 * RPC service to manage categories.
 */
@RemoteServiceRelativePath("duc/categories")
public interface CategoryService extends AbstractServiceIn<Category> {}
