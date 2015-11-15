package com.github.totoCastaldi.services.credential.restResource;

import com.google.common.base.Optional;
import com.github.totoCastaldi.restServer.model.CustomerDao;
import com.github.totoCastaldi.restServer.model.CustomerEntity;
import org.apache.commons.lang.StringUtils;

/**
 * Created by github on 08/11/15.
 */
public class ExampleCustomerDao implements CustomerDao {

    @Override
    public Optional<CustomerEntity> findByUsername(String username) {
        CustomerEntity result = null;
        if (StringUtils.equals(username, "stop-play-minecraft")) {
            result = CustomerEntity.of(
                "stop-play-minecraft",
                "cac3d65e597df4a39840089798677231",
                false,
                true
            );
        }
        return Optional.fromNullable(result);
    }
}
