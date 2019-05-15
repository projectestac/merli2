package cat.xtec.merli.duc.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cat.xtec.merli.domain.lom.Resource;


/**
 * RPC service to manage learning objects.
 */
@RemoteServiceRelativePath("duc/resources")
public interface ResourceService extends AbstractServiceIn<Resource> {}
