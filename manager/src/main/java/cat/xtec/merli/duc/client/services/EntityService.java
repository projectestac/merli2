package cat.xtec.merli.duc.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cat.xtec.merli.domain.taxa.Entity;


/**
 * RPC service to manage generic entities.
 */
@RemoteServiceRelativePath("duc/entities")
public interface EntityService extends DucServiceAbstract<Entity> {}
