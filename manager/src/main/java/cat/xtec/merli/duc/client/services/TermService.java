package cat.xtec.merli.duc.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cat.xtec.merli.domain.taxa.Term;


/**
 * RPC service to manage vocabulary terms.
 */
@RemoteServiceRelativePath("duc/terms")
public interface TermService extends AbstractServiceIn<Term> {}
