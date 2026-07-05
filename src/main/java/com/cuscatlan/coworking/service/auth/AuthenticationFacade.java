package com.cuscatlan.coworking.service.auth;

import com.cuscatlan.coworking.entity.User;

public interface AuthenticationFacade {

    User getCurrentUser();

}